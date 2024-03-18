#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_connect(JNIEnv *env, jobject thiz, jstring host,
                                                jint port) {
    // TODO: implement connect()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_disconnect(JNIEnv *env, jobject thiz) {
    // TODO: implement disconnect()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_setOnConnectListener(JNIEnv *env, jobject thiz, jobject l) {
    // TODO: implement setOnConnectListener()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_write(JNIEnv *env, jobject thiz, jbyteArray byte_array) {
    // TODO: implement write()
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_nativelib_NativeSocket_isConnected(JNIEnv *env, jobject thiz) {
    // TODO: implement isConnected()
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_NativeSocket_read___3B(JNIEnv *env, jobject thiz,
                                                  jbyteArray byte_array) {
    // TODO: implement read()
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_NativeSocket_read___3BII(JNIEnv *env, jobject thiz,
                                                    jbyteArray byte_array, jint start_index,
                                                    jint length) {
    // TODO: implement read()
}