import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DialogueBox {
    private ArrayList<String> messages;
    private ArrayList<BufferedImage> personSpeakingInMessage;
    private int currentMessagePosition;
    //private int x, y;

    public DialogueBox()
    {
        messages = new ArrayList<String>();
        personSpeakingInMessage = new ArrayList<BufferedImage>();
        currentMessagePosition = 0;
    }

    public boolean isDialogue()
    {
        return !messages.isEmpty();
    }

    public void addMessage(String message)
    {
        messages.add(wrap(message, 65));
        personSpeakingInMessage.add(null);
        //System.out.println(message);
    }
    public void addMessage(String message, TalkingCharacter personTalking)
    {
        messages.add(wrap(message, 65));
        personSpeakingInMessage.add(personTalking.getTalkingImage());
        //System.out.println(message);
    }

    public String wrap(String string, int lineLength) {
        StringBuilder b = new StringBuilder();
        for (String line : string.split(Pattern.quote("\n"))) {
            b.append(wrapLine(line, lineLength));
        }
        //System.out.println("Test");
        //System.out.println(b.toString());
        return b.toString();
    }
    private String wrapLine(String line, int lineLength) {
        if (line.length() == 0) return "\n";
        if (line.length() <= lineLength) return line + "\n";
        String[] words = line.split(" ");
        StringBuilder allLines = new StringBuilder();
        StringBuilder trimmedLine = new StringBuilder();
        for (String word : words) {
            if (trimmedLine.length() + 1 + word.length() <= lineLength) {
                trimmedLine.append(word).append(" ");
            } else {
                allLines.append(trimmedLine).append("\n");
                trimmedLine = new StringBuilder();
                trimmedLine.append(word).append(" ");
            }
        }
        if (trimmedLine.length() > 0) {
            allLines.append(trimmedLine);
        }
        allLines.append("\n");
        return allLines.toString();
    }

    public boolean nextMessage()
    {
        if(currentMessagePosition < messages.size() - 1)
        {
            currentMessagePosition++;
            return true;
        }
        return false;
    }

    public void endDialogue()
    {
        messages = new ArrayList<String>();
        personSpeakingInMessage = new ArrayList<BufferedImage>();
        currentMessagePosition = 0;
    }

    public void draw(Graphics g)
    {
        if(!messages.isEmpty())
        {
            g.setFont(new Font("Dialog", Font.BOLD, 50));

            g.setColor(new Color(0, 0, 0));
            g.fillRect(145, 845, 1630, 210);

            g.setColor(new Color(255, 255, 255));
            g.fillRect(150, 850, 1620, 200);
            
            g.setColor(new Color(0, 0, 0));
            View.DrawCorrectlyFormattedString(g, messages.get(currentMessagePosition), 155, 840);
            if(personSpeakingInMessage.get(currentMessagePosition) != null)
            {
                g.drawImage(personSpeakingInMessage.get(currentMessagePosition), 170, 650, 200, 200, null);
            }
        }
    }
}
