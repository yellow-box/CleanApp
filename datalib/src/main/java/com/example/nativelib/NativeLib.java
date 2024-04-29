package com.example.nativelib;

public class NativeLib {
    private NativeLib(){}
    public static NativeLib instance = new NativeLib();

    // Used to load the 'nativelib' library on application startup.
    static {
        System.loadLibrary("nativelib");
    }

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native byte[] aes_enc(byte[] data, String key, String iv);
    public native byte[] aes_dec(byte[] data, String key, String iv);
}