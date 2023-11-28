import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private JPanel boardPanel;
    

    public GUI() {
        frame = new JFrame("kchess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new BoardPanel();
        frame.add(boardPanel);

            frame.setSize(500, 500);
            frame.setVisible(true);

        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }


}
