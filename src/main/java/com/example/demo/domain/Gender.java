package com.example.demo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    MALE("남자"),
    FEMALE("여자");
    @Getter
    private final String value;
}
