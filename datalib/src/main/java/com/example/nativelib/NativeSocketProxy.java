package com.example.nativelib;

import androidx.annotation.NonNull;

import com.example.domain.socket.ISocket;

/**
 *
 */
public class NativeSocketProxy implements ISocket {

    private final NativeSocket nativeSocket = new NativeSocket();
    private ConnectListener l = null;

    @Override
    public void connect(@NonNull String host, int port) {
        nativeSocket.connect(host, port);
        l.onConnect();
    }

    @Override
    public void disconnect() {
        nativeSocket.disconnect();
    }

    @Override
    public void setOnConnectListener(@NonNull ConnectListener l) {
        this.l = l;
    }

    @Override
    public void write(@NonNull byte[] byteArray) {
        nativeSocket.write(byteArray);
    }

    @Override
    public int read(@NonNull byte[] byteArray) {
        return read(byteArray, 0, byteArray.length);
    }

    @Override
    public int read(@NonNull byte[] byteArray, int startIndex, int length) {
        return nativeSocket.read(byteArray, startIndex, length);
    }

    @Override
    public boolean isConnected() {
        return nativeSocket.isConnected();
    }
}
