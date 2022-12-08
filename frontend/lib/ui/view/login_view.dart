import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/login_viewmodel.dart';
import 'package:huemap_app/ui/view/signup_view.dart';
import 'package:huemap_app/ui/viewmodel/signup_viewmodel.dart';
import 'package:provider/provider.dart';


class LoginView extends StatelessWidget {
  LoginView({Key? key}) : super(key: key);

  late LoginViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<LoginViewModel>(context);
    return Container(
      child: Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          backgroundColor: Colors.white,
          foregroundColor: Colors.black,
          elevation: 0,
          title: const Center(child: Text('로그인')),
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Container(
                  padding: const EdgeInsets.only(left: 10, top: 20),
                  child: const Text('이메일'),
                ),
                Container(
                  padding: const EdgeInsets.all(10),
                  child: TextField(
                      controller: viewModel.emailController,
                      decoration: InputDecoration(
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10),
                        borderSide: BorderSide.none,
                      ),
                      filled: true,
                      fillColor: const Color(0xFFEEEEEE),
                      hintText: '이메일을 입력해 주세요.',
                    )
                  )
                ),
                // viewModel.isEmailValid ? const SizedBox.shrink() :
                // Container(
                //   padding: const EdgeInsets.only(left: 10, bottom: 20),
                //   child:
                //   const Text(
                //       '사용자를 찾을 수 없습니다.',
                //       style: TextStyle(color: Colors.redAccent)
                //   ),
                // ),
                Container(
                  padding: const EdgeInsets.only(left: 10),
                  child: const Text('비밀번호'),
                ),
                Container(
                    padding: const EdgeInsets.all(10),
                    child: TextField(
                        controller: viewModel.passwordController,
                        decoration: InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(10),
                              borderSide: BorderSide.none,
                            ),
                            filled: true,
                            fillColor: const Color(0xFFEEEEEE),
                            hintText: '비밀번호를 입력해 주세요.'
                        )
                    )
                ),
               // viewModel.isPasswordValid ? const SizedBox.shrink() :
               // Container(
               //   padding: const EdgeInsets.only(left: 10, bottom: 20),
               //   child:
               //   const Text(
               //       '비밀번호가 일치하지 않습니다.',
               //       style: TextStyle(color: Colors.redAccent)
               //   ),
               // ),
                viewModel.isValidData ? const SizedBox.shrink() :
                Container(
                  padding: const EdgeInsets.only(left: 10, bottom: 20),
                  child:
                  Text(
                      viewModel.errorMessage,
                      style: TextStyle(color: Colors.redAccent)
                  ),
                ),
                Container(
                  padding: EdgeInsets.only(bottom: 30, top: 20),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Container(
                        width: 160,
                        height: 50,
                        padding: EdgeInsets.symmetric(horizontal:15),
                        child: TextButton(
                          onPressed: () { viewModel.signIn (context); },
                          style: TextButton.styleFrom(
                            backgroundColor: const Color(0xCCE6F4FF),
                            foregroundColor: Colors.black,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            )
                          ),
                          child: const Text(
                              '로그인',
                              style: TextStyle(
                                fontSize: 16,
                              )
                          )
                        ),
                      ),
                      Container(
                        width: 160,
                        height: 50,
                        padding: EdgeInsets.symmetric(horizontal:15),
                        child: TextButton(
                            onPressed: () => Navigator.of(context).push(MaterialPageRoute(
                              builder: (context) => ChangeNotifierProvider<SignupViewModel>(
                              create: (_) => SignupViewModel(), child: SignupView())),
                            ),
                            style: TextButton.styleFrom(
                              backgroundColor: Color(0xFFEEEEEE),
                              foregroundColor: Colors.black,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              )
                            ),
                            child: const Text(
                                '회원가입',
                              style: TextStyle(
                                fontSize: 16,
                              )
                            )
                        ),
                      ),
                    ]
                  )
                ),

              ]
            ),
          ),
        ),
      )

    );
  }
}
