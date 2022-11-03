import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/datasource/remote_datasource.dart';

class BinRepository {
    final RemoteDataSource _remoteDataSource;

    BinRepository () :
        _remoteDataSource = RemoteDataSource();
    // final LocalDataSource _localDataSource;

    // Future<List<Bin>> getCachedPosts() {
    //   return _localDataSource.getCachedPosts();
    // }

    Future<List<Bin>> getBins() {
      return _remoteDataSource.getBins();
    }

}