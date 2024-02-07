package com.morris.opensquare.services;

import org.json.JSONArray;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.File;

public interface FileService {

    /**
     * Get a {@link File} from given file name.
     *
     * @param fileName {@link String} file to get
     * @return {@link File}
     */
    File getFile(@NonNull String fileName);

    /**
     * Writes {@link JSONArray} object to given file.
     *
     * @param json {@link JSONArray} proposed json array object written to file
     * @param fileName {@link String} file name to write object
     *
     * @return {@link File} the written file
     */
    File writeJsonToFile(@NonNull JSONArray json, @NonNull String fileName);

    /**
     * Get BufferedReader from process execution.
     *
     * @param command process command
     * @return {@link BufferedReader}
     */
    BufferedReader processBufferedReader(@NonNull String command);
}
