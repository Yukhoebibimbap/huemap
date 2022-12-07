class SuggestBinLocationRequest {
  String gu;
  double latitude;
  double longitude;
  String type;

  SuggestBinLocationRequest(this.gu, this.latitude, this.longitude, this.type);

  Map<String, dynamic> toJson() {
    return {
      'gu': gu,
      'latitude': latitude,
      'longitude': longitude,
      'type': type,
    };
  }
}