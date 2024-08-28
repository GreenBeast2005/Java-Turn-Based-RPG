/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the individual wall class for the basic pacman project
                this class holds the data for the walls
******************************************************************/

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Wall extends Sprite {
    private static BufferedImage defaultWallImage, stoneWallImage;
    private String loadedWall;

    //Default constructor
    public Wall(String loadedWall, int x, int y, int h, int w)
    {
        super(x, y, h, w);

        this.loadedWall = loadedWall;

        switch(loadedWall)
        {
            case "wall":
                if(defaultWallImage == null)
                {
                    defaultWallImage = View.LOAD_IMAGE("Images\\Sprites\\" + loadedWall + ".png");
                    System.out.println("Loaded " + loadedWall + " Image");
                }
                break;
            case "stoneWall":
                if(stoneWallImage == null)
                {
                    stoneWallImage = View.LOAD_IMAGE("Images\\Sprites\\" + loadedWall + ".png");
                    System.out.println("Loaded " + loadedWall + " Image");
                }
                break;
        }
        
    }

    //This is the other constructor i made for the 8 bit alternative, since every wall has the same size
    public Wall(String loadedWall, int x, int y)
    {
        this(loadedWall, x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    public Wall(String loadedWall)
    {
        this(loadedWall, 0, 0);
    }

    //Constructor based on json object
    public Wall(Json ob)
  	{
        this(ob.getString("loadedWall"), (int)ob.getLong("x"), (int)ob.getLong("y"), WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    //takes in an x and y from mouse, the converts it to proper grid coordinates, then it returns true if this wall is in that spot
    public boolean isClicked(int x, int y)
    {
        x = WorldSizeInformation.CONVERT_CORD_TO_GRID(x);
		y = WorldSizeInformation.CONVERT_CORD_TO_GRID(y);

        if(this.x == x && this.y == y)
            return true;
        return false;
    }

    public void setPosition(int x, int y, int h, int w)
    {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    //Required methods for sprite class
    //Tells the walls to draw themselves
    @Override
    public BufferedImage getDrawnImage(String loadedImage)
    {
        switch(loadedImage)
        {
            case "wall": return defaultWallImage;
            case "stoneWall": return stoneWallImage;
        }
        return null;
    }

    @Override
    public void draw(Graphics g, int scrollLPosX, int scrollPosY)
    {
        switch(loadedWall)
        {
            case "wall": g.drawImage(defaultWallImage, x - scrollLPosX, y - scrollPosY, w, h, null); break;
            case "stoneWall": g.drawImage(stoneWallImage, x - scrollLPosX, y - scrollPosY, w, h, null); break;
        }
        
    }

    @Override
    public void update() { }

    @Override
    public void handleCollision(Sprite collidedSprite) { }

    // Marshals this object into a JSON DOM
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("loadedWall", loadedWall);
        return ob;
    }

    @Override
    public boolean isWall()
    {
        return true;
    }

    //Used for debuging
    @Override 
    public String toString()
    {
        return "Wall (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }
}
