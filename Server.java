/*
* This class defines Server that starts Client handler threads and communicates responses
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server 
{
	public static ServerSocket serverSocket = null;
	public static Socket clientSocket = null;
	public static final ClientHandler[] handler = new ClientHandler[2]; // one handler for each player
	public static final int port = 7654;


	public static void main(String args[]) 
	{
		
		try 
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Connection made");
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}

		while (true) 
		{
			try 
			{
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < 2; i++) 
				{
					if (handler[i] == null) 
					{
						(handler[i] = new ClientHandler(clientSocket, handler)).start(); // create handler and start
						break;
					}
				}	
			} 
			catch (IOException e) 
			{
				System.out.println(e);
			}
		}
	}
}
