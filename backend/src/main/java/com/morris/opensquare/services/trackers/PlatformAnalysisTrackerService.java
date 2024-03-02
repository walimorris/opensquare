package com.morris.opensquare.services.trackers;

import com.morris.opensquare.models.Platform;
import jakarta.servlet.http.HttpSession;

public interface PlatformAnalysisTrackerService {

    /**
     * Increment current user's platform analysis count.
     *
     * @param platform {@link String} platform being incremented
     * @param session {@link HttpSession} current session
     *
     * @return int - how many times the count was incremented. Value should
     * always be 1, unless incremented within a transaction that requires
     * this count to be incremented multiple times in a single process.
     *
     * @see Platform
     */
    int incrementPlatformAnalysisCount(Platform platform, int incrementBy, HttpSession session);
}
