#include <jni.h>
#include <string>
#include "android/log.h"

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <cstring>
#include <stdio.h>

extern "C"
#include "lib_interface.h"
#include "CSocket.h"
#define LOGD(tag, content)  __android_log_print(ANDROID_LOG_DEBUG,tag,content)
#define LOGE(tag, content)  __android_log_print(ANDROID_LOG_ERROR,tag,content)
#define tag "CSocket"
CSocket *nativeSocket = nullptr;

int littleEndianToInt(unsigned char *data, int len) {
    if (len < 4) {
        printf("data size must be 4");
        return -1;
    }
    int result = data[0] | (data[1] << 8) | (data[2] << 16) | (data[3] << 24);
    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    const char *v = get_lib_version();
    return env->NewStringUTF(v);
}


//====CSocket

void CSocket::disconnect() {
    ::close(clientSocketFd);
}


int CSocket::read(jbyte *buf, int startIndex, int length) {
    if (isConnected()) {
        int bytesRead = recv(clientSocketFd, &(buf[startIndex]), length, 0);
        LOGD(tag, "socket read data");
        return bytesRead;
    }
    LOGD(tag, "socket is not connected");
    return -2;
}

void CSocket::write(jbyte *buf, int len) {
    if (isConnected()) {
        send(clientSocketFd, buf, len, 0);
//        fflush((FILE *)clientSocketFd);
        LOGD(tag, "socket success write");
    }
    LOGD(tag, "socket is not connected");
    return;
}

bool CSocket::isConnected() {
    int error;
    socklen_t len = sizeof(error);
    getsockopt(clientSocketFd, SOL_SOCKET, SO_ERROR, &error, &len);
    return error == 0;
}

void CSocket::connect(const char *ip, int port) {
    clientSocketFd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    LOGD(tag, "start connect!");
    if (clientSocketFd < 0) {
        LOGD(tag, "Failed to create socket");
        return;
    }
    struct sockaddr_in serverAddr;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    inet_pton(AF_INET, ip, &serverAddr.sin_addr);

    if (::connect(clientSocketFd, (struct sockaddr *) &serverAddr, sizeof(serverAddr)) != 0) {
        printf("Failed to connect socket,ip:%s,port:%d\n", ip, port);
        ::close(clientSocketFd);
        return;
    } else {
        printf("success to connect socket,ip:%s,port:%d\n", ip, port);
    }
}
//====CSocket


extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_connect(JNIEnv *env, jobject thiz, jstring host,
                                                jint port) {
    nativeSocket = new CSocket();
    const char *ip = env->GetStringUTFChars(host, NULL);
    nativeSocket->connect(ip, (int) port);
    env->ReleaseStringUTFChars(host, ip);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_disconnect(JNIEnv *env, jobject thiz) {
    if (nativeSocket != NULL) {
        nativeSocket->disconnect();
        delete nativeSocket;
        nativeSocket = NULL;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_write(JNIEnv *env, jobject thiz, jbyteArray byte_array) {
    if (nativeSocket != NULL && nativeSocket->isConnected()) {
        // 获取字节数组的长度
        jsize length = env->GetArrayLength(byte_array);

        // 获取字节数组的指针
        jbyte *jbyteP = env->GetByteArrayElements(byte_array, NULL);
        nativeSocket->write(jbyteP, length);
        env->ReleaseByteArrayElements(byte_array, jbyteP, JNI_FALSE);
    } else {
        LOGD(tag, "native socket is not connected");
    }
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_nativelib_NativeSocket_isConnected(JNIEnv *env, jobject thiz) {
    if (nativeSocket == NULL) {
        return false;
    }
    return nativeSocket->isConnected();
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_NativeSocket_read___3BII(JNIEnv *env, jobject thiz,
                                                    jbyteArray byte_array, jint start_index,
                                                    jint length) {
    if (nativeSocket != NULL && nativeSocket->isConnected()) {
        // 获取字节数组的指针
        jbyte *jbyteP = env->GetByteArrayElements(byte_array, NULL);
        int readSize = nativeSocket->read(jbyteP, start_index, length);
        env->ReleaseByteArrayElements(byte_array, jbyteP, JNI_FALSE);
        return readSize;
    } else {
        LOGD(tag, "native socket is not connected");
        return 0;
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_testNativeModifyByte(JNIEnv *env, jobject thiz,
                                                             jbyteArray bytes) {
    // 获取字节数组的长度
    jsize length = env->GetArrayLength(bytes);

    // 获取字节数组的指针
    jbyte *jbyteP = env->GetByteArrayElements(bytes, NULL);
    jbyteP[0] = 5;
    jbyteP[1] = 2;


    //这里 mode 要传递0
    env->ReleaseByteArrayElements(bytes, jbyteP, JNI_FALSE);
}

char *ConvertJByteaArrayToChars(JNIEnv *env, jbyteArray bytearray) {
    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);
    int chars_len = env->GetArrayLength(bytearray);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;

    env->ReleaseByteArrayElements(bytearray, bytes, 0);

    return chars;
}

jbyteArray charToJByteArray(JNIEnv *env, unsigned char *buf) {
    size_t len = strlen(reinterpret_cast<const char *>(buf));
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte *>(buf));
    delete buf;
    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_nativelib_NativeLib_aes_1enc(JNIEnv *env, jobject thiz, jbyteArray data,
                                              jstring key, jstring iv) {
    jsize len = env->GetArrayLength(data); // 获取数组长度
    char *cdata = ConvertJByteaArrayToChars(env, data);
    const char *k = env->GetStringUTFChars(key, nullptr);
    const char *v = env->GetStringUTFChars(iv, nullptr);
    unsigned char *r = aes_enc(k, v, reinterpret_cast<const unsigned char *>(cdata), len);
    delete cdata;
    return charToJByteArray(env, r);
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_nativelib_NativeLib_aes_1dec(JNIEnv *env, jobject thiz, jbyteArray data,
                                              jstring key, jstring iv) {
    jsize len = env->GetArrayLength(data); // 获取数组长度
    char *cdata = ConvertJByteaArrayToChars(env, data);
    const char *k = env->GetStringUTFChars(key, nullptr);
    const char *v = env->GetStringUTFChars(iv, nullptr);
    unsigned char *r = aes_dec(k, v, reinterpret_cast<const unsigned char *>(cdata), len);
    delete cdata;
    return charToJByteArray(env, r);
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_nativelib_NativeLib_rsa_1enc(JNIEnv *env, jobject thiz, jbyteArray data,
                                              jstring path) {
    jsize len = env->GetArrayLength(data); // 获取数组长度
    char *cdata = ConvertJByteaArrayToChars(env, data);
    const char *cPath = env->GetStringUTFChars(path, 0);
    unsigned char *r = rsa_enc(cPath, reinterpret_cast<const unsigned char *>(cdata), len);
    delete cdata;
    return charToJByteArray(env, r);
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_nativelib_NativeLib_rsa_1dec(JNIEnv *env, jobject thiz, jbyteArray data,
                                              jstring path) {
    jsize len = env->GetArrayLength(data); // 获取数组长度
    char *cdata = ConvertJByteaArrayToChars(env, data);
    const char *cPath = env->GetStringUTFChars(path, 0);
    unsigned char *r = rsa_dec(cPath, reinterpret_cast<const unsigned char *>(cdata), len);
    delete cdata;
    return charToJByteArray(env, r);
}