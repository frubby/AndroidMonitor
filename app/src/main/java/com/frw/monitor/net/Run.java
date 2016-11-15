package com.frw.monitor.net;

import com.frw.monitor.bean.Data;

public class Run {
    public Data data = new Data();//规约提供的数据
    TcpLink link = new LinkPro();//换成你实际实现的类

    int refreshTime = 1;//换成实际刷新时间
    boolean flag = true;//线程运行标志

    public void run() {//单开一线程来跑
        Protocol protocol = new Protocol(link);

        while (flag) {
            if (!protocol.getLinkState()) {
                try {
                    while (flag && !link.tcpConnect())
                        Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!flag)
                break;

            protocol.dataProcess();
            data = protocol.getData();

            if (!protocol.getLinkState()) {
                link.disconnected();
                protocol.setLinkStateNum(0);
            }

            try {
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
