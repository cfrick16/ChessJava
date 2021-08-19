package Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Game.Move;
import Game.MovesAndResult;

public class AIPlayer extends Player{
	
	private final int max_opening = 2;
	private String openingName;
	private AIPlayer mockOpp;
	private int opening;
	private int level;
	
	public AIPlayer(char c) {
		this(c,4);
		
	}
	
	public AIPlayer(char c, int l) {
		super(c);
		mockOpp = new AIPlayer(c == 'w' ? 'b' : 'w', this);
		opening = max_opening;
		level = l;
		
	}
	
	public AIPlayer(char c, AIPlayer p) {
		super(c);
		mockOpp = p;
		opp = p;
		opening = 0;
	}

	private Move select_opening_move(Board b){
		if(color == 'b')
			return null;
		if(max_opening - opening == 1){
			return new Move(3,6,3,4);
		} else if(max_opening - opening == 2){
			if(b.getSquare(4,3) != null){
				openingName = "Scandinavian";
				System.out.println(openingName + " opening\n");
				return new Move(3,4,4,3);
			}
			if(b.getSquare(2, 3) != null){
				openingName = "Duras";
				System.out.println(openingName + " opening\n");
				return new Move(3,4,2,3);
			}
			openingName = "Kings Pawn";
			System.out.println(openingName + " opening\n");
			return new Move(6,7,5,5);
		} else if(max_opening - opening == 3){
			if(openingName.equals("Scandinavian") && b.getSquare(4, 3) != null ){
				return new Move(6,7,5,5);
			} else if(openingName.equals("Kings Pawn") && b.getSquare(5, 3) != null && b.getSquare(5,3).getPieceType() == 'b'){
				return new Move(4,7,0,3);
			} else if(openingName.equals("Kings Pawn") && b.getSquare(4,3)!= null){
				return new Move(3,4,4,3);
			} else if(openingName.equals("Kings Pawn")){
				return new Move(1,7,2,5);
			}
			
			
				
		}
		
		return null;
	}
	
	
	@Override
	public Move select_move(Board b) {
		// TODO Auto-generated method stub
		if(opening-- >= 0){
			Move m = select_opening_move(b);
			if(m != null){
				System.out.println("Opening Preset move " + m );
				return m;
			}
			else
				opening = 0;
		}
		int current_eval = b.evaluate();
		
		MovesAndResult mar = new MovesAndResult(b);
		ArrayList<Move> moves = getAllLegalMoves(mar.board);
		List<MovesAndResult> results = new ArrayList<MovesAndResult>();
		for(Move m : moves){
			MovesAndResult candidate = new MovesAndResult(mar, m);
			candidate = mockOpp.getBestMove(candidate, level-1);
			
			if((color == 'w' && candidate.evaluate() - current_eval > 200 ) || (color =='b' && candidate.evaluate() - current_eval < -200)){
				MovesAndResult candidate2 = mockOpp.getBestMove(new MovesAndResult(mar,m), 2);
				candidate2 = mockOpp.getBestMove(candidate2, 2);
				if((color == 'b' && candidate2.evaluate() < candidate.evaluate()) && (color == 'w' && candidate2.evaluate() > candidate.evaluate())){
					candidate = candidate2;
					System.out.println("EMERGENCY SWITCH");
				}
			}
			results.add(candidate);
		}
		
		Collections.sort(results);
		int flipped = -1;
		if(color == 'w'){
			Collections.reverse(results);
			flipped = 1;
		}
		MovesAndResult best = results.get(0);
		ArrayList<MovesAndResult> ties = new ArrayList<MovesAndResult>();
		int beat = best.evaluate();

		for(MovesAndResult m : results){
			if(Math.abs(m.evaluate() - beat) < 10 )
				ties.add(m);
		}
		
		beat = b.mockMove(ties.get(0).getFirstMove()).evaluateNoMaterial() *flipped;
		System.out.println(ties.size() + " Lines tied for material, deciding via positioning");
		Board mock = null;
		for(MovesAndResult t : ties){
			mock = b.mockMove(t.getFirstMove());
			int eval = mock.evaluateNoMaterial();
			System.out.println(flipped*eval + " " + t.getFirstMove());

			if(eval*flipped > beat){
				beat = eval;
				best = t;
			}
		}

		System.out.println(best);
		return best.getFirstMove();
	}
	
	public MovesAndResult getBestMove(MovesAndResult mar, int level){
		// Time to evaluate and send home
		if(level == 0){
			return mar;
		}
		ArrayList<Move> moves = getAllMoves(mar.board);
		MovesAndResult best = null;
		int best_eval = 0;
		int eval = 0;

		for(Move m : moves){
			MovesAndResult candidate = new MovesAndResult(mar, m);
			candidate = mockOpp.getBestMove(candidate, level-1);
			if(candidate == null){
				return null;
			}
			if(candidate != null)
				eval = candidate.evaluate();
			if(best == null || (candidate != null && 
					(color == 'w' && eval > best_eval) || (color == 'b' && eval< best_eval))){
				best = candidate;
				best_eval = eval;
			}
		}
		return best;
	}
	

}
