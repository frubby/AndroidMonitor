package com.monitor.net;

/*
68 01 00 00 00 00 00 68 81 4E
01 00 00 00 00 00 地址 台区
57 04 00 00 A相1.111
67 2B 00 00 B相11.111
07 B2 01 00 C相111.111
00 00 00 00
00 00 00 00

02 00 00 00 00 00 地址 开关1
AE 08 00 00 A相2.222
CE 56 00 00 B相22.222
0E 64 03 00 C相222.222
01 00 00 00  动作次数 1
01 00 00 00  A相 优先级最高
03 00 00 00 00 00 地址 开关2
05 0D 00 00 A相3.333
35 82 00 00 B相33.333
15 16 05 00 C相333.333
03 00 00 00  动作次数 3
F4 00 00 00  C相 优先级最低

8E 16
*/

import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;

public class Protocol {
    private TcpLink link;
    private boolean linkState;
    private int linkStateNum;

    private int sendPoint;

    private Data data;


    public Protocol(TcpLink linkPro) {
        // TODO Auto-generated constructor stub
        link = linkPro;
        init();
    }

    private void init() {
        linkState = false;
        linkStateNum = 0;

        sendPoint = 0;

        data = new Data();
    }

    public void dataProcess() {
        stateProcess();
        dataReceiveProcess();
        dataSendProcess();
    }

    private void stateProcess() {
        ++linkStateNum;

        if (linkStateNum > 10)
            init();
        else
            linkState = true;
    }

    private void dataSendProcess() {
        int sum = makeData();

        //sum += sendPoint;
        if (sum > 0)
            sendPoint = link.tcpSend(sum);
    }

    private int makeData() {

        return 0;
    }

    private void dataReceiveProcess() {
        int sum = link.tcpReceive(4096);

        int ptr = 0;
        int frame_len = 0;
        while (sum >= 12) {
            if (link.receiveData[ptr] == 0x68 && link.receiveData[ptr + 7] == 0x68 && link.receiveData[ptr + 9] != 0) {
                frame_len = (int) (getLongData(ptr+9,1) + 12);
                if (frame_len > sum || calcSum(ptr, frame_len - 2) != link.receiveData[ptr + frame_len - 2])
                    break;

                dataPro(ptr);

                ptr += frame_len;
                sum -= frame_len;

                continue;
            }
            ++ptr;
            --sum;
        }
    }

    private byte calcSum(int ptr, int len) {
        long sum = 0;
        for (int i = 0; i < len; ++i)
            sum += link.receiveData[ptr + i];

        return (byte) (sum & 0xFF);
    }

    private void dataPro(int ptr) {
        int num = 0;

        ptr += 8;
        switch (link.receiveData[ptr]) {
            case (byte) 0x81:
                ptr += 1;
                num = (int) getLongData(ptr, 1);
                if (num % 26 != 0)
                    return;
                num /= 26;
                ptr += 1;

                data.num = num - 1;
                if (data.num > 32)
                    return;

                int temp = 0;
                data.sdata.clear();
                for (int i = 0; i < num; ++i) {
                    if (i == 0) {
                        data.address = getLongData(ptr, 6);
                        ptr += 6;

                        data.Ia = (float) getLongData(ptr, 4) / 1000;
                        ptr += 4;
                        data.Ib = (float) getLongData(ptr, 4) / 1000;
                        ptr += 4;
                        data.Ic = (float) getLongData(ptr, 4) / 1000;
                        ptr += 4;
                        float max = data.Ia > data.Ib ? data.Ia : data.Ib;
                        max = max > data.Ic ? max : data.Ic;
                        float min = data.Ia < data.Ib ? data.Ia : data.Ib;
                        min = min < data.Ic ? min : data.Ic;
                        data.imbalance = (max - min) / max * 100;

                        ptr += 4;
                        ptr += 4;
                        continue;
                    }

                    SwitchData switchData=new SwitchData();
                    switchData.address = getLongData(ptr, 6);
                    ptr += 6;

                    switchData.Ia = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    switchData.Ib = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    switchData.Ic = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;

                    switchData.num = (int) getLongData(ptr, 4);
                    ptr += 4;

                    switchData.load = 0;
                    temp = (int) getLongData(ptr, 4);
                    ptr += 4;
                    switch (temp & 0x07) {
                        case 0x00:
                            switchData.switchState = "断开";
                            break;
                        case 0x01:
                            switchData.switchState = "A相";
                            switchData.load = switchData.Ia;
                            break;
                        case 0x02:
                            switchData.switchState = "B相";
                            switchData.load = switchData.Ib;
                            break;
                        case 0x04:
                            switchData.switchState = "C相";
                            switchData.load = switchData.Ic;
                            break;
                        default:
                            switchData.switchState = "无效";
                            break;
                    }
                    int l = (temp & 0xF0) >> 4;//等具体解释
                    switchData.loadType = Integer.toString(l);

                    data.sdata.add(switchData);
                }
                break;
            default:
                break;
        }

        linkStateNum = 0;
    }

    private long getLongData(int ptr, int len) {
        if (len > 8)
            len = 8;

        long sum = 0, temp = 0;
        int n = -1;
        while (++n < len) {
            temp = link.receiveData[ptr + n] < 0 ? 256 + link.receiveData[ptr + n] : link.receiveData[ptr + n];
            sum |= temp << (8 * n);
        }

        return sum;
    }

    public boolean getLinkState() {
        return linkState;
    }

    public void setLinkStateNum(int linkStateNum) {
        this.linkStateNum = linkStateNum;
    }

    public Data getData() {
        return data;
    }
}
