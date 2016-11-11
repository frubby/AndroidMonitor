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


            while (true) {
                try {
                    String ret = input.readUTF();
                    Log.i("test", "result: " + ret);
                    if (ret.equals("finish"))
                        break;
                } catch (Exception e) {
                    Log.e("test", e.getMessage());
                }
            }
            if (input != null)
                input.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }


}
