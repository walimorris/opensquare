package com.morris.opensquare.models.kafka;

public class OpenSquareTaskRequest {
    private String name;

    public OpenSquareTaskRequest(String name) {
        this.name = name;
    }

    public OpenSquareTaskRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
