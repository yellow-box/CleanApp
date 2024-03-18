package com.example.nativelib;

public class NativeLib {

    // Used to load the 'nativelib' library on application startup.
    static {
        System.loadLibrary("rust_security");
    }

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void printVersion() {
//        Linker linker = Linker.nativeLinker();
//        SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
//
//        MemorySegment getCLangVersion = loaderLookup.find("GetCLangVersion").get()
    }

}