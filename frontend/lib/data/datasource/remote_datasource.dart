import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:huemap_app/data/model/bin.dart';

import 'package:http/http.dart' as http;

class RemoteDataSource {
  Future<List<Bin>> getBins(Type t) async {
    List<Bin> list = <Bin>[];

    const path = '/api/v1/bins';
    var params = <String, String>{};
    params.addAll({'type': t.toParameter()});
    final uri = Uri.https('huemap.shop', path, params);
    final res = await http.get(uri);
    if (res.statusCode == HttpStatus.ok) {
      final decoded = utf8.decode(res.bodyBytes);
      final json = jsonDecode(decoded);
      for(var data in json['data']){
        list.add(Bin.fromJson(data));
      }
      return list;
    } else {
      throw Exception("Error on Response");
    }
  }
}