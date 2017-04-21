import jdk.net.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Socket> sockets;
    private static final ClientHandler[] handlers = new ClientHandler[2];

    Server(){
        sockets = new ArrayList<>();
    }
        private void getConnection()
        {
            // Wait for a connection from the client
            try
            {
                System.out.println("Waiting for client connections on port 7654.");
                ServerSocket serverSock = new ServerSocket(7654);
                // This is an infinite loop, the user will have to shut it down
                // using control-c
                while (true)
                {
                    Socket connectionSock = serverSock.accept();
                    // Add this socket to the list
                    sockets.add(connectionSock);
                    for (int i = 0; i < 2; i++) 
                    {
                        if (handlers[i] == null) 
                        {
                            (handlers[i] = new ClientHandler(connectionSock, handlers)).start();
                            break;
                        }
                    }
                    // Send to ClientHandler the socket and arraylist of all sockets
                    //ClientHandler handler = new ClientHandler(connectionSock, this.sockets);
                    //Thread theThread = new Thread(handler);
                    //theThread.start();
                }
                // Will never get here, but if the above loop is given
                // an exit condition then we'll go ahead and close the socket
                //serverSock.close();
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }

        public static void main(String[] args)
        {
            Server server = new Server();
            server.getConnection();
        }

}
