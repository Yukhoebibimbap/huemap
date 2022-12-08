class BinDetail {
  final int id;
  final String gu;
  final double lat, lng;
  final String address;
  final String addressDesc;
  final String type;
  final String updatedAt;
  final bool hasClosure;
  final bool isCandidate;

  // json 생성자
  BinDetail.fromJson(Map<String, dynamic> json) :
        id = json['id'],
        gu = json['gu'],
        lat = json['latitude'],
        lng = json['longitude'],
        address = json['address'],
        addressDesc = json['addressDescription'] ?? "",
        type = json['type'],
        updatedAt = json['updatedAt'],
        hasClosure = json['hasClosure'],
        isCandidate = json['isCandidate'];

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'gu': gu,
      'latitude': lat,
      'longitude': lng,
      'address': address,
      'addressDescription': addressDesc,
      'type': type,
      'updatedAt': updatedAt,
      'hasClosure': hasClosure,
      'isCandidate': isCandidate
    };
  }
}

final Map detailString = {
  'address': '도로명 주소',
  'addressDesc': '상세 설명',
  'type': '수거함 유형'
};

final Map binTypesKor = {
  'GENERAL': '일반 쓰레기',
  'RECYCLE': '재활용 쓰레기',
  'LAMP': '폐형광등',
  'BATTERY': '폐건전지',
  'CLOTHES': '의류',
  'MEDICINE': '폐의약품'
};

final Map binTypesEng = {
  '일반 쓰레기': 'GENERAL',
  '재활용 쓰레기': 'RECYCLE',
  '폐형광등': 'LAMP',
  '폐건전지': 'BATTERY',
  '의류': 'CLOTHES',
  '폐의약품': 'MEDICINE'
};

// enum Type  {
//   general,
//   recycle,
//   lamp,
//   battery,
//   clothes,
//   medicine
// }
//
// extension ParseToString on Type {
//   String toParameter() {
//     return (this.toString().split('.').last).toUpperCase();
//   }
// }