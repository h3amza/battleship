/*
 * A combination of client handler and client listener. 
 * Defines all functions to play the game and also displays data to client screen.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler extends Thread 
{

    public Socket clientSocket = null;
    private final ClientHandler[] handlers;

    public ClientHandler(Socket connectionSock, ClientHandler[] handlers) 
    {
        this.clientSocket = connectionSock;
        this.handlers = handlers;
    }
}
