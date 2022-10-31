import 'dart:io';
import 'dart:developer';
import 'dart:convert';
import 'package:cp949_codec/cp949_codec.dart';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;

import 'package:webview_flutter/webview_flutter.dart';
import 'package:huemap_app/get_current_position.dart';
import 'package:huemap_app/bin.dart';
import 'package:flutter/foundation.dart';

void main(){runApp(const MaterialApp(home:MapPage()));}


class MapPage extends StatefulWidget {
  final String url = "118.67.130.12/map.html";

  // 채널을 설정, 쓰레기통 정보 가져오고 객체화
  const MapPage({Key? key}) : super(key: key);

  @override
  State<MapPage> createState() => _MapPageState();
}

class _MapPageState extends State<MapPage> {
  // 웹뷰와의 통신을 위한 컨트롤러, 채널
  WebViewController? controller;
  Set<JavascriptChannel>? channel;

  List<Bin>? bins;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(children: [
        Container(
          height: 480,
          child: WebView(
            initialUrl: widget.url,
            onWebViewCreated: (controller) {
              this.controller = controller;
              bins = [Bin(controller, 33.450701, 126.570667, 0)];
            },
            javascriptMode: JavascriptMode.unrestricted,
            javascriptChannels: channel,
          ),
        ),
        ElevatedButton(onPressed: getBins, child: const Text('Button'))
      ],
      ),
    );
  }

  void getBins () async {
    const path = '/api/v1/bins';
    const params = <String, String>{'type' : 'GENERAL'};
    final uri = Uri.https('huemap.shop', path, params);
    final res = await http.get(uri);
    if( res.statusCode == HttpStatus.ok)
      {

      }

  }

  @override
  void initState() {
    super.initState();
    channel = {JavascriptChannel(
        name: 'onClickMarker', onMessageReceived: (message) {
      Fluttertoast.showToast(msg: message.message);
    })};
  }

  void setCategoryMarkers(int category)
  {

  }

  void unsetCategoryMarkers(int category)
  {

  }
}
