package brickbreaker;

import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 32;
	
	private Timer timer;
	private int delay = 8;
	
	private int paddle = 220;
	
	private int ballposX = 285;
	private int ballposY = 680;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private Bricks map;

	private Image ballImg;
	private Image paddleImg;
	
	public Gameplay() {
		map = new Bricks(4, 8);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();

		try {
		    ballImg = ImageIO.read(new File("./resources/ball.png"));
		    paddleImg = ImageIO.read(new File("./resources/paddle.png"));
		} catch (IOException e) {
		    System.out.println("Failed to retrieve resources");
		}
	}
	
	public void paint(Graphics g) {
		// background 
		g.setColor(Color.lightGray);
		g.fillRect(1, 1, 600, 800);
		
		//drawing map
		map.draw((Graphics2D)g);
		
		//the paddle
		g.drawImage(paddleImg, paddle, 700, paddle+170, 730, 0, 0, 2048, 2048, null);
		//the ball
		g.drawImage(ballImg, ballposX, ballposY, ballposX+36, ballposY+24, 0, 0, 480, 320, null);
		
		if (ballposY > 800) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.black);
            g.setFont(new Font("seri", Font.BOLD, 30));
            g.drawString("  Game Over Score: " + score, 140, 320);

            g.setFont(new Font("seri", Font.BOLD, 30));
            g.drawString("  Press Enter to Restart", 130, 360);
        }
        if(totalBricks == 0){
            play = false;
            ballYdir = -2;
            ballXdir = -1;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("  Game Over: "+score,190,300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);


        }

		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			if(new Rectangle(ballposX, ballposY, 20,20).intersects(new Rectangle(paddle,700, 150, 8))) {
				ballYdir = -ballYdir;
			}
			A: for(int i = 0; i<map.map.length; i++) {
				for(int j = 0; j<map.map[0].length; j++) {
					if(map.map[i][j] >0) {
						int brickX= j* map.brickWidth;
						int brickY = i* map.brickHeight;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 1;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x +  brickRect.width) {
								ballXdir = -ballXdir;
							}else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}
					
			ballposX += ballXdir;
			ballposY +=ballYdir;
			if(ballposX <0) {
				ballXdir = -ballXdir;
			}
			if(ballposY <0) {
				ballYdir = -ballYdir;
			}
			if(ballposX >570) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(paddle >=445) {
				paddle = 445;
			}else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(paddle < 10) {
				paddle = 10;
			}else {
				moveLeft();
			}
		}
		 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	            if (!play) {
	                ballposX = 285;
	                ballposY = 680;
	                ballXdir = -1;
	                ballYdir = -2;
	                score = 0;
	                paddle = 220;
	                totalBricks = 32;
	                map = new Bricks(4, 8);

	                repaint();
	            }
	        }
		
	}

	public void moveRight() {
		play = true;
		paddle+=20;
	}

	public void moveLeft() {
		play = true;
		paddle-=20;
	}
}
