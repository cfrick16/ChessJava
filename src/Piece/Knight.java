package Piece;

import java.util.ArrayList;

import Game.Move;

public class Knight extends Piece {

	
	public Knight(char c){
		super(3,c,"Knight");
		type = 'n';
	}

	@Override
	public ArrayList<Move> getPossibleMoves(char [][] map, int start_x, int start_y) {
		ArrayList<Move> ret = new ArrayList<Move>();
		assert(start_x >= 0 && start_y >= 0);
		
		for(int move_x : new int[]{-1,1}){
			for(int move_y : new int[]{-2,2}){
				int cur_x = start_x + move_x;
				int cur_y = start_y + move_y;
				if(cur_x >= 0 && cur_y >= 0 && cur_x < 8 && cur_y < 8){
					char square = map[cur_x][cur_y];
					switch(square){
						case 'o':
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
		for(int move_x : new int[]{-2,2}){
			for(int move_y : new int[]{-1,1}){
				int cur_x = start_x + move_x;
				int cur_y = start_y + move_y;
				if(cur_x >= 0 && cur_y >= 0 && cur_x < 8 && cur_y < 8){
					char square = map[cur_x][cur_y];
					switch(square){
						case 'o':
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
		return ret;
	}
	
	@Override
	public String toString() {
		return color=='w'?"wN":"bN";
	}
	
	
}
