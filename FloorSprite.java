/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the individual wall class for the basic pacman project
                this class holds the data for the walls
******************************************************************/

import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Graphics;

public class FloorSprite extends Sprite {
    private static BufferedImage floorImage;

    private int selectedFloorSprite;
    //private String loadedWall;

    //Default constructor
    public FloorSprite(int x, int y, int h, int w)
    {
        super(x, y, h, w);

        Random rand = new Random();

        selectedFloorSprite = rand.nextInt(32);

        // System.out.println("Floor Sprite x: " + (selectedFloorSprite % 8) + " y: " + (selectedFloorSprite / 8));

        if(floorImage == null)
        {
            floorImage = View.LOAD_IMAGE("Images\\Sprites\\TX Tileset Grass.png");
            System.out.println("Loaded TX Tileset Grass Image");
        }
        
    }

    //This is the other constructor i made for the 8 bit alternative, since every wall has the same size
    public FloorSprite(int x, int y)
    {
        this(x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    public FloorSprite()
    {
        this(0, 0);
    }

    //Constructor based on json object
    public FloorSprite(Json ob)
  	{
        this((int)ob.getLong("x"), (int)ob.getLong("y"), WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
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
        return floorImage;
    }

    @Override
    public void draw(Graphics g, int scrollLPosX, int scrollPosY)
    {

        g.drawImage(floorImage.getSubimage(selectedFloorSprite%8 * 32, selectedFloorSprite/8 * 32, 32, 32), x - scrollLPosX, y - scrollPosY, w, h, null);
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
        // ob.add("loadedWall", loadedWall);
        return ob;
    }

    @Override
    public boolean isFloorSprite()
    {
        return true;
    }

    //Used for debuging
    @Override 
    public String toString()
    {
        return "Floor (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }
}
