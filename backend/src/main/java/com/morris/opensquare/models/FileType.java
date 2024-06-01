package com.morris.opensquare.models;

import lombok.Getter;

@Getter
public enum FileType {
    PDF("pdf");
    private final String value;

    FileType(String value) {
        this.value = value;
    }
}
