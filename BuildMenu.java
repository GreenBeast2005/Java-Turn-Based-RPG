import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class BuildMenu {

    //Since this is a data type used by controller and view I figured making it public statatic is fine
	public static enum SpecificEditMode
	{
		addWall(2, 0)
		{
            @Override
            public Sprite buildSprite(int x, int y)
            {
                return new Wall(getDrawnImage(getSelectedType()), x, y);
            }
			@Override
			public SpecificEditMode prevMode()
			{
				return values()[SpecificEditMode.values().length - 1];
			}
			@Override
			public String getDrawnImage(int selectedType)
			{
				switch(selectedType)
				{
					case 0: return "wall";
					case 1: return "stoneWall";
				}
				return "";
			}	
		},
		removeSprite(1, 0)
		{
			@Override
			public String getDrawnImage(int selectedType)
			{
				return "remove sprite";
			}
		},
		addNPC(3, 0)
		{
            @Override
            public Sprite buildSprite(int x, int y)
            {
                return new TalkingInteractable(getDrawnImage(getSelectedType()), x, y);
            }
			@Override
			public String getDrawnImage(int selectedType)
			{
				switch(selectedType)
				{
					case 0: return "npc";
					case 1: return "npcHero";
					case 2: return "npcRedHairGirl";
				}
				return "";
			}
		},
		addSave(1, 0)
		{
            @Override
            public Sprite buildSprite(int x, int y)
            {
                return new SaveInteractable(getDrawnImage(getSelectedType()), x, y);
            }
			@Override
			public String getDrawnImage(int selectedType)
			{
				switch(selectedType)
				{
					case 0: return "save";
				}
				return "";
			}
		},
		addFloor(1, 0)
		{
            @Override
            public Sprite buildSprite(int x, int y)
            {
                return new FloorSprite(x, y);
            }
			@Override
			public String getDrawnImage(int selectedType)
			{
				switch(selectedType)
				{
					case 0: return "floor";
				}
				return "";
			}
		},
		addEncounter(1, 0)
		{
            @Override
            public Sprite buildSprite(int x, int y)
            {
                return new EncounterTrigger(getDrawnImage(getSelectedType()), x, y);
            }
			@Override
			public SpecificEditMode nextMode() 
			{
				
				return values()[0];
			}
			@Override
			public String getDrawnImage(int selectedType)
			{
				switch(selectedType)
				{
					case 0: return "encounterTrigger";
				}
				return "";
			}
		};

		private int numberOfTypes, selectedType;

		public void nextElement() 
		{
			selectedType++;
			selectedType = selectedType % numberOfTypes;
		}

		public void prevElement()
		{
			selectedType--;
			if(selectedType < 0)
			{
				selectedType = numberOfTypes - 1;
			}
		}

		public SpecificEditMode nextMode() 
		{
			return values()[ordinal() + 1];
		}

		public SpecificEditMode prevMode()
		{
			return values()[ordinal() - 1];
		}

		public int getNextSelectedType()
		{
			int temp = selectedType;
			temp++;
			temp = temp % numberOfTypes;
			return temp;
		}
		public int getPrevSelectedType()
		{
			int temp = selectedType;
			temp --;
			if(temp < 0)
			{
				temp = numberOfTypes - 1;
			}
			return temp;
		}
		public int getSelectedType()
		{
			return selectedType;
		}

		public String getDrawnImage(int selectedType)
		{
			return "";
		}

        public Sprite buildSprite(int x, int y) 
        { 
            return null;
        }

		SpecificEditMode(int numberOfTypes, int selectedType)
		{
			this.numberOfTypes = numberOfTypes;
			this.selectedType = selectedType;
		}
	}

    private SpecificEditMode specificEditMode;

    public BuildMenu()
    {
        specificEditMode = SpecificEditMode.addWall;
    }

    public void nextMode()
    {
        specificEditMode = specificEditMode.nextMode();
    }
    public void prevMode()
    {
        specificEditMode = specificEditMode.prevMode();
    }
    public void nextElement()
    {
        specificEditMode.nextElement();
    }
    public void prevElement()
    {
        specificEditMode.prevElement();
    }

    public Sprite build(int x, int y)
    {
        return specificEditMode.buildSprite(x, y);
    }

	public Boolean isRemoveSprite()
	{
		return specificEditMode == SpecificEditMode.removeSprite;
	}

	public Boolean isFloorSprite()
	{
		return specificEditMode == SpecificEditMode.addFloor;
	}
    
    private void drawDialOfBlocks(Graphics g, SpecificEditMode drawDial, int x, int y, int sizeOfIcons)
	{
		switch(drawDial)
		{
			case addWall: 
				g.drawImage((new Wall(drawDial.getDrawnImage(drawDial.getPrevSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getPrevSelectedType())), x, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new Wall(drawDial.getDrawnImage(drawDial.getSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getSelectedType())), x + sizeOfIcons + 5, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new Wall(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getNextSelectedType())), x + sizeOfIcons*2 + 10, y, sizeOfIcons, sizeOfIcons, null);
			 	break;
			case addEncounter: 
				g.drawImage((new EncounterTrigger(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getPrevSelectedType())), x, y, sizeOfIcons, sizeOfIcons, null); 
				g.drawImage((new EncounterTrigger(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getSelectedType())), x + sizeOfIcons + 5, y, sizeOfIcons, sizeOfIcons, null); 
				g.drawImage((new EncounterTrigger(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getNextSelectedType())), x + sizeOfIcons*2 + 10 , y, sizeOfIcons, sizeOfIcons, null); 
				break;
			case addNPC: 
				g.drawImage((new TalkingInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getPrevSelectedType())), x, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new TalkingInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getSelectedType())), x + sizeOfIcons + 5, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new TalkingInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getNextSelectedType())), x + sizeOfIcons*2 + 10, y, sizeOfIcons, sizeOfIcons, null);
				break;
			case addSave: 
				g.drawImage((new SaveInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getPrevSelectedType())), x, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new SaveInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getSelectedType())), x + sizeOfIcons + 5, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage((new SaveInteractable(drawDial.getDrawnImage(drawDial.getNextSelectedType()))).getDrawnImage(drawDial.getDrawnImage(drawDial.getNextSelectedType())), x + sizeOfIcons*2 + 10, y, sizeOfIcons, sizeOfIcons, null);
				break;
			case addFloor: 
				g.drawImage(new FloorSprite().getDrawnImage(drawDial.getDrawnImage(drawDial.getPrevSelectedType())), x, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage(new FloorSprite().getDrawnImage(drawDial.getDrawnImage(drawDial.getSelectedType())), x + sizeOfIcons + 5, y, sizeOfIcons, sizeOfIcons, null);
				g.drawImage(new FloorSprite().getDrawnImage(drawDial.getDrawnImage(drawDial.getNextSelectedType())), x + sizeOfIcons*2 + 10, y, sizeOfIcons, sizeOfIcons, null);
				break;
		}
	}
	public void draw(Graphics g, int x, int y)
	{
		final int sizeOfIcons = 80;
		g.setFont(new Font("Dialog", Font.BOLD, 30));
		g.setColor(new Color(125, 125, 125, 100));
		g.fillRect(x, y, (sizeOfIcons * 3) + 20, (sizeOfIcons) + 10);
		g.fillRect(x, y - sizeOfIcons - 10, (sizeOfIcons * 3) + 20, (sizeOfIcons * 3) + 30);
		g.setColor(new Color(0, 125, 0, 100));
		g.fillRect(x + (sizeOfIcons) + 5, y, (sizeOfIcons) + 10, (sizeOfIcons) + 10);
		drawDialOfBlocks(g, specificEditMode, x + 5, y + 5, sizeOfIcons);
		drawDialOfBlocks(g, specificEditMode.prevMode(), x + 5, y - (sizeOfIcons) - 5, sizeOfIcons);
		drawDialOfBlocks(g, specificEditMode.nextMode(), x + 5, y + (sizeOfIcons) + 15, sizeOfIcons);
		

		g.setColor(new Color(255, 255, 255));
		g.drawString(specificEditMode.getDrawnImage(specificEditMode.getSelectedType()), x + (sizeOfIcons) + 20, y + (sizeOfIcons) + 20);
	}
}
