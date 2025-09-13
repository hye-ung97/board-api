package org.board.board.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberType {
  ADMIN("ADMIN"),
  USER("USER");

  private final String value;

  MemberType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static MemberType fromValue(String value) {
    for (MemberType type : MemberType.values()) {
      if (type.value.equals(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid MemberType: " + value);
  }
}
