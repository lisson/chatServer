package chatServer.handlers;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import chatServer.output.OutputManager;
import chatServer.shared.OpenClients;
import chatServer.shared.StringBuffer;


public class ConnectionHandler implements Runnable {

	public ConnectionHandler(LinkedList l, Semaphore s, int n)
	{
		//Queue is the list of client socket waiting to get into the server
		queue = l;
		//Semaphore is used to signal the broadaster
		sem = s;
		//gets the output manager
		output = OutputManager.getReference();
		workerID = n;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		buff = StringBuffer.getReference();
		clients = OpenClients.getReference();
		Scanner input = null;
		
		while(true)
		{
			while(queue.isEmpty())
			{
				synchronized (queue) {
					try {
						output.debug("Thread " + workerID
								+ " waiting for queue");
						queue.wait();
					} catch (Exception e) {
					}
				}
			}
			
			synchronized(queue)
			{
				client = (Socket) queue.removeFirst();
				queue.notifyAll();
			}
			
			output.info("Thread " + workerID + " serving client " + client.getInetAddress().toString());
			
			synchronized(clients)
			{
				try {
					output.debug("Registering worker " + workerID);
					clients.registerClient(workerID, new PrintWriter(client.getOutputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				clients.notifyAll();
			}
			
			try
			{
				input = new Scanner(client.getInputStream());
			}
			catch(Exception e)
			{
				output.error("Failed to get input stream");
				System.exit(1);
			}
			
			output.debug("Thread "+workerID + " waiting for input");
			message = input.nextLine();
			while(message.compareTo("--quit") != 0)
			{
				synchronized(buff)
				{
					if(buff.flag())
					{
						try
						{
							output.debug("Thread "+workerID+" waiting for buffer");
							buff.wait();
						}
						catch(Exception e)
						{}
					}
					else
					{
						output.debug("Thread " + workerID+" writing to buffer");
						buff.setMessage("User " + workerID + ": " + message, workerID);
						output.debug("Thread " + workerID+" setting flag");
						buff.setFlag();
						//signal semaphore
						output.debug("Thread " + workerID+" releasing semaphore");
						sem.release();
					}
				}
				//System.out.println("Thread "+workerID + " waiting for input");
				try
				{
					//if the connection has been closed, set message to quit
					message = input.nextLine();
				}
				catch(Exception e)
				{
					message = "--quit";
				}
			}
			
			//Send exit message
			synchronized(buff)
			{
				if(buff.flag())
				{
					try
					{
						//System.out.println("Thread "+workerID+" waiting for buffer");
						buff.wait();
					}
					catch(Exception e)
					{}
				}
				else
				{
					output.debug("Thread " + workerID+" writing to buffer");
					buff.setMessage("User " + workerID + " has left the room.", workerID);
					output.debug("Thread " + workerID+" setting flag");
					buff.setFlag();
					//signal semaphore
					output.debug("Thread " + workerID+" releasing semaphore");
					sem.release();
				}
			}
			
			output.info("Thread " + workerID + " disconnecting client " + client.getInetAddress().toString());
			synchronized(clients)
			{
				clients.unregisterClient(workerID);
				clients.notifyAll();
			}
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	private OpenClients clients = null;
	private OutputManager output = null;
	private int workerID;
	private Semaphore sem = null;
	private StringBuffer buff = null;
	private String message = "";
	private Socket client = null;		private LinkedList queue = null;
}
