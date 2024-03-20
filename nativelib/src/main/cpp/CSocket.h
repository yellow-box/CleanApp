//
// Created by hy on 2024/3/18.
//

#ifndef CLEANAPP_CSOCKET_H
#define CLEANAPP_CSOCKET_H
#define bytearray(name) unsigned char name[]
class CSocket {
public:
    int  clientSocketFd;
    void connect(const char *ip, int port);

    void disconnect();

    int read(bytearray(buf), int startIndex, int length);

    void write(bytearray(buf));

    bool isConnected();

};

#endif //CLEANAPP_CSOCKET_H
