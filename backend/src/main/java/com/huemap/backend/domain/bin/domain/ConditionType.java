package com.huemap.backend.domain.bin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ConditionType {
  DAMAGED,
  DIRTY,
  FULL;

  @JsonCreator
  public static ConditionType from(String name) {
    for (ConditionType type : ConditionType.values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }

    return null;
  }
}
