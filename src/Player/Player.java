package Player;

import java.util.ArrayList;

import Board.Board;
import Piece.Piece;
import Game.Move;

import Game.Game;

public abstract class Player {
	
	protected char color;
	private int[] enPassantThreat;
	protected Player opp;
	public Player(char c){
		color = c;
	}
	
	public void addOpp(Player p){
		opp = p;
	}
	
	public abstract Move select_move(Board b);
	
	public void receivedMove(Move move){
	
	}
	
	public boolean canCaptureKing(Board b){
		int [] kingSquare = b.getKingSquare(opp.getColor());
		ArrayList<Move> moves = getAllMoves(b);
		for(Move m : moves){
			if(m.endx == kingSquare[0] && m.endy == kingSquare[1])
				return true;
		}
		
		return false;
	}
	
	public void setEnPassantThreat(Move m, Board b){
		if(b.getSquare(m.startx, m.starty).getPieceType() == 'p' && Math.abs(m.endy - m.starty) == 2){
			enPassantThreat = m.getEndCoord();
		} else{
			enPassantThreat = null;
		}
	}
	public int[] getEnPassantThreat(){
		return enPassantThreat;
	}
	
	public ArrayList<Move> getLegalMoves(int x, int y, Board b){
		ArrayList<Move> sMoves = getMoves(x,y,b, true);
		ArrayList<Move> illegalMoves = new ArrayList<Move>();
		for(Move m : sMoves){
			if(opp.canCaptureKing(b.mockMove(m)))
				illegalMoves.add(m);
		}
		sMoves.removeAll(illegalMoves);
		return sMoves;
	}
	
	// Does not include Castles
	public ArrayList<Move> getMoves(int x, int y, Board b, boolean castles){
		Piece p = b.getSquare(x, y);
		ArrayList<Move> sMoves = p.getPossibleMoves(
				b.getMap(x,y), x, y);
				if(castles && p.getPieceType() == 'k')
					sMoves.addAll(getCastles(b));
				if(p.getPieceType() == 'p')
					sMoves.addAll(getEnPassant(x,y,b));
		return sMoves;
	}
	
	public ArrayList<Move> getAllLegalMoves(Board b){
		ArrayList<Move> pMoves = new ArrayList<Move>();
		for(int x = 0; x < 8; ++x){
			for(int y = 0; y < 8; ++y){
				Piece p = b.getSquare(x,y);
				if(p != null && p.getColor() == color){
					pMoves.addAll(getLegalMoves(x, y, b));
				}
			}
		}
		return pMoves;
		
	}
	
	// Does not include Castles
	public ArrayList<Move> getAllMoves(Board b){
		ArrayList<Move> pMoves = new ArrayList<Move>();
		for(int x = 0; x < 8; ++x){
			for(int y = 0; y < 8; ++y){
				Piece p = b.getSquare(x,y);
				if(p != null && p.getColor() == color){
					pMoves.addAll(getMoves(x, y, b, false));
				}
			}
		}
		// Avoid kingSafe loop with castles, Shouldnt need castles becuase you cant capture king with castles
		//		pMoves.addAll(getCastles(b,o));
		return pMoves;
		
	}
	
	private ArrayList<Move> getCastles(Board b){
		ArrayList<Move> ret = new ArrayList<Move>();
		int start = 7;
		if(color == 'b'){
			start = 0;
		}
		if(opp.canCaptureKing(b))
			return ret;
		
		boolean kingside = false;
		if(isMyPiece(b.getSquare(3,start), 'k') && isMyPiece(b.getSquare(0,start),'r')
				&& b.getSquare(2,start) == null && b.getSquare(1, start) == null) {
			kingside = true;
			for(int i = 0; i < 3; ++i){
				Board mock_b = b.mockMove(new Move(3,start,i,start));
				if (opp.canCaptureKing(mock_b))
					kingside = false;
			}

		}
		boolean queenside = false;
		if(isMyPiece(b.getSquare(3,start), 'k') && isMyPiece(b.getSquare(7,start),'r')
				&& b.getSquare(4,start) == null && b.getSquare(5,start) == null && b.getSquare(6, start) == null) {
			queenside = true;
			for(int i = 4; i < 6; ++i){
				Board mock_b = b.mockMove(new Move(3,start,i,start));
				if (opp.canCaptureKing(mock_b))
					queenside = false;
			}
			if(opp.canCaptureKing(b))
				queenside = false;
		}
		if(kingside){
			ret.add(new Move(3,start, 1, start, 'c'));
		} 
		if(queenside){
			ret.add(new Move(3,start,5,start, 'c'));
		}
		
		return ret;
	}
	
	private ArrayList<Move> getEnPassant(int x, int y, Board b){
		ArrayList<Move> ret = new ArrayList<Move>();
		int [] opponentPawnSquare = opp.getEnPassantThreat();
		if(opponentPawnSquare == null)
			return ret;
		
		if(Math.abs(opponentPawnSquare[0] - x) == 1 && opponentPawnSquare[1] == y){
			int myDirection = opponentPawnSquare[1] == 3 ? -1 : 1;
			ret.add(new Move(x,y,opponentPawnSquare[0],opponentPawnSquare[1] + myDirection, 'e'));
		}

		return ret;
	}
	
	private boolean isMyPiece(Piece p, char type){
		if(p == null)
			return false;
		return p.getColor() == color && p.getPieceType() == type;
	}
	
	public char getColor(){
		return color;
	}
}
