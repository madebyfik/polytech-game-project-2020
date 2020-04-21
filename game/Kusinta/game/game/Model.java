package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.graphics.View;
import player.Player;
import room.Room;


public class Model {
	
	int m_x, m_y, m_width, m_height, x_decalage, y_decalage;
	
	Room m_room;
	Coord centerScreen; // position du personnage plus tard;
	
	Player m_player;
	View m_view;
//	Opponent[] m_opponents;
	
	public Model() throws IOException {
		m_room = new Room();
		centerScreen = m_room.getStartCoord();
		setCenterScreen();
		m_view = null;
	}
	
	public void setView(View view) {
		m_view = view;
	}
	
	public void setCenterScreen() {
		x_decalage = m_width / 2 - centerScreen.X();
		y_decalage = m_height / 2 - centerScreen.Y();
	}
	
	public void tick(long elapsed) {
		long ratio = (long)(elapsed / m_view.getTickPeriod() + 1);
		m_player.setRatio(ratio);
		
		
	}
	
	public void paint(Graphics g, int width, int height) {
		m_width = width;
		m_height = height;
		setCenterScreen();
		Graphics gp = g.create(m_x + x_decalage, m_y + y_decalage, m_width - x_decalage, m_height - y_decalage);
		m_room.paint(gp);
		gp.dispose();
	}
	
	/*
	 * Loading a sprite
	 */
	public static BufferedImage[] loadSprite(String filename, int nrows, int ncols) throws IOException {
	    File imageFile = new File(filename);
	    if (imageFile.exists()) {
	      BufferedImage image = ImageIO.read(imageFile);
	      int width = image.getWidth(null) / ncols;
	      int height = image.getHeight(null) / nrows;

	      BufferedImage[] images = new BufferedImage[nrows * ncols];
	      for (int i = 0; i < nrows; i++) {
	        for (int j = 0; j < ncols; j++) {
	          int x = j * width;
	          int y = i * height;
	          images[(i * ncols) + j] = image.getSubimage(x, y, width, height);
	        }
	      }
	      return images;
	    }
	    return null;
	  }
	
	
	
}
