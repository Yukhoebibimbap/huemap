import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/constant_value.dart';

class DetailView extends StatelessWidget {
  const DetailView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    late MapViewModel viewModel;
    MapViewModel model = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);

    return Consumer<MapViewModel>(
          builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).detail_visible,
            child: Expanded(
              flex: 14,
              child: SingleChildScrollView(
                child: Container(
                  child: Stack(
                    alignment: AlignmentDirectional.topStart,
                    children: [
                      TextButton(
                        onPressed: viewModel.pop_bottom_widget,
                        child: Icon(Icons.reply, color: Colors.black,size: 30),
                      ),
                      SizedBox(
                        width: MediaQuery.of(context).size.width,
                        child: Column(
                          children: [
                            const Padding(
                              padding: EdgeInsets.all(8),
                              child: Text(
                                '수거함 상세',
                                style: TextStyle(fontSize: 24, color:Colors.black),
                              ),
                            ),
                            Padding(
                              padding: EdgeInsets.all(8),
                              child: SizedBox(
                                width: MediaQuery.of(context).size.width * 0.8,
                                height: MediaQuery.of(context).size.height * 0.075 * viewModel.pin_detail_index.length,
                                child:  Column(
                                  children: Provider.of<MapViewModel>(context).pin_detail_data.asMap().entries.map<Widget>(
                                          (entry) => Expanded(
                                        child: Row(
                                          children: [
                                            Expanded(
                                              flex: 3,
                                              child: Container(
                                                  decoration: BoxDecoration(
                                                      border: Border.all(color:Colors.grey)
                                                  ),
                                                  alignment: Alignment.center,
                                                  child: Text(
                                                      viewModel.pin_detail_index[entry.key],
                                                      style: const TextStyle(color:Colors.black),
                                                      textAlign: TextAlign.center
                                                  )
                                              ),
                                            ),
                                            Expanded(
                                              flex: 10,
                                              child: Container(
                                                  decoration: BoxDecoration(
                                                      border: Border.all(color:Colors.grey)
                                                  ),
                                                  alignment: Alignment.center,
                                                  child: Padding(
                                                    padding: const EdgeInsets.all(8.0),
                                                    child: Text(entry.value.toString(),style: const TextStyle(color:Colors.black)),
                                                  )
                                              ),
                                            ),
                                          ],
                                        ),
                                      )
                                  ).toList(),
                                ),
                              ),
                            ),
                            Visibility(
                                visible: Provider.of<MapViewModel>(context).closure_warning_message,
                                child: Text(
                                  "주의 : 잘못된 위치 제보를 받은 수거함입니다.",
                                  style: TextStyle(
                                      color: Colors.redAccent,
                                      fontWeight: FontWeight.w300),
                                )
                            ),
                            Padding(
                              padding: EdgeInsets.fromLTRB(30,15,5,5),
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.start,
                                crossAxisAlignment: CrossAxisAlignment.center,
                                children: const [
                                  Text(
                                    "제보",
                                    style: TextStyle(
                                        fontSize: 24.0,
                                        color: Colors.black,
                                        fontWeight: FontWeight.w300),)
                                ],
                              ),
                            ),
                            Padding(
                                padding: EdgeInsets.all(20),
                                child: Padding(
                                  padding: EdgeInsets.fromLTRB(30,0,30,0),
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Row(
                                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                          children: [
                                            ElevatedButton(
                                                onPressed: () {viewModel.toggle_bottom_widget('missing');},
                                                style: ElevatedButton.styleFrom(
                                                  fixedSize: const Size(130,30),
                                                  backgroundColor: Color(0xFFCCE6F4), // Background color
                                                ),
                                                child: Text("잘못된 위치 제보",style:TextStyle(color: Colors.black))
                                            ),
                                            ElevatedButton(
                                                onPressed: () {viewModel.toggle_bottom_widget('condition');},
                                                style: ElevatedButton.styleFrom(
                                                  fixedSize: const Size(130,30),
                                                  backgroundColor: Color(0xFFCCE6F4), // Background color
                                                ),
                                                child: Text("상태 제보",style:TextStyle(color: Colors.black))
                                            )
                                          ]
                                      ),
                                      Padding(padding: EdgeInsets.all(5)),
                                      Visibility(
                                        visible: Provider.of<MapViewModel>(context).show_vote_button,
                                        child: Row(
                                          children: [
                                            ElevatedButton(
                                                onPressed: () {viewModel.showDialog(Dialog_Type.vote, Widget_Type.vote);},
                                                style: ElevatedButton.styleFrom(
                                                  fixedSize: const Size(130,30),
                                                  backgroundColor: Color(0xFFCCE6F4), // Background color
                                                ),
                                                child: Text("투표",style:TextStyle(color: Colors.black))
                                            ),
                                          ],
                                        ),
                                      )
                                    ],
                                  ),
                                )
                            )
                          ],
                        ),
                      ),
                    ],
                  )
                ),
              ),
            ),
          ),
    );
  }
}