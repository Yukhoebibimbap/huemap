/*
  map_viewmodel.dart :
  지도 화면에서 출력하는 데이터, 데이터를 조작하는 로직을 다룹니다.

  - 수거함 정보를 불러오고 지도에 표시합니다.
  - 표시할 수거함의 종류를 toggle 변수, 버튼으로 조정합니다.
  - 사용자의 GPS 위치를 불러와 지도를 옮깁니다.

 */

import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:huemap_app/constant_value.dart';
import 'package:path_provider/path_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter/material.dart';


import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/model/binDetail.dart';
import 'package:huemap_app/data/model/reportPresencesRequest.dart';
import 'package:huemap_app/data/model/reportWithUserLocationRequest.dart';
import 'package:huemap_app/data/model/suggestBinLocationRequest.dart';
import 'package:huemap_app/data/repository/bin_repository.dart';
import 'package:huemap_app/data/repository/report_repository.dart';
import 'package:huemap_app/data/repository/suggest_repository.dart';
import 'package:huemap_app/get_current_position.dart';
import 'package:huemap_app/constant_value.dart';
import 'package:huemap_app/data/model/voteInfo.dart';
import 'package:huemap_app/constant_value.dart';
import 'package:huemap_app/ui/view/login_view.dart';
import 'package:huemap_app/data/model/userInfo.dart';
import 'package:huemap_app/ui/page/login_page.dart';


import 'package:flutter/services.dart' show ImmutableBuffer, rootBundle;

class MapViewModel with ChangeNotifier{
  late final BinRepository _binRepository;
  late final ReportRepository _reportRepository;
  late final SuggestRepository _suggestRepository;
  WebViewController? controller;
  late final String url;
  late final Set<JavascriptChannel> channel;
  int count = 0;

  Map<int, List<Bin>> get items => _items;
  late Map<int, int> offset = <int, int>{};
  final Map<int, List<Bin>> _items = <int, List<Bin>>{};
  Map<String,dynamic> get binDetail => _binDetail;
  // late BinDetail _binDetail = <dynamic, dynamic>{} as BinDetail;
  late Map<String,dynamic> _binDetail;
  late String currentBinId;

  late Future<List<Bin>> binLoadCompleted;
  late Future<String> assetLoadCompleted;

  final onLoad = <bool>[true,false,false,false,false,false];
  final onMarker = <bool>[true,false,false,false,false,false];
  bool onPinDrop = false;

  List<String> bottom_widget_stack = [];
  bool _detail_visible = false;
  bool get detail_visible => _detail_visible;
  bool _report_visible = false;
  bool get report_visible => _report_visible;
  bool _suggestion_visible = false;
  bool get suggestion_visible => _suggestion_visible;
  bool _missing_visible = false;
  bool get missing_visible => _missing_visible;
  bool _dialog_visible = false;
  bool get dialog_visible => _dialog_visible;
  bool _condition_visible = false;
  bool get condition_visible => _condition_visible;
  bool double_button_dialog = true;
  bool _closure_warning_message = false;
  bool get closure_warning_message => _closure_warning_message;
  bool _show_vote_button = false;
  bool get show_vote_button => _show_vote_button;
  bool _show_floating_button = true;
  bool get show_floating_button => _show_floating_button;

  final constantValue = ConstantValue();
  var pin_detail_index = ['도로명 주소', '상세 설명', '수거함 종류'];
  var pin_detail_data = ['서울특별시 동작구 동작대로29길 69','두성프라자 건물 뒷편','폐건전지'];
  var dropBinMenu = '일반 쓰레기';
  var dropGuMenu = '강남구';
  var conditionMenu = '가득참';
  var dialog_title = '';
  var image_path;

  var custom_lat;
  var custom_lng;

  var dia_type;
  var wid_type;

  var context;

  MapViewModel() {

    // 데이터 계층 연결, 자바스크립트 채널 생성
    _binRepository = BinRepository();
    _reportRepository = ReportRepository();
    _suggestRepository = SuggestRepository();
    binLoadCompleted = loadItems(Type.general);
    assetLoadCompleted = loadAssets();

    channel = {
      JavascriptChannel(name: 'onClickMarker', onMessageReceived: (message) {
        // Fluttertoast.showToast(msg: message.message);
        var id = message.message;
        toggleBinDetail(id);
      }),
      JavascriptChannel(name: 'onClickSuggestion', onMessageReceived: (message) {
        toggle_bottom_widget('suggestion');
      }),
      JavascriptChannel(name: 'onClickReport', onMessageReceived: (message) {
        toggle_bottom_widget('report');
      }),
      JavascriptChannel(name: 'onClickMap', onMessageReceived: (message) {
        pop_all();
      }),
      JavascriptChannel(name: 'onDropCustom', onMessageReceived: (message) {
        parseLatLng(message.message);
      })
    };
  }

  Future<List<Bin>> loadItems(Type t) async {
    late Future<List<Bin>> binData;
    binData = _binRepository.getBins(t);
    binData.then((data) {
      _items.addAll({t.index : data});
      offset.addAll({t.index : count});
      count += data.length;
    });

    return binData;
  }

  Future<Map<String,dynamic>> loadBinDetail(String id) async {
    late Future<Map<String,dynamic>> binDetail;
    binDetail = _binRepository.getBinDetail(id);
    binDetail?.then((data) {
      _binDetail = data!;
    });

    return binDetail;
  }

  // 타입 버튼을 처음 누를때 먼저 호출
  void initBinMarker(Type t) {
    String script = "";

    for(var j in _items[t.index]!) {
      script += "initMarker(${j.lat}, ${j.lng}, ${j.id}, ${t.index}, ${j.isCandidate});";
    }

    controller!.runJavascript(script);

  }

  void toggleBinMarker(Type t) {
    if(onMarker[t.index]){
      unsetBinMarker(t);
    } else {
      if(!onLoad[t.index]){
        loadItems(t).then((_) {
          initBinMarker(t);
          setBinMarker(t);
          onLoad[t.index] = true;
        });
      } else {
        setBinMarker(t);
      }
    }
    onMarker[t.index] = !onMarker[t.index];
    notifyListeners();
  }

  // 타입을 전달받고 해당 타입의 수거함 마커 표시
  void setBinMarker(Type t) {
    String script = "";
    for(var i = 0; i < _items[t.index]!.length ; i++) {
      script += "setMarker(${i + offset[t.index]!});";
    }
    controller!.runJavascript(script);
  }

  // 위의 반대
  void unsetBinMarker(Type t) {
    String script = "";
    for(var i = 0; i < _items[t.index]!.length ; i++) {
      script += "unsetMarker(${i + offset[t.index]!});";
    }
    controller!.runJavascript(script);
  }

  void panToCurrent() {
    final pos = determinePosition();
    pos.then((value) => controller!.runJavascript(""
        "panToCurrent(${value.latitude},${value.longitude})"
        ""));
  }

  void togglePinDrop() {
    if(onPinDrop){
      controller!.runJavascript(""
          "pinDropped.setMap(null);"
          "kakao.maps.event.removeListener(map, 'click', dropPin);"
          "custom.setMap(null);"
          "");
    } else {
      controller!.runJavascript(""
          "pinDropped.setMap(map);"
          "kakao.maps.event.addListener(map, 'click', dropPin);"
          "");
    }
    onPinDrop = !onPinDrop;
    notifyListeners();
  }

  Future<String> loadAssets() async {
    var status = await Permission.storage.status;
    if (!status.isGranted) {
      await Permission.storage.request();
    }

    String cssText = await rootBundle.loadString('assets/map.css');
    String htmlText = await rootBundle.loadString('assets/map.html');
    String jsText = await rootBundle.loadString('assets/map.js');
    ByteData current = await rootBundle.load('assets/markers/current.png');
    List<ByteData> pins = [];
    for(var i=0; i<6; i++) {
      // log('assets/markers/tile00$i.png');
      pins.add(await rootBundle.load('assets/markers/tile00$i.png'));
    }
    List<ByteData> candidates = [];
    for(var i=0; i<6; i++) {
      candidates.add(await rootBundle.load('assets/markers/candidate00$i.png'));
    }

    final tempDir = await getTemporaryDirectory();
    final cssPath = '${tempDir.path}/map.css';
    final htmlPath = '${tempDir.path}/map.html';
    final jsPath = '${tempDir.path}/map.js';
    await Directory('${(tempDir.path)}/markers').create();
    final currentPath = '${tempDir.path}/markers/current.png';
    final pinPath = '${tempDir.path}/markers/';

    await File(cssPath).writeAsString(cssText);
    await File(htmlPath).writeAsString(htmlText);
    await File(jsPath).writeAsString(jsText);
    await File(currentPath).writeAsBytes(
        current.buffer.asUint8List()
    );
    for(var i=0; i<6; i++) {
      await File('${pinPath}tile00$i.png').writeAsBytes(
        pins[i].buffer.asUint8List()
      );
    }
    for(var i=0; i<6; i++) {
      await File('${pinPath}candidate00$i.png').writeAsBytes(
          candidates[i].buffer.asUint8List()
      );
    }

    return (url = Uri(scheme: 'file', path: htmlPath).toString());
  }

  void vote(binId) async {
    final pos = await determinePosition();
    String result = await _binRepository.vote(VoteInfo(
      binId, pos.latitude, pos.longitude
    ));
    if(result == 'ok') {
      // 성공 메세지를 띄우고 voteView를 닫는다.
    } else {
      // 실패 메세지를 띄운다.
    }
  }

  void pop_all() {
    bottom_widget_stack = [];
    _show_floating_button = true;
    hide_all();
  }

  void hide_all() {
    _detail_visible = false;
    _missing_visible = false;
    _condition_visible = false;
    _report_visible = false;
    _suggestion_visible = false;
    notifyListeners();
  }

  void toggle_bottom_widget(String idx) {
    hide_all();
    if (idx == 'detail') {
      pop_all();
      bottom_widget_stack.add('detail');
      _detail_visible = !_detail_visible;
    }
    if (idx == 'missing') {
      if(checkLogin()) {
        bottom_widget_stack.add('missing');
        _missing_visible = !_missing_visible;
      }
    }
    if (idx == 'condition') {
      if(checkLogin()) {
        bottom_widget_stack.add('condition');
        _condition_visible = !_condition_visible;
      }
    }
    if (idx == 'report') {
      if(checkLogin()) {
        bottom_widget_stack.add('report');
        _report_visible = !_report_visible;
      }
    }
    if (idx == 'suggestion') {
      if(checkLogin()) {
        bottom_widget_stack.add('suggestion');
        _report_visible = !_report_visible;
      }
    }
    _show_floating_button = false;
    notifyListeners();
  }

  void pop_bottom_widget() {
    bottom_widget_stack.removeLast();
    if(bottom_widget_stack.isNotEmpty) {
      var temp = bottom_widget_stack.last;
      bottom_widget_stack.removeLast();
      toggle_bottom_widget(temp);
    }
    else {
      pop_all();
    }
    notifyListeners();
  }

  void toggleBinDetail(String id) {
    toggle_bottom_widget('detail');
    loadBinDetail(id).then((_) {
      // _binDetail =
      currentBinId = id;
      List<String> temp = [];
      temp.add(_binDetail['address']);
      temp.add(_binDetail['addressDescription']);
      temp.add(binTypesKor[_binDetail['type']]);

      if(_binDetail['isCandidate']) {
        _show_vote_button = true;
        pin_detail_index = constantValue.binDetailIndexCandidate;
        temp.add("0"); // 수정 필요
      }
      else {
        _show_vote_button = false;
        pin_detail_index = constantValue.binDetailIndex;
      }

      if(_binDetail['hasClosure']) {
        _closure_warning_message = true;
      }
      else {
        _closure_warning_message = false;
      }

      pin_detail_data = temp;
      panToLatLng(_binDetail['latitude'], _binDetail['longitude']);
    });
    notifyListeners();
  }

  void panToLatLng(lat,lng) {
    controller!.runJavascript("map.panTo(new kakao.maps.LatLng($lat, $lng))");
    notifyListeners();
  }

  void parseLatLng(String LatLng) {
    log(LatLng);
    Map<String, dynamic> json = jsonDecode(LatLng);
    custom_lat = json['lat'];
    custom_lng = json['lng'];
    log(custom_lat.toString());
    log(custom_lng.toString());
  }

  void changeDropBinMenu(String binType) {
    dropBinMenu = binType;
    notifyListeners();
  }

  void changeDropGuMenu(String gu) {
    dropGuMenu = gu;
    notifyListeners();
  }

  void showDialog(Dialog_Type dia_t, Widget_Type wid_t ) {
    if(checkLogin()) {
      dia_type = dia_t;
      wid_type = wid_t;
      if (dia_t == Dialog_Type.submit) {
        dialog_title = "작성한 내용으로 제출합니다.";
      }
      if (dia_t == Dialog_Type.vote) {
        dialog_title = "해당 위치에 투표함이 존재합니까?";
      }
      // if () {
      //
      // }
      _dialog_visible = true;
      notifyListeners();
    }
  }

  void dialogLeftPressed(BuildContext context) async {
    late var result;

    if (dia_type == Dialog_Type.response) {
      _dialog_visible = false;
      double_button_dialog = true;
      notifyListeners();
      if (wid_type == Widget_Type.unauthorized) {
        Navigator.of(context).pushReplacement(MaterialPageRoute(
            builder: (context) => (LoginPage()))
        );
      }
      return;
    }
    if (wid_type == Widget_Type.report) {
      final reportPresencesRequest = ReportPresencesRequest(binTypesEng[dropBinMenu], custom_lat, custom_lng);
      result = await _reportRepository.reportPresences(reportPresencesRequest);
    }
    if (wid_type == Widget_Type.missing) {
      final pos = await determinePosition();
      final reportClosuresRequest = ReportWithUserLocationRequest(pos.latitude, pos.longitude);
      result = await _reportRepository.reportClosures(currentBinId, reportClosuresRequest);
    }
    if (wid_type == Widget_Type.suggest) {
      final suggestBinLocationRequest = SuggestBinLocationRequest(dropGuMenu, custom_lat, custom_lng, binTypesEng[dropBinMenu]);
      result = await _suggestRepository.suggestBinLocation(suggestBinLocationRequest);
    }
    if (wid_type == Widget_Type.vote) {
      final pos = await determinePosition();
      final voteCandidate = ReportWithUserLocationRequest(pos.latitude, pos.longitude);
      result = await _reportRepository.voteCandidate(currentBinId, voteCandidate);
    }
    if (wid_type == Widget_Type.condition) {
      final pos = await determinePosition();
      final reportConditionReq = ReportPresencesRequest(binConditionEng[conditionMenu], pos.latitude, pos.longitude);
      result = await _reportRepository.reportCondition(currentBinId, image_path, reportConditionReq);
    }

    log(result);
    if (result == 'unauthorized') {
      dialog_title = '인증 오류가 발생했습니다. 다시 로그인해 주세요.';
      double_button_dialog = false;
      showDialog(Dialog_Type.response, Widget_Type.unauthorized);
    }
    else {
      dialog_title = result;
      double_button_dialog = false;
      showDialog(Dialog_Type.response, Widget_Type.none);
    }
  }

  void dialogRightPressed() {
    _dialog_visible = false;
    notifyListeners();
  }

  void changeConditionMenu(String menu) {
    conditionMenu = menu;
    notifyListeners();
  }


  void updateFile(String file) {
    image_path = file;
    notifyListeners();
  }

  bool checkLogin() {
    final userInfo = UserInfo();
    if(userInfo.id == -1) {
      Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => (LoginPage()))
      );
    }
    if(userInfo.id != -1) {
      return true;
    } else {
      return false;
    }

  }
}
