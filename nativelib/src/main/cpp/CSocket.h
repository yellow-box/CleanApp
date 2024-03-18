//
// Created by hy on 2024/3/18.
//

#ifndef CLEANAPP_CSOCKET_H
#define CLEANAPP_CSOCKET_H
#define bytearray(name) unsigned char type[]

class CSocket {
public:
    void connect(char *ip, char *port);

    void disconnect();

    int read(bytearray(buf));

    int read(bytearray(buf), int startIndex, int length);

    void write(bytearray(buf));

    bool isConnected();

};

#endif //CLEANAPP_CSOCKET_H
