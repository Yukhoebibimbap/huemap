import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:huemap_app/ui/view/map_view.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/ui/view/user_view.dart';
import 'package:huemap_app/data/model/userInfo.dart';
import 'package:huemap_app/ui/page/login_page.dart';

void main(){runApp(const MaterialApp(debugShowCheckedModeBanner: false,home:RootPage()));}

class RootPage extends StatefulWidget {
  const RootPage({Key? key}) : super(key: key);

  @override
  State<RootPage> createState() => _RootPageState();
}

class _RootPageState extends State<RootPage> {
  static final _widgetOptions = <Widget> [
    Column(children: [
      ChangeNotifierProvider<MapViewModel>(
          create: (_) => MapViewModel(),
          child: MapView()),
    ]),
    Column(
      children: [
        Flexible(
          fit: FlexFit.tight,
          child: UserView(),
        ),
      ])
  ];

  int _selectedIndex = 0;

  @override
  Widget build(BuildContext context) {

    SystemChrome.setEnabledSystemUIOverlays([]);

    return Scaffold(
        body: _widgetOptions.elementAt(_selectedIndex),
        bottomNavigationBar: BottomNavigationBar(
          items: const <BottomNavigationBarItem> [
            BottomNavigationBarItem(
              icon: Icon(Icons.map),
              label: '지도',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.account_circle),
              label: '사용자',
            )
          ],
          currentIndex: _selectedIndex,
          onTap: _onItemTapped,
        )
    );
  }
  void _onItemTapped(int index) {
    final userInfo = UserInfo();
    if(index == 1) {
      if(userInfo.id == -1) {
        Navigator.of(context).push(MaterialPageRoute(
            builder: (context) => (LoginPage()))
        );
      }
    }
    if(userInfo.id != -1) {
      setState(() {
        _selectedIndex = index;
      });
    }
  }
}