class ConstantValue {
  static const List<String> _dropdownBinTypeList = ['일반 쓰레기', '재활용 쓰레기', '폐형광등', '폐건전지', '의류','폐의약품'];
  static const List<String> _dropdownGuList = ['강남구','강동구','강북구','강서구','관악구','광진구','구로구','금천구','노원구','도봉구','동대문구','동작구','마포구','서대문구','서초구','성동구','성북구','송파구','양천구','영등포구','용산구','은평구','종로구','중구','중랑구'];
  static const List<String> _binDetailIndex = ['도로명 주소', '상세 설명', '수거함 종류'];
  static const List<String> _binDetailIndexCandidate = ['도로명 주소', '상세 설명', '수거함 종류', '수거함 제보 횟수'];

  List<String> get dropdownBinTypeList => _dropdownBinTypeList;
  List<String> get dropdownGuList => _dropdownGuList;
  List<String> get binDetailIndex => _binDetailIndex;
  List<String> get binDetailIndexCandidate => _binDetailIndexCandidate;
}

enum Dialog_Type  {
  submit,
  response,
  vote,
}

enum Widget_Type  {
  report,
  suggest,
  condition,
  missing,
  vote,
}
