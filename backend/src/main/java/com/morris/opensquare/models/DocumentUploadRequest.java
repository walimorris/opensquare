package com.morris.opensquare.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentUploadRequest {
    private String file;
    private String filename;
    private boolean save;
}
