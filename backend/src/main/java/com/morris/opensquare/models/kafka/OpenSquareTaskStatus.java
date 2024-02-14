package com.morris.opensquare.models.kafka;

import com.morris.opensquare.models.Status;

import java.io.Serializable;

public class OpenSquareTaskStatus implements Serializable {
    private String taskId;
    private String taskName;
    private float percentageComplete;
    private Status status;
    private String resultUrl;

    public OpenSquareTaskStatus(String taskId, String taskName, float percentageComplete, Status status, String resultUrl) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.percentageComplete = percentageComplete;
        this.status = status;
        this.resultUrl = resultUrl;
    }

    public OpenSquareTaskStatus() {}

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public float getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(float percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}
