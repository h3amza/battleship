
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args)
    {
        final String hostname = "locaclhost";
        final int port = 7654;

        try {
            Socket socket = new Socket(hostname,port);
            DataOutputStream serverOut = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connected to server on port " + port);

            ClientListener clientListener = new ClientListener(socket);
            Thread thread = new Thread(clientListener);
            thread.start();

            Scanner keyboard = new Scanner(System.in);

            while(true)
            {
                String data = keyboard.nextLine();
                serverOut.writeBytes(data + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
