import 'dart:developer';

import 'package:flutter/material.dart';

import 'package:provider/provider.dart';

import 'package:huemap_app/data/repository/signInfo_repository.dart';
import 'package:huemap_app/data/model/signInInfo.dart';
import 'package:huemap_app/ui/page/root_page.dart';

class LoginViewModel with ChangeNotifier {
  late final _signInfoRepository;

  // bool isEmailValid = true;
  // bool isPasswordValid = true;
  bool isValidData = true;
  String errorMessage = "";

  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  final signInInfo = SignInInfo('', '');

  LoginViewModel () {
    _signInfoRepository = SignInfoRepository();
  }

  void signIn(BuildContext context) async {
    signInInfo.email = emailController.text;
    signInInfo.password = passwordController.text;

    String result = await _signInfoRepository.postSignInInfo(signInInfo);
    // String? value = await SignInfoRepository.flutter_storage.read(key: 'jwt');
    // log(value!);
    if(result == 'success') {
      Navigator.of(context).pushReplacement(MaterialPageRoute(
          builder: (context) => (RootPage()))
      );
    } else {
      // result = -result;
      //
      // isEmailValid = !(result%2 == 1);
      // result ~/= 2;
      // isPasswordValid = !(result%2 == 1);
      isValidData = false;
      errorMessage = result;

      notifyListeners();
    }
  }

  void signUp() {

  }
}