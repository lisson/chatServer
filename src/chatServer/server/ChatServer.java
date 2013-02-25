package chatServer.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import chatServer.broadcast.Broadcast;
import chatServer.handlers.ConnectionHandler;
import chatServer.shared.OpenClients;

public class ChatServer {
	public static ChatServer getReference()
	{
		if(cServer == null)
		{
			cServer = new ChatServer();
			return cServer;
		}
		return cServer;
	}
	
	public void setPort(int p)
	{
		port = p;
	}
	
	public void setWorker(int w)
	{
		nWorkers=w;
	}
	
	public void start()
	{
		sem = new Semaphore(0);
		new Thread(new Broadcast(sem)).start();
		try
		{
			sSocket = new ServerSocket(port);
		}
		catch(Exception e)
		{
			System.out.println("Failed to initiate Server Socket");
		}
		queue = new LinkedList();
		handler = new ConnectionHandler[nWorkers];
		OpenClients clients = OpenClients.getReference();
		clients.setClients(nWorkers);
		for (int i=0;i<nWorkers;i++)
		{
			handler[i] = new ConnectionHandler(queue, sem,i);
			new Thread(handler[i]).start();
		}
		
		Socket client = null;
		System.out.println("Server: Waiting for client on port "+port);
		while(true)
		{
			try
			{
				client = sSocket.accept();
			}
			catch(Exception e)
			{
				System.out.println("Failed to accept client");
			}
			synchronized(queue)
			{
				if(client != null)
				{
					queue.add(client);
				}
				queue.notifyAll();
			}
		}
	}
	
	private ChatServer()
	{
	}
	
	private Semaphore sem;
	private static ChatServer cServer= null;
	private int port;
	private int nWorkers;
	private ServerSocket sSocket = null;
	private LinkedList queue = null;
	private ConnectionHandler[] handler = null;
}
