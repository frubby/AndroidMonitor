package com.monitor.tcpdata;

import java.net.Socket;

public interface TcpLink {
	public byte[] sendData = new byte[4096];
	public byte[] receiveData = new byte[4096];


	/**
	 * 连接服务端
	 * @return 成功 true
	 */
	public boolean tcpConnect(Socket socket);

	/**
	 * 关闭连接
	 * @return 成功 true
	 */
	public boolean disconnected();

	/**
	 * 发送接收数据
	 * @param num 需处理字节数
	 * @return 实际处理字节数
	 */
	public int tcpSend(int num);
	public int tcpReceive(int num);
}
