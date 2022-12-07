class ReportClosuresRequest {
  double latitude;
  double longitude;

  ReportClosuresRequest(this.latitude, this.longitude);

  Map<String, dynamic> toJson() {
    return {
      'latitude': latitude,
      'longitude': longitude
    };
  }
}