package chatServer.broadcast;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;

import chatServer.shared.OpenClients;
import chatServer.shared.StringBuffer;


public class Broadcast implements Runnable {

	public Broadcast(Semaphore s)
	{
		sem = s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		buff = StringBuffer.getReference();
		clients = OpenClients.getReference();
		
		while(true)
		{
			try
			{
				//System.out.println("Broadcaster waiting for signal");
				sem.acquire();
			}
			catch(Exception e)
			{}
			
			if(buff.flag())
			{
				synchronized(buff)
				{
					synchronized(clients)
					{
						list = clients.getClients();
						//System.out.println("Sending message " + buff.getMessage() + " to all clients");
						try {
							for(int i=0;i<list.length;i++)
							{
								if(list[i]!=null && buff.lastMod() != i)
								{
									//System.out.println("Thread " + i + " is not null, writing");
									list[i].write(buff.getMessage()+"\r\n");
									list[i].flush();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//System.out.println("Writing t user  " + i " was no successful");
							//e.printStackTrace();
						}
						clients.notifyAll();
					}
					//System.out.println("Unsetting flag");
					buff.unsetFlag();
					buff.notifyAll();
				}
			}
		}
	}

	private PrintWriter[] list = null;
	private Semaphore sem = null;
	private StringBuffer buff = null;
	private OpenClients clients = null;
}
