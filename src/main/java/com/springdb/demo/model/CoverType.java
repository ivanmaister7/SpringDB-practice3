package com.springdb.demo.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CoverType {

    HARD("Hard"),
    PAPER("Paper");

    private final String name;

}
