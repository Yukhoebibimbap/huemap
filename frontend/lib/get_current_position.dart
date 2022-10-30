/*
    사용자에게 위치 권한 허가를 묻고, 위치를 받아옴

 */

import 'package:geolocator/geolocator.dart';

Future<Position> determinePosition() async {
  bool serviceEnabled;
  LocationPermission permission;

  serviceEnabled = await Geolocator.isLocationServiceEnabled();
  if(!serviceEnabled) {
    return Future.error('Location services Are disabled.');
  }

  permission = await Geolocator.checkPermission();
  if( permission == LocationPermission.denied) {
    permission = await Geolocator.requestPermission();
    if(permission == LocationPermission.denied) {
      return Future.error('Location permissions are denied');
    }
  }

  if(permission == LocationPermission.deniedForever) {
    return Future.error(
      'Location permissions are permanently denied, we cannot request permissions.'
    );
  }

  return await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
}