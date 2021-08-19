package Piece;

import java.util.ArrayList;

import Game.Move;

public class Pawn extends Piece {

	
	public Pawn(char c){
		super(1,c,"Pawn");
		type = 'p';

	}
	
	@Override
	public ArrayList<Move> getPossibleMoves(char [][] map, int start_x, int start_y) {
		ArrayList<Move> ret = new ArrayList<Move>();
		assert(start_x >= 0 && start_y >= 0);
		
		int forward = color == 'w' ? -1 : 1;

		if(map[start_x][start_y+forward] == 'o'){
			ret.add(new Move(start_x,start_y, start_x,start_y+forward));
		}
		if(start_x != 7 && map[start_x+1][start_y+forward] == 'e'){
			ret.add(new Move(start_x,start_y, start_x+1,start_y+forward));
		}
		if(start_x != 0 && map[start_x-1][start_y+forward] == 'e'){
			ret.add(new Move(start_x,start_y, start_x-1,start_y+forward));
		}
		// Double jump
		if(start_y+2*forward >= 0 && start_y+2*forward < 8 && 
				((color == 'w' && start_y == 6) || (color == 'b' && start_y == 1)) &&
				map[start_x][start_y+forward] == 'o' && map[start_x][start_y+2*forward] == 'o'){
			ret.add(new Move(start_x,start_y, start_x,start_y+2*forward));
		}
		
		return ret;
	}
	@Override
	public String toString() {
		return color=='w'?"wP":"bP";
	}
	
	
}
