package com.huemap.backend.report.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.locationtech.jts.geom.Point;

import com.huemap.backend.bin.domain.BinType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRESENCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Presence extends Report {

  @Column(columnDefinition = "GEOMETRY")
  private Point location;

  @Enumerated(EnumType.STRING)
  private BinType type;

}
