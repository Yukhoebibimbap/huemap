import 'dart:io';
import 'dart:developer';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/get_current_position.dart';

class MapView extends StatelessWidget {
  late MapViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<MapViewModel>(context);
    return Column(children: [
      FutureBuilder(
      future: viewModel.loadItems(),
      builder: (context, snapshot) {
        if(!snapshot.hasData){
          return Container(
            height: 480,
          );}
        else {
        return Container(
          height: 480,
          child: WebView(
              initialUrl: viewModel.url,
              onWebViewCreated: (controller) {
                viewModel.controller = controller;
              },
              javascriptMode: JavascriptMode.unrestricted,
              javascriptChannels: viewModel.channel,

              onPageFinished: (url) {
                viewModel.initBinMarker();
              }
          ),
        );}
      }),

      ElevatedButton(
          onPressed: viewModel.panToCurrent,
          child: const Text('toCurrentPosition')),
    ]);
  }
}

