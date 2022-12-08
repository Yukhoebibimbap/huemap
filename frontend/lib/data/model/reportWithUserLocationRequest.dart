class ReportWithUserLocationRequest {
  double latitude;
  double longitude;

  ReportWithUserLocationRequest(this.latitude, this.longitude);

  Map<String, dynamic> toJson() {
    return {
      'latitude': latitude,
      'longitude': longitude
    };
  }
}