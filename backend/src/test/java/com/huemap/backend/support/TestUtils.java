package com.huemap.backend.support;

import java.lang.reflect.Constructor;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.test.util.ReflectionTestUtils;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.bin.domain.ConditionType;
import com.huemap.backend.domain.report.domain.Closure;
import com.huemap.backend.domain.report.domain.Condition;
import com.huemap.backend.domain.report.domain.Image;
import com.huemap.backend.domain.report.domain.Presence;
import com.huemap.backend.domain.suggestion.domain.Suggestion;
import com.huemap.backend.domain.user.domain.User;

public class TestUtils {
  public static Bin getBin() throws Exception {
    Class<?> clazz = Class.forName("com.huemap.backend.domain.bin.domain.Bin");
    Constructor<?> constructor = clazz.getDeclaredConstructor();
    constructor.setAccessible(true);
    Bin bin = (Bin)constructor.newInstance();

    String pointWKT = String.format("POINT(%s %s)", 126.9876779, 37.5833354);
    Point point = (Point) new WKTReader().read(pointWKT);

    ReflectionTestUtils.setField(bin, "gu", "종로구");
    ReflectionTestUtils.setField(bin, "location", point);
    ReflectionTestUtils.setField(bin, "address", "서울특별시 종로구 창덕궁7길 5");
    ReflectionTestUtils.setField(bin, "addressDescription", null);
    ReflectionTestUtils.setField(bin, "type", BinType.CLOTHES);
    ReflectionTestUtils.setField(bin, "isCandidate", false);
    ReflectionTestUtils.setField(bin, "deleted", false);

    return bin;
  }

  public static Closure getClosure(Bin bin) {
    Closure closure = Closure.builder()
                             .userId(1L)
                             .bin(bin)
                             .build();

    return closure;
  }

  public static Presence getPresence(Bin bin) {
    return Presence.of(1L, bin);
  }

  public static Condition getCondition(Bin bin, Image image, ConditionType type) {
    Condition condition = Condition.builder()
                                   .userId(1L)
                                   .bin(bin)
                                   .image(image)
                                   .type(type)
                                   .build();
    return condition;
  }

  public static User getUser() {
    User user = User.builder()
                    .email("huemap@gmail.com")
                    .name("name")
                    .password("Password1234!")
                    .build();
    ReflectionTestUtils.setField(user, "id", 1L);
    return user;
  }

  public static Suggestion getSuggestion(Long userId) {
    Suggestion suggestion = Suggestion.builder()
        .userId(userId)
        .gu("강남구")
        .type(BinType.GENERAL)
        .build();
    return suggestion;
  }
}
