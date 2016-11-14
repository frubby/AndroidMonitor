package com.frw.monitor.net;

import com.frw.monitor.bean.Data;

public class Run {
    public Data data = new Data();//规约提供的数据
    TcpLink link = new LinkPro();//换成你实际实现的类

    int refreshTime = 1;//换成实际刷新时间

    public void run() {//单开一线程来跑
        Protocol protocol = new Protocol(link);

        do {
            if (!protocol.getLinkState()) {
                try {
                    while (!link.tcpConnect())
                        Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            protocol.dataProcess();
            data = protocol.getData();

            if (!protocol.getLinkState()) {
                link.disconnected();
            }

            try {
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (protocol.getLinkState());

    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Run run = new Run();
        run.run();
    }

}
