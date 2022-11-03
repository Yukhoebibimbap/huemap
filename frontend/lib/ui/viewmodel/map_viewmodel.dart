import 'package:flutter/foundation.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/repository/bin_repository.dart';

class MapViewModel with ChangeNotifier{
  late final BinRepository _binRepository;
  final String url = "118.67.130.12/map.html";
  WebViewController? controller;
  late final Set<JavascriptChannel> channel;

  List<Bin> get items => _items;
  List<Bin> _items = [];

  MapViewModel() {
    // _binRepository = BinRepository();
    // _loadItems();

    channel = {JavascriptChannel(name: 'onClickMarker', onMessageReceived: (message) {
      Fluttertoast.showToast(msg: message.message);
    })};
  }

  Future<void> _loadItems() async {
    _items = await _binRepository.getBins();
    notifyListeners();
  }

  Future<void> notifyListen() async {
    notifyListeners();
  }

  // void initBinMarker(int category) {
  //   controller!.runJavascript(
  //       'initMarker(${lat}, ${lng}, ${category});'
  //   );
  // }
  //
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



}
