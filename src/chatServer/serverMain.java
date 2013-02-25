package chatServer;

import chatServer.output.OutputManager;
import chatServer.server.ChatServer;


public class serverMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port=0;
		int max=1;
		try
		{
			port = Integer.parseInt(args[0]);
			max = Integer.parseInt(args[1]);
		}
		catch(Exception e)
		{
			System.out.println("Not an integer");
			System.exit(1);
		}
		
		ChatServer server = ChatServer.getReference();
		(OutputManager.getReference()).setVerbosity(args[2]);
		server.setPort(port);
		server.setWorker(max);
		System.out.println("Setting up " + max + " workers");
		System.out.println("Server verbosity level: " + args[2]);
		server.start();
	}

}
