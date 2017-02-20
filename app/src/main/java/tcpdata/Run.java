package tcpdata;

import com.monitor.bean.Data;
import com.monitor.bean.SwitchData;

public class Run {
	public Data data = new Data();//规约提供的数据
	public SwitchData sdata = new SwitchData();//遥控后返回的数据
	TcpLink link = new LinkPro();//换成你实际实现的类

	int refreshTime = 1000;//换成实际刷新时间
	boolean flag = true;//线程运行标志

	public void run() {//单开一线程来跑
		Protocol protocol = new Protocol(link);

		while(flag) {
			if(!protocol.getLinkState())
			{
//				try {
//					while(flag && !link.tcpConnect())
//						Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			if(!flag)
				break;

			protocol.dataProcess();

			data = protocol.getData();//需处理 名称匹配


			protocol.getSwitchData(sdata);//需处理  地址查找后修改相应开关 address=0无效，<0时num=错误码, >0开关地址

			if(!protocol.getLinkState())
			{
				link.disconnected();
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
