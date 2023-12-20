package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Board.Board;

public class EndMenuGUI extends JDialog{

    private JPanel panel;

    private JLabel message;

    private JButton backButton;
    private JButton exitButton;

    private Board board;

    public EndMenuGUI(String winner, Board board) {
        super();
        // this.panel = new JPanel();
        // this.add(panel);
        setSize(400, 400);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String message = winner + " wins!";
        this.board = board;

        this.message = new JLabel(message);
        this.message.setSize(100, 100);

        backButton = new JButton("Back");
        exitButton = new JButton("Exit");
        backButton.setSize(50, 50);
        exitButton.setSize(50, 50); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.setSize(50,50);
        buttonPanel.add(backButton, BorderLayout.WEST);
        buttonPanel.add(exitButton, BorderLayout.EAST);

        add(Box.createHorizontalStrut(10));
        add(this.message);
        add(Box.createHorizontalStrut(10));
        add(buttonPanel);

        backButton.addActionListener(e -> {
            setVisible(false);
            dispose();
            
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });


    }
}