import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/constant_value.dart';
import 'package:rotating_icon_button/rotating_icon_button.dart';

class MissingView extends StatelessWidget {
  const MissingView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);
    List<String> dropdownList = ['일반 쓰레기', '재활용 쓰레기', '폐형광등', '폐건전지', '의류','폐의약품'];
    String selectedDropdown = '일반 쓰레기';

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).missing_visible,
            child: Expanded(
              flex: 5,
              child: SingleChildScrollView(
                  child: Container(
                    child: Stack(
                      children: [
                        TextButton(
                          onPressed: viewModel.pop_bottom_widget,
                          child: Icon(Icons.reply, color: Colors.black,size: 30),
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
                                    '존재하지 않는 수거함 제보',
                                    style: TextStyle(fontSize: 24, color:Colors.black),
                                  ),
                                ),
                                // Padding(
                                //   padding: EdgeInsets.fromLTRB(30,15,5,5),
                                //   child: Row(
                                //     mainAxisAlignment: MainAxisAlignment.start,
                                //     crossAxisAlignment: CrossAxisAlignment.center,
                                //     children: const [
                                //       Text(
                                //         "수거함 주소",
                                //         style: TextStyle(
                                //             fontSize: 24.0,
                                //             color: Colors.black,
                                //             fontWeight: FontWeight.w300),
                                //       ),
                                //     ],
                                //   ),
                                // ),
                                Padding(
                                  padding: EdgeInsets.all(8),
                                  child:  Column(
                                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                      children: [
                                        Row(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            children: [
                                              const Padding(
                                                padding: EdgeInsets.fromLTRB(30,0,0,0),
                                                child: Text(
                                                  "현재 위치 확인",
                                                  style: TextStyle(
                                                      fontSize: 18.0,
                                                      color: Colors.black,
                                                      fontWeight: FontWeight.w300),
                                                ),
                                              ),
                                              Padding(
                                                padding: EdgeInsets.fromLTRB(30,0,50,0),
                                                child: Row(
                                                  children: [
                                                    Visibility(
                                                      visible: Provider.of<MapViewModel>(context).location_mismatch,
                                                      child: const Text(
                                                        "위치 불일치",
                                                        style: TextStyle(
                                                            fontSize: 15.0,
                                                            color: Colors.redAccent,
                                                            fontWeight: FontWeight.w300),
                                                      ),
                                                    ),
                                                    Padding(padding: EdgeInsets.all(3)),
                                                    RotatingIconButton(
                                                      onTap: () {viewModel.panToCurrent();},
                                                      elevation: 3.0,
                                                      borderRadius: 20.0,
                                                      rotateType: RotateType.full,
                                                      background: Colors.white60,
                                                      child: const Icon(
                                                        Icons.refresh_sharp,
                                                        size: 17,
                                                      ),
                                                    ),
                                                  ],
                                                ),
                                              ),
                                            ]
                                        ),
                                      ]
                                  ),
                                ),
                                Padding(
                                  padding: EdgeInsets.all(15),
                                  child: ElevatedButton(
                                      onPressed: () {viewModel.showDialog(Dialog_Type.submit, Widget_Type.missing);},
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