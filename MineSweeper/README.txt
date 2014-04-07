								MineSweeper in Android
								----------------------
How to Run the project?
1. Import this project in eclipse.
2. Eclipse should be ready with android setup.
3. Run this project.

How to install?
Install this application either using eclipse/adb by installing apk file.

Game Rules
----------
Minesweeper is a grid of tiles, each of which may or may not cover hidden mines. 
The goal is to click on every tile except those that have mines. 
When a user clicks a tile, one of two things happens. 
If the tile was covering a mine, the mine is revealed and the game ends in failure. 
If the tile was not covering a mine, it instead reveals the number of adjacent (including diagonals) tiles that are 
covering mines – and, if that number was 0, behaves as if the user has clicked on every cell around it. 
When the user is confident that all tiles not containing mines have been clicked, the user presses a Validate button 
that checks the clicked tiles: if the user is correct, the game ends in victory, if not, the game ends in failure.

Design Details
--------------
1. supports 8x8 grid with 10 randomly placed mines in it.
2. "New Game" Button can be used to start a new game.
3. If every tile is marked and "Validate" button is pressed, it gives an alert of failure or victory.
4. "Cheat Open" Button can be used to see all the mines without spoiling the current game.
5. "Cheat Close" Button is used to hide all the mines.

Implementation Details and Future enhancements
----------------------------------------------
1. With number of grids and number of mines, if given as user input, Level of difficulty and size of board can be implemented.
2. The grid of mines is implemented using buttons and can be changed to images or grids for further extensibility.
3. Mines are represented as -1 and blanks with zero and if with integers depending on the neighbouring mines.

Specific Detail
---------------
While developing this app, I used Motorola Xoom device and it worked perfectly.