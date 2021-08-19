package Board;
import java.io.File;
import java.util.*;
import Piece.*;
import Game.Move;

public class Board {
	private Piece[][] squares;
	private int savedValue;
	
	public Board(){
		savedValue = 0;
		squares = new Piece[8][8];
		for(int i:new int[]{0,7}){
			char color = i == 0 ? 'b' : 'w'; 
			squares[3][i] = new King(color);
			squares[4][i] = new Queen(color);
			squares[5][i] = new Bishop(color);
			squares[6][i] = new Knight(color);
			squares[7][i] = new Rook(color);
			squares[2][i] = new Bishop(color);
			squares[1][i] = new Knight(color);
			squares[0][i] = new Rook(color);
		}
		for(int i = 0; i < 8; ++i){
			squares[i][6] = new Pawn('w');
			squares[i][1] = new Pawn('b');
		}

	}
	
	public Board(Board b){
		savedValue = b.savedValue;

		squares = new Piece[8][8];
		for(int i = 0; i < 8; ++i){
			for(int j = 0; j < 8; ++j){
				squares[i][j] = b.squares[i][j];
			}
		}
	}

	protected File[][] getImageFiles(){
		File [][] ret = new File[8][8];
		for(int i = 0; i < squares.length; ++i){
			for(int j = 0; j <squares[i].length; ++j){
				if(squares[i][j] != null)
					ret[i][j] = squares[i][j].getImg();
			}
		}
		return ret;
	}

	
	public char[][] getMap(int x, int y){
		char [][] map = new char[8][8];
		char color = squares[x][y].getColor();
		for(int i = 0; i < 8; ++i){
			for(int j = 0; j < 8; ++j){
				if(squares[i][j] == null)
					map[i][j] = 'o';
				else if(squares[i][j].getColor() == color)
					map[i][j] = 'f';
				else
					map[i][j] = 'e';
			}
		}
		map[x][y] = 's';
		return map;
	}
	
	public Piece getSquare(int x, int y){
		return squares[x][y];
	}
	
	public Piece getSquare(int[] n){
		return getSquare(n[0],n[1]);
	}
	
	// Returns True if a legal move was made	
	public void makeMove(Move move){
		
		Piece startPiece = getSquare(move.startx, move.starty);

		if(startPiece.getClass() == Pawn.class && (move.endy == 7 || move.endy == 0))
			startPiece = new Queen(startPiece.getColor());
		squares[move.endx][move.endy] = startPiece;
		squares[move.startx][move.starty] = null;
		
		if(move.special == 'c'){
			makeMove(move.AdditionalMove);
			savedValue += startPiece.getColor() == 'w' ? 35 : -35;
		} else if (move.special == 'e'){
			squares[move.AdditionalMove.endx][move.AdditionalMove.endy] = null;
		}
	}
	
	public int [] getKingSquare(char color){
		for(int x = 0; x < 8; ++x){
			for(int y = 0; y < 8; ++y){
				Piece p = squares[x][y];
				if(p != null && p.getClass() == King.class && p.getColor() == color)
					return new int[]{x,y}; 
			}
		}
		int p = 1/0;
		return null;
	}
	
	
	
	
	
	private int evaluatePawn(int x, int y){
		int ret = 100;
		Piece p = getSquare(x,y);
		char otherColor = p.getColor() == 'w' ? 'b':'w';
		boolean passed = true;
		if(p.getColor() == 'b'){
			for(int new_y = y+1; new_y < 8; ++new_y){
			
				// Check for stacked pawns
				if(getSquare(x,new_y) != null && getSquare(x, new_y).equals(p)){
					ret -= 2;
				}
				
				if(passed){
					if(x > 0 && getSquare(x-1,new_y) != null && getSquare(x-1,new_y).isPiece('p', otherColor)){
						passed = false;
					} 
					if(x < 7 && getSquare(x+1,new_y) != null && getSquare(x+1,new_y).isPiece('p', otherColor)){
						passed = false;
					}
				}
			}
		} else {
			for(int new_y = y-1; new_y >= 0; --new_y){
				// Check for stacked pawns
				if(getSquare(x,new_y) != null && getSquare(x, new_y).equals(p)){
					ret -= 2;
				}
				
				if(passed){
					if(x > 0 && getSquare(x-1,new_y) != null && getSquare(x-1,new_y).isPiece('p', otherColor)){
						passed = false;
					} 
					if(x < 7 && getSquare(x+1,new_y) != null && getSquare(x+1,new_y).isPiece('p', otherColor)){
						passed = false;
					}
				}
		
			}
		}
	
		// Check if its supported
		int back = -1;
		if(p.getColor() == 'w')
			back = 1;
		if((x > 0 && p.equals(getSquare(x-1,y+back))) || (x < 7 && p.equals(getSquare(x+1,y+back)))){
			ret+=2;
		}
		
		if(passed)
			ret += 16;
		
		int distFromStart = y-1;
		if(p.getColor() == 'w')
			distFromStart = 6-y;
		
		ret += distFromStart/2;
		
		return ret ;
	}

	private int evaluateKnight(int x, int y){
		int ret = 300;
		int num_squares = 8;
		if(y == 0 || y == 7)
			num_squares -= 4;
		else if(y == 1 || y == 7)
			num_squares -= 2;
		
		if(x == 0 || x == 7){
			num_squares /= 2;
		} else if(x == 1 || x == 6){
			num_squares *= 2;
			num_squares /= 3;
		}
		
		return ret + num_squares;
	}
	
	private int evaluateBishop(int x, int y){
		int ret = 300;
		Piece p = getSquare(x,y);
		int home = p.getColor() == 'b' ? 0 : 7;
		if(y != home)
			ret += 2;
		ret += p.getPossibleMoves(getMap(x,y),x , y).size();
		return ret;
	}
	
	private int evaluateRook(int x, int y){
		int ret = 500;
		// Open File check
		boolean isClear = true;
		int curr_y = y;
		int clearSpaces = -1;
		if(y < 4){
			while(isClear && curr_y < 7){
				++curr_y;
				isClear = getSquare(x,curr_y) == null;
				clearSpaces += 1;
			}
		} else{
			while(isClear && curr_y > 0){
				--curr_y;
				isClear = getSquare(x,curr_y) == null;
				clearSpaces += 1;
			}
		}
		if(isClear)
			++clearSpaces;
		
		if(clearSpaces > 2)
			ret += clearSpaces-2;
		return ret;
	}
	
	private int evaluateQueen(int x, int y){
		int ret = 900;
		//ret += getSquare(x,y).getPossibleMoves(getMap(x,y),x,y).size();

		return ret;
	}
	
	private int evaluateKing(int x, int y){
		int ret = 10000000;
		
		ret += savedValue;
		return ret;
	}
	
	
	public int evaluatePiece(int x, int y){
		int ret = 0;
		Piece p = getSquare(x,y);
		if(p != null){
			int reward_direction = p.getColor() == 'w' ? 1 : -1;
			switch(p.getPieceType()){
				case 'p':
					ret += evaluatePawn(x, y) * reward_direction;
					break;
				case 'k':
					ret += evaluateKing(x,y) * reward_direction;
					break;
				case 'q':
					ret += evaluateQueen(x,y) * reward_direction;
					break;
				case 'r':
					ret += evaluateRook(x,y) * reward_direction;
					break;
				case 'b':
					ret += evaluateBishop(x,y) * reward_direction;
					break;
				case 'n':
					ret += evaluateKnight(x,y) * reward_direction;
					break;
			}
		}
		return ret;
	}
	
	
	public int evaluateMaterialOnly(){
		int ret = 0;
		for(int x = 0; x < 8; ++x){
			for(int y = 0; y < 8; ++y){
				Piece p = getSquare(x,y);
				if(p!= null){
					ret += (p.getColor() == 'w' ? 1 : -1) * p.getValue();
				}
			}
		}
		return ret *100;
	}
	public int evaluateNoMaterial(){
		return evaluate() - evaluateMaterialOnly();
	}
	// Positive means white is winning, negative means black is winning
	public int evaluate(){
		int ret = 0;
		for(int x = 0; x < 8; ++x){
			for(int y = 0; y < 8; ++y){
				ret += evaluatePiece(x,y);
			}
		}
		//		ret += evalMaterial();
		//		ret *= 1000;
		//		// ret += evalPawns();
		//		ret -= evalDevelopment(0, 'b');
		//		ret += evalDevelopment(7, 'w');
		//		
		//		ret += evalCentralPieces();
		return ret;	
	}
	
	public String toString(){
		String ret = "";
		for(Piece[] r:squares){
			for(Piece p:r){
				if(p == null)
					ret += " Em ";
				else
					ret += " " + p.toString()+ " ";
			}
			ret += "\n";
		}
		return ret;
	}
	
	public Board mockMove(Move m){
		Board ret = new Board(this);
		ret.makeMove(m);
		return ret;
	}
	


}
