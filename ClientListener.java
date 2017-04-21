import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientListener implements Runnable{
    private Socket connectionSock = null;

    ClientListener(Socket socket) {
        this.connectionSock = socket;
    }

    public void run(){
        try
        {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
            while (true)
            {
                // Get data sent from the server
                String serverText = serverInput.readLine();
                if (serverInput != null)
                {
                    System.out.println(serverText);
                    if(serverText.contains("Win") || serverText.contains("Lose"))
                    {
                        System.out.println("Ending Game");
                        connectionSock.close();
                        System.exit(0);
                    }
                }
                else
                {
                    // Connection was lost
                    System.out.println("Closing connection for socket " + connectionSock);
                    connectionSock.close();
                    break;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.toString());
        }
    }
}
