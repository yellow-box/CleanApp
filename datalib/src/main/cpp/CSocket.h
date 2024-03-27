//
// Created by hy on 2024/3/18.
//

#ifndef CLEANAPP_CSOCKET_H
#define CLEANAPP_CSOCKET_H
class CSocket {
public:
    int  clientSocketFd;
    void connect(const char *ip, int port);

    void disconnect();

    int read(jbyte *buf, int startIndex, int length);

    void write(jbyte *buf,int len);

    bool isConnected();

};

#endif //CLEANAPP_CSOCKET_H
