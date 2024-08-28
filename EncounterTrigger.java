import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class EncounterTrigger extends Sprite implements Interactable {

    private Party enemyParty;
    private static BufferedImage triggerImage;
    private String loadedImage;

    public EncounterTrigger(String loadedImage, int x, int y, int w, int h)
    {
        super(x, y, w, h);
        this.loadedImage = loadedImage;

        if(triggerImage == null)
        {
            triggerImage = View.LOAD_IMAGE("Images\\Sprites\\encounter.png");
            System.out.println("Loaded Encounter Trigger Images");
        }
    }

    public EncounterTrigger(String loadedImage, int x, int y)
    {
        this(loadedImage, x, y, WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
    }

    public EncounterTrigger(String loadedImage)
    {
        this(loadedImage, 0, 0, 0, 0);
    }

     //Constructor based on json object
     public EncounterTrigger(Json ob)
     {
       this(ob.getString("loadedImage"), (int)ob.getLong("x"), (int)ob.getLong("y"), WorldSizeInformation.GRID_SIZE, WorldSizeInformation.GRID_SIZE);
   }

    @Override
    public boolean isEncounterTrigger()
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
        MessageBus.sendMessage("enter battle", "JsonEncounterFiles\\enemyTeam1.json");
    }

    @Override
    public void update() { }

    @Override
    public BufferedImage getDrawnImage(String loadedImage)
    {
        return triggerImage;
    }

    @Override
    public void draw(Graphics g, int scrollPosX, int scrollPosY) {
        g.drawImage(triggerImage, x - scrollPosX, y - scrollPosY, w, h, null);
    }

    @Override
    public Json marshal() 
    { 
        Json ob = Json.newObject();
        ob.add("loadedImage", loadedImage);
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) { }
    
}
