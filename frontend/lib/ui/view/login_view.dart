import 'package:flutter/material.dart';

void main(){runApp(const MaterialApp(home:LoginView()));}


class LoginView extends StatelessWidget {
  const LoginView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.white,
          foregroundColor: Colors.black,
          elevation: 0,
          title: const Center(child: Text('로그인')),
        ),
        body: Column(
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
            Container(
              padding: const EdgeInsets.only(left: 10),
              child: const Text('비밀번호'),
            ),
            Container(
                padding: const EdgeInsets.only(top: 10, left: 10, right: 10, bottom: 30),
                child: TextField(
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
              padding: EdgeInsets.only(bottom: 30),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    width: 160,
                    height: 50,
                    padding: EdgeInsets.symmetric(horizontal:15),
                    child: TextButton(
                      onPressed: (){},
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
                        onPressed: (){},
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
            Container(
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Container(
                        width: 80,
                        height: 80,
                        padding: EdgeInsets.symmetric(horizontal:10),
                        child: ElevatedButton(
                          onPressed: () {},
                          style: ElevatedButton.styleFrom(
                            shape: CircleBorder(),
                            backgroundColor: Colors.blue,
                          ),
                          child: Text('a', style: TextStyle(fontSize: 24)),
                        ),
                      ),
                      Container(
                        width: 80,
                        height: 80,
                        padding: EdgeInsets.symmetric(horizontal: 10),
                        child: ElevatedButton(
                          onPressed: () {},
                          child: Text('b', style: TextStyle(fontSize: 24)),
                          style: ElevatedButton.styleFrom(
                            shape: CircleBorder(),
                            backgroundColor: Colors.blue,
                          ),
                        ),
                      ),
                      Container(
                        width: 80,
                        height: 80,
                        padding: EdgeInsets.symmetric(horizontal: 10),
                        child: ElevatedButton(
                          onPressed: () {},
                          child: Text('c', style: TextStyle(fontSize: 24)),
                          style: ElevatedButton.styleFrom(
                            shape: CircleBorder(),
                            backgroundColor: Colors.blue,
                          ),
                        ),
                      ),
                    ]
                )
            ),
          ]
        ),
      )

    );
  }
}
