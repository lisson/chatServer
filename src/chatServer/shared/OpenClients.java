package chatServer.shared;
import java.io.PrintWriter;

public class OpenClients {
	OpenClients(){}
	
	public static OpenClients getReference()
	{
		if(reference == null)
		{
			reference = new OpenClients();
			return reference;
		}
		return reference;
	}
	
	public void setClients(int n)
	{
		clients = new PrintWriter[n];
	}
	
	public void registerClient(int n, PrintWriter c)
	{
		clients[n] = c;
		clients[n].write("Welcome\r\n");
		clients[n].write("Type \"--quit\" to disconnect\r\n");
		clients[n].flush();
		total++;
		clients[n].write("There are " + total + " users in this room\r\n");
		clients[n].flush();
	}
	
	public void unregisterClient(int n)
	{
		clients[n] = null;
		total--;
	}
	
	public PrintWriter[] getClients()
	{
		return clients;
	}
	
	
	public int getTotal()
	{
		return total;
	}
	
	private int total = 0;
	private PrintWriter[] clients = null;
	private static OpenClients reference;
}
