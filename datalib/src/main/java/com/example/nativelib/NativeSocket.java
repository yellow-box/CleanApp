package com.example.nativelib;


public class NativeSocket {

    static {
        System.loadLibrary("nativelib");
    }

    public native void connect(String host, int port);

    public native void disconnect();

    public native void write(byte[] byteArray);

    public native int read(byte[] byteArray, int startIndex, int length);

    public native boolean isConnected();

    //在c层对 bytes作修改，然后java层感知到这个修改
    public native void testNativeModifyByte(byte[] bytes);
}
