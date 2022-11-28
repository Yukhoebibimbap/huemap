import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class ReportView extends StatelessWidget {
  const ReportView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).report_visible,
            child: Expanded(
              flex: 15,
              child: SingleChildScrollView(
                child: Container(
                  child: Stack(
                    children: [
                      const Padding(
                        padding: EdgeInsets.all(12),
                        child: Icon(Icons.reply, size: 30),
                      ),
                      SizedBox(
                        width: MediaQuery.of(context).size.width,
                        child: Column(
                          // mainAxisAlignment: MainAxisAlignment.start,
                          // crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              const Padding(
                                padding: EdgeInsets.all(8),
                                child: Text(
                                  '새 수거함 제보 & 건의',
                                  style: TextStyle(fontSize: 24, color:Colors.black),
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.all(8),
                                child: Container(
                                    decoration: const BoxDecoration(
                                      color: Colors.grey,
                                    ),
                                    width: MediaQuery.of(context).size.width * 0.7,
                                    height: MediaQuery.of(context).size.height * 0.25,
                                    child: Center(
                                      child: TextButton(
                                        onPressed: () {},
                                        child: Icon(Icons.photo_camera, color: Colors.black,size: 100),
                                      ),
                                    )
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.fromLTRB(30,15,5,5),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: const [
                                    Text(
                                      "수거함 주소",
                                      style: TextStyle(
                                          fontSize: 24.0,
                                          color: Colors.black,
                                          fontWeight: FontWeight.w300),
                                    ),
                                  ],
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.all(8),
                                child:  Column(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Row(
                                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                          children: const [
                                            Padding(
                                              padding: EdgeInsets.fromLTRB(30,0,0,0),
                                              child: Text(
                                                "사용자 위치",
                                                style: TextStyle(
                                                    fontSize: 18.0,
                                                    color: Colors.black,
                                                    fontWeight: FontWeight.w300),
                                              ),
                                            ),
                                            Padding(
                                              padding: EdgeInsets.fromLTRB(30,0,50,0),
                                              child: Text(
                                                "불일치",
                                                style: TextStyle(
                                                    fontSize: 18.0,
                                                    color: Colors.black,
                                                    fontWeight: FontWeight.w300),
                                              ),
                                            )
                                          ]
                                      ),
                                      Padding(padding: EdgeInsets.all(15),),
                                      Row(
                                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                          children: const [
                                            Padding(
                                              padding: EdgeInsets.fromLTRB(30,0,0,0),
                                              child: Text(
                                                "수거함 종류",
                                                style: TextStyle(
                                                    fontSize: 18.0,
                                                    color: Colors.black,
                                                    fontWeight: FontWeight.w300),
                                              ),
                                            ),
                                            Padding(
                                              padding: EdgeInsets.fromLTRB(30,0,50,0),
                                              child: Text(
                                                "재활용",
                                                style: TextStyle(
                                                    fontSize: 18.0,
                                                    color: Colors.black,
                                                    fontWeight: FontWeight.w300),
                                              ),
                                            )
                                          ]
                                      ),
                                    ]
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.all(10),
                                child: ElevatedButton(
                                    onPressed: () {},
                                    style: ElevatedButton.styleFrom(
                                      fixedSize: const Size(130,30),
                                      backgroundColor: Color(0xFFCCE6F4), // Background color
                                    ),
                                    child: Text("제출",style:TextStyle(color: Colors.black))
                                ),
                              ),
                            ]
                        ),
                      )
                    ],
                  ),
                )
              ),
            )
        )
    );
  }
}