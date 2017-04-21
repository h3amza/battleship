
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Client implements Runnable {

    public static Board p1Board,p2Board;
    public static Ship[] ships;
    public static Socket clientSocket;
    public static BufferedReader inStream,inLine;
    public static PrintStream outStream;
    public static String response,input;
    public static int boardSize = 10;
    public static boolean m_request = false;
    public static boolean m_game = false;
    public static boolean isClosed = false;
    public static String p2Name = "";
    public static boolean isInGame = false;
    public static int startingP = 0; 
    public static int row,column;
    public static char orientation;
    public static boolean set; 

    public static final String hostname = "localhost";
    public static final int port = 7654; 

    public static void main(String[] args)
    {
        p1Board = new Board(5,5);
        p2Board = new Board(5,5);
        Ship[] ships = new Ship[3];

        try 
        {

            clientSocket = new Socket(hostname,port);
            inLine = new BufferedReader(new InputStreamReader(System.in));
            outStream = new PrintStream(clientSocket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            System.out.println("##BATTLESHIP##");
            new Thread(new Client()).start();
            while(!isClosed) // game still on
            {
                for(int i=0;i<3;i++)
                {
                    set = false;
                    //clearScreen();
                    System.out.println("Set up board");
                    System.out.println("Enter row and column values [0-4]");
                    System.out.println("Each ship has size 2");
                    p1Board.drawBoard();

                    System.out.println("What's the row value?");
                    row = Integer.parseInt(inLine.readLine());
                    System.out.println("What's the column value?");
                    column = Integer.parseInt(inLine.readLine());
                    System.out.println("Horizontal or Vertical orientation?(H/V)");
                    orientation = inLine.readLine().charAt(0);
                    if(orientation == 'H')
                        ships[i] = new Ship(row,column,orientation);
                    else
                        ships[i] = new Ship(row,column,orientation);
                    set = p1Board.setShip(ships[i]);
                    if(!set)
                    {
                        System.out.println("Something went wrong. Press Enter to try again.");
                        inLine.readLine();
                    }
                }
                System.out.println("P1 Board:");
                System.out.println("");
                p1Board.drawBoard();
                if(startingP==0)
                    startingP=1;
                outStream.println("Ready " + p2Name);
                System.out.println("Press Enter to start the game");
                inLine.readLine();
                while(!isInGame)
                {
                    System.out.println("Wait for other player's turn");
                    busywait(1000);
                }
                while(isInGame)
                {
                    if(startingP==1)
                    {
                        move();
                    }
                    if(startingP==2)
                    {
                        enemyPrint();
                    }
                    inLine.readLine();
                }
            }
            outStream.close();
            inStream.close();
            clientSocket.close();
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong.");
        } 
    }

    public static void move()
    {
        try
        {
            System.out.println("P2 Board");
            System.out.println("");
            p2Board.drawBoard();
            System.out.println("");
            System.out.println("Attack Coordinates:");
            System.out.println("row:");
            row = Integer.parseInt(inLine.readLine());
            System.out.println("column:");
            column = Integer.parseInt(inLine.readLine());
            outStream.println("move "+p2Name+(Integer.toString(row)+Integer.toString(column)));
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong.");
        }   
    }
    public static void enemyPrint()
    {
        System.out.println("P1 Board:");
        System.out.println("");
        p1Board.drawBoard();
        System.out.println("Wait!");
    }

    public static void busywait(int milli)
    {
        try
        {
            Thread.sleep(milli);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
    public void run(){} // to be implemented
}
