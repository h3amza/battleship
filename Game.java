import java.util.Scanner;
import java.util.Arrays;

public class Game
{
  Scanner input = new Scanner(System.in);
  public static char[][] boardFG = new char[5][5]; 
  public static char[][] boardBG= new char[5][5]; 

  Game()
  {
    System.out.println("You can add 3 ships, each of size 2 to the board");
    for(int i=0;i<5;i++)
    {
      for(int j=0;j<5;j++)
      {
        boardFG[i][j] = '~';
        boardBG[i][j] = '~';
      }
    }
  }
  public void printBoard()
  {
    System.out.println("  A  B  C  D  E");
    for(int i=0;i<5;i++)
    {
      System.out.print(Integer.toString(i+1));
      for(int j=0;j<5;j++)
      {
        System.out.print(" "+boardBG[i][j] + " ");
      }
      System.out.println("");
    }
  }
  public void setBoard()
  {
    for(int i = 0;i<3;i++)
    {
      System.out.println("Enter the coordinates for the origin of the ship (eg:A2)"); 
      String ship = input.nextLine();
      if(checkInput(ship))
      {
        int row = getRow(ship);
        int col = getColumn(ship);
        boardBG[row-1][col] = 'O';
      }
      else
        System.out.println("here");
    }
  }

  public int getColumn(String coords)
  {
    String[] temp = coords.split("");
    int index=0;
    switch(temp[0])
    {
      case "A":
        index = 0;
        break;
      case "B":
        index = 1;
        break;
      case "C":
        index = 2;
        break;
      case "D":
        index = 3;
        break;
      case "E":
        index = 4;
        break;
    }
    return index;
  }
  public int getRow(String coords)
  {
    String[] temp = coords.split("");
    return Integer.parseInt(temp[1]);
  }
  public boolean checkInput(String coords)
  {
    String[] alpha = {"A","B","C","D","E"};
    String[] numeral = {"1","2","3","4","5"};
    String[] temp = coords.split("");
    if(!findItem(alpha,temp[0]))
      return false;
    if(!findItem(numeral,temp[1]))
      return false;
    return true;
  }


  public boolean findItem(String[] arr,String val)
  {
    for(int i = 0;i<arr.length;i++)
    {
      if(arr[i].equals(val))
        return true;
    }
    return false;
  }

  public int otherCoords(int row, int col) // check corners, then sides, then remaining
  {
    if (row == 0 && col == 0)
    {
      System.out.println("1)" + Integer.toString(row+1) + "," + Integer.toString(col));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col+1));
      return 2;
    }

    if (row == 4 && col == 4)
    {
      System.out.println("1)" + Integer.toString(row-1) + "," + Integer.toString(col));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col-1));
      return 2;
    }

    if (row == 0 && col == 4)
    {
      System.out.println("1)" + Integer.toString(row+1) + "," + Integer.toString(col));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col-1));
      return 2;
    }

    if (row == 4 && col == 0)
    {
      System.out.println("1)" + Integer.toString(row-1) + "," + Integer.toString(col));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col+1));
      return 2;
    }

    if(row == 4)
    {
      System.out.println("1)" + Integer.toString(row) + "," + Integer.toString(col+1));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col-1));
      System.out.println("3)" + Integer.toString(row-1) + "," + Integer.toString(col));
      return 3;
    }

    if(row == 0)
    {
      System.out.println("1)" + Integer.toString(row) + "," + Integer.toString(col+1));
      System.out.println("2)" + Integer.toString(row) + "," + Integer.toString(col-1));
      System.out.println("3)" + Integer.toString(row-1) + "," + Integer.toString(col));
      return 3;
    }
    // finish this

    return -1;

  }

}



