import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/get_camera_image.dart';
import 'package:huemap_app/constant_value.dart';
import 'package:rotating_icon_button/rotating_icon_button.dart';

class ConditionView extends StatelessWidget {
  const ConditionView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);
    final constantValue = ConstantValue();
    // List<String> dropdownList = ['가득참', '주변 더러움', '수거함 불량'];
    // String selectedDropdown = '가득참';

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).condition_visible,
            child: Expanded(
              flex: 18,
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
                                    '수거함 상태 제보',
                                    style: TextStyle(fontSize: 24, color:Colors.black),
                                  ),
                                ),
                                const Padding(
                                  padding: EdgeInsets.all(8),
                                  child:
                                  CameraView(),
                                ),
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
                                        // Padding(padding: EdgeInsets.all(10),),
                                        Row(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            children: [
                                              Padding(
                                                padding: EdgeInsets.fromLTRB(30,0,0,0),
                                                child: Text(
                                                  "수거함 상태",
                                                  style: TextStyle(
                                                      fontSize: 18.0,
                                                      color: Colors.black,
                                                      fontWeight: FontWeight.w300),
                                                ),
                                              ),
                                              // Padding(
                                              //   padding: EdgeInsets.fromLTRB(30,0,50,0),
                                              //   child: Text(
                                              //     "재활용",
                                              //     style: TextStyle(
                                              //         fontSize: 18.0,
                                              //         color: Colors.black,
                                              //         fontWeight: FontWeight.w300),
                                              //   ),
                                              // ),
                                              Padding(
                                                padding: EdgeInsets.fromLTRB(0,0,50,0),
                                                child: DropdownButton(
                                                    value: Provider.of<MapViewModel>(context).conditionMenu,
                                                    items: constantValue.dropdownConditionList.map((String item) {
                                                      return DropdownMenuItem<String>(
                                                        child: Text('$item'),
                                                        value: item,
                                                      );
                                                    }).toList(),
                                                    onChanged: (dynamic value) {viewModel.changeConditionMenu(value);}
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
                                      onPressed: () {viewModel.showDialog(Dialog_Type.submit, Widget_Type.condition);},
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