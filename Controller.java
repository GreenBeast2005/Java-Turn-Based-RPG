/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the controller class for our basic pacman assignment
				As a basic description, this class handles all the input
				for our "game", that input includes keyboard, and mouse input.
******************************************************************/

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;

public class Controller implements MouseListener, MouseMotionListener, KeyListener, MessageListener
{
	//Lets the controller have referene to the model and view
	private Model currentModel, overworldModel, battleModel;
	private View view;
	private LoadingScreenView loadingView;
	private JFrame gameWindow;

	private boolean absorbtionMode;

	private static boolean editMode, controlEditMenuMode;

	private boolean moveUp, moveDown, moveLeft, moveRight;

	//Constructor
	public Controller(Model overworldModel, Model battleModel, JFrame frame)
	{
		this.overworldModel = overworldModel;
		this.battleModel = battleModel;

		currentModel = overworldModel;

		this.gameWindow = frame;

		absorbtionMode = false;
		editMode = false;
		controlEditMenuMode = false;

		moveDown = false;
		moveLeft = false;
		moveUp = false;
		moveRight = false;

		MessageBus.addListener(this);
		MessageBus.addListener((MessageListener)overworldModel);
		MessageBus.addListener((MessageListener)battleModel);
	}
	void setView(View v)
	{
		view = v;
	}
	void setLoadingView(LoadingScreenView v)
	{
		loadingView = v;
	}

	//Update function that basically is just in charge of calling model for relevent input.
	public void update() 
	{ 
		if(currentModel.isContinuousDirectionInput())
		{
			if(moveUp) {
				currentModel.inputUp();
			}
			if(moveDown) {
				currentModel.inputDown();
			}
			if(moveLeft) {
				currentModel.inputLeft();
			}
			if(moveRight) {
				currentModel.inputRight();
			}
			if(!moveDown && !moveLeft && !moveUp && !moveRight){
				currentModel.inputNoDirection();
			}
		}else {
			moveDown = false;
			moveLeft = false;
			moveRight = false;
			moveUp = false;
		}
		
	}

	@Override
	public void onMessageReceived(String message, String data)
	{
		switch(message)
		{
			case "enter battle": currentModel = battleModel; break;
			case "exit battle": currentModel = overworldModel; break;
			default: System.out.println("Controller class did not recieve message"); break;
		}
		view.setModel(currentModel);
	}

	//This function is called when you click on the window
	public void mousePressed(MouseEvent e)
	{
		currentModel.inputMouseClicked(e.getX(), e.getY());
	}

	//These are a bunch of empty functions, the are required since this is a mouseListener class as well
	public void mouseReleased(MouseEvent e) 
	{
		// System.out.println(e.getX() + " mouse x, " + e.getY() + " mouse y.");
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }

	//Allows you to "Draw" walls or erase lots of walls at once
	public void mouseDragged(MouseEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e) { }

	//Gets called when the key is pressed down, used for the exiting program calls, and the scrolling window
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_UP: moveUp = true; break;
			case KeyEvent.VK_DOWN: moveDown = true; break;
			case KeyEvent.VK_LEFT: moveLeft = true; break;
			case KeyEvent.VK_RIGHT: moveRight = true; break;

			case KeyEvent.VK_ESCAPE: System.exit(0); break;
			
			//When you hold the shift key it puts you into absorbtion mode so that you can absorb the transformation orbs
			case KeyEvent.VK_SHIFT: currentModel.inputAlternateControlsStart(); break;
		}

		char keyPressed = Character.toLowerCase(e.getKeyChar());
		if(keyPressed == 'q')
		{
			System.exit(0);
		}
	}

	//Gets called when the key is released
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_UP: moveUp = false; break;
			case KeyEvent.VK_DOWN: moveDown = false; break;
			case KeyEvent.VK_LEFT: moveLeft = false; break;
			case KeyEvent.VK_RIGHT: moveRight = false; break;

			case KeyEvent.VK_BACK_SPACE: currentModel.inputBack(); break;
			case KeyEvent.VK_ENTER:
				if(UniversalUI.isTakingInput()) 
				{
					UniversalUI.InputUse();
				}else {
					currentModel.inputUse();
				}
				break;
			case KeyEvent.VK_SHIFT: currentModel.inputAlternateControlsEnd(); break;
		}

		if(!currentModel.isContinuousDirectionInput())
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_UP: currentModel.inputUp(); break;
				case KeyEvent.VK_DOWN: currentModel.inputDown(); break;
				case KeyEvent.VK_LEFT: currentModel.inputLeft(); break;
				case KeyEvent.VK_RIGHT: currentModel.inputRight(); break;
			}
		}

		char keyPressed = Character.toLowerCase(e.getKeyChar());

		switch(keyPressed)
		{
			case 'e': currentModel.inputDebugMode(); break;
			case 'l': PlayerData.unMarshal(Json.load("PlayerData.json")); break;
		}
	}

	//Empty function that is required for keyListener
	public void keyTyped(KeyEvent e) { }
}
