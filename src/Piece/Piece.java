package Piece;
import java.io.File;
import java.util.ArrayList;
import Game.Move;

public abstract class Piece{
	protected int value;
	protected File img;
	protected char color;
	protected char type;
	
	public Piece(int v, char c, String piece_name){
		value = v;
		color = c;
		if(color == 'w'){
			img = new File("imgs/White"+piece_name+".png");
		} else{
			img = new File("imgs/Black"+piece_name+".png");
		}
	}

	public char getPieceType(){
		return type;
	}
	public File getImg(){
		return img;
	}
	public int getValue(){
		return value;
	}
	public char getColor(){
		return color;
	}
	
	public boolean isPiece(char t, char c){
		return t == type && c == color;
	}
	
	abstract public ArrayList<Move> getPossibleMoves(char [][] map, int start_x, int start_y);
	
	abstract public String toString();
	
	@Override
	public boolean equals(Object o){

		if(o == null || o.getClass().isInstance(Piece.class))
			return false;
		
	
		Piece p = (Piece) o;
		return p.getColor() == color && p.getPieceType() == type;
	}
}


