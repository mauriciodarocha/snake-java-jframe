package snake;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int[] x = new int[GAME_UNITS];
	final int[] y = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	GamePanel() {
		random = new Random();
		setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(new MyKeyAdapter());
		startGame();		
	}
	public void startGame() {
		newApple();
		bodyParts = 3;
		applesEaten = 0;
		x[0]=0;
		y[0]=0;
		move();
		direction='R';
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {	
		if (running) {
			// Grid
//			for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			g.setColor(new Color(206, 7, 7));
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(new Color(41, 137, 1));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(81, 203, 31));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			showText(g,40,"Score: "+applesEaten,SCREEN_WIDTH, 40);
		} else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
			case 'U' -> y[0] = y[0] - UNIT_SIZE;
			case 'D' -> y[0] = y[0] + UNIT_SIZE;
			case 'L' -> x[0] = x[0] - UNIT_SIZE;
			case 'R' -> x[0] = x[0] + UNIT_SIZE;
		}
	}
	public void checkApple() {
		if ((x[0]==appleX)&&(y[0]==appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		// checks if head collides with body
		for(int i=bodyParts;i>0;i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
				break;
			}
		}
		// checks if head collides left border
		if (x[0]<0) {
			running = false;
		}
		// checks if head collides right border
		if (x[0]>SCREEN_WIDTH) {
			running = false;
		}
		// checks if head collides top border
		if (y[0]<0) {
			running = false;
		}
		// checks if head collides bottom border
		if (y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		showText(g,40,"Score: "+applesEaten,SCREEN_WIDTH, 40);
		showText(g,75,"Game Over!",SCREEN_WIDTH, SCREEN_HEIGHT/2);
		timer.stop();
	}
	public void showText(Graphics g,int size,String text,int x, int y) {
		g.setColor(new Color(206, 7, 7));
		g.setFont(new Font("Ink Free",Font.BOLD,size));
		FontMetrics metrics = getFontMetrics(g.getFont());
		int xPos = (x - metrics.stringWidth(text))/2;
		g.drawString(text,xPos,y);		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT -> {
					if (direction!='R') {
							direction = 'L';
						}
				}
				case KeyEvent.VK_RIGHT -> {
					if (direction!='L') {
							direction = 'R';
						}
				}
				case KeyEvent.VK_UP -> {
					if (direction!='D') {
							direction = 'U';
						}
				}
				case KeyEvent.VK_DOWN -> {
					if (direction!='U') {
							direction = 'D';
						}
				}
				case KeyEvent.VK_SPACE -> {
					if(!running) {
							startGame();
						}
				}
			}
		}
		
	}
}
