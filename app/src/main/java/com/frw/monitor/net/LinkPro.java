package com.frw.monitor.net;

public class LinkPro implements TcpLink {

	@Override
	public boolean tcpConnect() {
		// TODO Auto-generated method stub
		System.out.println("���ӷ����");
		return true;
	}

	@Override
	public boolean disconnected() {
		// TODO Auto-generated method stub
		System.out.println("�ر�����");
		return true;
	}

	@Override
	public int tcpSend(int num) {
		// TODO Auto-generated method stub
		System.out.println("����"+num+"�ֽ�");
		return num;
	}

	@Override
	public int tcpReceive(int num) {
		// TODO Auto-generated method stub
		System.out.println("����"+num+"�ֽ�");
		return 0;
	}

}
