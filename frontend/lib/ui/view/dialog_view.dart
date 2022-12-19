import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';
import 'package:huemap_app/ui/viewmodel/login_viewmodel.dart';

class DialogView extends StatelessWidget {
  const DialogView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);
    // LoginViewModel loginViewModel = new LoginViewModel();
    // loginViewModel = Provider.of<LoginViewModel>(context) ;

    return Consumer<MapViewModel>(
        builder: (context, countProvider, child) => Visibility(
            visible: Provider.of<MapViewModel>(context).dialog_visible,
            child: AlertDialog(
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0)
              ),
              actionsAlignment: MainAxisAlignment.center,
              title: Visibility(
                  visible: !Provider.of<MapViewModel>(context).api_loading,
                  child: Text(viewModel.dialog_title)
              ),
              content: Visibility(
                  visible: Provider.of<MapViewModel>(context).api_loading,
                  child: const SizedBox(
                    height: 100,
                    child: Center(
                        child:SizedBox(
                          height: 50.0,
                          width: 50.0,
                          child: CircularProgressIndicator(
                              valueColor: AlwaysStoppedAnimation(Colors.blue),
                              strokeWidth: 5.0
                          ),
                        )
                    ),
                  ),
              ),
              actions: <Widget>[
                Visibility(
                  visible: !Provider.of<MapViewModel>(context).api_loading,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        fixedSize: const Size(130,30),
                        backgroundColor: Color(0xFFCCE6F4), // Background color
                      ),
                      child: new Text("예", style:TextStyle(color: Colors.black)),
                      onPressed: () {viewModel.dialogLeftPressed(context);},
                    ),
                  ),
                ),
                Visibility(
                  visible: Provider.of<MapViewModel>(context).double_button_dialog && !Provider.of<MapViewModel>(context).api_loading,
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