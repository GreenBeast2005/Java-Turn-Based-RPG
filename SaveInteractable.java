import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SaveInteractable extends Sprite implements Interactable {

    private static BufferedImage saveImage;
    private String loadedImage;

    public SaveInteractable(String loadedImage, int x, int y, int w, int h)
    {
        super(x, y, w, h);

        this.loadedImage = loadedImage;

        switch(loadedImage)
        {
            case "save":
                if(saveImage == null)
                {
                    saveImage = View.LOAD_IMAGE("Images\\Sprites\\" + loadedImage + ".png");
                    System.out.println("Loaded " + loadedImage + " Image");
                }
                break;
        }
    }

    public SaveInteractable(String loadedImage, int x, int y)
    {
        this(loadedImage, x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    public SaveInteractable(String loadedImage)
    {
        this(loadedImage, 0, 0);
    }

    @Override
    public void interact() 
    {
        UniversalUI.addMessageToDialogue("Saved Game!");
        PlayerData.marshal().save("PlayerData.json");
        System.out.println("Testing save");
    }

    @Override
    public boolean isInteractable()
    {
        return true;
    }

    @Override
    public boolean isSaveInteractable()
    {
        return true;
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g, int scrollPosX, int scrollPosY) {
        g.drawImage(saveImage, x - scrollPosX, y - scrollPosY, w, h, null);
    }

    @Override
    public BufferedImage getDrawnImage(String loadedImage) {
        return saveImage;
    }

    @Override
    public Json marshal() 
    { 
        return null;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) 
    { 

    }
    
}
