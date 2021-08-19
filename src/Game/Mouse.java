package Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	private int size;
	private Game g;

	public Mouse(int s, Game ga) {
		size = s;
		g = ga;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int length = size / 8;
		int x = (int) (e.getX() / length);
		int y = (int) ((e.getY() - (.5 * length)) / length);

		if (x > 7 || y > 7)
			return;

		g.handleClick(x, y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
