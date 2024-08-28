/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the main class file for the basic pacman project
				this is where all the main classes are instantiated
				also contains the size of the game world.
******************************************************************/

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Game extends JFrame
{
	private Model overworldModel, battleModel;
	private Controller controller;
	private View view;
	private LoadingScreenView loadingView;

	public Game()
	{
		//Initializes the model, controller, view so that they can be 
		Json firstLevelOb = null;
		try {
			firstLevelOb = Json.load("map.json");
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println("Unable to load the map");
			System.exit(1);
		}
		
		PlayerData.unMarshal(Json.load("PlayerData.json"));

		overworldModel = new OverworldModel();
		battleModel = new BattleModel();

		controller = new Controller(overworldModel, battleModel, this);
		view = new View(controller, overworldModel);
		loadingView = new LoadingScreenView(controller);

		//Enables the view component to actually read input from the mouse, as you can see this is part of JPanel
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
		//This enables this JFrame componet to read input from the keyboard, this is part of JFrame
		this.addKeyListener(controller);

		//These are just some of the generic setup for the Window
		this.setTitle("Badass Turn-Based Game :)");
		
		//I changed the set size method to this, because the setsize method includes the window border and I want control of the size of the content pane
		view.setPreferredSize(new Dimension(View.GAME_WINDOW_SIZE_X, View.GAME_WINDOW_SIZE_Y)); 

		this.getContentPane().add(view); //Right here we set up the JFrame to display our JPanel object
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void run()
	{
		while(true)
		{
			controller.update();
			overworldModel.update();
			battleModel.update();
			view.repaint();
			//loadingView.repaint(); // This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			//Go to sleep for 40 milliseconds, this leads to a frame rate of 25 frames per second
			try
			{
				Thread.sleep(40);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	//The magic main function, notice how short it is, most of the work is done in the actual classes not the main function
	public static void main(String[] args)
	{
		System.out.println("Controls:\n\tNavigation: arrow keys\n\tConfirm: enter\n\tBack: backspace\n\tEnter Absorbtion Mode?: shift");
		Game g = new Game();
		g.run();
	}
}
