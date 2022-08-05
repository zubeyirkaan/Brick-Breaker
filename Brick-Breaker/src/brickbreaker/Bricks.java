package brickbreaker;

import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Bricks {
	public int map[][];
	public int brickWidth;
	public int brickHeight;

	BufferedImage redImg;
	BufferedImage yellowImg;

	public Bricks(int row, int col) {
		map = new int[row][col];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1;
			}
		}
	
		brickWidth = 600/col;
		brickHeight = 120/row;

		try {
		    redImg = ImageIO.read(new File("./resources/red.png"));
		    yellowImg = ImageIO.read(new File("./resources/yellow.png"));
		} catch (IOException e) {
		    System.out.println("Failed to retrieve resources");
		}
	}

	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					g.drawImage(i % 2 == 1 ? redImg : yellowImg, j * brickWidth, i * brickHeight, (j * brickWidth) + brickWidth, (i * brickHeight) + brickHeight, 0, 0, 200, 200, null);
					// g.fillRect(j * brickWidth, i * brickHeight, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(2));
					g.setColor(Color.white);
					g.drawRect(j * brickWidth, i * brickHeight , brickWidth, brickHeight);
				}
			}
		}
		
	}

	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
		
	}
}
