package com.huemap.backend.report.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.huemap.backend.bin.domain.Bin;
import com.huemap.backend.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CLOSURE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Closure extends Report {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bin_id")
  private Bin bin;

  private int count;

  private boolean deleted;

  @Builder
  public Closure(final User user, final Bin bin) {
    super(user);
    this.bin = bin;
  }
}
