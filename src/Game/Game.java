package Game;

import java.util.ArrayList;

import Board.Board;
import Board.HumanBoard;
import Piece.Piece;
import Player.AIPlayer;
import Player.HumanPlayer;
import Player.Player;

public class Game {
	private HumanBoard b;
	private Player whitePlayer;
	private Player blackPlayer;
	private Player activePlayer;
	private int size;

	public Game(){
		this(600);
	}
	
	public Game(int s){
		size = s;
		
		b = new HumanBoard(600, new Mouse(size, this));
		//whitePlayer = new HumanPlayer('w');
		whitePlayer = new AIPlayer('w');
		blackPlayer = new HumanPlayer('b');
		blackPlayer.addOpp(whitePlayer);
		whitePlayer.addOpp(blackPlayer);
		activePlayer = whitePlayer;

	}
	
	public void playGame(){
		int turn = 0; 
		while(true){
			Move move = activePlayer.select_move(b);
			activePlayer.setEnPassantThreat(move, b);
			b.makeMove(move);
			System.out.println("Move made: " + move + "  Current Board evaluation: " + b.evaluate() + " \n");
			highlightCheck();
			activePlayer = ++turn % 2 == 0 ? whitePlayer : blackPlayer;
			
		}
	}
	
	private void highlightCheck(){
		if(activePlayer.canCaptureKing(b)){
			int [] kingSquare = b.getKingSquare(getOtherPlayer(activePlayer).getColor());
			b.check(kingSquare[0], kingSquare[1]);
		}
	}
	

	
	public Player getOtherPlayer(Player p){
		if(p.getColor() == 'w')
			return blackPlayer;
		return whitePlayer;
	}
	
	public static Move getMove(Move m, ArrayList<Move> moves){
		for(Move move : moves){
			if(m.equals(move))
				return move;
		
		}
		return null;
	}
	// When a click is made, decide whether the human player is selecting, moving or doing nothing
	public void handleClick(int x, int y){
		if(activePlayer.getClass() == AIPlayer.class){
			return;
		}
		Piece p = b.getSquare(x, y);
		int [] selected = b.getSelected();

		if(selected == null){
			if(p == null || p.getColor() != activePlayer.getColor()){
				// If nothing is selected and a square not containing a friendly piece, do nothing
				return;
			} else {
				b.select(x,y, activePlayer.getLegalMoves(x, y, b));
				return;
			}
		} else{
			if(p != null && p.getColor() == activePlayer.getColor()){
				// There was a selected piece but we clicked on a teammate.
				b.select(x, y, activePlayer.getLegalMoves(x, y, b));
				return;
			} else { 
				Move moveCandidate = new Move(selected,x,y);
				ArrayList<Move> sMoves = activePlayer.getLegalMoves(selected[0],selected[1], b);
				// Check to make sure move is legal
				moveCandidate = getMove(moveCandidate, sMoves);
				if(moveCandidate != null){
					activePlayer.receivedMove(moveCandidate);					
				}

			}
		}
	}
}
