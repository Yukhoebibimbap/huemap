import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/model/binDetail.dart';
import 'package:huemap_app/data/model/userInfo.dart';

import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';
import 'package:requests/requests.dart';
import 'package:dio/dio.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';
import 'package:form_data/form_data.dart' as form_data;

class RemoteDataSource {
  RemoteDataSource._privateConstructor();
  static final RemoteDataSource _instance = RemoteDataSource._privateConstructor();
  static final flutter_storage = FlutterSecureStorage();

  factory RemoteDataSource() {
    return _instance;
  }

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

  Future<Map<String,dynamic>> getBinDetail(String id) async {
    BinDetail? binDetail;

    var path = '/api/v1/bins/$id';
    // var params = <String, String>{};
    // params.addAll({'type': t.toParameter()});
    final uri = Uri.https('huemap.shop', path);
    final res = await http.get(uri);
    print('Response body: ${res.body}');
    if (res.statusCode == HttpStatus.ok) {
      final decoded = utf8.decode(res.bodyBytes);
      Map<String, dynamic> json = jsonDecode(decoded);
      binDetail = BinDetail.fromJson(json['data']);
      log(json['data'].toString());
      return binDetail.toJson();
    } else {
      throw Exception("Error on Response");
    }
  }
  
  Future<int> postSignUpInfo(signUpInfo) async {
    const path = '/api/v1/users';
    final uri = Uri.https('huemap.shop', path);
    final headers = {'Content-Type' : 'application/json'};
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(signUpInfo);
    final res = await http.post(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );
    final decoded = utf8.decode(res.bodyBytes);
    final json = jsonDecode(decoded);

    if(json['message'] == 'ok'){
      return json['data']['id'];
    } else if(json['message'] == '적절하지 않은 요청 값입니다.') {
      var ret = 0;
      for(var error in json['errors']) {
        switch(error['field']) {
          case 'email':
            ret += 1;
            break;
          case 'name':
            ret += 2;
            break;
          case 'password':
            ret += 4;
            break;
        }
      }
      return -ret;
    } else {
      return -8;
    }
  }

  Future<String> postSignInInfo(signInInfo) async {
    const path = '/api/v1/users/login';
    final uri = Uri.https('huemap.shop', path);
    final headers = {'Content-Type' : 'application/json'};
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(signInInfo);
    final res = await http.post(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );
    final decoded = utf8.decode(res.bodyBytes);
    var json = jsonDecode(decoded);

    print('Response body: ${res.body}');
    if(res.statusCode == HttpStatus.ok){
      flutter_storage.write(key: 'jwt', value: json['accessToken']);
      log('success');
      
      final userInfo = UserInfo();
      userInfo.setUserInfo(signInInfo.email, json['id'], json['accessToken'], json['grantType']);
      
      return 'success';
    } else {
      return json['message'];
    }
  }

  Future<String> vote(voteInfo) async {
    final path = '/api/v1/bins/${voteInfo.binId}/vote';
    final uri = Uri.https('huemap.shop', path);
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(
      {
        "latitude": voteInfo.latitude,
        "longitude": voteInfo.longitude
      }
    );
    final res = await http.post(
        uri,
        body: jsonString,
        encoding: encoding
    );

    if(res.statusCode == HttpStatus.ok) {
      return "ok";
    } else {
      final decoded = utf8.decode(res.bodyBytes);
      var json = jsonDecode(decoded);
      return json['message'];
    }

  }

  Future<String> reportPresences(reportPresencesRequest) async {
    return 'unauthorized';
    const path = '/api/v1/bins/report-presences';
    final uri = Uri.https('huemap.shop', path);
    final token = await flutter_storage.read(key: 'jwt');
    log(token!);
    final headers = {
      "Content-Type": "application/json",
      "Accept": "*/*",
      'Authorization': 'Bearer $token',
    };
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(reportPresencesRequest);
    final res = await http.post(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );
    // print('Response header: ${res.headers}');
    // print('Response body: ${res.body}');
    final decoded = utf8.decode(res.bodyBytes);
    final json = jsonDecode(decoded);

    if(res.statusCode == HttpStatus.created){
      return '정상적으로 처리 되었습니다.';
    } else if (res.statusCode == HttpStatus.unauthorized) {
      return 'unauthorized';
    } else {
      log('failed');
      return json['message'];
    }
  }

  Future<String> reportClosures(binId, reportClosuresRequest) async {
    String path = '/api/v1/bins/$binId/report-closures';
    final uri = Uri.https('huemap.shop', path);
    final token = await flutter_storage.read(key: 'jwt');
    log(token!);
    final headers = {
      "Content-Type": "application/json",
      "Accept": "*/*",
      'Authorization': 'Bearer $token',
    };
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(reportClosuresRequest);
    // log(path);
    log(jsonString);
    final res = await http.post(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );

    final decoded = utf8.decode(res.bodyBytes);
    final json = jsonDecode(decoded);

    if(res.statusCode == HttpStatus.created){
      return '정상적으로 처리되었습니다.';
    } else if (res.statusCode == HttpStatus.unauthorized) {
      return 'unauthorized';
    } else {
      return json['message'];
    }
  }

  Future<String> voteCandidate(binId, reportClosuresRequest) async {
    String path = '/api/v1/bins/$binId/vote';
    final uri = Uri.https('huemap.shop', path);
    final token = await flutter_storage.read(key: 'jwt');
    log(path);
    log(token!);
    final headers = {
      "Content-Type": "application/json",
      "Accept": "application/json",
      'Authorization': 'Bearer $token',
    };
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(reportClosuresRequest);
    log(jsonString);
    final res = await http.put(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );

    if(res.statusCode == HttpStatus.ok){
      return '정상적으로 처리되었습니다.';
    } else if (res.statusCode == HttpStatus.unauthorized) {
      return 'unauthorized';
    } else {
      final decoded = utf8.decode(res.bodyBytes);
      final json = jsonDecode(decoded);
      print('Response body: ${res.body}');
      return json['message'];
    }
  }

  Future<String> reportCondition(binId, file_path, voteCandidateRequest) async {
    // var _dio = Dio();
    // Dio addInterceptors(Dio dio) {
    //   return dio..interceptors.add(InterceptorsWrapper(
    //       onRequest: (RequestOptions options) => requestInterceptor(options),
    //       onResponse: (Response response) => responseInterceptor(response),
    //       onError: (DioError dioError) => errorInterceptor(dioError)));
    // }
    // void setupDio() {
    //   _dio.interceptors.add(DioInterceptors());
    //   _dio.options.baseUrl = _config.restBaseURL;
    //   _dio.options.contentType = 'application/json';
    // };
    // dio.interceptors.add(PrettyDioLogger());
    // dio.interceptors.add(PrettyDioLogger(
    //     requestHeader: true,
    //     requestBody: true,
    //     responseBody: true,
    //     responseHeader: false,
    //     error: true,
    //     compact: true,
    //     maxWidth: 90));
    // // https://huemap.shop/
    // String path = 'https://huemap.shop/api/v1/bins/$binId/report-condition';
    // final uri = Uri.https('huemap.shop', path);
    // final token = await flutter_storage.read(key: 'jwt');
    // // log(path);
    // // log(token!);
    // final headers = {
    //   "Content-Type": "multipart/form-data; application/json; charset=utf-8",
    //   'Authorization': 'Bearer $token',
    // };
    // final encoding = Encoding.getByName('utf-8');
    // final jsonString = jsonEncode(voteCandidateRequest);
    // // // log(jsonString);
    // // final res = await http.post(
    // //     uri,
    // //     headers: headers,
    // //     body: jsonString,
    // //     encoding: encoding
    // // );
    // var formData = FormData.fromMap({
    //   // 'file': await MultipartFile.fromFile(file_path,filename: 'upload.jpg', contentType: MediaType('multipart','form-data')),
    //   'dto': jsonString
    // });
    // // var formData = form_data.FormData();
    // // formData.addFile('file', await File(file_path).readAsBytes(),
    // //     filename: 'upload.png', contentType: 'multipart/form-data'
    // // );
    // // formData.add('dto', jsonString);
    // final res = await dio.post(
    //     path,
    //     data: formData,
    //     options: Options(headers: {
    //       "Authorization": "Bearer $token",
    //       // 'Accept-Encoding': "gzip, deflate, br",
    //       // 'Accept': '*/*',
    //       // 'Connection': 'keep-alive',
    //       'Content-Type': 'application/json'
    //     })
    // );

    // final request = http.MultipartRequest('POST', Uri.parse(path));
    // request.headers.addAll({
    //   "Content-Type": "application/json",
    //   'Authorization': 'Bearer $token',
    // });
    // request.fields['dto']=jsonString;
    // request.files.add(
    //     await http.MultipartFile.fromPath(
    //       'file', file_path,
    //       contentType: MediaType('multipart','form-data'),
    //     )
    // );
    // print(request.headers);
    // print(request.files);
    // print(request.fields);
    // var res = await request.send();
    // print('Response body: ${request.headers}');
    // final json = jsonDecode(res.data);
    // print('Response body: ${json}');

    // final url = Uri.parse('https://huemap.shop/api/v1/bins/$binId/report-condition');

    // final format = DateFormat('yyyy-MM-dd');
    // final completionDate = format.format(project.completionDate);



    // if(res.statusCode == HttpStatus.created){
    //   return '정상적으로 처리되었습니다.';
    // } else if (res.statusCode == HttpStatus.unauthorized) {
    //   return 'unauthorized';
    // } else {
    //   // final decoded = utf8.decode(res.stream.bytesToString());
    //   return 'fail';
    // }
    return "fuck u";
  }

  Future<String> suggestBinLocation(suggestBinLocationRequest) async {
    String path = '/api/v1/suggestions/bin-location';
    final uri = Uri.https('huemap.shop', path);
    final token = await flutter_storage.read(key: 'jwt');
    log(token!);
    final headers = {
      "Content-Type": "application/json",
      "Accept": "*/*",
      'Authorization': 'Bearer $token',
    };
    final encoding = Encoding.getByName('utf-8');
    final jsonString = jsonEncode(suggestBinLocationRequest);
    log(jsonString);
    final res = await http.post(
        uri,
        headers: headers,
        body: jsonString,
        encoding: encoding
    );
    final decoded = utf8.decode(res.bodyBytes);
    final json = jsonDecode(decoded);

    if(res.statusCode == HttpStatus.created){
      return '정상적으로 처리되었습니다.';
    } else if (res.statusCode == HttpStatus.unauthorized) {
      return 'unauthorized';
    } else {
      return json['message'];
    }
  }
}