package Piece;

import java.util.ArrayList;

import Game.Move;

public class Rook extends Piece {

	
	public Rook(char c){
		super(5,c,"Rook");
		type = 'r';

	}

	@Override
	public ArrayList<Move> getPossibleMoves(char [][] map, int start_x, int start_y) {
		ArrayList<Move> ret = new ArrayList<Move>();
		assert(start_x >= 0 && start_y >= 0);
		
		for(int move_x : new int[]{-1,0,1}){
			for(int move_y : new int[]{-1,0,1}){
				// Just skip if you're moving diagonally or up  and down
				if((move_x == 0 && move_y != 0) || (move_x != 0 && move_y ==0)){
					int cur_x = start_x + move_x;
					int cur_y = start_y + move_y;
					boolean valid = true;
					while(valid && cur_x >= 0 && cur_y >= 0 && cur_x < 8 && cur_y < 8){
						char square = map[cur_x][cur_y];
						switch(square){
							case 'o':
								ret.add(new Move(start_x,start_y, cur_x, cur_y));
								break;
							case 'e':
								ret.add(new Move(start_x,start_y, cur_x, cur_y));
							case 'f':
								valid = false;
								break;
							default:
								assert(false);
						}
						cur_x += move_x;
						cur_y += move_y;
					}
				}
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return color=='w'?"wR":"bR";
	}
	
	
}
