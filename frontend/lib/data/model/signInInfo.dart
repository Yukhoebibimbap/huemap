class SignInInfo {
  String email;
  String password;

  SignInInfo(this.email, this.password);

  Map<String, String> toJson() {
    return {
      'email': email,
      'password': password,
    };
  }
}