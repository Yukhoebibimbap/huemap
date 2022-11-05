import 'dart:developer';

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

  List<List<Bin>> get items => _items;
  final List<List<Bin>> _items = <List<Bin>>[];

  MapViewModel() {
    _binRepository = BinRepository();

    channel = {JavascriptChannel(name: 'onClickMarker', onMessageReceived: (message) {
      Fluttertoast.showToast(msg: message.message);
    })};
  }

  Future<List<Bin>> loadItems() async {
    late Future<List<Bin>> binData;
    for (Type t in Type.values) {
      binData = _binRepository.getBins(t);
      binData.then((data) {
        _items.add(data);
      });
    }
    return binData;
  }

  void initBinMarker() {
    String script = "";

    for(var i=0; i < _items.length; i++) {
      for(var j in _items[i]) {
        script += "initMarker(${j.lat}, ${j.lng}, ${j.id});";
      }
    }

    log(script);

    controller!.runJavascript(script);

    // 현재 유일한 데이터 위치로 이동
    controller!.runJavascript("map.panTo(new kakao.maps.LatLng(37.495716, 127.029214))");
  }

  // static void setBinMarker() {
  //   controller!.runJavascript(
  //       'setMarker(${idx});'
  //   );
  // }
  //
  // static void unsetBinMarker() {
  //   controller!.runJavascript(
  //       'unsetMarker(${idx});'
  //   );
  // }


  void panToCurrent() {
    final pos = determinePosition();
    pos.then((value) => controller!.runJavascript(""
        "panToCurrent(${value.latitude},${value.longitude})"
        ""));
  }
}
