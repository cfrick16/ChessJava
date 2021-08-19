package Board;

import java.util.ArrayList;

import Game.Mouse;
import Game.Move;
import Piece.Pawn;
import Piece.Piece;
import Piece.Queen;

public class HumanBoard extends Board{

	private int[] selected;
	private BoardGraphics bg;
	public HumanBoard(int i, Mouse mouse) {
		super();
		bg = new BoardGraphics(i,this.getImageFiles(),mouse);
	}
	
	public void select(int x, int y, ArrayList<Move> pMoves){
		deselect(true);
		selected = new int[]{x,y};
		bg.highlight(x, y);
		
		for(Move p : pMoves){
			bg.highlightPMove(p.endx, p.endy);
		}
	}
	
	public void check(int x, int y){
		bg.check(x,y);
	}
	
	public int[] getSelected(){
		return selected;
	}
	
	public void deselect(boolean keepCheck){
		selected = null;
		bg.resetAllGridColors(keepCheck);
	}
	
	
	@Override
	public void makeMove(Move move){
		boolean animation = selected == null;
		boolean capture = getSquare(move.getEndCoord()) != null;
		super.makeMove(move);
		if(animation){
			deselect(false);
			bg.highlight(move.startx, move.starty);
			wait(250);
			if(capture)
				bg.check(move.endx, move.endy);
			else
				bg.highlight(move.endx, move.endy);
			wait(1250);
		}
		bg.updateImage(move.startx, move.starty, null);
		bg.updateImage(move.endx, move.endy, getSquare(move.endx, move.endy).getImg());
		if(move.special == 'c'){
			move = move.AdditionalMove;
			bg.updateImage(move.startx, move.starty, null);
			bg.updateImage(move.endx, move.endy, getSquare(move.endx, move.endy).getImg());
		} else if (move.special == 'e'){
			move = move.AdditionalMove;
			bg.updateImage(move.getEndCoord(), null);
		}
		

		deselect(false);
		// Uncomment to view piece values
//				for(int x = 0; x < 8; ++x)
//					for(int y = 0; y < 8; ++y)
//						if(getSquare(x,y) == null)
//							bg.updateSquareValue(x,y,0,"");
//						else
//							bg.updateSquareValue(x,y,this.evaluatePiece(x, y), getSquare(x,y).toString());
	}
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}
}