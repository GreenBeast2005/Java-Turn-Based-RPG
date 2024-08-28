import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class SpriteHashtable {
    private Sprite spriteGridArray[][];
    private int prevPlayerX, prevPlayerY;

    private BufferedImage combinedImage;
    private Graphics2D g2d;


    public SpriteHashtable()
    {
        spriteGridArray = new Sprite[(WorldSizeInformation.GAME_WORLD_SIZE_X + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE][(WorldSizeInformation.GAME_WORLD_SIZE_Y + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE];

        combinedImage = new BufferedImage(WorldSizeInformation.GAME_WORLD_SIZE_X, WorldSizeInformation.GAME_WORLD_SIZE_Y, BufferedImage.TYPE_INT_ARGB);
        g2d = combinedImage.createGraphics();

        prevPlayerX = -1;
        prevPlayerY = -1;
        //System.out.println("Size of array: x " + WorldSizeInformation.GAME_WORLD_SIZE_X/WorldSizeInformation.GRID_SIZE + ", y " + WorldSizeInformation.GAME_WORLD_SIZE_Y/WorldSizeInformation.GRID_SIZE);
    }
    public SpriteHashtable(Json ob)
    {
        this();
        unMarshal(ob);
    }

    public void unMarshal(Json ob)
    {
        spriteGridArray = new Sprite[(WorldSizeInformation.GAME_WORLD_SIZE_X + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE][(WorldSizeInformation.GAME_WORLD_SIZE_Y + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE];
        prevPlayerX = -1;
        prevPlayerY = -1;
        
        Json wallList = ob.get("walls");
		Json npcList = ob.get("npcs");
		Json encounterList = ob.get("encounters");
        for(int i = 0; i < wallList.size(); i++)
        	addElement(new Wall(wallList.get(i)));
		for(int i = 0; i < npcList.size(); i++)
        	addElement(new TalkingInteractable(npcList.get(i)));
		for(int i = 0; i < encounterList.size(); i++)
        	addElement(new EncounterTrigger(encounterList.get(i)));
    }

    public void addElement(Sprite addedSprite)
    {
        int x = WorldSizeInformation.CONVERT_CORD_TO_GRID(addedSprite.getX());
        int y = WorldSizeInformation.CONVERT_CORD_TO_GRID(addedSprite.getY());
        if(addedSprite.isPlayerSprite())
        {
            prevPlayerX = WorldSizeInformation.CONVERT_CORD_TO_GRID(addedSprite.getX());
            prevPlayerY = WorldSizeInformation.CONVERT_CORD_TO_GRID(addedSprite.getY());
        }

        //System.out.println("Spot in array: " + x/WorldSizeInformation.GRID_SIZE + ", " + y/WorldSizeInformation.GRID_SIZE);

        spriteGridArray[x/WorldSizeInformation.GRID_SIZE][y/WorldSizeInformation.GRID_SIZE] = addedSprite;

        addedSprite.draw(g2d, 0, 0);

        // spriteArray[addedSprite.getX()/WorldSizeInformation.GRID_SIZE + ((addedSprite.getX()/WorldSizeInformation.GRID_SIZE) * (addedSprite.getY()/WorldSizeInformation.GRID_SIZE))] = addedSprite;
    }

    public Json marshal()
    {
        Json ob = Json.newObject();

        Json wallList = Json.newList();
		Json npcList = Json.newList();
		Json encounterList = Json.newList();

        for(int i = 0; i < spriteGridArray.length; i++)
		{
            for(int j = 0; j < spriteGridArray[i].length; j++)
            {
                if(spriteGridArray[i][j] == null)
                {
                    //continue;
                }else if(spriteGridArray[i][j].isWall())
                {
                    wallList.add(spriteGridArray[i][j].marshal());
                }else if(spriteGridArray[i][j].isEncounterTrigger())
                {
                    encounterList.add(spriteGridArray[i][j].marshal());
                }else if(spriteGridArray[i][j].isTalkingInteractable())
                {
                    npcList.add(spriteGridArray[i][j].marshal());
                }
            }
			
		}
		ob.add("walls", wallList);
		ob.add("npcs", npcList);
		ob.add("encounters", encounterList);
        return ob;
    }

    public Sprite getElement(int x, int y)
    {
        x = WorldSizeInformation.CONVERT_CORD_TO_GRID(x);
        y = WorldSizeInformation.CONVERT_CORD_TO_GRID(y);

        //System.out.println("Getting element at: x " + x/WorldSizeInformation.GRID_SIZE + ", y " + y/WorldSizeInformation.GRID_SIZE);

        return spriteGridArray[x/WorldSizeInformation.GRID_SIZE][y/WorldSizeInformation.GRID_SIZE];
    }

    public void updatePlayerPosition()
    {
        if(prevPlayerX > -1 && prevPlayerY > -1)
        {
            Sprite playerSprite = spriteGridArray[prevPlayerX/WorldSizeInformation.GRID_SIZE][prevPlayerY/WorldSizeInformation.GRID_SIZE];
        spriteGridArray[prevPlayerX/WorldSizeInformation.GRID_SIZE][prevPlayerY/WorldSizeInformation.GRID_SIZE] = null;

        prevPlayerX = WorldSizeInformation.CONVERT_CORD_TO_GRID(playerSprite.getX());
        prevPlayerY = WorldSizeInformation.CONVERT_CORD_TO_GRID(playerSprite.getY());

        spriteGridArray[WorldSizeInformation.CONVERT_CORD_TO_GRID(playerSprite.getX())/WorldSizeInformation.GRID_SIZE][WorldSizeInformation.CONVERT_CORD_TO_GRID(playerSprite.getY())/WorldSizeInformation.GRID_SIZE] = playerSprite;
        }
    }

    public boolean removeElement(int x, int y)
    {
        x = WorldSizeInformation.CONVERT_CORD_TO_GRID(x);
        y = WorldSizeInformation.CONVERT_CORD_TO_GRID(y);

        if(spriteGridArray[x/WorldSizeInformation.GRID_SIZE][y/WorldSizeInformation.GRID_SIZE] != null)
        {
            spriteGridArray[x/WorldSizeInformation.GRID_SIZE][y/WorldSizeInformation.GRID_SIZE] = null;
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.setComposite(AlphaComposite.Clear); // Transparent color
            g2d.fillRect(x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
            g2d.dispose(); // Fill region with transparent color
            return true;
        }
        return false;
    }

    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    {
        g.drawImage(combinedImage, 0 - scrollPosX, 0 - scrollPosY, null);
    }

    public Sprite[] getSeenSprites(int startingX, int endingX, int startingY, int endingY)
    {
        if(startingX > 0)
            startingX -= WorldSizeInformation.GRID_SIZE;
        if(startingY > 0)
            startingY -= WorldSizeInformation.GRID_SIZE;

        endingX += WorldSizeInformation.GRID_SIZE ;
        endingY += WorldSizeInformation.GRID_SIZE;

        startingX = WorldSizeInformation.CONVERT_CORD_TO_GRID(startingX);
        endingX = WorldSizeInformation.CONVERT_CORD_TO_GRID(endingX);
        startingY = WorldSizeInformation.CONVERT_CORD_TO_GRID(startingY);
        endingY = WorldSizeInformation.CONVERT_CORD_TO_GRID(endingY);

        startingX = startingX/WorldSizeInformation.GRID_SIZE;
        endingX = endingX/WorldSizeInformation.GRID_SIZE;
        startingY = startingY/WorldSizeInformation.GRID_SIZE;
        endingY = endingY/WorldSizeInformation.GRID_SIZE;

        Sprite returnedArray[] = new Sprite[(endingX - startingX) * (endingY - startingY)];
        
        for(int i = 0; i < endingY - startingY; i++)
        {
            for(int j = 0; j < endingX - startingX; j++)
            {
                if(j + startingX < (WorldSizeInformation.GAME_WORLD_SIZE_X + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE && i + startingY < (WorldSizeInformation.GAME_WORLD_SIZE_Y + WorldSizeInformation.GRID_SIZE)/WorldSizeInformation.GRID_SIZE)
                    returnedArray[(i * (endingX - startingX)) + j] = spriteGridArray[j + startingX][i + startingY];
                //System.out.println(((i * (endingX - startingX)) + j) + " = x " + j + ", y " + i + " " + spriteGridArray[j][i]);
            }
        }
        return returnedArray;
    }
}
