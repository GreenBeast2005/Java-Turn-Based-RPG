import java.awt.Graphics;

public class UniversalUI {
    private static boolean takingInput = false;

    private static DialogueBox dialogueBox = new DialogueBox();

    public static void Draw(Graphics g)
    {
        dialogueBox.draw(g);
    }

    public static void InputUse()
    {
		if(!dialogueBox.nextMessage())
		{
			dialogueBox.endDialogue();
            takingInput = false;
		}
    }

    public static boolean isTakingInput()
    {
        return takingInput;
    }

    public static void addMessageToDialogue(String message)
    {
        dialogueBox.addMessage(message);
        takingInput = true;
    }

    public static void addMessageToDialogue(String message, TalkingCharacter talkingCharacter)
    {
        dialogueBox.addMessage(message, talkingCharacter);
        takingInput = true;
    }
}
