package Piece;

import java.util.ArrayList;

import Game.Move;

public class King extends Piece {

	
	public King(char c){
		super(100000,c,"King");
		type = 'k';
	}
	
	@Override
	public ArrayList<Move> getPossibleMoves(char [][] map, int start_x, int start_y) {
		ArrayList<Move> ret = new ArrayList<Move>();
		assert(start_x >= 0 && start_y >= 0);
		
		for(int move_x : new int[]{-1,0,1}){
			for(int move_y : new int[]{-1,0,1}){
				// Just skip if you're not moving
				if(move_x != 0 || move_y != 0){
					int cur_x = start_x + move_x;
					int cur_y = start_y + move_y;
					if(cur_x >= 0 && cur_y >= 0 && cur_x < 8 && cur_y < 8){
						char square = map[cur_x][cur_y];
						switch(square){
							case 'o':
								ret.add(new Move(start_x,start_y, cur_x, cur_y));
								break;
							case 'e':
								ret.add(new Move(start_x,start_y, cur_x, cur_y));
								break;
							case 'f':
								break;
							default:
								assert(false);
						}
					}
				}
			}
		}
		return ret;
	}
	@Override
	public String toString() {
		return color=='w'?"wK":"bK";
	}
	
	
}
