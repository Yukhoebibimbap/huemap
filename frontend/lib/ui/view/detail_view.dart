import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class DetailView extends StatelessWidget {
  const DetailView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    late MapViewModel viewModel;
    MapViewModel model = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);

    return Consumer<MapViewModel>(
          builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).pin_visible,
            child: Expanded(
              flex: 14,
              child: SingleChildScrollView(
                child: Container(
                  child: Stack(
                    alignment: AlignmentDirectional.topStart,
                    children: [
                      const Padding(
                        padding: EdgeInsets.all(12),
                        child: Icon(Icons.reply, size: 30),
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
                                width: MediaQuery.of(context).size.width * 0.7,
                                height: MediaQuery.of(context).size.height * 0.25,
                                child:  Column(
                                  children: model.temp_format.asMap().entries.map<Widget>(
                                          (entry) => Expanded(
                                        child: Row(
                                          children: [
                                            Expanded(
                                              flex: 2,
                                              child: Container(
                                                  decoration: BoxDecoration(
                                                      border: Border.all(color:Colors.grey)
                                                  ),
                                                  alignment: Alignment.center,
                                                  child: Text(
                                                      viewModel.pin_detail[entry.key],
                                                      style: const TextStyle(color:Colors.black),
                                                      textAlign: TextAlign.center
                                                  )
                                              ),
                                            ),
                                            Expanded(
                                              flex: 5,
                                              child: Container(
                                                  decoration: BoxDecoration(
                                                      border: Border.all(color:Colors.grey)
                                                  ),
                                                  alignment: Alignment.center,
                                                  child: Text(entry.value.toString(),style: const TextStyle(color:Colors.black))
                                              ),
                                            ),
                                          ],
                                        ),
                                      )
                                  ).toList(),
                                ),
                              ),
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
                                                onPressed: () {viewModel.toggle_report();},
                                                style: ElevatedButton.styleFrom(
                                                  fixedSize: const Size(130,30),
                                                  backgroundColor: Color(0xFFCCE6F4), // Background color
                                                ),
                                                child: Text("잘못된 위치 제보",style:TextStyle(color: Colors.black))
                                            ),
                                            ElevatedButton(
                                                onPressed: () {},
                                                style: ElevatedButton.styleFrom(
                                                  fixedSize: const Size(130,30),
                                                  backgroundColor: Color(0xFFCCE6F4), // Background color
                                                ),
                                                child: Text("상태 제보",style:TextStyle(color: Colors.black))
                                            )
                                          ]
                                      ),
                                      Padding(padding: EdgeInsets.all(5)),
                                      Row(
                                        children: [
                                          ElevatedButton(
                                              onPressed: () {},
                                              style: ElevatedButton.styleFrom(
                                                fixedSize: const Size(130,30),
                                                backgroundColor: Color(0xFFCCE6F4), // Background color
                                              ),
                                              child: Text("투표",style:TextStyle(color: Colors.black))
                                          ),
                                        ],
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