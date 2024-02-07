package com.morris.opensquare.services;

import com.morris.opensquare.models.digitalfootprints.OS;

public interface IdentityGenerator {

    /**
     * Get random {@link java.util.UUID}
     *
     * @return {@link String} UUID
     */
    String getRandomUUID();

    /**
     * Get Operating System.
     *
     * @return {@link String} operating system name
     */
    OS whichOS();
}
