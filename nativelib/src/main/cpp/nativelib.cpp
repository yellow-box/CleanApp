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
    std::string hello = "Hello from C++";
//    LOGD("tag",  "f");
    const char *v = get_lib_version();
    return env->NewStringUTF(v);
}


void CSocket::disconnect() {
    ::close(clientSocketFd);
}


int CSocket::read(bytearray(buf), int startIndex, int length) {
    while (isConnected()) {
        int bytesRead = recv(clientSocketFd, buf, sizeof(&buf), 0);
//        int peddingSize = littleEndianToInt(buffer, 4);
        if (bytesRead > 0) {

        }
        //调用 java 回调,将数据传递出去，java层需要自己复制一份，c层释放
//        if (readCallback != nullptr) {
//            readCallback(dataBuf,peddingSize);
//        }
    }
}

void CSocket::write(bytearray(buf)) {

}

bool CSocket::isConnected() {
    int error;
    socklen_t len = sizeof(error);
    getsockopt(clientSocketFd, SOL_SOCKET, SO_ERROR, &error, &len);
    return error == 0;
}

void CSocket::connect(const char *ip, int port) {
    clientSocketFd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (clientSocketFd < 0) {
        printf("Failed to create socket\n");
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
    nativeSocket->disconnect();
    delete nativeSocket;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_setOnConnectListener(JNIEnv *env, jobject thiz, jobject l) {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeSocket_write(JNIEnv *env, jobject thiz, jbyteArray byte_array) {

}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_nativelib_NativeSocket_isConnected(JNIEnv *env, jobject thiz) {
    if(nativeSocket == NULL){
        return false;
    }
    return ;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_NativeSocket_read___3B(JNIEnv *env, jobject thiz,
                                                  jbyteArray byte_array) {
    return 0;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_NativeSocket_read___3BII(JNIEnv *env, jobject thiz,
                                                    jbyteArray byte_array, jint start_index,
                                                    jint length) {
    return 0;
}