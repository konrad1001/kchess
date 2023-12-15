package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Board.Move;
import Pieces.Piece;
import gui.ChessBoardGUI.Log;
import util.BoardTools;
import util.Colour;

import static gui.ChessBoardGUI.*;

public class TakenPiecesGUI extends JPanel {

    private final JPanel whiteTakenPanel;
    private final JPanel blackTakenPanel;

    private static final Dimension PANEL_DIMENSION = new Dimension(40, 80);

    public TakenPiecesGUI() {
        super(new BorderLayout());
        setBackground(java.awt.Color.decode("#FFFACD"));
        
        this.whiteTakenPanel = new JPanel(new GridLayout(8, 2));
        this.blackTakenPanel = new JPanel(new GridLayout(8, 2));
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
                final ImageIcon icon = new ImageIcon(path);
                final JLabel imageLabel = new JLabel(icon);
                this.whiteTakenPanel.add(imageLabel);
            } catch (Exception e) {
                System.out.printf("Error loading piece image for taken %s", takenPiece);
            }    
        }
        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final String path = BoardTools.getPiecePath(takenPiece);
                final ImageIcon icon = new ImageIcon(path);
                final JLabel imageLabel = new JLabel(icon);
                this.whiteTakenPanel.add(imageLabel);
            } catch (Exception e) {
                System.out.printf("Error loading piece image for taken %s", takenPiece);
            }
        }
        validate();
    }

    

}