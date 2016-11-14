package com.frw.monitor.net;

public class Run {
	public Data data = new Data();//��Լ�ṩ������
	TcpLink link = new LinkPro();//������ʵ��ʵ�ֵ���
	
	int refreshTime = 1;//����ʵ��ˢ��ʱ��
	
	public void run() {//����һ�߳�����
		Protocol protocol = new Protocol(link);
		
		do {
			if(!protocol.getLinkState())
			{
				try {
					while(!link.tcpConnect())
						Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			protocol.dataProcess();
			data = protocol.getData();
			
			if(!protocol.getLinkState())
			{
				link.disconnected();
			}

			try {
				Thread.sleep(refreshTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(protocol.getLinkState());
		
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
