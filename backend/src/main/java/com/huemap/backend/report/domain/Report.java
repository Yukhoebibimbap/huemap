package com.huemap.backend.report.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import com.huemap.backend.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "type")
public abstract class Report extends BaseEntity {

  @NotNull
  private Long userId;

  protected Report(final Long userId) {
    this.userId = userId;
  }
}
