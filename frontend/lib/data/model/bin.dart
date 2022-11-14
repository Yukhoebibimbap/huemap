class Bin {
  final int id;
  final double lat, lng;
  final String type;

  // json 생성자
  Bin.fromJson(Map<String, dynamic> json) :
        id = json['id'],
        lat = json['latitude'],
        lng = json['longitude'],
        type = json['type'];
}

enum Type  {
  general,
  recycle,
  lamp,
  battery,
  clothes,
  medicine
}

extension ParseToString on Type {
  String toParameter() {
    return (this.toString().split('.').last).toUpperCase();
  }
}