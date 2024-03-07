package com.morris.opensquare.repositories;

import com.morris.opensquare.models.youtube.YouTubeVideo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeVideoRepository extends MongoRepository<YouTubeVideo, String> {

    @Query("{videoId :  '?0'}")
    YouTubeVideo findOneByVideoId(String videoId);
}
