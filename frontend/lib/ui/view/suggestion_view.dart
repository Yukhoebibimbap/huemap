import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/constant_value.dart';

class SuggestionView extends StatelessWidget {
  const SuggestionView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);
    var constantValue = ConstantValue();
    // List<String> dropdownBinTypeList = ['일반 쓰레기', '재활용 쓰레기', '폐형광등', '폐건전지', '의류','폐의약품'];
    // List<String> dropdownGuList = ['강남구','강동구','강북구','강서구','관악구','광진구','구로구','금천구','노원구','도봉구','동대문구','동작구','마포구','서대문구','서초구','성동구','성북구','송파구','양천구','영등포구','용산구','은평구','종로구','중구','중랑구'];

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).suggestion_visible,
            child: Expanded(
              flex: 10,
              child: SingleChildScrollView(
                  child: Container(
                    child: Stack(
                      children: [
                        TextButton(
                          onPressed: viewModel.hide_all,
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
                                    '새 수거함 건의',
                                    style: TextStyle(fontSize: 24, color:Colors.black),
                                  ),
                                ),
                                // Padding(
                                //   padding: EdgeInsets.all(8),
                                //   child: Container(
                                //       decoration: const BoxDecoration(
                                //         color: Colors.grey,
                                //       ),
                                //       width: MediaQuery.of(context).size.width * 0.7,
                                //       height: MediaQuery.of(context).size.height * 0.25,
                                //       child: Center(
                                //         child: TextButton(
                                //           onPressed: () {},
                                //           child: Icon(Icons.photo_camera, color: Colors.black,size: 100),
                                //         ),
                                //       )
                                //   ),
                                // ),
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
                                            children: [
                                              const Padding(
                                                padding: EdgeInsets.fromLTRB(30,0,0,0),
                                                child: Text(
                                                  "자치구",
                                                  style: TextStyle(
                                                      fontSize: 18.0,
                                                      color: Colors.black,
                                                      fontWeight: FontWeight.w300),
                                                ),
                                              ),
                                              Padding(
                                                padding: EdgeInsets.fromLTRB(0,0,50,0),
                                                child: DropdownButton(
                                                    value: Provider.of<MapViewModel>(context).dropGuMenu,
                                                    items: constantValue.dropdownGuList.map((String item) {
                                                      return DropdownMenuItem<String>(
                                                        child: Text('$item'),
                                                        value: item,
                                                      );
                                                    }).toList(),
                                                    onChanged: (dynamic value) {viewModel.changeDropGuMenu(value);}
                                                ),
                                              )
                                            ]
                                        ),
                                        Padding(padding: EdgeInsets.all(15),),
                                        Row(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            children: [
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
                                                padding: EdgeInsets.fromLTRB(0,0,50,0),
                                                child: DropdownButton(
                                                    value: Provider.of<MapViewModel>(context).dropBinMenu,
                                                    items: constantValue.dropdownBinTypeList.map((String item) {
                                                      return DropdownMenuItem<String>(
                                                        child: Text('$item'),
                                                        value: item,
                                                      );
                                                    }).toList(),
                                                    onChanged: (dynamic value) {viewModel.changeDropBinMenu(value);}
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
                                      onPressed: () {viewModel.showDialog(Dialog_Type.submit, Widget_Type.suggest);},
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