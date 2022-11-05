package com.huemap.backend.report.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.huemap.backend.bin.domain.Bin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRESENCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Presence extends Report {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bin_id")
  private Bin bin;

  private int count;

  @Builder
  public Presence(final Long userId, final Bin bin) {
    super(userId);
    this.bin = bin;
  }
}