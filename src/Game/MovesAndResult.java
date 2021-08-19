package Game;

import java.util.ArrayList;

import Board.Board;
import Piece.Piece;

public class MovesAndResult implements Comparable<MovesAndResult> {
	public ArrayList<Move> moves;
	public Board board;
	private int eval;
	private boolean evalCached = false;
	
	public MovesAndResult(Move m, Board b){
		moves = new ArrayList<Move>();
		moves.add(m);
		board = b;
	}
	public MovesAndResult(Board b){
		moves = new ArrayList<Move>();
		board = b;
	}
	
	@SuppressWarnings("unchecked")
	public MovesAndResult(MovesAndResult mar, Move m){
		moves = new ArrayList<Move>();
		moves = (ArrayList<Move>) mar.moves.clone();
		board = mar.board.mockMove(m);
		moves.add(m);
		eval = mar.eval;
	}
	
	public Move getFirstMove(){
		if(moves.size() == 0)
			return null;
		return moves.get(0);
	}

	public Move getLastMove(){
		if(moves.size() == 0)
			return null;
		return moves.get(moves.size()-1);
	}
	
	public void setEvaluate(int e){
		eval = e;
		evalCached = true;
	}
	
	public int evaluate(){
		if(!evalCached){
			eval = board.evaluate();
			evalCached = true;
		}
		return eval;
	}
	
	// Returns positive if this is better for white than O is
	@Override
	public int compareTo(MovesAndResult o){
		return board.evaluate() - o.board.evaluate();
	}
	
	public int compareToTotal(MovesAndResult o){
		return board.evaluate() - o.board.evaluate();
	}
	
	@Override
	public String toString(){
		String ret = "Predicted Evaluation: " + board.evaluate()+ " MOVES: ";
		for(Move m : moves)
			ret+=  m + ",  ";
		return ret;
	}
	

}
