package com.huemap.backend.domain.report.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.huemap.backend.domain.bin.domain.Bin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRESENCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Presence extends Report {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bin_id")
  private Bin bin;

  private int count;

  private boolean deleted;

  private Presence(final Long userId, final Bin bin) {
    super(userId);
    this.bin = bin;
    this.count = 1;
  }

  public static Presence of(final Long userId, final Bin bin) {
    return new Presence(userId, bin);
  }

  public void delete() {
    this.deleted = true;
  }

  public void addCount() {
    this.count += 1;
  }
}
