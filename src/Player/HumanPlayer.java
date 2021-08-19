package Player;

import java.util.ArrayList;

import Board.Board;
import Game.Move;

public class HumanPlayer extends Player{

	public HumanPlayer(char c) {
		super(c);
	}

	private volatile boolean waitingForMove = true;
	private Move move;
	
	@Override
	public Move select_move(Board b) {
		// Do nothing until player makes a decision
		while(waitingForMove){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		waitingForMove = true;
		assert(move != null);
		return move;
	}
	
	@Override
	public void receivedMove(Move m){
		move = m;
		waitingForMove = false;
	}


	
}
