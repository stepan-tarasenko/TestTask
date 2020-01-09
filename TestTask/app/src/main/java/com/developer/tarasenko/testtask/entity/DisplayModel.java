package com.developer.tarasenko.testtask.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DisplayModel {
    private String type;
    private String contents;
    private String url;
}
