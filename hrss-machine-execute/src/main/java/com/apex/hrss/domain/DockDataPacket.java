package com.apex.hrss.domain;

public class DockDataPacket {

    private byte[] content;
    private String session;
    private int command;
    private byte flag;
    private int version;

    public DockDataPacket(byte[] content, int version, String session, int command, byte flag) {
        this.content = content;
        this.version = version;
        this.session = session;
        this.command = command;
        this.flag = flag;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public boolean success() {
        return flag == 0x00;
    }
}
