import 'package:flutter/material.dart';

class UserView extends StatelessWidget {
  UserView({Key? key}) : super(key: key);

  final List<String> entries = <String>['건의내역', '공지사항', '로그아웃'];

  Widget itemBuilder(BuildContext context, int index) {
    return ListTile(
      title: Text(entries[index]),
      trailing: Icon(Icons.chevron_right),
      onTap: (){},
    );
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      color: const Color(0xFFCCE6F4),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        mainAxisSize: MainAxisSize.max,
        children: [
          Flexible(
            fit: FlexFit.tight,
            flex: 2,
            child: Row(
              mainAxisSize: MainAxisSize.max,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Flexible(
                  fit: FlexFit.tight,
                  flex: 3,

                  child: Container(
                    color: const Color(0xFFCCE6F4),
                    child: Stack(
                      children: [
                        const Padding(
                          padding: EdgeInsets.all(12),
                          child: CircleAvatar(
                            radius: 100,
                            backgroundColor: Colors.white,
                          ),
                        ),
                        const Padding(
                          padding: EdgeInsets.all(15),
                          child: CircleAvatar(
                            radius: 100,
                            backgroundColor: Color(0xFFE7E7E7),
                          ),
                        ),
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            Container(
                              padding: const EdgeInsets.only(top:55, left: 55),
                              width: 85,
                              height: 85,
                              child: FloatingActionButton(
                                onPressed: () {},
                                child: const Icon(Icons.edit),
                              )
                            ),
                            Container(
                              width: double.maxFinite,
                            )
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
                Flexible(
                  fit: FlexFit.tight,
                  flex: 7,
                  child: Padding(
                    padding: const EdgeInsets.all(15),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Container(
                          padding: const EdgeInsets.only(top: 5, left: 5, right: 5, bottom: 10),
                          child: Text(
                            '육빔',
                            style: TextStyle(fontSize: 18),
                          ),
                        ),
                        Container(
                          decoration: const BoxDecoration(
                            color: Color(0xFFE7E7E7),
                            borderRadius: BorderRadius.all(Radius.circular(5)),
                          ),
                          padding: const EdgeInsets.all(5),
                          child: Text(
                            '안녕하세요',
                          ),
                        ),
                      ]
                    ),
                  ),
                ),
              ]
            ),
          ),
          Flexible(
            fit: FlexFit.tight,
            flex: 7,
            child: Container(
              color: Colors.white,
              child: ListView.separated(
                padding: EdgeInsets.all(0),
                itemCount: entries.length,
                itemBuilder: itemBuilder,
                separatorBuilder: (context, index) => const Divider(),
              ),
            ),
          )
        ],
      ),
    );
  }
}
