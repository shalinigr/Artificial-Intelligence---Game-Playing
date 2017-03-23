# Artificial-Intelligence---Game-Playing
Java code to implement minimax and alpha-beta pruning alogrithms and display move based on the given board.</br>
A player can make the following types of moves: </br>
* Stake - The move can take any open space on the board. This will create a new piece on the board. This move can
be made as many times as one wants to during the game, but only once per turn. However, a Stake cannot
conquer any pieces. It simply allows one to arbitrarily place a piece anywhere unoccupied on the board. Once
 a Stake is done, player's turn is complete. </br>
 * Raid - From any space a player occupies on the board, player can take the one next to it (up, down, left, right, but not
diagonally) if it is unoccupied. The space player originally held is still occupied. Thus, player gets to create a new
piece in the raided square. Any enemy touching the square player has taken is conquered and that square is
turned to the player's side (player turn its piece to player's side). A Raid can be done even if it will not conquer another
piece. Once the player has made this move, the turn is over.
