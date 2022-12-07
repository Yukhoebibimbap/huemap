class SaveReportRequest {
  String type;
  double latitude;
  double longitude;

  SaveReportRequest(this.type, this.latitude, this.longitude);

  Map<String, String> toJson() {
    return {
      'type': type,
      'latitude': latitude.toString(),
      'longitude': longitude.toString()
    };
  }
}