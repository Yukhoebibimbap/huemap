import 'package:huemap_app/data/model/signUpInfo.dart';
import 'package:huemap_app/data/model/signInInfo.dart';
import 'package:huemap_app/data/datasource/remote_datasource.dart';

class SignInfoRepository {
  final RemoteDataSource _remoteDataSource;

  SignInfoRepository () :
        _remoteDataSource = RemoteDataSource();
  // final LocalDataSource _localDataSource;

  // Future<List<Bin>> getCachedBins() {
  //   return _localDataSource.getCachedBins();
  // }

  Future<int> postSignUpInfo(signUpInfo) async {
    return await _remoteDataSource.postSignUpInfo(signUpInfo);
  }

  Future<String> postSignInInfo(signInInfo) async {
    return await _remoteDataSource.postSignInInfo(signInInfo);
  }


}
