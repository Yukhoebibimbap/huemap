class ReportPresencesRequest {
  String type;
  double latitude;
  double longitude;

  ReportPresencesRequest(this.type, this.latitude, this.longitude);

  Map<String, dynamic> toJson() {
    return {
      'type': type,
      'latitude': latitude,
      'longitude': longitude
    };
  }
}