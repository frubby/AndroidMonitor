package com.monitor.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by yoho on 2016/11/15.
 */

public class TcpLinkImpl implements TcpLink {
    public final String TAG = "TcpLinkImpl";
    Socket socket = null;
    InputStream it;

    @Override
    public boolean tcpConnect(String ip, int port) {
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress, 8000);
            socket.setSoTimeout(5000);
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

        if (it != null) {
            try {
                it.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        int recvNum = 0;
        byte recvByte[] = new byte[4096];
        try {
            recvNum = it.read(recvByte);
            for (int k = 0; k < recvNum; k++) {
                System.out.print(" " + Integer.toHexString(recvByte[k] & 0xFF));
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        if (recvNum > 0) {
            System.arraycopy(recvByte, 0, receiveData, 0, recvNum);
            System.out.println("\n num  " + recvNum);
        }
        return recvNum;
    }
}
