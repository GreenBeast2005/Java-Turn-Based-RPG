import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

public class TalkingInteractable extends Sprite implements Interactable, TalkingCharacter {
    private static BufferedImage defaultNPCImage, heroNPCImage, redHailGirlNPCImage;
    private String loadedImage;

    private ArrayList<String> messages;

    public TalkingInteractable(String loadedNPC, int x, int y, int w, int h)
    {
        super(x, y, w, h);
        this.loadedImage = loadedNPC;
        switch(loadedNPC)
        {
            case "npc":
                if(defaultNPCImage == null)
                {
                    defaultNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npc.png");
                    System.out.println("Loaded npc Image");
                }
                break;
            case "npcHero":
                if(heroNPCImage == null)
                {
                    heroNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npcHero.png");
                    System.out.println("Loaded npcHero Image");
                }
                break;
            case "npcRedHairGirl":
                if(redHailGirlNPCImage == null)
                {
                    redHailGirlNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npcRedHairGirl.png");
                    System.out.println("Loaded npcRedHairGirl Image");
                }
                break;
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TalkingInteractableOptions dialog = new TalkingInteractableOptions(frame); // Set array size as needed
        messages = dialog.getMessages();

        frame.dispose(); // Close the frame after getting values
    }
    public TalkingInteractable(String loadedImage, int x, int y)
    {
        this(loadedImage, x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }
    public TalkingInteractable(String loadedImage)
    {
        super(0, 0, 0, 0);
        this.loadedImage = loadedImage;
        switch(loadedImage)
        {
            case "npc":
                if(defaultNPCImage == null)
                {
                    defaultNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npc.png");
                    System.out.println("Loaded npc Image");
                }
                break;
            case "npcHero":
                if(heroNPCImage == null)
                {
                    heroNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npcHero.png");
                    System.out.println("Loaded npcHero Image");
                }
                break;
            case "npcRedHairGirl":
                if(redHailGirlNPCImage == null)
                {
                    redHailGirlNPCImage = View.LOAD_IMAGE("Images\\Sprites\\npcRedHairGirl.png");
                    System.out.println("Loaded npcRedHairGirl Image");
                }
                break;
        }
    } 
    //Constructor based on json object
    public TalkingInteractable(Json ob)
    {
        this(ob.getString("loadedImage"), (int)ob.getLong("x"), (int)ob.getLong("y"), WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    @Override
    public boolean isTalkingInteractable()
    {
        return true;
    }

    @Override
    public boolean isInteractable()
    {
        return true;
    }

    @Override
    public void interact() 
    {
        for(int i = 0; i < messages.size(); i++)
            UniversalUI.addMessageToDialogue(messages.get(i), this);
    }

    @Override
    public BufferedImage getDrawnImage(String loadedImage)
    {
        switch(loadedImage)
        {
            case "npc": return defaultNPCImage;
            case "npcHero": return heroNPCImage;
            case "npcRedHairGirl": return redHailGirlNPCImage;
        }
        return null;
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g, int scrollPosX, int scrollPosY) {
        switch(loadedImage)
        {
            case "npc": g.drawImage(defaultNPCImage, x - scrollPosX, y - scrollPosY, w, h, null); break;
            case "npcHero": g.drawImage(heroNPCImage, x - scrollPosX, y - scrollPosY - h/2, w, h + h/2, null); break;
            case "npcRedHairGirl": g.drawImage(redHailGirlNPCImage, x - scrollPosX, y - scrollPosY - h/2, w, h + h/2, null); break;
        }
    }
    @Override
    public BufferedImage getTalkingImage()
    {
        switch(loadedImage)
        {
            case "npc": return defaultNPCImage;
            case "npcHero": return heroNPCImage.getSubimage(0, 0, 100, 100);
            case "npcRedHairGirl": return redHailGirlNPCImage.getSubimage(0, 0, 100, 100);
        }
        return null;
    }

    @Override
    public Json marshal() { 
        Json ob = Json.newObject();
        ob.add("loadedImage", loadedImage);
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) { }
}
