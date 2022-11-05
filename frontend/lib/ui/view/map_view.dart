import 'dart:io';
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
    return FutureBuilder(
      future: viewModel.loadItems(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return Container(
            height: 480,
          );
        }
        else {
          return Container(
            height: 480,
            child: Stack(
              children: [
                WebView(
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
                Column(
                  mainAxisAlignment: MainAxisAlignment.end,
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    FloatingActionButton(
                        onPressed: viewModel.panToCurrent,
                        child: const Icon(Icons.my_location)
                    ),
                    Container(
                      width: 360,
                    )
                  ]
                )
              ]
            )
          );
        }
      }
    );
  }
}

