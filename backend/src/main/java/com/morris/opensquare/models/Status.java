package com.morris.opensquare.models;

public enum Status {
    SUBMITTED,
    STARTED,
    STARTING,
    RUNNING,
    READY,
    FINISHED,
    STOPPED,
    STOPPING,
    TERMINATED,
    PREP_FILES_CSV,
    EXTRACTING_DATA_SET,
    CRAWLING_DATA_SET,
    PREP_DATA_SET,
    TRANSFORMING_DATA_SET,
    LOADING_CLEAN_DATA_SET
}
