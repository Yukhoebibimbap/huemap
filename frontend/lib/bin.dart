/*
  API호출을 통해 들여온 쓰레기통의 데이터를 dart의 클래스로 생성

  좌표, 분류 등의 속성을 저장

  상세 정보를 불러오는 것은 또 한번의 API호출을 거치는 것이 좋아보임.
 */

import 'package:webview_flutter/webview_flutter.dart';

class Bin {
  final double lat, lng;
  final int category;


  // Json을 클래스의 필드로 대입, 백엔드에 따라 달라짐, 마커를 생성하는 역할도 겸한다.
  // 객체 배열과 같은 순서로 생성함으로 인덱스를 일치.

  // 테스트용 기본 생성자
  Bin(WebViewController? controller, this.lat, this.lng, this.category);

  // json 생성자
  Bin.fromJson(WebViewController controller, Map<String, dynamic> json) :
        lat = json['lat'],
        lng = json['lng'],
        category = json['category']
  {

  }

  void initBinMarker(WebViewController controller) {
    controller!.runJavascript(
        'initMarker(${lat}, ${lng}, ${category});'
    );
  }

  static void setBinMarker(WebViewController controller, int idx) {
    controller!.runJavascript(
        'setMarker(${idx});'
    );
  }

  static void unsetBinMarker(WebViewController controller, int idx) {
    controller!.runJavascript(
        'unsetMarker(${idx});'
    );
  }

}