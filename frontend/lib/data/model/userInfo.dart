class UserInfo {
  String email;
  String accessToken;
  String grantType;
  int id;

  UserInfo._privateConstructor(this.email, this.id, this.accessToken, this.grantType);
  static final UserInfo _instance = UserInfo._privateConstructor('', -1, '', '');

  setUserInfo(email, id, accessToken, grantType) {
    this.email = email;
    this.id = id;
    this.accessToken = accessToken;
    this.grantType = grantType;
  }

  factory UserInfo() {
    return _instance;
  }
}