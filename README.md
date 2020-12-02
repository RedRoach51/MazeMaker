# MazeMaker
CS Project, Makes Maze

Created using BlueJ IDE


This code allows you to create a drawing or maze on this grid.
When you activate the line walker by pressing SPACE, the program will randomly move to available points and drawing lines until it is no longer able to.
Once this happens, the program will stop moving to points and drawing until activated again through user input.
The program can start lines even from already drawn ones, as long as there is an available point adjacent to it.
However, the user can manually draw lines before and after activating the line walker using WASD keys, even to points that are no longer available.

Controls:
W,S,A,D: Move location, draw line to new location
Up,Down,Left,Right : Move location, no line to new location
Space: Start Maze program
F: Manually stop Maze program
Q: (Debug) Returns coordinates of point and used status.
R: (Debug) Returns if valid move for Maze program exists.
