import javax.swing.*;
import java.awt.*;

public class LoadingScreenView extends JPanel {
    private JLabel loadingLabel;

    public LoadingScreenView(Controller c) {
        setSize(1920, 1080);
        
        setLayout(new BorderLayout());

        // Create a label to display loading message
        loadingLabel = new JLabel("Loading, please wait...", JLabel.CENTER);
        add(loadingLabel, BorderLayout.CENTER);

        c.setLoadingView(this);
    }

    public void showLoadingScreen() {
        setVisible(true);
    }

    public void hideLoadingScreen() {
        setVisible(false);
    }
}
