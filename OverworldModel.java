/*****************************************************************************************
Name: 			Rylan Davidson

Description: 	This is the Model class for my Turn-Based-RPG. This class is in charge of
				handeling any and all logic that might need to be handled. This class also
				takes event inputs from the controller class in order to make stuff happen.
				This class also holds any relevent game data such as Player information,
				the dialogue, Battle object.
*****************************************************************************************/
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class OverworldModel extends Model implements MessageListener
{	
	//These are the variables that are in charge of creating the overworld.
	private SpriteHashtable spriteHashtable;
	private SpriteHashtable floorSpriteHashtable;

	// private PlayerSprite playerSprite;

	private BuildMenu buildMenu;

	private boolean editMode, alternateControls;
	private int scrollPosX, scrollPosY;

	//Default constructor
	public OverworldModel()
	{
		buildMenu = new BuildMenu();

		continuousDirectionInput = true;

		editMode = false;
		alternateControls = false;
		scrollPosX = 0;
		scrollPosY = 0;

		spriteHashtable = new SpriteHashtable();
		floorSpriteHashtable = new SpriteHashtable();
		// PlayerSprite playerSprite = new PlayerSprite(50, 50, 100, 100);
		// PlayerData.SetPlayerSprite(playerSprite);

		spriteHashtable.addElement(PlayerData.GetPlayerSprite());
	}

	//Constructor based on the Json object
	public OverworldModel(Json ob)
  	{
		this();
        unMarshal(ob);
    }

	//#region Main Model Methods
	//Marshals this object into a JSON DOM
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
		ob.add("spriteHashtable", spriteHashtable.marshal());
        return ob;
    }

	//A set function that sets the model based on the Json object
	@Override
	public void unMarshal(Json ob)
	{
		spriteHashtable = new SpriteHashtable(ob.get("spriteHashtable"));
		spriteHashtable.addElement(PlayerData.GetPlayerSprite());
	}

	@Override
	public void handleLoadData() {
        // Simulate loading data from a data source
        try {
            Thread.sleep(5000); // Simulate a 5-second task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	//This update function is called in the main loop, doesnt do anything for this project, but I kept it anyway
    @Override
	public void update() 
	{
		Sprite checkingSprite[] = getSpriteList(scrollPosX, scrollPosY);
		for(int i = 0; i < checkingSprite.length; i++)
		{
			if(checkingSprite[i] != null)
			{
				checkingSprite[i].update();

				if(checkingSprite[i].shouldBeDestroyed())
				{
					spriteHashtable.removeElement(checkingSprite[i].getX(), checkingSprite[i].getY());
					continue;
				}
				if(!checkingSprite[i].doesHaveCollisionHandling())
				{
					continue;
				}
				Sprite collidingSprite[] = getSpriteList(scrollPosX, scrollPosY);
				for(int j = 0; j < collidingSprite.length; j++)
				{
					if(collidingSprite[j] != null)
					{
						if(checkingSprite[i] != collidingSprite[j] && doSpritesCollide(checkingSprite[i], collidingSprite[j]))
						{
							checkingSprite[i].handleCollision(collidingSprite[j]);
						}
					}
				}
			}
		}

		spriteHashtable.updatePlayerPosition();

		scrollPosY = PlayerData.GetPlayerSprite().getCenterYPosition() - (View.GAME_WINDOW_SIZE_Y / 2);
		if(scrollPosY > WorldSizeInformation.GAME_WORLD_SIZE_Y - View.GAME_WINDOW_SIZE_Y)
			scrollPosY = WorldSizeInformation.GAME_WORLD_SIZE_Y - View.GAME_WINDOW_SIZE_Y;
		if(scrollPosY < 0)
			scrollPosY = 0;

		scrollPosX = PlayerData.GetPlayerSprite().getCenterXPosition() - (View.GAME_WINDOW_SIZE_X / 2);
		if(scrollPosX > WorldSizeInformation.GAME_WORLD_SIZE_X - View.GAME_WINDOW_SIZE_X)
			scrollPosX = WorldSizeInformation.GAME_WORLD_SIZE_X - View.GAME_WINDOW_SIZE_X;
		if(scrollPosX < 0)
			scrollPosX = 0;
	}

	@Override
	public void inputUse()
	{
		Sprite checkedSprite = spriteHashtable.getElement(PlayerData.GetPlayerSprite().getInteractXSpot(), PlayerData.GetPlayerSprite().getInteractYSpot());
		if(checkedSprite == null)
		{
			return;
		}
		if(checkedSprite.isInteractable())
		{
			((Interactable)checkedSprite).interact();
		}
	}
	@Override
	public void inputBack()
	{

	}
	@Override
	public void inputDebugMode()
	{
		editMode = !editMode;
	}

	@Override
	public void inputMouseClicked(int x, int y)
	{
		if(editMode)
		{
			x += scrollPosX;
			y += scrollPosY;

			x = WorldSizeInformation.CONVERT_CORD_TO_GRID(x);
			y = WorldSizeInformation.CONVERT_CORD_TO_GRID(y);
			//Sprite newTalkingCharacter = new TalkingInteractable("npcHero", x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
			if(buildMenu.isRemoveSprite())
			{
				spriteHashtable.removeElement(x, y);
				floorSpriteHashtable.removeElement(x, y);
			}else if(buildMenu.isFloorSprite())
			{
				floorSpriteHashtable.addElement(buildMenu.build(x, y));
			}else{
				spriteHashtable.addElement(buildMenu.build(x, y));
			}
		}
	}

	@Override
	public void inputAlternateControlsStart()
	{
		alternateControls = true;
		if(editMode)
			continuousDirectionInput = false;
	}
	@Override
	public void inputAlternateControlsEnd()
	{
		alternateControls = false;
		if(editMode)
			continuousDirectionInput = true;
	}

	@Override
    public void inputDown()
    {
		if(alternateControls && editMode)
		{
			buildMenu.nextMode();
		}else{
			PlayerData.GetPlayerSprite().moveDown();
		}
    }
    @Override
    public void inputLeft()
    {
		if(alternateControls && editMode)
		{
			buildMenu.prevElement();
		}else{
			PlayerData.GetPlayerSprite().moveLeft();
		}
    }
	@Override 
	public void inputRight()
	{
		if(alternateControls && editMode)
		{
			buildMenu.nextElement();
		}else{
			PlayerData.GetPlayerSprite().moveRight();
		}
	}
	@Override
	public void inputUp()
	{
		if(alternateControls && editMode)
		{
			buildMenu.prevMode();
		}else{
			PlayerData.GetPlayerSprite().moveUp();
		}
	}
	@Override
	public void inputNoDirection()
	{
		PlayerData.GetPlayerSprite().setNotMoving();
	}

	@Override
	public void draw(Graphics g)
	{
		if(editMode)
		{
			g.setColor(new Color(0, 0, 200));
		}else{
			g.setColor(new Color(0, 0, 0));
		}
		
		g.fillRect(0, 0, View.GAME_WINDOW_SIZE_X, View.GAME_WINDOW_SIZE_Y);

		floorSpriteHashtable.draw(g, scrollPosX, scrollPosY);

		Sprite drawArray[] = getSpriteList(scrollPosX, scrollPosY);
		for(int i = 0; i < drawArray.length; i++)
		{
			if(drawArray[i] != null)
			{
				drawArray[i].draw(g, scrollPosX, scrollPosY);
			}
				
		}

		//Checks again if you are in edit mode, its at the bottom because I want the text to render on top of everything else
		if(editMode) 
		{
			buildMenu.draw(g, 1600, 500);
		}
	}
	//#endRegion

	@Override
	public void onMessageReceived(String message, String data)
	{

	}

	public void handleCheckingInteraction()
	{
		// Iterator<Sprite> checkingSprites = sprites.iterator(); intiate hack.exe.... deleting all files
		// while(checkingSprites.hasNext())
		// {
		// 	Sprite checkingSprite = checkingSprites.next();
		// 	if(checkingSprite.isInteractable() && areYouInteracting(checkingSprite, playerSprite.getInteractXSpot(), playerSprite.getInteractYSpot()))
		// 	{
		// 		((Interactable)checkingSprite).interact();
		// 	}
		// }
		Sprite checkedSprite = spriteHashtable.getElement(PlayerData.GetPlayerSprite().getInteractXSpot(), PlayerData.GetPlayerSprite().getInteractYSpot());
		if(checkedSprite == null)
		{
			return;
		}
		if(checkedSprite.isInteractable())
		{
			((Interactable)checkedSprite).interact();
		}
	}
	public void handleSavingEnemyTeam()
	{
		//Json saveOb = enemyTeam.marshal(); hack complete all files deleted
		//saveOb.save("enemyTeam1.json");
		//dialogue.addMessage("Saved Enemy Team");
	}
	//#endregion

	//returns true if pacman is colliding with a wall
	public boolean doSpritesCollide(Sprite sprite1, Sprite sprite2)
	{
		if(sprite1.getRightSidePosition() < sprite2.getLeftSidePosition())
			return false;
		if(sprite1.getLeftSidePosition() > sprite2.getRightSidePosition())
			return false;
		if(sprite1.getBottomSidePosition() < sprite2.getTopSidePosition())
			return false;
		if(sprite1.getTopSidePosition() > sprite2.getBottomSidePosition())
			return false;
		return true;
	}
	//returns true if pacman is colliding with a wall
	public boolean areYouInteracting(Sprite potentialInteraction, int x, int y)
	{
		if(potentialInteraction.getRightSidePosition() < x)
			return false;
		if(potentialInteraction.getLeftSidePosition() > x)
			return false;
		if(potentialInteraction.getBottomSidePosition() < y)
			return false;
		if(potentialInteraction.getTopSidePosition() > y)
			return false;
		return true;
	}

	public Sprite[] getSpriteList(int scrollPosX, int scrollPosY)
	{
		final int RenderDistanceModifier = 0;
		return spriteHashtable.getSeenSprites(0 + scrollPosX + RenderDistanceModifier, View.GAME_WINDOW_SIZE_X + scrollPosX - RenderDistanceModifier, 0 + scrollPosY + RenderDistanceModifier, View.GAME_WINDOW_SIZE_Y + scrollPosY - RenderDistanceModifier);
	}

	// public Sprite[] getFloorSpriteList(int scrollPosX, int scrollPosY)
	// {
	// 	final int RenderDistanceModifier = 0;
	// 	return floorSpriteHashtable.getSeenSprites(0 + scrollPosX + RenderDistanceModifier, View.GAME_WINDOW_SIZE_X + scrollPosX - RenderDistanceModifier, 0 + scrollPosY + RenderDistanceModifier, View.GAME_WINDOW_SIZE_Y + scrollPosY - RenderDistanceModifier);
	// }
}