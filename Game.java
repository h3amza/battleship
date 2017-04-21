/*
* obsolete class, no longer in use
*/
import java.util.Scanner;
import java.util.Arrays;

public class Game
{
  Scanner input = new Scanner(System.in);
  public static char[][] p1Board = new char[5][5]; // foreground, will be displayed to other player
  public static char[][] p2Board = new char[5][5]; // background grid, will hold ships
  boolean isP1 = true;
  boolean p1Connected = false;
  boolean p2Connected = false;
  int p1Turns = 0;
  int p2Turns = 0;

  Game()
  {
    System.out.println("You can add 3 ships, each of size 2 to the board");
    for(int i=0;i<5;i++)
    {
      for(int j=0;j<5;j++)
      {
        p1Board[i][j] = '~';
        p1Board[i][j] = '~';
      }
    }
  }

  public int getTurnCount()
  {
    return ((isP1) ? p1Turns : p2Turns);
  }

  public boolean isEmpty()
  {
    for(int i=0;i<5;i++)
    {
      for(int j=0;j<5;j++)
      {
        if(((isP1) ? p1Board[i][j] : p2Board[i][j]) == 'O')
          return false;
      }
    }
    return true;
  }

  public void printBoard()
  {
    if(isP1)
      {
      System.out.println("  A  B  C  D  E");
      for(int i=0;i<5;i++)
      {
        System.out.print(Integer.toString(i+1));
        for(int j=0;j<5;j++)
        {
          System.out.print(" "+p1Board[i][j] + " ");
        }
        System.out.println("");
      }
    }
    else
    {
      System.out.println("  A  B  C  D  E");
      for(int i=0;i<5;i++)
      {
        System.out.print(Integer.toString(i+1));
        for(int j=0;j<5;j++)
        {
          System.out.print(" "+p2Board[i][j] + " ");
        }
        System.out.println("");
      }
    }
  }

  public void setBoard()
  { 
    if(isP1)
    {
      for(int i = 0;i<3;i++)
      {
        System.out.println("Enter the coordinates for the origin of the ship (eg:A2)"); 
        String ship = input.nextLine();
        if(checkInput(ship))
        {
          int row = getRow(ship);
          int col = getColumn(ship);
          p1Board[row-1][col] = 'O';
          int neighbors = otherCoords(row-1,col);
          ship = input.nextLine();
          if(checkInput(ship))
          {
            row = getRow(ship);
            col = getColumn(ship);
            p1Board[row-1][col] = 'O';
          }
        }
        else
          System.out.println("something went wrong with setBoard()");
      }
    }
    else
    {
      for(int i = 0;i<3;i++)
      {
        System.out.println("Enter the coordinates for the origin of the ship (eg:A2)"); 
        String ship = input.nextLine();
        if(checkInput(ship))
        {
          int row = getRow(ship);
          int col = getColumn(ship);
          p2Board[row-1][col] = 'O';
          int neighbors = otherCoords(row-1,col);
          ship = input.nextLine();
          if(checkInput(ship))
          {
            row = getRow(ship);
            col = getColumn(ship);
            p2Board[row-1][col] = 'O';
          }
        }
        else
          System.out.println("something went wrong with setBoard()");
      }
    }
  }

  public void move()
  {
    System.out.println("Enter the coordinates you want to hit (eg:A2)");
    String ship = input.nextLine();
      if(checkInput(ship))
      {
        int row = getRow(ship);
        int col = getColumn(ship);
        // incomplete function
      }
      else
        System.out.println("something went wrong with move()");
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

  public String getAlpha(int c)
  {
    String[] alpha = {"A","B","C","D","E"};
    return alpha[c];
  }

  public int otherCoords(int row, int col)
  { 
    // check corners, then sides, then remaining
    System.out.println("Here are some suggestions for the other part of the ship.\nPick a coordinate");
    //System.out.println("row:" + row +" col:"+ col);
    if (row == 0 && col == 0) // top left corner
    {
      System.out.println("1) " + getAlpha(col) + Integer.toString(row+1+1));
      System.out.println("2) " + getAlpha(col+1) + Integer.toString(row+1));
      return 2;
    }

    if (row == 4 && col == 4) // bottom right corner
    {
      System.out.println("1)" + getAlpha(col) + Integer.toString(row-1+1));
      System.out.println("2)" + getAlpha(col-1) + Integer.toString(row+1));
      return 2;
    }

    if (row == 0 && col == 4) // bottom left corner
    {
      System.out.println("1)" + getAlpha(col) + Integer.toString(row+1+1));
      System.out.println("2)" + getAlpha(col-1) + Integer.toString(row+1));
      return 2;
    }

    if (row == 4 && col == 0) // bottom right corner
    {
      System.out.println("1)" + getAlpha(col) + Integer.toString(row-1+1));
      System.out.println("2)" + getAlpha(col+1) + Integer.toString(row+1));
      return 2;
    }

    if(row == 4) // bottom row
    {
      System.out.println("1)" + getAlpha(col+1) + Integer.toString(row+1));
      System.out.println("2)" + getAlpha(col-1) + Integer.toString(row+1));
      System.out.println("3)" + getAlpha(col) + Integer.toString(row-1+1));
      return 3;
    }

    if(row == 0) // top row
    {
      System.out.println("1)" + getAlpha(col+1) + Integer.toString(row+1));
      System.out.println("2)" + getAlpha(col-1) + Integer.toString(row+1));
      System.out.println("3)" + getAlpha(col) + Integer.toString(row+1+1));
      return 3;
    }

    if(col == 4) // bottom row
    {
      System.out.println("1)" + getAlpha(col) + Integer.toString(row+1+1));
      System.out.println("2)" + getAlpha(col) + Integer.toString(row-1+1));
      System.out.println("3)" + getAlpha(col-1) + Integer.toString(row+1));
      return 3;
    }

    if(col == 0) // top row
    {
      System.out.println("1)" + getAlpha(col) + Integer.toString(row+1+1));
      System.out.println("2)" + getAlpha(col) + Integer.toString(row-1+1));
      System.out.println("3)" + getAlpha(col+1) + Integer.toString(row+1));
      return 3;
    }
    
    System.out.println("1)" + getAlpha(col) + Integer.toString(row+1+1));
    System.out.println("2)" + getAlpha(col) + Integer.toString(row-1+1));
    System.out.println("3)" + getAlpha(col+1) + Integer.toString(row+1));
    System.out.println("4)" + getAlpha(col-1) + Integer.toString(row+1));
    return 4;
  }

}
