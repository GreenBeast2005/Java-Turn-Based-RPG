import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Enemy extends Creature {
    private BufferedImage smallImage;
    private BufferedImage character;

    public Enemy(String ImageName, String Name, int PhysAtk, int PhysDef, int ElemAtk, int ElemDef, int MaxHp, Type.DamageType defaultDamageType)
    {
        super(ImageName, Name, PhysAtk, PhysDef, ElemAtk, ElemDef, MaxHp, defaultDamageType);

        if(smallImage == null)
        {
            smallImage = View.LOAD_IMAGE("Images\\Enemies\\" + ImageName + "SmallImage.png");
            character = View.LOAD_IMAGE("Images\\Enemies\\" + ImageName + "Idle.png");
            System.out.println("Loaded " + ImageName);
        }
    }

    public Enemy(Json ob)
    {
        this(
            ob.getString("imageName"),
            ob.getString("name"),
            (int)ob.getLong("physAtk"),
            (int)ob.getLong("physDef"),
            (int)ob.getLong("elemAtk"),
            (int)ob.getLong("elemDef"),
            (int)ob.getLong("maxHp"),
            Type.DamageType.values()[(int)ob.getLong("defaultDamageType")] );
    }

    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("imageName", imageName);
        ob.add("name", name);
        ob.add("physAtk", physAtk);
        ob.add("physDef", physDef);
        ob.add("elemAtk", elemAtk);
        ob.add("elemDef", elemDef);
        ob.add("maxHp", maxHp);
        ob.add("defaultDamageType", defaultDamageType.ordinal());
        return ob;
    }

    @Override
    public boolean isEnemy()
    {
        return true;
    }

    @Override
    public void drawYourself(Graphics g, int x, int y)
    {
        if(!isDead)
        {
            g.drawImage(smallImage, x, y, 60, 60, null);

            g.setColor(new Color(255, 0, 0));
            g.fillRect(x + 65, y + 45, 140, 15);

            g.setColor(new Color(0, 255, 0));
            g.fillRect(x + 65, y + 45, (int)(140 * ((double)currentHp) / ((double)maxHp)), 15);

            g.setFont(new Font("Dialog", Font.BOLD, 20));
            g.setColor(new Color(255, 255, 255));
            g.drawString(name, x + 65, y + 40);

            g.setFont(new Font("Dialog", Font.PLAIN, 10));
            g.drawString(currentHp + "/" + maxHp, x + 210, y + 55);

            g.drawImage(character, x, y + 65, 200, 200, null);
        }else
        {
            g.drawString("Dead " + name, x, y);
        }
    }
}
