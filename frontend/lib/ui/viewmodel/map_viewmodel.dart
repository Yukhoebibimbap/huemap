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

import 'package:flutter/foundation.dart';
import 'package:path_provider/path_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:permission_handler/permission_handler.dart';


import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/repository/bin_repository.dart';
import 'package:huemap_app/get_current_position.dart';
import 'package:huemap_app/data/model/voteInfo.dart';

import 'package:flutter/services.dart' show ImmutableBuffer, rootBundle;

class MapViewModel with ChangeNotifier{
  late final BinRepository _binRepository;
  WebViewController? controller;
  late final String url;
  late final Set<JavascriptChannel> channel;
  int count = 0;

  Map<int, List<Bin>> get items => _items;
  late Map<int, int> offset = <int, int>{};
  final Map<int, List<Bin>> _items = <int, List<Bin>>{};

  late Future<List<Bin>> binLoadCompleted;
  late Future<String> assetLoadCompleted;

  final onLoad = <bool>[true,false,false,false,false,false];
  final onMarker = <bool>[true,false,false,false,false,false];
  bool onPinDrop = false;

  MapViewModel() {
    // 데이터 계층 연결, 자바스크립트 채널 생성
    _binRepository = BinRepository();
    binLoadCompleted = loadItems(Type.general);
    assetLoadCompleted = loadAssets();


    channel = {JavascriptChannel(name: 'onClickMarker', onMessageReceived: (message) {
      Fluttertoast.showToast(msg: message.message);
    })};
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
}
