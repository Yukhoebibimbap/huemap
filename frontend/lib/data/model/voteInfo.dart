class VoteInfo {
  int binId;
  double latitude, longitude;

  VoteInfo(this.binId, this.latitude, this.longitude);

  Map<String, dynamic> toJson() {
    return {
      'binid': binId,
      'latitude': latitude,
      'longitude': longitude
    };
  }
}

