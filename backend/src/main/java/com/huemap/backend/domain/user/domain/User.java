package com.huemap.backend.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.huemap.backend.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "`user`")
public class User extends BaseEntity {

  @NotNull
  private String email;

  @NotNull
  private String name;

  @NotNull
  private String password;

  @Builder
  public User(final String email, final String name, final String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
