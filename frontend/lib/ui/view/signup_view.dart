import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/signup_viewmodel.dart';
void main(){runApp(MaterialApp(home:SignupView()));}


class SignupView extends StatelessWidget {
  SignupView({Key? key}) : super(key: key);
  late SignupViewModel viewModel;

  @override
  Widget build(BuildContext context) {
    viewModel = Provider.of<SignupViewModel>(context);
    return Container(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.white,
          foregroundColor: Colors.black,
          elevation: 0,
          title: const Center(child: Text('회원가입')),
          automaticallyImplyLeading: false,
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
                              hintText: '이메일을 입력해 주세요.'
                          )
                      )
                  ),

                  (viewModel.isEmailValid && !(viewModel.isEmailOverlapped)) ?
                      Container(padding: const EdgeInsets.only(bottom:20)) :
                      const SizedBox.shrink(),

                  viewModel.isEmailValid ? const SizedBox.shrink() :
                  Container(
                    padding: const EdgeInsets.only(left: 10, bottom: 20),
                    child:
                    const Text(
                        '올바른 형식의 이메일 주소여야 합니다.',
                        style: TextStyle(color: Colors.redAccent)
                    ),
                  ),

                  !(viewModel.isEmailOverlapped) ? const SizedBox.shrink() :
                  Container(
                    padding: const EdgeInsets.only(left: 10, bottom: 20),
                    child:
                    const Text(
                        '중복된 메일 주소입니다.',
                        style: TextStyle(color: Colors.redAccent)
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.only(left: 10),
                    child: const Text('닉네임'),
                  ),
                  Container(
                      padding: const EdgeInsets.all(10),
                      child: TextField(
                          controller: viewModel.nameController,
                          decoration: InputDecoration(
                              border: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(10),
                                borderSide: BorderSide.none,
                              ),
                              filled: true,
                              fillColor: const Color(0xFFEEEEEE),
                              hintText: '닉네임을 입력해 주세요.'
                          )
                      )
                  ),
                  Container(
                    padding: const EdgeInsets.only(left: 10, bottom: 20),
                    child: viewModel.isNameValid ? const SizedBox.shrink() :
                    const Text(
                        '닉네임은 특수문자를 제외한 2~10자리여야 합니다.',
                        style: TextStyle(color: Colors.redAccent)
                  ),
                  ),
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
                  Container(
                    padding: const EdgeInsets.only(left: 10, bottom: 30),
                    child: viewModel.isPasswordValid ? const SizedBox.shrink() :
                    const Text(
                        '비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.',
                        style: TextStyle(color: Colors.redAccent)

                  ),
                  ),
                  Container(
                      padding: EdgeInsets.only(bottom: 30),
                      child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Container(
                              width: 160,
                              height: 50,
                              padding: EdgeInsets.symmetric(horizontal:15),
                              child: TextButton(
                                  onPressed: () { viewModel.signUp(context); },
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

  void dispose() {
    viewModel.emailController.dispose();
    viewModel.nameController.dispose();
    viewModel.passwordController.dispose();

  }
}
