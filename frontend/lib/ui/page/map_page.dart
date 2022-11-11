import 'package:flutter/material.dart';
import 'package:huemap_app/ui/view/map_view.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

void main(){runApp(const MaterialApp(home:MapPage()));}

class MapPage extends StatelessWidget {
  const MapPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(children: [
        ChangeNotifierProvider<MapViewModel>(
          create: (_) => MapViewModel(),
          child: MapView()),
      ]),
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
      )
    );
  }
}
