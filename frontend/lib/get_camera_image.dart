import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:image_picker/image_picker.dart';
import 'package:huemap_app/ui/viewmodel/map_viewmodel.dart';

class CameraView extends StatefulWidget {
  const CameraView({Key? key}) : super(key: key);

  @override
  _CameraViewState createState() => _CameraViewState();
}

class _CameraViewState extends State<CameraView> {
  File? _image;
  final picker = ImagePicker();

  // 비동기 처리를 통해 카메라와 갤러리에서 이미지를 가져온다.
  Future getImage(ImageSource imageSource, viewModel) async {
    final image = await picker.pickImage(source: imageSource);
    print(image!.path);
    viewModel.updateFile(image!.path);
    setState(() {
      _image = File(image!.path); // 가져온 이미지를 _image에 저장
    });
  }

  Widget showImage(viewModel) {
    return Container(
        color: const Color(0xffd0cece),
        width: MediaQuery.of(context).size.width * 0.5,
        height: MediaQuery.of(context).size.width * 0.5,
        child: Center(
            child: _image == null
                ? TextButton(
                  child: Icon(Icons.add_a_photo, color: Colors.black,size: 30),
                  onPressed: () {
                    getImage(ImageSource.camera, viewModel);
                  },
                )
                : Image.file(File(_image!.path))));
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations(
        [DeviceOrientation.portraitUp, DeviceOrientation.portraitDown]);

    MapViewModel viewModel = new MapViewModel();
    viewModel = Provider.of<MapViewModel>(context);

    return Container(
        // color: Colors.white,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            showImage(viewModel),
          ],
        ));
  }
}