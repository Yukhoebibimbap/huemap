package com.huemap.backend.domain.report.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huemap.backend.domain.bin.domain.Bin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CONDITION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "`conditions`")
public class Condition extends Report {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bin_id")
  private Bin bin;

  @Embedded
  private Image image;

  private boolean deleted;

  @Builder
  public Condition(final Long userId, final Bin bin, final Image image) {
    super(userId);
    this.bin = bin;
    this.image = image;
  }
}
