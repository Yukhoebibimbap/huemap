import 'dart:io'
import 'dart:developer';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class MapView extends StatelessWidget {
  late MapViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<MapViewModel>(context);
    return Column(children: [
      Container(
        height: 480,
        child: WebView(
          initialUrl: viewModel.url,
          onWebViewCreated: (controller) {
            viewModel.controller = controller;
            // viewModel.controller!.runJavascript("marker = new kakao.maps.Marker({position: map.getCenter()});"
            //     "kakao.maps.event.addListener(map, 'click', function(mouseEvent) {"
            //     "var latlng = mouseEvent.latLng;"
            //     "marker.setPosition(latlng);"
            //     "});");
          },
          javascriptMode: JavascriptMode.unrestricted,
          javascriptChannels: viewModel.channel,
        ),
      ),
      ElevatedButton(onPressed: viewModel.notifyListen, child: const Text('Button')),
    ]);
  }
}

