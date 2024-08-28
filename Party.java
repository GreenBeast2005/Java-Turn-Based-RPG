import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Party {
    private ArrayList<Creature> partyMembers;
    private ArrayList<Boolean> hadTurn;

    private int selectedPartyMember;

    private boolean selectionMode;

    public Party()
    {
        partyMembers = new ArrayList<Creature>();
        hadTurn = new ArrayList<Boolean>();
        selectedPartyMember = 0;
        selectionMode = false;
    }
    public Party(Json ob)
    {
        this();
        Json partyList = ob.get("partyMembers");
        Json partyTypeList = ob.get("partyTypes");
        for(int i = 0; i < partyList.size(); i++)
        {
            switch(partyTypeList.get(i).getString("type"))
            {
                case "enemy" : addPartyMember(new Enemy(partyList.get(i))); break;
                case "partyMember" : addPartyMember(new PartyMember(partyList.get(i))); break;
            }
        }
    }

    public void update()
    {
        Iterator<Creature> partyIter = partyMembers.iterator();
        while(partyIter.hasNext())
        {
            partyIter.next().update();
        }
        if(!getEntirePartyDead() && partyMembers.get(selectedPartyMember).getIsDead())
        {
            nextMember();
        }
    }

    public Json marshal()
    {
        Json ob = Json.newObject();
        Json partyList = Json.newList();
        Json partyTypeList = Json.newList();
        for(int i = 0; i < partyMembers.size(); i++)
        {
            Json ob2 = Json.newObject();
            partyList.add(partyMembers.get(i).marshal());
            if(partyMembers.get(i).isEnemy())
                ob2.add("type", "enemy");
            if(partyMembers.get(i).isPartyMember())
                ob2.add("type", "partyMember");
            partyTypeList.add(ob2);
        }
        ob.add("partyMembers", partyList);
        ob.add("partyTypes", partyTypeList);
        return ob;
    }

    public boolean entirePartyHasTakenTurn()
    {
        Iterator<Boolean> turnsResetIter = hadTurn.iterator();
        boolean clearTurns = true;
        while(turnsResetIter.hasNext())
        {
            if(!turnsResetIter.next())
            {
                clearTurns = false;
            }
        }
        return clearTurns;
    }

    public void resetTurns()
    {
        for(int i = 0; i < hadTurn.size(); i++)
        {
            hadTurn.set(i, false);
        }
    }

    public void handleHavingTurn()
    {
        hadTurn.set(selectedPartyMember, true);
        nextMember();
    }

    public void addPartyMember(Creature partyMember)
    {
        partyMembers.add(partyMember);
        hadTurn.add(false);
    }

    public void nextMember()
    {
        do
        {
            selectedPartyMember++;
            selectedPartyMember = selectedPartyMember % partyMembers.size();
        }while(hadTurn.get(selectedPartyMember) || partyMembers.get(selectedPartyMember).getIsDead());
    }

    public void prevMember()
    {
        do
        {
            selectedPartyMember--;
            selectedPartyMember = selectedPartyMember < 0 ? partyMembers.size() - 1: selectedPartyMember;
        }while(hadTurn.get(selectedPartyMember) || partyMembers.get(selectedPartyMember).getIsDead());
    }

    public Creature getSelectedMember()
    {
        if(selectedPartyMember < partyMembers.size() && selectedPartyMember > -1)
            return partyMembers.get(selectedPartyMember);
        return new PartyMember("sonic", "default", 0, 0, 0, 0, 0, 0, Type.DamageType.blunt);
    }
    public boolean getEntirePartyDead()
    {
        boolean isDead = true;
        Iterator<Creature> deathIter = partyMembers.iterator();
        while(deathIter.hasNext())
        {
            if(!deathIter.next().getIsDead())
            {
                isDead = false;
            }
        }
        return isDead;
    }

    //These are for drawing the controls on the correct party member
    public int getXOfCurrentPartyMember(int partyX)
    {
        return partyX + 200 + (selectedPartyMember * 350);
    }
    public int getYOfCurrentPartyMember(int partyY)
    {
        return partyY + 80 + (selectedPartyMember * 30);
    }


    public void enterSelectionMode()
    {
        selectionMode = true;
    }

    public void exitSelectionMode()
    {
        selectionMode = false;
    }

    public void drawYourself(Graphics g, int x, int y)
    {
        for(int i = 0; i < partyMembers.size(); i++)
        {
            if(selectionMode && i == selectedPartyMember)
            {
                g.setColor(new Color(200, 0, 200));
                g.fillRect(x - 5 + (i * 350), y - 5 + (i * 30), 260, 70);
            }
            partyMembers.get(i).drawYourself(g, x + (i * 350), y + (i * 30));
        }
    }

}
