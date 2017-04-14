import jdk.net.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Socket> sockets;
    private Game game;

    Server(){
        sockets = new ArrayList<>();
        game = new Game();
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
                    // Send to ClientHandler the socket and arraylist of all sockets
                    ClientHandler handler = new ClientHandler(connectionSock, this.sockets, game);
                    Thread theThread = new Thread(handler);
                    theThread.start();
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
