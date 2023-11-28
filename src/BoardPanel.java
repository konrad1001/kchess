import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.Console;
import java.io.File;


public class BoardPanel extends JPanel {
    private BufferedImage chessboardImage;

    public BoardPanel() {
        try {
            chessboardImage = ImageIO.read(new File("kchess\\src\\graphics\\8x8.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(chessboardImage, 0, 0, getWidth(), getHeight(), this);
    }

}