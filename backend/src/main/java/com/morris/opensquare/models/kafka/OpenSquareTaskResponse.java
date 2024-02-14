package com.morris.opensquare.models.kafka;

import java.io.Serializable;

public class OpenSquareTaskResponse implements Serializable {
    private String taskId;
    private String name;

    public OpenSquareTaskResponse(String taskId, String name) {
        this.taskId = taskId;
        this.name = name;
    }

    public OpenSquareTaskResponse() {}

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
