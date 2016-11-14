package com.frw.monitor.net;

public interface TcpLink {
	public char[] sendData = new char[512];
	public char[] receiveData = new char[512];
	
	
	/**
	 * ���ӷ����
	 * @return �ɹ� true
	 */
	public boolean tcpConnect();
	
	/**
	 * �ر�����
	 * @return �ɹ� true
	 */
	public boolean disconnected();
	
	/**
	 * ���ͽ�������
	 * @param num �账���ֽ���
	 * @return ʵ�ʴ����ֽ���
	 */
	public int tcpSend(int num);
	public int tcpReceive(int num);
}
