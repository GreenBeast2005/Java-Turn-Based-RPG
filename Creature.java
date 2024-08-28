import java.awt.Graphics;

public abstract class Creature {
    protected String name, imageName;
    protected int physAtk, physDef, elemAtk, elemDef;
    protected int currentHp, maxHp;

    protected Type.DamageType defaultDamageType;

    protected boolean isDead;
    
    public Creature(String imageName, String Name, int PhysAtk, int PhysDef, int ElemAtk, int ElemDef, int MaxHp, Type.DamageType defaultDamageType)
    {
        this.name = Name;
        this.imageName = imageName;
        this.physAtk = PhysAtk;
        this.physDef = PhysDef;
        this.elemAtk = ElemAtk;
        this.elemDef = ElemDef;
        this.maxHp = MaxHp;
        currentHp = MaxHp;

        isDead = false;

        this.defaultDamageType = defaultDamageType;
    }

    public Skill getDefaultAttackSkill()
    {
        return new Skill("Normal Attack", 100, 0, Type.DamageTypeForStatCalculation.physical, defaultDamageType);
    }

    public boolean isPartyMember()
    {
        return false;
    }

    public boolean isEnemy()
    {
        return false;
    }

    public void update()
    {
        
    }

    public void takeDamage(int damage, Type.DamageTypeForStatCalculation damageTypeForStatCalculation)
    {
        damage = getDamageTaken(damage, damageTypeForStatCalculation);
        currentHp -= damage;
        if(currentHp <= 0 && !isDead)
        {
            currentHp = 0;
            isDead = true;
        }
    }
    public int getDamageTaken(int damage, Type.DamageTypeForStatCalculation damageTypeForStatCalculation)
    {
        switch(damageTypeForStatCalculation)
        {
            case physical: damage -= physDef; break;
            case elemental: damage -= elemDef; break;
        }
        if(damage < 1)
            damage = 1;
        return damage;
    }

    public String getName()
    {
        return name;
    }

    public int getPhysAtk()
    {
        return physAtk;
    }
    public int getElemAtk()
    {
        return elemAtk;
    }

    public int getMaxHp()
    {
        return maxHp;
    }

    public int getCurrentHp()
    {
        return currentHp;
    }

    public boolean getIsDead()
    {
        return isDead;
    }

    public abstract void drawYourself(Graphics g, int x, int y);
    public abstract Json marshal();
}
