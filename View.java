/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the view class for our basic pacman java project
				this class is in charge of actually generating what you see
				in the JFrame.
******************************************************************/

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Iterator;
import java.io.File;

public class View extends JPanel
{
	public static final int GAME_WINDOW_SIZE_X = 1920;
	public static final int GAME_WINDOW_SIZE_Y = 1080;

	private Model currentModel;

	private int scrollPosY, scrollPosX;

	//Constructor, gives the view class a reference to the controller, and the model.
	public View(Controller c, Model m)
	{
		currentModel = m;

		scrollPosY = 0;
		scrollPosX = 0;
		
		c.setView(this);
	}

	public void setModel(Model m)
	{
		currentModel = m;
	}

	//Used by other classes to load their images
	public static BufferedImage LOAD_IMAGE(String filePath)
	{
		try
		{
			return ImageIO.read(new File(filePath));
		}
		catch(Exception e) 
		{
			e.printStackTrace(System.err);
			System.out.println("Unable to load " + filePath);
			System.exit(1);
		}
		return null;
	}

	public void handleScrolling()
	{
		// scrollPosY = currentModel.getPlayerSprite().getCenterYPosition() - (GAME_WINDOW_SIZE_Y / 2);
		// if(scrollPosY > WorldSizeInformation.GAME_WORLD_SIZE_Y - GAME_WINDOW_SIZE_Y)
		// 	scrollPosY = WorldSizeInformation.GAME_WORLD_SIZE_Y - GAME_WINDOW_SIZE_Y;
		// if(scrollPosY < 0)
		// 	scrollPosY = 0;

		// scrollPosX = currentModel.getPlayerSprite().getCenterXPosition() - (GAME_WINDOW_SIZE_X / 2);
		// if(scrollPosX > WorldSizeInformation.GAME_WORLD_SIZE_X - GAME_WINDOW_SIZE_X)
		// 	scrollPosX = WorldSizeInformation.GAME_WORLD_SIZE_X - GAME_WINDOW_SIZE_X;
		// if(scrollPosX < 0)
		// 	scrollPosX = 0;
	}

	public static void DrawCorrectlyFormattedString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			if(line != "\n")
				g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

	

	//Already part of JPanel class, and by having this here it gets overwritten in order to display the things I choose
	public void paintComponent(Graphics g)
	{
		handleScrolling();

		currentModel.draw(g);

		UniversalUI.Draw(g);
	}

	public int getScrollPosY()
	{
		return scrollPosY;
	}
	public int getScrollPosX()
	{
		return scrollPosX;
	}
}
