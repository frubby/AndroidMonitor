package com.frw.monitor.net;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by yoho on 2016/11/9.
 */

public class TcpClient {
    Socket socket = null;


    public String getData() {
        String data = "";
        try {

            socket = new Socket("172.16.6.29", 1234);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //                System.out.print("enter: \t");
//                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                out.writeUTF(str);

            char[] revDatas = new char[4096];
            try {
                int i = 0;
                while (true) {
                    char ret = input.readChar();
                    revDatas[i++] = ret;
//                        Log.i("test", "result: " + Integer.toHexString((int)ret));
                    if (ret == 0x16)
                        break;
                }
                int pos = i - 1;
                System.out.println("\npos size : " + pos);
                for (i = 10; i < pos - 10; i++) {
                    if ((i - 10) % 26 == 0)
                        System.out.println("");
                    System.out.print(" " + Integer.toHexString((int) revDatas[i]));

                }
            } catch (Exception e) {
                Log.e("test", e.getMessage());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }


}
