package com.morris.opensquare.models;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class OpensquareMultipartFile implements MultipartFile {
    private MultipartFile input;

    public OpensquareMultipartFile() {}

    public OpensquareMultipartFile(MultipartFile input) {
        this.input = input;
    }

    @NotNull
    @Override
    public String getName() {
        return input.getName();
    }

    @Override
    public String getOriginalFilename() {
        if (input.getOriginalFilename() != null) {
            return input.getOriginalFilename();
        }
        return null;
    }

    @Override
    public String getContentType() {
        if (input.getOriginalFilename() != null) {
            return input.getContentType();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        try {
            return input.getBytes().length == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getSize() {
        try {
            return input.getBytes().length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return input.getBytes();
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input.getBytes());
    }

    @Override
    public void transferTo(@NotNull File destination) throws IOException, IllegalStateException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
            fileOutputStream.write(input.getBytes());
        }
    }
}
