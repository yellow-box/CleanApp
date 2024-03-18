package com.example.nativelib;

import com.example.domain.socket.ISocket;

public class NativeSocket {
    public native void connect(String host, int port);

    public native void disconnect();

    public native void setOnConnectListener(ISocket.ConnectListener l);

    public native void write(byte[] byteArray);

    public native int read(byte[] byteArray);

    public native int read(byte[] byteArray, int startIndex, int length);

    public native boolean isConnected();
}
