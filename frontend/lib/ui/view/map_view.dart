import 'dart:io';
import 'dart:developer';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:huemap_app/data/model/bin.dart';
import 'package:provider/provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class MapView extends StatelessWidget {
  late MapViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<MapViewModel>(context);
    return FutureBuilder(
      future: viewModel.loadCompleted,
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return Flexible(
            fit: FlexFit.tight,
            child: Container(),
          );
        }
        else {
          return Flexible (
            fit: FlexFit.tight,
            child: Container(
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
                        viewModel.initBinMarker(Type.general);
                        viewModel.controller!.runJavascript("map.panTo(new kakao.maps.LatLng(37.495716, 127.029214))");
                      }
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:10, top: 0, right: 10, bottom: 20),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children:[
                            buttonBuilder(Type.general, "일반"),
                            buttonBuilder(Type.recycle, "재활용"),
                            buttonBuilder(Type.lamp, "폐형광등")
                          ]
                        ),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children:[
                            buttonBuilder(Type.battery, "폐건전지"),
                            buttonBuilder(Type.clothes, "의류"),
                            buttonBuilder(Type.medicine, "폐의약품")
                          ]
                        ),
                      ]
                    ),
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.end,
                    crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      Padding(
                        padding: EdgeInsets.all(10),
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
                  )
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
            if(!viewModel.onLoad[t.index] && viewModel.onMarker[t.index]){}
            else {
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

