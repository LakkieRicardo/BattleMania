package net.battle.test.handlers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BungeeRequest {
    public ByteArrayOutputStream bytes;
    public DataOutputStream out;

    public BungeeRequest(ByteArrayOutputStream bytes, DataOutputStream stream) {
        this.bytes = bytes;
        this.out = stream;
    }

    public BungeeRequest() {
        this.bytes = new ByteArrayOutputStream();
        this.out = new DataOutputStream(this.bytes);
    }
}