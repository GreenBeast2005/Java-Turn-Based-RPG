import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;

public class PartyMember extends Creature {
    private ArrayList<Skill> skills;
    private int selectedSkill;

    private int absorbedOrbs, transformationLevel;

    private int currentMana, maxMana;
    private int amountOfOrbsToTransform;

    private BufferedImage[] smallImage;
    private BufferedImage character;

    private int currentFrame;

    public PartyMember(String ImageName, String Name, int PhysAtk, int PhysDef, int ElemAtk, int ElemDef, int MaxHp, int MaxMana, Type.DamageType defaultDamageType)
    {
        super(ImageName, Name, PhysAtk, PhysDef, ElemAtk, ElemDef, MaxHp, defaultDamageType);

        this.maxMana = MaxMana;
        currentMana = MaxMana;

        transformationLevel = 0;
        isDead = false;

        skills = new ArrayList<Skill>();
        selectedSkill = 0;

        absorbedOrbs = 0;
        amountOfOrbsToTransform = 3;

        if(smallImage == null)
        {
            smallImage = new BufferedImage[2];
            smallImage[0] = View.LOAD_IMAGE("Images\\PartyMembers\\" + ImageName + "SmallImage0.png");
            smallImage[1] = View.LOAD_IMAGE("Images\\PartyMembers\\" + ImageName + "SmallImage1.png");
            character = View.LOAD_IMAGE("Images\\PartyMembers\\" + ImageName + "Idle.png");
            System.out.println("Loaded " + ImageName);
        }
        currentFrame = 0;
    }

    public PartyMember(Json ob)
    {
        this(ob.getString("imageName"), 
        ob.getString("name"), 
        (int)ob.getLong("physAtk"), 
        (int)ob.getLong("physDef"), 
        (int)ob.getLong("elemAtk"), 
        (int)ob.getLong("elemDef"), 
        (int)ob.getLong("maxHp"), 
        (int)ob.getLong("maxMana"),
        Type.DamageType.values()[(int)ob.getLong("defaultDamageType")]);
    }

    public boolean canTransform()
    {
        return absorbedOrbs >= amountOfOrbsToTransform;
    }

    public void disperseOrbs()
    {
        absorbedOrbs = 0;
    }

    public void transform()
    {
        transformationLevel++;
        disperseOrbs();
    }

    public void absorbOrb()
    {
        absorbedOrbs++;
    }

    public void addSkill(Skill skill)
    {
        skills.add(skill);
    }

    public void useSkill(Enemy target)
    {
        skills.get(selectedSkill).use(target, this);
    }

    public void prevSkill()
    {
        if(selectedSkill <= 0)
        {
            selectedSkill = skills.size() - 1;
        }else{
            selectedSkill--;
        }
    }

    public void nextSkill()
    {
        if(selectedSkill >= skills.size() - 1)
        {
            selectedSkill = 0;
        }else{
            selectedSkill++;
        }
    }

    public ArrayList<Skill> getSkills()
    {
        return skills;
    }

    public Skill getSelectedSkill()
    {
        return skills.get(selectedSkill);
    }

    public int getSelectedSkillNumber()
    {
        return selectedSkill;
    }
    
    public void takeMana(int cost)
    {
        currentMana -= cost;
    }

    public int getCurrentMana()
    {
        return currentMana;
    }

    public int getAmountOfAbsorbedOrbs()
    {
        return absorbedOrbs;
    }

    public int getTransformationLevel()
    {
        return transformationLevel;
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
        ob.add("maxMana", maxMana);
        ob.add("defaultDamageType", defaultDamageType.ordinal());
        return ob;
    }

    @Override
    public boolean isPartyMember()
    {
        return true;
    }

    @Override
    public void drawYourself(Graphics g, int x, int y)
    {
        if(!isDead)
        {
            if(absorbedOrbs > 0)
            {
                g.setColor(new Color(255, 255, 0));
                g.fillRect(x - 5, y - 5, 70, 70);
                g.setFont(new Font("Dialog", Font.PLAIN, 10));
                g.setColor(new Color(255, 255, 255));
                g.drawString(absorbedOrbs + "/" + amountOfOrbsToTransform, x + 65, y + 5);
            }
            g.drawImage(smallImage[transformationLevel], x, y, 60, 60, null);

            g.setColor(new Color(255, 0, 0));
            g.fillRect(x + 65, y + 25, 140, 15);
            if(maxMana > 0)
                g.fillRect(x + 65, y + 45, 140, 15);

            g.setColor(new Color(0, 255, 0));
            g.fillRect(x + 65, y + 25, (int)(140 * ((double)currentHp) / ((double)maxHp)), 15);

            if(maxMana > 0)
            {
                g.setColor(new Color(0, 0, 255));
                g.fillRect(x + 65, y + 45, (int)(140 * ((double)currentMana) / ((double)maxMana)), 15);
            }

            g.setFont(new Font("Dialog", Font.BOLD, 20));
            g.setColor(new Color(255, 255, 255));
            g.drawString(name, x + 65, y + 20);

            g.setFont(new Font("Dialog", Font.PLAIN, 10));
            g.drawString(currentHp + "/" + maxHp, x + 210, y + 35);

            g.drawImage(character.getSubimage(0 + ((currentFrame % 6)/2 * 200), 0, 200, 200), x, y + 65, 200, 200, null);

            if(maxMana > 0)
            {
                g.drawString(currentMana + "/" + maxMana, x + 210, y + 55);
            }            
        }else
        {
            g.drawString("Dead " + name, x, y);
        }
        currentFrame++;
        currentFrame = currentFrame % 6;
    }

}
