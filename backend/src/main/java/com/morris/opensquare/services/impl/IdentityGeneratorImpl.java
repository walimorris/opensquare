package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.digitalfootprints.OS;
import com.morris.opensquare.services.IdentityGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.morris.opensquare.utils.Constants.LINUX;
import static com.morris.opensquare.utils.Constants.MAC_OS_X;

@Service
public class IdentityGeneratorImpl implements IdentityGenerator {

    private static final String OS_NAME = "os.name";

    @Override
    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public OS whichOS() {
        String systemOS = System.getProperty(OS_NAME);
        return switch (systemOS) {
            case MAC_OS_X -> OS.MAC_OS_X;
            case LINUX -> OS.LINUX;
            default -> OS.WINDOWS;
        };
    }
}
