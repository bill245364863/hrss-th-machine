package com.apex.hrssmachineexecute.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayBuilder {
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public ByteArrayBuilder append(byte b) {
        buffer.write(b);
        return this;
    }

    public ByteArrayBuilder append(byte[] b) {
        try {
            buffer.write(b);
        } catch (IOException e) {
        }
        return this;
    }

    public byte[] toByteArray() {
        return buffer.toByteArray();
    }
}
