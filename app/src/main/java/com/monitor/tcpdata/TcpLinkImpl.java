package com.monitor.tcpdata;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yoho on 2016/11/15.
 */

public class TcpLinkImpl implements TcpLink {
    private static final String TAG = "TcpLinkImpl";
    Socket socket = null;
    InputStream it;
    OutputStream ot;

    @Override
    public boolean tcpConnect(Socket soc) {
        try {
            this.socket = soc;
            it = socket.getInputStream();
            ot = socket.getOutputStream();
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
        try {
            ot.write(sendData, 0, num);
            ot.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "tcp send error");
        }
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
