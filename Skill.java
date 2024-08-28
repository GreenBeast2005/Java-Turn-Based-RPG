public class Skill {
    private int damage, manaCost;
    private String attackName;
    private Type.DamageTypeForStatCalculation damageTypeForStatCalculation;
    private Type.DamageType damageType;

    public Skill(String name, int damage, int manaCost, Type.DamageTypeForStatCalculation damageTypeForStatCalculation, Type.DamageType damageType)
    {
        this.damage = damage;
        this.manaCost = manaCost;
        attackName = name;
        this.damageTypeForStatCalculation = damageTypeForStatCalculation;
        this.damageType = damageType;
    }

    public boolean canCast(PartyMember caster)
    {
        if(caster.getCurrentMana() >= manaCost)
        {
            return true;
        }
        return false;
    }

    public boolean use(Creature target, Creature caster)
    {
        if((caster.isPartyMember() && ((PartyMember)caster).getCurrentMana() >= manaCost) || caster.isEnemy())
        {
            int calculatedDamage = calculateDamage(target, caster);
            if(caster.isPartyMember())
            {
                ((PartyMember)caster).takeMana(manaCost);
            }
            target.takeDamage(calculatedDamage, damageTypeForStatCalculation);
            UniversalUI.addMessageToDialogue(caster.getName() + " used " + attackName + " on " + target.getName() + " for " + target.getDamageTaken(calculateDamage(target, caster), damageTypeForStatCalculation));
            return true;
        }
        return false;
    }

    private int calculateDamage(Creature target, Creature caster)
    {
        double damagePercent = (double)damage/100;
        int calculatedDamage = 0;
        switch(damageTypeForStatCalculation)
        {
            case physical: calculatedDamage = (int)(caster.getPhysAtk() * damagePercent); break;
            case elemental: calculatedDamage = (int)(caster.getElemAtk() * damagePercent); break;
        }
        if(caster.isPartyMember())
        {
            calculatedDamage = (int)(calculatedDamage * Math.pow(1.2, ((PartyMember)caster).getTransformationLevel()));
            if(((PartyMember)caster).canTransform())
            {
                calculatedDamage = (int)(calculatedDamage * 1.4);
            }else{
                double absorbedOrbsDamage = 1.0;
                for(int i = 0; i < ((PartyMember)caster).getAmountOfAbsorbedOrbs(); i++)
                {
                    absorbedOrbsDamage += 0.1;
                }
                calculatedDamage = (int)(calculatedDamage * absorbedOrbsDamage);
            }
        }
        return calculatedDamage;
    }

    public String getName()
    {
        return attackName;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getManaCost()
    {
        return manaCost;
    }
}
