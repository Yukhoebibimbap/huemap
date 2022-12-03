import 'package:flutter/material.dart';

import 'package:provider/provider.dart';

import 'package:huemap_app/data/repository/signInfo_repository.dart';
import 'package:huemap_app/data/model/signInInfo.dart';
import 'package:huemap_app/ui/page/root_page.dart';

class LoginViewModel with ChangeNotifier {
  late final _signInfoRepository;

  bool isEmailValid = true;
  bool isPasswordValid = true;

  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  final signInInfo = SignInInfo('', '');

  LoginViewModel () {
    _signInfoRepository = SignInfoRepository();
  }

  void signIn(BuildContext context) async {
    signInInfo.email = emailController.text;
    signInInfo.password = passwordController.text;

    var result = await _signInfoRepository.postSignInInfo(signInInfo);

    if(true) {
      Navigator.of(context).pushReplacement(MaterialPageRoute(
          builder: (context) => (RootPage()))
      );
    } else {
      result = -result;

      isEmailValid = !(result%2 == 1);
      result ~/= 2;
      isPasswordValid = !(result%2 == 1);

      notifyListeners();
    }
  }

  void signUp() {

  }
}