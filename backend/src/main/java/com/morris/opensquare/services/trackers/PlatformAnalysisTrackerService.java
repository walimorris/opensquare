package com.morris.opensquare.services.trackers;

import jakarta.servlet.http.HttpSession;

public interface PlatformAnalysisTrackerService {

    /**
     * Increment current user's platform analysis count.
     *
     * @param platform {@link String} platform being incremented
     * @param session {@link HttpSession} current session
     */
    int incrementPlatformAnalysisCount(String platform, int incrementBy, HttpSession session);
}
