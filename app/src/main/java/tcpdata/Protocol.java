package tcpdata;

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

68 01 00 00 00 00 00 68 81 4E 01 00 00 00 00 00 57 04 00 00 67 2B 00 00 07 B2 01 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 AE 08 00 00 CE 56 00 00 0E 64 03 00 01 00 00 00 01 00 00 00 03 00 00 00 00 00 05 0D 00 00 35 82 00 00 15 16 05 00 03 00 00 00 F4 00 00 00 8E 16

 */

import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;

public class Protocol {
    private TcpLink link;
    private boolean linkState;
    private int linkStateNum;

    private int sendPoint;

    private Data data;
    private Data result;
    private boolean dataFlag;

    private SwitchData switchdata;
    private int sendNum;

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
        result = new Data();
        dataFlag = false;

        switchdata = new SwitchData();
        sendNum = 0;
    }

    public void dataProcess() {
        stateProcess();
        dataReceiveProcess();
        dataSendProcess();
    }

    private void stateProcess() {
        ++linkStateNum;

        if (linkStateNum > 10)  //连续10次没有收到msg,重新连接
            init();
        else
            linkState = true;
    }

    private void dataSendProcess() {
        if (sendNum > 0)
            sendPoint = link.tcpSend(sendNum);
        sendNum = 0;
    }

    private int makeData(SwitchData sdata) {
        int ptr = sendNum;
        link.sendData[ptr++] = 0x68;
        link.sendData[ptr++] = (byte) (sdata.address & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.address >> 8) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.address >> 16) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.address >> 24) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.address >> 32) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.address >> 40) & 0xff);
        link.sendData[ptr++] = 0x68;
        link.sendData[ptr++] = 0x04;
        link.sendData[ptr++] = 0x0A;

        link.sendData[ptr++] = (byte) (sdata.num & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.num >> 8) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.num >> 16) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.num >> 24) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.num >> 32) & 0xff);
        link.sendData[ptr++] = (byte) ((sdata.num >> 40) & 0xff);

        int state = 0;
        if (sdata.switchState == "断开")
            state |= 0x00;
        else if (sdata.switchState == "A相")
            state |= 0x01;
        else if (sdata.switchState == "B相")
            state |= 0x02;
        else if (sdata.switchState == "C相")
            state |= 0x04;
        int loadType = Integer.parseInt(sdata.loadType) | state;
        link.sendData[ptr++] = (byte) loadType;

        link.sendData[ptr++] = calcSum(sendNum, ptr - sendNum, false);
        link.sendData[ptr++] = 0x16;

        return ptr;
    }

    private void dataReceiveProcess() {
        int sum = link.tcpReceive(4096);

        int ptr = 0;
        int frame_len = 0;
        while (sum >= 12) {
            if (link.receiveData[ptr] == 0x68 && link.receiveData[ptr + 7] == 0x68 && link.receiveData[ptr + 9] != 0) {
                frame_len = (int) (getLongData(ptr + 9, 1) + 12);
                if (frame_len > sum || calcSum(ptr, frame_len - 2, true) != link.receiveData[ptr + frame_len - 2])
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

    private byte calcSum(int ptr, int len, boolean isReceive) {
        long sum = 0;
        if (isReceive) {
            for (int i = 0; i < len; ++i)
                sum += link.receiveData[ptr + i];
        } else {
            for (int i = 0; i < len; ++i)
                sum += link.sendData[ptr + i];
        }

        return (byte) (sum & 0xFF);
    }

    private void dataPro(int ptr) {
        int num = 0, temp = 0, i = 0;

        ptr += 8;
        if ((link.receiveData[ptr] & 0x20) == 0)
            dataFlag = true;
        else
            link.receiveData[ptr] &= ~(0x20);

        switch (link.receiveData[ptr]) {
            case (byte) 0x81:
                ptr += 1;
                num = (int) getLongData(ptr, 1);
                if (num % 26 != 0)
                    return;
                num /= 26;
                ptr += 1;

                if (getLongData(ptr - 9, 6) == getLongData(ptr, 6)) {
                    data.num = 0;

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
                }

                i = data.num;
                if (data.num == 0)
                    data.num = num - 1;
                else
                    data.num += num;
                if (data.num > 64)
                    return;

                for (; i < data.num; ++i) {
                    SwitchData newData=new SwitchData();
                    newData.address = getLongData(ptr, 6);
                    ptr += 6;

                    newData.Ia = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    newData.Ib = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;
                    newData.Ic = (float) getLongData(ptr, 4) / 1000;
                    ptr += 4;

                    newData.num = (int) getLongData(ptr, 4);
                    ptr += 4;

                    newData.load = 0;
                    temp = (int) getLongData(ptr, 4);
                    ptr += 4;
                    switch (temp & 0x07) {
                        case 0x00:
                            newData.switchState = "断开";
                            break;
                        case 0x01:
                            newData.switchState = "A相";
                            newData.load = newData.Ia;
                            break;
                        case 0x02:
                            newData.switchState = "B相";
                            newData.load = newData.Ib;
                            break;
                        case 0x04:
                            newData.switchState = "C相";
                            newData.load = newData.Ic;
                            break;
                        default:
                            newData.switchState = "无效";
                            break;
                    }
                    int l = (temp & 0xF0) >> 4;//等具体解释
                    newData.loadType = Integer.toString(l);

                    data.sdata.add(newData);
                }

                if (dataFlag) {
                    result = data;

                    dataFlag = false;
                    data.num = 0;
                }
                break;
            case (byte) 0x84:
                ptr += 1;
                num = (int) getLongData(ptr, 1);
                if (num % 26 != 0) {
                    switchdata.address = -1;
                    switchdata.num = -1;
                    break;
                }
                ptr += 1;

                switchdata.address = getLongData(ptr, 6);
                ptr += 6;

                switchdata.Ia = (float) getLongData(ptr, 4) / 1000;
                ptr += 4;
                switchdata.Ib = (float) getLongData(ptr, 4) / 1000;
                ptr += 4;
                switchdata.Ic = (float) getLongData(ptr, 4) / 1000;
                ptr += 4;

                switchdata.num = (int) getLongData(ptr, 4);
                ptr += 4;

                switchdata.load = 0;
                temp = (int) getLongData(ptr, 4);
                ptr += 4;
                switch (temp & 0x07) {
                    case 0x00:
                        switchdata.switchState = "断开";
                        break;
                    case 0x01:
                        switchdata.switchState = "A相";
                        switchdata.load = switchdata.Ia;
                        break;
                    case 0x02:
                        switchdata.switchState = "B相";
                        switchdata.load = switchdata.Ib;
                        break;
                    case 0x04:
                        switchdata.switchState = "C相";
                        switchdata.load = switchdata.Ic;
                        break;
                    default:
                        switchdata.switchState = "无效";
                        break;
                }
                int l = (temp & 0xF0) >> 4;//等具体解释
                switchdata.loadType = Integer.toString(l);
                break;
            case (byte) 0xC4:
                switchdata.address = -1;
                switchdata.num = link.receiveData[ptr + 2];
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
            temp = link.receiveData[ptr + n];
            if (temp < 0)
                temp += 256;
            sum |= temp << (8 * n);
        }

        return sum;
    }

    public void setLinkState(boolean state) {
        linkState = state;
    }

    public boolean getLinkState() {
        return linkState;
    }

    public void setLinkStateNum(int linkStateNum) {
        this.linkStateNum = linkStateNum;
    }

    public Data getData() {
        return result;
    }

    public void setSwitchData(SwitchData sdata) {
        sendNum += makeData(sdata);
    }

    public void getSwitchData(SwitchData sdata) {
        sdata = switchdata;
        switchdata.address = 0;
    }
}
