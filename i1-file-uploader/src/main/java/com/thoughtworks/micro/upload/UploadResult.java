package com.thoughtworks.micro.upload;

import org.apache.commons.fileupload.FileItem;

public class UploadResult {
    private long size;
    private String name;
    private String type;

    public UploadResult(FileItem fileItem) {
        this.name = fileItem.getName();
        this.type = fileItem.getContentType();
        this.size = fileItem.getSize();
    }

    public String asJSON() {
        StringBuilder stringBuilder = new StringBuilder("[{")
                .append("\"name\":\"").append(this.name).append("\"")
                .append(",\"type\":\"").append(this.type).append("\"")
                .append(",\"size\":\"").append(this.size).append("\"")
                .append("}]");
        return stringBuilder.toString();
    }
}
