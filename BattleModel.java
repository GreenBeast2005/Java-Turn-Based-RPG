import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BattleModel extends Model implements MessageListener {
    //Kind of a mini model class, handles all the logic relating to battles so that I can exit the battle and enter the land of the overworld

    private Party enemyParty;
    // private Party playerParty;

    private int amountOfTransformationOrbs;

	private BufferedImage background;

    private boolean inBattle, absorbtionMode;

    private BattleInteractions selectedInteraction;
    private BattleMenus currentMenu;

    //Since this is a data type used by controller and view I figured making it public statatic is fine
	public static enum BattleInteractions
	{
		attack
		{
			@Override
			public BattleInteractions prev()
			{
				return values()[2];
			}	
		},
		skill,
		item 
		{
			@Override
			public BattleInteractions next() 
			{
				return values()[0];
			}
		};

		public BattleInteractions next() 
		{
			return values()[ordinal() + 1];
		}

		public BattleInteractions prev()
		{
			return values()[ordinal() - 1];
		}
	}

	public static enum BattleMenus
	{
		main,
		skill,
		item,
		chooseEnemy
	}

    public BattleModel()
    {
        super();
        selectedInteraction = BattleInteractions.attack;
        currentMenu = BattleMenus.main;
        absorbtionMode = false;

        // Party playerParty = new Party();

		// PartyMember partyMember1 = new PartyMember("sonic", "Sonic the Hedgehog", 10, 10, 10, 10, 100, 50, Type.DamageType.blunt);
		// partyMember1.addSkill(new Skill("Super Chili Dog", 130, 10, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember1.addSkill(new Skill("Gotta Go Fast!!", 120, 5, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember1.addSkill(new Skill("Werehog Head", 140, 15, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember1.addSkill(new Skill("InstaKill", 100000, 0, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));

		// playerParty.addPartyMember(partyMember1);

		// PartyMember partyMember2 = new PartyMember("metalSonic", "Metal Sonic", 10, 10, 10, 10, 100, 50, Type.DamageType.ranged);
		// partyMember2.addSkill(new Skill("Blasting Ass", 130, 10, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember2.addSkill(new Skill("Jumping Jimbo", 120, 5, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember2.addSkill(new Skill("Piss yourself", 140, 15, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember2.addSkill(new Skill("InstaKill", 100000, 0, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));

		// playerParty.addPartyMember(partyMember2);

		// PartyMember partyMember3 = new PartyMember("metalSonic", "Super Piss Boy", 10, 10, 10, 10, 100, 50, Type.DamageType.ranged);
		// partyMember3.addSkill(new Skill("Blasting Ass", 130, 10, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember3.addSkill(new Skill("Jumping Jimbo", 120, 5, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember3.addSkill(new Skill("Piss yourself", 140, 15, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));
		// partyMember3.addSkill(new Skill("InstaKill", 100000, 0, Type.DamageTypeForStatCalculation.elemental, Type.DamageType.magic));

		// playerParty.addPartyMember(partyMember3);

		// PlayerData.SetPlayerParty(playerParty);

        background = View.LOAD_IMAGE("Images\\Backgrounds\\battleback8.png");

        amountOfTransformationOrbs = 0;

        setChoosePlayerMode();

        inBattle = false;
    }

    // public BattleModel(Party playerParty, Party enemyParty)
    // {
    //     this.enemyParty = enemyParty;
    //     this.playerParty = playerParty;

    //     this.enemyParty.resetTurns();
    //     this.playerParty.resetTurns();
    // }

	// public BattleModel(Party enemyParty)
	// {
	// 	// this(Model.GetPlayerParty(), enemyParty);
	// }

    //#region Main Model Methods
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        //enemyParty = new Party
        return ob;
    }

    @Override
    public void unMarshal(Json ob)
    {
        enemyParty = new Party(ob);
    }

    @Override
    public void handleLoadData()
    {

    }

	//This update function is called in the main loop, doesnt do anything for this project, but I kept it anyway
    @Override
	public void update() 
	{
        if(inBattle)
        {
            enemyParty.update();
            PlayerData.GetPlayerParty().update();
            if(PlayerData.GetPlayerParty().entirePartyHasTakenTurn())
            {
                handleEnemyiesTakingTurns();
                PlayerData.GetPlayerParty().resetTurns();
            }
        }
	}

    @Override
	public void inputUse()
	{
        if(absorbtionMode)
        {
            handleAbsorbtionInput();
        }else {
            switch(currentMenu)
            {
                case main:
                    switch (selectedInteraction) 
                    {
                        case attack: currentMenu = BattleMenus.chooseEnemy; setChooseEnemyMode(); break;
                        case skill: currentMenu = BattleMenus.skill; break;
                        case item: currentMenu = BattleMenus.item; break;
                    }
                    break;
                case skill: currentMenu = BattleMenus.chooseEnemy; setChooseEnemyMode(); break;
                case item:
                case chooseEnemy:
                    switch (selectedInteraction) 
                    {
                        case attack: currentMenu = BattleMenus.main; handleAttackInput(); setChoosePlayerMode(); break;
                        case skill: currentMenu = BattleMenus.main; handleSkillInput(); setChoosePlayerMode(); break;
                        case item: currentMenu = BattleMenus.main; setChoosePlayerMode(); break;
                    }
                    break;
            }
        }
	}

    @Override
    public void inputBack()
    {
        switch(currentMenu)
        {
            case skill:
            case item:
            case chooseEnemy: currentMenu = BattleMenus.main; break;
        }
    }

    @Override
    public void inputDebugMode()
    {

    }

    @Override
    public void inputMouseClicked(int x, int y)
    {

    }

    @Override
    public void inputAlternateControlsStart()
    {
        absorbtionMode = true;
    }
    @Override
    public void inputAlternateControlsEnd()
    {
        absorbtionMode = false;
    }

    @Override
    public void inputDown()
    {
        switch(currentMenu)
        {
            case main: selectedInteraction = selectedInteraction.next(); break;
            case skill: navigateNextSkill(); break;
            case item:
        }
    }
    @Override
    public void inputLeft()
    {
        switch(currentMenu)
        {
            case main: navigatePrevPartyMember(); releaseOrbs(); break;
            case chooseEnemy: navigatePrevEnemy(); break;
        }
    }
    @Override
    public void inputRight()
    {
        switch(currentMenu)
        {
            case main: navigateNextPartyMember(); releaseOrbs(); break;
            case chooseEnemy: navigateNextEnemy(); break;
        }
    }
    @Override
    public void inputUp()
    {
        switch(currentMenu)
        {
            case main: selectedInteraction = selectedInteraction.prev(); break;
            case skill: navigatePrevSkill(); break;
            case item:
        }
    }
    @Override
    public void inputNoDirection()
    {

    }

    @Override
    public void draw(Graphics g)
	{
		//Sets the font of the little edit mode text in the bottom right corner
		g.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		g.drawImage(background, 0, 0, View.GAME_WINDOW_SIZE_X, View.GAME_WINDOW_SIZE_Y, null);

		PlayerData.GetPlayerParty().drawYourself(g, 30, 500);
		
		enemyParty.drawYourself(g, 500, 200);

		if(amountOfTransformationOrbs > 0)
		{
			handleDrawingOrbs(g, 500, 350);
		}

		if(!UniversalUI.isTakingInput())
			drawInteractions(g, PlayerData.GetPlayerParty().getXOfCurrentPartyMember(30), PlayerData.GetPlayerParty().getYOfCurrentPartyMember(500), getSelectedPartyMember());
	}
    //#endregion

    @Override
    public void onMessageReceived(String message, String data)
    {
        switch(message)
        {
            case "enter battle": unMarshal(Json.load(data)); inBattle = true; break;
            default: System.out.println("BattleModel did not recieve message"); break;
        }
    }

	public boolean checkForVictory()
	{
		return enemyParty.getEntirePartyDead();
	}

	//These are the handling for specific inputs attack/skill/item
	public void handleVictory()
	{
		// Model.AddMessageToDialogue("Wowie good job beating all those guys!");
		// Model.AddMessageToDialogue("I mean seriously they were tough!");
        MessageBus.sendMessage("exit battle", "");
        inBattle = false;
	}

    public void handleAttackInput()
    {
        PlayerData.GetPlayerParty().getSelectedMember().getDefaultAttackSkill().use(enemyParty.getSelectedMember(), PlayerData.GetPlayerParty().getSelectedMember());
		UniversalUI.addMessageToDialogue("Good attack!");

		if(((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).canTransform())
		{
			((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).transform();
		}else{
			((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).disperseOrbs();
			amountOfTransformationOrbs++;
		}
		PlayerData.GetPlayerParty().handleHavingTurn();
		if(checkForVictory())
		{
			handleVictory();
		}
    }
	public void handleSkillInput()
	{
		((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).getSelectedSkill().use(enemyParty.getSelectedMember(), PlayerData.GetPlayerParty().getSelectedMember());
		UniversalUI.addMessageToDialogue("Good skill use brother!");

		if(((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).canTransform())
		{
			((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).transform();
		}else{
			((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).disperseOrbs();
		}
		PlayerData.GetPlayerParty().handleHavingTurn();
		if(checkForVictory())
		{
			handleVictory();
		}
	}
	public void handleAbsorbtionInput()
	{
		if(amountOfTransformationOrbs > 0)
		{
			amountOfTransformationOrbs--;
			((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).absorbOrb();
		}
	}
	public void handleEnemyiesTakingTurns()
	{
		// Model.AddMessageToDialogue("Enemies take their turn.");
		// Model.AddMessageToDialogue("Nothing happens!");
	}

	public void releaseOrbs()
	{
		amountOfTransformationOrbs += ((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).getAmountOfAbsorbedOrbs();
		((PartyMember)PlayerData.GetPlayerParty().getSelectedMember()).disperseOrbs();
	}

	public int getAmountOfTransformationOrbs()
	{
		return amountOfTransformationOrbs;
	}

	public void setChooseEnemyMode()
	{
        if(enemyParty != null)
		    enemyParty.enterSelectionMode();
		PlayerData.GetPlayerParty().exitSelectionMode();
	}

	public void setChoosePlayerMode()
	{
		PlayerData.GetPlayerParty().enterSelectionMode();

        if(enemyParty != null)
		    enemyParty.exitSelectionMode();
	}

	//Navigation functions for navigating player and enemy party
	public void navigatePrevPartyMember()
	{
		releaseOrbs();
		PlayerData.GetPlayerParty().prevMember();
	}
	public void navigateNextPartyMember()
	{
		releaseOrbs();
		PlayerData.GetPlayerParty().nextMember();
	}
	public void navigatePrevEnemy()
	{
		enemyParty.prevMember();
	}
	public void navigateNextEnemy()
	{
		enemyParty.nextMember();
	}

	//Navigation functions for navigating player items and skills
	public void navigatePrevSkill()
	{
		getSelectedPartyMember().prevSkill();
	}
	public void navigateNextSkill()
	{
		getSelectedPartyMember().nextSkill();
	}

	public String getPlayerName()
	{
		return PlayerData.GetPlayerParty().getSelectedMember().getName();
	}

	public String getEnemyName()
	{
		return enemyParty.getSelectedMember().getName();
	}

	public PartyMember getSelectedPartyMember()
	{
		return (PartyMember)PlayerData.GetPlayerParty().getSelectedMember();
	}

	public Party getEnemyParty()
	{
		return enemyParty;
	}

	public void handleDrawingOrbs(Graphics g, int x, int y)
	{
		g.setColor(new Color(0, 200, 0));
		for(int i = 0; i < getAmountOfTransformationOrbs(); i++)
		{
			g.fillOval(x, y + (i * 25), 20, 20);
		}
	}

    //Draws the interactions menu: attack, skill, item (Allows for special effects based on status of partymember)
	public void drawInteractions(Graphics g, int x, int y, PartyMember partyMember)
	{
		g.setFont(new Font("Dialog", Font.PLAIN, 25));

		g.setColor(new Color(0, 0, 0));
		g.fillRect(x - 5, y - 5, 210, 170);

		g.setColor(new Color(123, 128, 135));
		g.fillRect(x, y, 200, 50);
		g.fillRect(x, y + 55, 200, 50);
		g.fillRect(x, y + 110, 200, 50);

		if(currentMenu == BattleMenus.main)
		{
			g.setColor(new Color(0, 0, 200));
			switch(selectedInteraction)
			{
				case attack: g.fillRect(x, y, 200, 50); break;
				case skill: g.fillRect(x, y + 55, 200, 50); break;
				case item: g.fillRect(x, y + 110, 200, 50); break;
			}
		}
		g.setColor(new Color(255, 255, 255));
		if(partyMember.canTransform())
		{
			g.drawString("Attack and Transform", x + 40, y + 35);
			g.drawString("Skill and Transform", x + 40, y + 90);
			g.drawString("Item", x + 40, y + 145);
		}else{
			g.drawString("Attack", x + 40, y + 35);
			g.drawString("Skill", x + 40, y + 90);
			g.drawString("Item", x + 40, y + 145);
		}
		if(currentMenu == BattleMenus.skill)
		{
			drawSkillInteractions(g, x + 205, y + 55, partyMember);
		}
	}
	private void drawSkillInteractions(Graphics g, int x, int y, PartyMember partyMember)
	{
		g.setFont(new Font("Dialog", Font.PLAIN, 25));
		g.setColor(new Color(123, 128, 135));
		y -= (partyMember.getSelectedSkillNumber() * 55);
		for(int i = 0; i < partyMember.getSkills().size(); i++)
		{
			g.setColor(new Color(123, 128, 135));
			g.fillRect(x, y + (i * 55), 200, 50);
			if(i == partyMember.getSelectedSkillNumber())
			{
				g.setColor(new Color(0, 0, 200));
				g.fillRect(x, y + (i * 55), 200, 50);
			}
			g.setColor(new Color(255, 255, 255));
			g.drawString(partyMember.getSkills().get(i).getName(), x + 10, y + 35 + (i * 55));
		}
	}

	
}
