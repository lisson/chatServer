package chatServer.shared;

public class StringBuffer {
	private StringBuffer(){}
	
	public static StringBuffer getReference()
	{
		if(buff == null)
		{
			buff = new StringBuffer();
			return buff;
		}
		return buff;
	}
	
	public void setFlag()
	{
		flag = true;
	}
	
	public boolean flag()
	{
		return flag;
	}
	
	public void setMessage(String s, int n)
	{
		message = s;
		lastMod = n;
	}
	
	public int lastMod()
	{
		return lastMod;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void unsetFlag()
	{
		flag = false;
	}
	
	private int lastMod;
	private static StringBuffer buff = null;
	private String message = null;
	private boolean flag = false;
}
