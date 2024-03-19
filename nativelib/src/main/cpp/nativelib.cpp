#include <jni.h>
#include <string>
#include "android/log.h"
#include "lib_interface.h"
#define LOGD(tag, content)  __android_log_print(ANDROID_LOG_DEBUG,tag,content)
#define LOGE(tag, content)  __android_log_print(ANDROID_LOG_ERROR,tag,content)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
//    LOGD("tag",  "f");
    const char* v =get_lib_version();
    return env->NewStringUTF(v);
}
//jint JNI_OnLoad(JavaVM* vm, void* reserved) {
//
//    JNIEnv* env = NULL;
//
//    jint result = -1;
//
//    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
//
//
//        goto bail;
//
//    }

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