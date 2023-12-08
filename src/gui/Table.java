package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table {
    
    private final JFrame gameFrame;

    private static final Dimension FRAME_DIMENSIONS = new Dimension(600,600);

    public Table() {
        this.gameFrame = new JFrame("kChess");

        
        this.gameFrame.setSize(FRAME_DIMENSIONS);
        this.gameFrame.setVisible(true);
    }

    

}
