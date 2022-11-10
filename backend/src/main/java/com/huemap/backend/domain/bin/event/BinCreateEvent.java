package com.huemap.backend.domain.bin.event;

import org.locationtech.jts.geom.Point;

import com.huemap.backend.domain.bin.domain.BinType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BinCreateEvent {
  private final BinType type;
  private final Point location;

  public static BinCreateEvent candidateOf(BinType type, Point location) {
    return new BinCreateEvent(type, location);
  }
}
