package com.frw.monitor.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by yoho on 2016/11/15.
 */

public class TcpLinkImpl implements TcpLink {
    Socket socket = null;

    DataInputStream input;
    InputStream it;

    @Override
    public boolean tcpConnect() {
        try {
            socket = new Socket("172.16.6.29", 1234);
            input = new DataInputStream(socket.getInputStream());
            it = socket.getInputStream();
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


        int i = 0;
//        for (i = 0; i < num; i++) {
        char ch = 0;
//            try {
//                ch = input.readChar();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return 0;
//            }
//            System.out.print(" " + Integer.toHexString((int) ch));
        int recvNum = 0;
        byte recvByte[] = new byte[8192];
        try {
            recvNum = it.read(recvByte);
            for (int k = 0; k < recvNum; k++) {
                System.out.print(" " + Integer.toHexString(recvByte[k]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.arraycopy(recvByte, 0, receiveData, 0, recvNum);
//        }
        System.out.println(" num  " + recvNum);

        return i;
    }
}
