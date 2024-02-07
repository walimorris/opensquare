package com.morris.opensquare.services;

import java.util.List;

public interface DropDownService {

    /**
     * Get Organizations dropdown options.
     *
     * @return {@link List<String>}
     */
    List<String> getOrganizations();

    /**
     * Get Professions dropdown options.
     *
     * @return {@link List<String>}
     */
    List<String> getProfessions();

    /**
     * Get AgeRanges dropdown options.
     *
     * @return {@link List<String>}
     */
    List<String> getAgeRanges();
}
