/*****************************************************************************************
Name: 			Rylan Davidson

Description: 	This is the abstract Model class for my Turn-Based-RPG. This class is in charge of
				handeling any and all logic that might need to be handled. This class also
				takes event inputs from the controller class in order to make stuff happen.
				This class also holds any relevent game data.
*****************************************************************************************/
import java.awt.Graphics;

public abstract class Model
{
	protected boolean continuousDirectionInput;
//Brap rmmmph yum brapppp
	//Default constructor
	public Model() 
	{ 
		continuousDirectionInput = false;
	}

	//These are the saving functions
    public abstract Json marshal();
	public abstract void unMarshal(Json ob);

	//This is used when things need to enter a loading screen
	public abstract void handleLoadData();

	//This update function is called in the main loop, used to update entities and move dynamic things
	public abstract void update();

	//#region Handling Input from Controller Class
	public abstract void inputUse();
	public abstract void inputBack();
	public abstract void inputDebugMode();

	public abstract void inputMouseClicked(int x, int y);

	public abstract void inputAlternateControlsStart();
	public abstract void inputAlternateControlsEnd();

	public boolean isContinuousDirectionInput()
	{
		return continuousDirectionInput;
	}
	
	public abstract void inputDown();
	public abstract void inputLeft();
	public abstract void inputRight();
	public abstract void inputUp();
	public abstract void inputNoDirection();
	//#endregion
	
	//Gets the model to drawitself
	public abstract void draw(Graphics g);
}