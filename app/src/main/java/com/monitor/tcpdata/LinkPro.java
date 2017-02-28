package com.monitor.tcpdata;

import java.net.Socket;

public class LinkPro implements TcpLink {

	@Override
	public boolean tcpConnect(Socket socket) {
		System.out.println("连接服务端");
		return true;
	}

	@Override
	public boolean disconnected() {
		// TODO Auto-generated method stub
		System.out.println("关闭连接");
		return true;
	}

	@Override
	public int tcpSend(int num) {
		// TODO Auto-generated method stub
		System.out.println("发送"+num+"字节");
		return num;
	}

	@Override
	public int tcpReceive(int num) {
		// TODO Auto-generated method stub
		//68 01 00 00 00 00 00 68 81 4E
		int ptr = 0;
		receiveData[ptr++] = 0x68;
		receiveData[ptr++] = 0x01;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x68;
		receiveData[ptr++] = (byte) 0x81;
		receiveData[ptr++] = 0x4E;

		//01 00 00 00 00 00 地址 台区
		receiveData[ptr++] = 0x01;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//57 04 00 00 A相1.111
		receiveData[ptr++] = 0x57;
		receiveData[ptr++] = 0x04;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//67 2B 00 00 B相11.111
		receiveData[ptr++] = 0x67;
		receiveData[ptr++] = 0x2B;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//07 B2 01 00 C相111.111
		receiveData[ptr++] = 0x07;
		receiveData[ptr++] = (byte) 0xB2;
		receiveData[ptr++] = 0x01;
		receiveData[ptr++] = 0x00;
		//00 00 00 00
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//00 00 00 00
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;

		//02 00 00 00 00 00 地址 开关1
		receiveData[ptr++] = 0x02;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//AE 08 00 00 A相2.222
		receiveData[ptr++] = (byte) 0xAE;
		receiveData[ptr++] = 0x08;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//CE 56 00 00 B相22.222
		receiveData[ptr++] = (byte) 0xCE;
		receiveData[ptr++] = 0x56;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//0E 64 03 00 C相222.222
		receiveData[ptr++] = 0x0E;
		receiveData[ptr++] = 0x64;
		receiveData[ptr++] = 0x03;
		receiveData[ptr++] = 0x00;
		//01 00 00 00  动作次数 1
		receiveData[ptr++] = 0x01;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//01 00 00 00  A相 优先级最高
		receiveData[ptr++] = 0x01;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;

		//03 00 00 00 00 00 地址 开关2
		receiveData[ptr++] = 0x03;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//05 0D 00 00 A相3.333
		receiveData[ptr++] = 0x05;
		receiveData[ptr++] = 0x0D;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//35 82 00 00 B相33.333
		receiveData[ptr++] = 0x35;
		receiveData[ptr++] = (byte) 0x82;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//15 16 05 00 C相333.333
		receiveData[ptr++] = 0x15;
		receiveData[ptr++] = 0x16;
		receiveData[ptr++] = 0x05;
		receiveData[ptr++] = 0x00;
		//03 00 00 00  动作次数 3
		receiveData[ptr++] = 0x03;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		//F4 00 00 00  C相 优先级最低
		receiveData[ptr++] = (byte) 0xF4;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;
		receiveData[ptr++] = 0x00;

		//8E 16
		receiveData[ptr++] = (byte) 0x8E;
		receiveData[ptr++] = 0x16;


		System.out.println("接收"+(receiveData[9] + 12)+"字节");
		return receiveData[9] + 12;
	}

}
