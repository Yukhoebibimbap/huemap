import 'dart:developer';

import 'package:huemap_app/data/model/bin.dart';
import 'package:huemap_app/data/model/binDetail.dart';
import 'package:huemap_app/data/datasource/remote_datasource.dart';

class BinRepository {
    final RemoteDataSource _remoteDataSource;

    BinRepository () :
        _remoteDataSource = RemoteDataSource();
    // final LocalDataSource _localDataSource;

    // Future<List<Bin>> getCachedBins() {
    //   return _localDataSource.getCachedBins();
    // }

    Future<List<Bin>> getBins(Type t) async {
      return await _remoteDataSource.getBins(t);
    }

    Future<BinDetail?> getBinDetail(String id) async {
      return await _remoteDataSource.getBinDetail(id);
    }
    
    Future<String> vote(voteInfo) async {
      return await _remoteDataSource.vote(voteInfo);
    }
}