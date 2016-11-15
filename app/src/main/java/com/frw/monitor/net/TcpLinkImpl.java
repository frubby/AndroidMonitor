package com.frw.monitor.net;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by yoho on 2016/11/15.
 */

public class TcpLinkImpl implements TcpLink {
    Socket socket = null;

    @Override
    public boolean tcpConnect() {
        try {
            socket = new Socket("172.16.6.29", 1234);
        } catch (IOException e) {
            return false;
        }
        if (socket.isConnected())
            return true;
        return false;

    }

    @Override
    public boolean disconnected() {
        if (socket == null)
            return true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public int tcpSend(int num) {

        return 0;
    }

    @Override
    public int tcpReceive(int num) {
        return 0;
    }
}
