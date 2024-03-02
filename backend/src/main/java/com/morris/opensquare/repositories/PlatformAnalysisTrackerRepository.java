package com.morris.opensquare.repositories;

import com.morris.opensquare.models.trackers.PlatformAnalysisTracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformAnalysisTrackerRepository extends MongoRepository<PlatformAnalysisTracker, String> {

    @Query("{ platform: '?0' }")
    @Update("{ $inc: { analysis_count: '?1' } }")
    int updatePlatformAnalysisTrackerByPlatformAndAnalysisCount(String platform, int incrementBy);
}
