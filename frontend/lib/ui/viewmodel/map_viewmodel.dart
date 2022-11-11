/*
  map_viewmodel.dart :
  지도 화면에서 출력하는 데이터, 데이터를 조작하는 로직을 다룹니다.

  - 수거함 정보를 불러오고 지도에 표시합니다.
  - 표시할 수거함의 종류를 toggle 변수, 버튼으로 조정합니다.
  - 사용자의 GPS 위치를 불러와 지도를 옮깁니다.

 */

import 'dart:developer'; // 디버깅 목적

import 'package:flutter/foundation.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/repository/bin_repository.dart';
import 'package:huemap_app/get_current_position.dart';

class MapViewModel with ChangeNotifier{
  late final BinRepository _binRepository;
  final String url = "118.67.130.12/mac.html";
  WebViewController? controller;
  late final Set<JavascriptChannel> channel;

  Map<int, List<Bin>> get items => _items;
  late Map<int, int> offset = <int, int>{};
  final Map<int, List<Bin>> _items = <int, List<Bin>>{};

  late Future<List<Bin>> loadCompleted;

  final onLoad = <bool>[true,false,false,false,false,false];
  final onMarker = <bool>[true,false,false,false,false,false];

  MapViewModel() {
    // 데이터 계층 연결, 자바스크립트 채널 생성
    _binRepository = BinRepository();
    loadCompleted = loadItems(Type.general);

    channel = {JavascriptChannel(name: 'onClickMarker', onMessageReceived: (message) {
      Fluttertoast.showToast(msg: message.message);
    })};
  }

  Future<List<Bin>> loadItems(Type t) async {
    late Future<List<Bin>> binData;
    int count = 0;
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
      script += "initMarker(${j.lat}, ${j.lng}, ${j.id});";
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
    log(onMarker[t.index].toString());
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
}
