import 'dart:io';
import 'dart:developer';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/ui/view/suggestion_view.dart';
import 'package:provider/provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/ui/view/detail_view.dart';
import 'package:huemap_app/ui/view/report_view.dart';
import 'package:huemap_app/ui/view/suggestion_view.dart';
import 'package:huemap_app/ui/view/missing_view.dart';
import 'package:huemap_app/ui/view/condition_view.dart';
import 'package:huemap_app/ui/view/dialog_view.dart';

class MapView extends StatelessWidget {
  late MapViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<MapViewModel>(context);
    return FutureBuilder(
      future: Future.wait([viewModel.binLoadCompleted, viewModel.assetLoadCompleted]),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return Flexible(
            fit: FlexFit.tight,
            child: Container(),
          );
        }
        else {
          viewModel.context = context;
          return Flexible (
            fit: FlexFit.tight,
            child: Container(
              child: Stack(
                children: [
                  Column(
                    children: [
                      Expanded(
                        flex: 10,
                        child: WebView(
                            initialUrl: viewModel.url,
                            onWebViewCreated: (controller) {
                              viewModel.controller = controller;

                            },
                            javascriptMode: JavascriptMode.unrestricted,
                            javascriptChannels: viewModel.channel,

                            onPageFinished: (url) {
                              viewModel.initBinMarker(Type.general);
                              viewModel.setBinMarker(Type.general);
                              viewModel.controller!.runJavascript("map.panTo(new kakao.maps.LatLng(37.495716, 127.029214))");
                            }
                        ),
                      ),
                      const DetailView(),
                      const ReportView(),
                      const SuggestionView(),
                      const MissingView(),
                      const ConditionView(),
                    ],
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:10, top: 0, right: 10, bottom: 20),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children:[
                            buttonBuilder(Type.general, "A 일반"),
                            buttonBuilder(Type.recycle, "B 재활용"),
                            buttonBuilder(Type.lamp, "C 형광등")
                          ]
                        ),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children:[
                            buttonBuilder(Type.battery, "D 건전지"),
                            buttonBuilder(Type.clothes, "E 의류"),
                            buttonBuilder(Type.medicine, "F 의약품")
                          ]
                        ),
                      ]
                    ),
                  ),
                  Visibility(
                    visible: viewModel.show_floating_button,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.end,
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(10),
                          child: FloatingActionButton(
                            onPressed: viewModel.togglePinDrop,
                            backgroundColor: viewModel.onPinDrop ? Colors.blueAccent : Colors.white,
                            foregroundColor: viewModel.onPinDrop ? Colors.white : Colors.blueAccent,
                            child: const Icon(Icons.pin_drop),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(10),
                          child: FloatingActionButton(
                            onPressed: viewModel.panToCurrent,
                            child: const Icon(Icons.my_location),
                          ),
                        ),
                        Flexible(
                          fit: FlexFit.tight,
                          flex: 0,
                          child:Container(),
                        ),
                      ]
                    ),
                  ),
                  const DialogView()
                ]
              )
            )
          );
        }
      }
    );
  }

  Widget buttonBuilder(Type t, String text) {
    return Flexible(
      fit: FlexFit.tight,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 5),
        child:OutlinedButton(
          onPressed: () {
            if(!viewModel.onMarker[t.index]) {
              for(var type in Type.values) {
                if(viewModel.onMarker[type.index]) {
                  viewModel.toggleBinMarker(type);
                }
              }
              viewModel.toggleBinMarker(t);

            }
          },
          style: OutlinedButton.styleFrom(
            shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.all(
                Radius.circular(18),
              ),
            ),
            backgroundColor: viewModel.onMarker[t.index] ?
            Colors.blueAccent : Colors.white
          ),
          child: Text(
              text,
              style: TextStyle(color:
              viewModel.onMarker[t.index] ?
              Colors.white : Colors.blueAccent)
          ),
        ),
      ),
    );
  }
}

