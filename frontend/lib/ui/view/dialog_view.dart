import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class DialogView extends StatelessWidget {
  const DialogView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).dialog_visible,
            child: AlertDialog(
              actionsAlignment: MainAxisAlignment.center,
              title: Text(viewModel.dialog_title),
              actions: <Widget>[
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      fixedSize: const Size(130,30),
                      backgroundColor: Color(0xFFCCE6F4), // Background color
                    ),
                    child: new Text("예", style:TextStyle(color: Colors.black)),
                    onPressed: () {viewModel.dialogLeftPressed();},
                  ),
                ),
                Visibility(
                  visible: Provider.of<MapViewModel>(context).double_button_dialog,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        fixedSize: const Size(130,30),
                        backgroundColor: Color(0xFFCCE6F4), // Background color
                      ),
                      child: new Text("아니오", style:TextStyle(color: Colors.black)),
                      onPressed: () {viewModel.dialogRightPressed();},
                    ),
                  ),
                ),
              ],
            )
        )
    );
  }
}