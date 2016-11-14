package com.frw.monitor.net;

public class LinkPro implements TcpLink {

	@Override
	public boolean tcpConnect() {
		// TODO Auto-generated method stub
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
		System.out.println("接收"+num+"字节");
		return 0;
	}

}
