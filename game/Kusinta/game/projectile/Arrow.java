package projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import automaton.Automaton;
import automaton.Category;
import automaton.Direction;
import environnement.Element;
import game.Coord;
import player.Player;

public class Arrow extends Projectile {

	public static final int SIZE = (int) (1.5 * Element.SIZE);
	static final int SPEED = 9;
	int moving;
	int DIMENSION;

	int m_height;
	int m_width;

	public Arrow(Automaton arrowAutomaton, int x, int y, double angle, Player player, Direction direction)
			throws Exception {
		super(arrowAutomaton, x, y, angle, player, player.getModel(), direction);

		loadImage("resources/Player/spriteArrow.png");

		DIMENSION = SIZE / (image.getHeight(null));

		float ratio = (float) (image.getWidth(null) * 4) / (float) (5 * image.getHeight(null));

		m_height = DIMENSION * image.getHeight(null);
		m_width = (int) (ratio * image.getWidth(null));

		if (m_direction.toString().equals("E")) {
			hitBox = new Coord((int) (m_coord.X() + (m_width / 2) * Math.cos(m_angle)),
					(int) (m_coord.Y() - (m_width / 2) * Math.sin(m_angle)));
		} else {
			hitBox = new Coord((int) (m_coord.X() - (m_width / 2) * Math.cos(m_angle)),
					(int) (m_coord.Y() - (m_width / 2) * Math.sin(m_angle)));
		}
		
		m_dead_time = 0;

		moving = 0;
	}

	@Override
	public boolean explode() {
		if (m_dead_time == 0) {
			m_dead_time = System.currentTimeMillis();
		}
		return true;
	}

	@Override
	public boolean move(Direction dir) {
		int tmpX = m_coord.X();
		int tmpY = m_coord.Y();

		if (moving == 0) {
			if (m_direction.toString().equals("E")) {
				m_coord.setX((int) (m_coord.X() + SPEED * Math.cos(m_angle)));
				m_coord.setY((int) (m_coord.Y() - SPEED * Math.sin(m_angle)));
			} else {
				m_coord.setX((int) (m_coord.X() - SPEED * Math.cos(m_angle)));
				m_coord.setY((int) (m_coord.Y() - SPEED * Math.sin(m_angle)));
			}
		}
		moving = (moving + 1) % 3;

		hitBox.translate(m_coord.X() - tmpX, m_coord.Y() - tmpY);

		return true;
	}

	public void paint(Graphics g) {
		long now = System.currentTimeMillis();
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));

		if (image != null) {
			int w = DIMENSION * m_width;
			int h = m_height;
			Graphics2D g2D = (Graphics2D) g;
			if (m_direction.toString().equals("E")) {
				g2D.rotate(-m_angle, m_coord.X(), m_coord.Y());
				g2D.drawImage(image, m_coord.X() - (w / 2), m_coord.Y() - h / 2, w, h, null);
				g2D.rotate(m_angle, m_coord.X(), m_coord.Y());
			} else {
				g2D.rotate(m_angle, m_coord.X(), m_coord.Y());
				g2D.drawImage(image, m_coord.X() + (w / 2), m_coord.Y() - h / 2, -w, h, null);
				g2D.rotate(-m_angle, m_coord.X(), m_coord.Y());
			}
		}
//		g.setColor(Color.blue);
//		Rectangle H = new Rectangle(hitBox.X() - 5, hitBox.Y() - 5, 10, 10);
//		g.fillRect(H.x, H.y, H.width, H.height);
//		g.setColor(Color.red);
//		g.fillRect(m_coord.X() - 5, m_coord.Y() - 5, 10, 10);

		if (now - getDeadTime() > 1000 && getState() == 2) {
			setAlpha(this.getAlpha() * 0.95f);
		}

	}

	@Override
	public boolean cell(Direction dir, Category cat) {
		boolean c = !((m_model.m_room.isBlocked(m_coord.X(), m_coord.Y()))
				|| (m_model.m_room.isBlocked(hitBox.X(), hitBox.Y())));
		if (m_State == HIT_STATE) {
			return !c;
		}
		if (!c) {
			m_State = HIT_STATE;
		}
		return c;
	}
	
}