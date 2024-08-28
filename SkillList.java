
public class SkillList {
    public static enum SkillEnum
    {
        instaKill("Insta Kill", 10000, 0, Type.DamageTypeForStatCalculation.physical, Type.DamageType.blunt),
        werehogHead("Werehog Head", 150, 10, Type.DamageTypeForStatCalculation.physical, Type.DamageType.sharp),
        pinchPunch("Pinch Punch", 120, 5, Type.DamageTypeForStatCalculation.physical, Type.DamageType.blunt);

        private String name;
        private int damage, manaCost;
        private Type.DamageTypeForStatCalculation damageTypeForStatCalculation;
        private Type.DamageType damageType;

        public Skill getSkill()
        {
            return new Skill(name, damage, manaCost, damageTypeForStatCalculation, damageType);
        }

        SkillEnum(String name, int damage, int manaCost, Type.DamageTypeForStatCalculation damageTypeForStatCalculation, Type.DamageType damageType)
        {
            this.name = name;
            this.damage = damage;
            this.manaCost = manaCost;
            this.damageTypeForStatCalculation = damageTypeForStatCalculation;
            this.damageType = damageType;
        }
    }
}
