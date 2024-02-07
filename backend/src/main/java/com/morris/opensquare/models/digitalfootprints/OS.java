package com.morris.opensquare.models.digitalfootprints;

public enum OS {
    MAC_OS_X ("Mac OS X"),
    LINUX ("Linux"),
    WINDOWS("Windows");

    private final String text;

    OS(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
