# Nightmare Hunter

To run the game locally, download Server.jar and Client.jar and run the following two commands in two seperate terminals:
1) `java -jar Server.jar`
2) `java -jar Client.jar`

*Important: The first terminal must display waiting for connection after running command 1 before running command 2.

To run the game over a network, download the code folder and change the hostname variable in line 17 of Screen.java to the IP address of the computer functioning as the Server.  To generate new jar files working with this change, run the following two commands in terminal:
1) `jar cfve Server.jar Server *.class *.jpg *.png`
2) `jar cfve Client.jar Client *.class *.jpg *.png`

Then, use the same commands as above to run the jar files.  Remember to make sure the server displays waiting for connection before running any clients.

Note: One of the primary purposes behind developing this game was to implement a 3d engine from start to finish.  As a result, it does not take advantage of hardware acceleration, running entirely on the CPU.  Therefore, the frame rate and resolution are significantly lower than would be expected of modern games, despite a number of optimizations.
