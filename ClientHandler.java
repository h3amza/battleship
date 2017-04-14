import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket socket = null;
    private ArrayList<Socket> socketList;
    private Game game;

    ClientHandler(Socket _socket, ArrayList<Socket> _socketList, Game _game)
    {
        this.socket = _socket;
        this.socketList = _socketList;
        this.game = _game;
    }

    public void run()
    {
        System.out.println("Connected to socket: " + socket);
        try {
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true)
            {
                String inputText = clientIn.readLine();
                if(inputText != null)
                {
                    System.out.println(inputText);
                }
                else
                {
                    System.out.println("Closing socket " + socket);
                    socketList.remove(socket);
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            socketList.remove(socket);
        }



    }
}
