package com.morris.opensquare.models.trackers;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("platform_analysis_tracker")
public class PlatformAnalysisTracker {

    @Id
    private ObjectId id;

    @Field("platform")
    private String platform;

    @Field("count")
    private int count;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getAnalysisCount() {
        return count;
    }

    public void setAnalysisCount(int analysisCount) {
        this.count = analysisCount;
    }

    @Override
    public String toString() {
        return "PlatformAnalysisTracker{" +
                "id=" + id +
                ", platform='" + platform + '\'' +
                ", analysisCount=" + count +
                '}';
    }
}
