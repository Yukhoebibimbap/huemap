import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:huemap_app/data/repository/signInfo_repository.dart';
import 'package:huemap_app/data/model/signUpInfo.dart';
import 'package:huemap_app/ui/page/root_page.dart';


import 'dart:developer';

class SignupViewModel with ChangeNotifier {
  late final _signInfoRepository;

  bool isEmailValid = true;
  bool isEmailOverlapped = false;
  bool isNameValid = true;
  bool isPasswordValid = true;

  final emailController = TextEditingController();
  final nameController = TextEditingController();
  final passwordController = TextEditingController();

  final signupInfo = SignUpInfo('', '', '');


  SignupViewModel () {
    _signInfoRepository = SignInfoRepository();
  }

  void signUp(BuildContext context) async {
    signupInfo.email = emailController.text;
    signupInfo.name  = nameController.text;
    signupInfo.password = passwordController.text;

    var result = await _signInfoRepository.postSignUpInfo(signupInfo);

    if(result >= 0) {
      Navigator.of(context).pop();
    } else {
      result = -result;

      isEmailValid = !(result%2 == 1);
      result ~/= 2;
      isNameValid = !(result%2 == 1);
      result ~/= 2;
      isPasswordValid = !(result%2 == 1);
      result ~/= 2;
      isEmailOverlapped = (result%2 == 1);

      log(isEmailValid.toString());
      log(isNameValid.toString());
      log(isPasswordValid.toString());
      log(isEmailOverlapped.toString());

      notifyListeners();
    }
  }
}