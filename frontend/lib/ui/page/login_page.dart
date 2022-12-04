import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/view/signup_view.dart';
import 'package:huemap_app/ui/viewmodel/signup_viewmodel.dart';
import 'package:huemap_app/ui/view/login_view.dart';
import 'package:huemap_app/ui/viewmodel/login_viewmodel.dart';

void main(){runApp(const MaterialApp(debugShowCheckedModeBanner: false, home: LoginPage()));}

class LoginPage extends StatelessWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    SystemChrome.setEnabledSystemUIOverlays([]);
    return Container(
      child: ChangeNotifierProvider<LoginViewModel> (
        create: (_) => LoginViewModel(),
        child: LoginView(),
      )
    );
  }
}
