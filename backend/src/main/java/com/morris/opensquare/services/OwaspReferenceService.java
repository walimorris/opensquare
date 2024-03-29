package com.morris.opensquare.services;

import com.morris.opensquare.models.notifications.OwaspBlogSnippet;

import java.io.IOException;

public interface OwaspReferenceService {

    /**
     * Scraps the Owasp GitHub page to pull all blogs. These blogs are than marshalled
     * into a list of OwaspBlogSnippets.
     *
     * @return String
     * @see OwaspBlogSnippet
     */
    String getOwaspBlogSnippetsList() throws IOException, InterruptedException;
}
