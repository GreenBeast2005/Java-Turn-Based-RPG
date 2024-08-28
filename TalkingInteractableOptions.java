import java.util.ArrayList;

import javax.swing.*;

public class TalkingInteractableOptions extends JDialog {
    private JTextField textField;
    private JButton nextButton;
    private JButton finishButton;

    private ArrayList<String> messages;

    public TalkingInteractableOptions(JFrame parent) {
        super(parent, "Set Parameters", true);

        messages = new ArrayList<String>();

        textField = new JTextField(20);
        nextButton = new JButton("Next");
        finishButton = new JButton("Finish");

        nextButton.addActionListener(e -> {
            messages.add(textField.getText());
            textField.setText(""); // Clear text field
        });

        finishButton.addActionListener(e -> {
            messages.add(textField.getText());
            dispose();
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Message:"));
        panel.add(textField);
        panel.add(nextButton);
        panel.add(finishButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public ArrayList<String> getMessages() 
    {
        return messages;
    }
}
