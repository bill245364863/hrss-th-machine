package com.apex.hrssmachineexecute.domain;

public class DockDataParam {
    private byte[] content;
    private int version;

    public DockDataParam(byte[] content, int version) {
        super();
        this.content = content;
        this.version = version;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
