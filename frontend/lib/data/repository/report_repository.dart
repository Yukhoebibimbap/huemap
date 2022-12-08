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

  Future<String> voteCandidate(binId, voteCandidateRequest) async {
    return await _remoteDataSource.voteCandidate(binId, voteCandidateRequest);
  }

  Future<String> reportCondition(binId, file_path, voteCandidateRequest) async {
    return await _remoteDataSource.reportCondition(binId, file_path, voteCandidateRequest);
  }
}