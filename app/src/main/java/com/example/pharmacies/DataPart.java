package com.example.pharmacies;

public class DataPart {
    private String fileName;
    private byte[] data;
    private String mimeType;

    public DataPart(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
        this.mimeType = "image/png"; // Adjust the MIME type as needed
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }
}

