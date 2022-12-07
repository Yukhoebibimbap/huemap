import 'package:huemap_app/data/datasource/remote_datasource.dart';

class SuggestRepository {
  final RemoteDataSource _remoteDataSource;

  SuggestRepository () :
        _remoteDataSource = RemoteDataSource();

  Future<String> suggestBinLocation(suggestBinLocationRequest) async {
    return await _remoteDataSource.suggestBinLocation(suggestBinLocationRequest);
  }
}