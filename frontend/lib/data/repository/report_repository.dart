import 'package:huemap_app/data/datasource/remote_datasource.dart';

class ReportRepository {
  final RemoteDataSource _remoteDataSource;

  ReportRepository () :
        _remoteDataSource = RemoteDataSource();

  Future<String> reportPresences(ReportPresencesRequest) async {
    return await _remoteDataSource.reportPresences(ReportPresencesRequest);
  }

  Future<String> reportClosures(binId, ReportClosuresRequest) async {
    return await _remoteDataSource.reportClosures(binId, ReportClosuresRequest);
  }
}