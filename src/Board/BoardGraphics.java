package Board;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import Game.Mouse;

public class BoardGraphics extends JFrame{

	private static final long serialVersionUID = 1L;
	private int size;
	private JPanel[][] panels;
	
	
	public BoardGraphics(int s, File[][] imgs, Mouse m){
		size = s;
		panels = new JPanel[8][8];
		initializeUI(imgs);
		this.addMouseListener(m);
	}
	
	public void initializeUI(File[][] imgs) {
        setSize(size, size);
        setVisible(false);
		// Make everything  else white
		setBackground(Color.WHITE);
		for(int i = 0; i < 8; ++i) {
			for(int j = 0; j < 8; ++j) {
		        int length = size/8;
		        if(panels[i][j] != null)
		        	this.remove(panels[i][j]);
				panels[i][j] = new JPanel();
				panels[i][j].setBounds(new Rectangle(i*length, j*length, length, length));
				updateImage(i,j, imgs[i][j]);
				add(panels[i][j]);
				panels[i][j].updateUI();			}
		}
		resetAllGridColors();
		JPanel p = new JPanel();
		p.setBackground(Color.white);
		add(p);
		
		// Create mouse listener for the entire JFrame
        setVisible(true);

	}
	
	public void updateImage(int [] nums, File img){
		updateImage(nums[0], nums[1], img);
	}
	public void updateImage(int i, int j, File img){
        int length = size/8;
        // Remove any existing panelling
        if(panels[i][j].getComponentCount() > 0) { 
			panels[i][j].remove(panels[i][j].getComponent(0));
		}
        
		if(img != null){
			Image myPicture;
			try {
				myPicture = ImageIO.read(img);
				myPicture = myPicture.getScaledInstance(length-1, length-1, Image.SCALE_DEFAULT);
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				picLabel.setVerticalAlignment(JLabel.TOP);
				panels[i][j].add(picLabel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		panels[i][j].updateUI();
	}
	
	public void highlight(int x, int y){
		updateColor(x,y,Color.YELLOW);
	}
	public void highlightPMove(int x, int y){
		updateColor(x,y,Color.ORANGE);
	}
	public void check(int x, int y){
		updateColor(x,y, Color.RED);
	}
	
	private void updateColor(int x, int y, Color c){
		panels[x][y].setBackground(c);
		panels[x][y].updateUI();
	}
	
	public void resetAllGridColors(){
		resetAllGridColors(false);
	}
	public void resetAllGridColors(boolean keepCheck){
		for(int i = 0; i < 8; ++i){
			for(int j = 0; j < 8; ++j){
				if(!keepCheck || !panels[i][j].getBackground().equals(Color.RED)){
					if((i + j) % 2 == 1)
						updateColor(i,j,Color.BLUE);
					else
						updateColor(i,j,Color.LIGHT_GRAY);
				}
			}		
		}
	}
	
	public void updateSquareValue(int x, int y, int value, String piece){
        while(panels[x][y].getComponentCount() > 0) { 
			panels[x][y].remove(panels[x][y].getComponent(0));
		}		
        if(value != 0){
        	
	        JLabel jlabel = new JLabel("<html>" +piece + "<br/>V: " + value + "</html>");
			jlabel.setFont(new Font("Verdana",1,15));
			if((x+y)%2 == 1)
				jlabel.setForeground(Color.WHITE);
			panels[x][y].add(jlabel);
        }
		panels[x][y].updateUI();
	}
}
