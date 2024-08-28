/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the playerSprite class for our basic playerSprite game
                this holds all of playerSprites data and cotains its images.
******************************************************************/

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class PlayerSprite extends Sprite {
    private int preX, preY;
    private int speed;

    private final int PLAYER_IMAGES_PER_DIRECTION = 3;
	private final int PLAYER_NUMBER_OF_DIRECTIONS = 4;
    private int currentFrame, directionThroughFrame;
    private static BufferedImage playerSpriteImage;

    private boolean isMoving;
    private Direction direction;

    //Im using this to make it easier for humans to read the code rather than tying dirictions to numbers
    private enum Direction
    {
        down,
        left,
        right,
        up
    }

    public PlayerSprite(int x, int y, int h, int w)
    {
        super(x, y, h, w);
        preX = x;
        preY = y;
        direction = Direction.down;
        currentFrame = 0;
        directionThroughFrame = 1;

        speed = 10;

        hasCollisionHandling = true;

        if(playerSpriteImage == null)
        {
            playerSpriteImage = View.LOAD_IMAGE("Images\\Sprites\\playerSprite.png");
        }
    }

    //Constructor based on json object
    public PlayerSprite(Json ob)
  	{
        this((int)ob.getLong("x"), (int)ob.getLong("y"), (int)ob.getLong("h"), (int)ob.getLong("w"));
    }

    // Marshals this object into a JSON DOM
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
     	ob.add("h", h);
        ob.add("w", w);
        return ob;
    }

    @Override
    public BufferedImage getDrawnImage(String loadedImage)
    {
        return null;
    }

    //Gets called inside model.update currently does nothing, but has potential to do something later
    @Override
    public void update() { }

    public void setPosition(int x, int y, int h, int w)
    {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    @Override
    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    {
        //Example of using the direction enum, when you do .ordinal it gives the position of the direction in the enum
        g.drawImage(playerSpriteImage.getSubimage((currentFrame/2) * 100, direction.ordinal() * 150, 100, 150), x - scrollPosX, y - scrollPosY - h/2, w, h + h/2, null);
        if(isMoving)
		{
			updateFrame();
		}
    }

    //Gets called if playerSprite is moving, doesnt get called if its not moving
    public void updateFrame()
	{
		currentFrame = (currentFrame + directionThroughFrame);
        if(currentFrame > 5)
        {
            directionThroughFrame *= -1;
            currentFrame = 4;
        }
        if(currentFrame < 0)
        {
            directionThroughFrame *= -1;
            currentFrame = 1;
        }
	}

    //Used to determine the direction playerSprite should be adjusted based on the collision
    public int getPreLeftSidePosition()
    {
        return preX;
    }
    public int getPreRightSidePosition()
    {
        return preX + w;
    }
    public int getPreTopSidePosition()
    {
        return preY;
    }
    public int getPreBottomSidePosition()
    {
        return preY + h;
    }

    //Gets called in the view class to make sure playerSprite sits in the very center of the screen
    public int getCenterXPosition()
    {
        return x + (w / 2);
    }
    public int getCenterYPosition()
    {
        return y + (h / 2);
    }
    

    public int getInteractXSpot()
    {
        int tempX = x + (w/2);
        switch(direction)
        {
            case up: break;
            case down: break;
            case left: tempX -= w; break;
            case right: tempX += w; break;
        }
        return tempX;
    }
    public int getInteractYSpot()
    {
        int tempY = y + (h/2);
        switch(direction)
        {
            case up: tempY -= h; break;
            case down: tempY += h; break;
            case left: break;
            case right: break;
        }
        return tempY;
    }

    //Used to move playerSprite around
    public void moveUp()
    {
        preY = y;
        y -= speed;
        direction = Direction.up;
        isMoving = true;
    }
    public void moveDown()
    {
        preY = y;
        y += speed;
        direction = Direction.down;
        isMoving = true;
    }
    public void moveLeft()
    {
        preX = x;
        x -= speed;
        direction = Direction.left;
        isMoving = true;
    }
    public void moveRight()
    {
        preX = x;
        x += speed;
        direction = Direction.right;
        isMoving = true;
    }

    //Gets called when playerSprite stops moving to stop the 
    public void setNotMoving()
    {
        isMoving = false;
    }

    @Override
    public boolean isPlayerSprite()
    {
        return true;
    }

    //Used for debuging, gives you all of playerSprites relevent information
    @Override 
    public String toString()
    {
        return "PlayerSprite (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    //This function takes the wall playerSprite is colliding with and then adjusts the position based on that.
    public void handleCollision(Sprite collidedSprite)
    {
        if(true)
        {
            if(getPreRightSidePosition() <= collidedSprite.getLeftSidePosition())
            {
                x = collidedSprite.getLeftSidePosition() - w - 1;
            }
            if(getPreLeftSidePosition() >= collidedSprite.getRightSidePosition())
            {
                x = collidedSprite.getRightSidePosition() + 1;
            }
            if(getPreBottomSidePosition() <= collidedSprite.getTopSidePosition())
            {
                y = collidedSprite.getTopSidePosition() - h - 1;
            }
            if(getPreTopSidePosition() >= collidedSprite.getBottomSidePosition())
            {
                y = collidedSprite.getBottomSidePosition() + 1;
            }
        }
    }
}
