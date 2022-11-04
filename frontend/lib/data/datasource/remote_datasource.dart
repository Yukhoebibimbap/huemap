import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:huemap_app/data/model/bin.dart';

import 'package:http/http.dart' as http;

class RemoteDataSource {
  Future<List<Bin>> getBins() async {
    List<Bin> list = <Bin>[];

    const path = '/api/v1/bins';
    const params = <String, String>{'type': 'GENERAL'};
    final uri = Uri.https('huemap.shop', path, params);
    final res = await http.get(uri);
    if (res.statusCode == HttpStatus.ok) {
      final decoded = utf8.decode(res.bodyBytes);
      final json = jsonDecode(decoded);
      log(json['data'].length.toString());
      for(var data in json['data']){
        list.add(Bin.fromJson(data));
      }
      return list;
    } else {
      throw Exception("Error on Response");
    }
  }
}