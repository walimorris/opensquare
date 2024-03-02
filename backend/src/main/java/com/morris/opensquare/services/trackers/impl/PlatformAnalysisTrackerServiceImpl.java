package com.morris.opensquare.services.trackers.impl;

import com.morris.opensquare.repositories.PlatformAnalysisTrackerRepository;
import com.morris.opensquare.services.trackers.PlatformAnalysisTrackerService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformAnalysisTrackerServiceImpl implements PlatformAnalysisTrackerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformAnalysisTrackerServiceImpl.class);

    private final PlatformAnalysisTrackerRepository platformAnalysisTrackerRepository;

    @Autowired
    public PlatformAnalysisTrackerServiceImpl(PlatformAnalysisTrackerRepository platformAnalysisTrackerRepository) {
        this.platformAnalysisTrackerRepository = platformAnalysisTrackerRepository;
    }

    @Override
    public int incrementPlatformAnalysisCount(String platform, int incrementBy, HttpSession session) {
        int platformAnalysisTrackerUpdateValue = platformAnalysisTrackerRepository
                .updatePlatformAnalysisTrackerByPlatformAndAnalysisCount(platform, incrementBy);

        LOGGER.info("{} tracker property was update to count with value: {}", platform, platformAnalysisTrackerUpdateValue);
        return platformAnalysisTrackerUpdateValue;
    }
}
