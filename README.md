# battleship
###### Implementation of the game of Battleship played by 2 players over a network

### Setting up for the first time
Visit http://www.oracle.com/technetwork/java/javase/downloads/index.html on instructions for installing Java SE on your computer. Once it has been installed, add java to your environment variables. Help can be found here: http://stackoverflow.com/questions/1672281/environment-variables-for-java-installation .

Now that java can be accessed through your terminal, you can now run the game.

Run the following on terminal:
```sh
$ javac Server.java
$ java Server.class
```
Once the server is running, run the following on two different terminal windows:
```sh
$ javac Client.java
$ java Client.class
```
### Instructions
- The game is user friendly and prompts the user whenever there is an error. The instructions are provided on screen and are fairly simple. The objective of the game is to destroy your enemy's ships by hitting coordinates. 

- If you are player1, wait for player2 to connect. Once player2 connects to the server, an automatic game notification will be sent to each player and the game will begin. 
- Set up the 2 ships using the coordinates and orientation. Once ready, wait for other player to finish setting up.
- When all players are ready, Player1 gets to go first. Select x and y coordinates to hit on the enemy board. The game will tell you whether you hit or miss.
- At every turn, your board will be displayed to you, as well as all attacks made on enemy board.
- When a player destroys all enemy ships, a notification is sent to both players and all instances (server, client etc.) exit.

### Todos
 
### Stretch Goals
 - After game chat option
 - Dynamic board and ship size with different number of ships
