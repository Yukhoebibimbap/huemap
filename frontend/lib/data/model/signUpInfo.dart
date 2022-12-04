class SignUpInfo {
  String email;
  String name;
  String password;

  SignUpInfo(this.email, this.name, this.password);

  Map<String, String> toJson() {
    return {
      'email': email,
      'name': name,
      'password': password
    };
  }
}

