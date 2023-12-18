package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Board.Move;
import Pieces.Piece;
import gui.ChessBoardGUI.Log;
import util.BoardTools;
import util.Colour;
;

public class TakenPiecesGUI extends JPanel {

    private final JPanel whiteTakenPanel;
    private final JPanel blackTakenPanel;

    private static final Dimension PANEL_DIMENSION = new Dimension(60, 50);

    public TakenPiecesGUI() {
        super(new BorderLayout());
        setBackground(Color.decode("#BDCFEA")); //light tile colour
        setPreferredSize(PANEL_DIMENSION);
        
        this.whiteTakenPanel = new JPanel(new GridLayout(1, 8));
        this.blackTakenPanel = new JPanel(new GridLayout(1, 8));

        this.whiteTakenPanel.setBackground(Color.decode("#BDCFEA")); 
        this.blackTakenPanel.setBackground(Color.decode("#BDCFEA")); 

        this.add(whiteTakenPanel, BorderLayout.NORTH);
        this.add(blackTakenPanel, BorderLayout.SOUTH);
    }

    public void redraw(final Log moveLog) {
        whiteTakenPanel.removeAll();
        blackTakenPanel.removeAll();
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getInteractedPiece();
                
                if (takenPiece.getColour() == Colour.WHITE) {
                    whiteTakenPieces.add(takenPiece);
                } else if (takenPiece.getColour() == Colour.BLACK) {
                    blackTakenPieces.add(takenPiece);
                };
            }
        }

        //sort pieces by value
        whiteTakenPieces.sort((Piece p1, Piece p2) -> p1.getValue() - p2.getValue());
        blackTakenPieces.sort((Piece p1, Piece p2) -> p1.getValue() - p2.getValue());

        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                final String path = BoardTools.getPiecePath(takenPiece);
                
                final BufferedImage image = ImageIO.read(new File(path));
                Image scaledImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                final JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                
                this.whiteTakenPanel.add(imageLabel);
            } catch (Exception e) {
                System.out.printf("Error loading piece image for taken %s", takenPiece);
            }    
        }
        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final String path = BoardTools.getPiecePath(takenPiece);
                final BufferedImage image = ImageIO.read(new File(path));
                Image scaledImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                final JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                this.blackTakenPanel.add(imageLabel);
            } catch (Exception e) {
                System.out.printf("Error loading piece image for taken %s", takenPiece);
            }
        }
        validate();
    }

    

}