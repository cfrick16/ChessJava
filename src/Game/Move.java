package Game;

public class Move {
	public int startx;
	public int starty;
	public int endx;
	public int endy;
	public char special = 'n';
	// Holds rookMove in event of a castle
	public Move AdditionalMove;
	
	public Move(int[] start, int[] end){
		this(start[0], start[1], end[0], end[1]);
	}
	
	public Move(int s1, int s2, int[] end){
		this(s1, s2, end[0], end[1]);
	}
	
	// Constructor for Castles and enPassant
	public Move(int s1, int s2, int e1, int e2, char specialChar){
		this(s1,s2,e1,e2);
		special = specialChar;
		if(specialChar == 'c'){
			if(e1 == 1){
				AdditionalMove = new Move(0, s2, 2, e2);
			}
			else if(e1 == 5){
				AdditionalMove = new Move(7, s2,4,e2);
			}
		} else if(specialChar == 'e'){
			AdditionalMove = new Move(s1,s2,e1,s2);

		}
	}
	

	public Move(int []start, int e1, int e2){
		this(start[0], start[1], e1, e2);
	}
	
	public Move(int s1, int s2, int e1, int e2){
		startx = s1;
		starty = s2;
		endx = e1;
		endy = e2;
	}
	
	public int [] getStartCoord(){
		return new int[]{startx, starty};
	}
	
	public int [] getEndCoord(){
		return new int[]{endx, endy};
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || o.getClass() != Move.class)
			return false;
		Move m = (Move) o;
		return startx == m.startx && starty == m.starty && endy == m.endy && endx == m.endx;
		
	}
	
	@Override
	public String toString(){
		if(AdditionalMove != null)
			return "("+startx+","+starty+")  to  (" + endx + "," + endy + ")" +" ALSO " + AdditionalMove.toString();
		return "("+startx+","+starty+")  to  (" + endx + "," + endy + ")";
	}
}
