package gui;

import javax.swing.*;

import util.BoardTools;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table {
    
    private final JFrame gameFrame;

    private final BoardPanel boardPanel;

    private static final Dimension FRAME_DIMENSIONS = new Dimension(600,600);

    public Table() {
        this.gameFrame = new JFrame("kChess");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar tableMenuBar = createMenuBar();

        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(FRAME_DIMENSIONS);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open pgn");
            }
        });
        fileMenu.add(openPGN);
        return fileMenu;
        
    }

    private class BoardPanel extends JPanel {
        private static final Dimension BOARD_PANEL_DIMENSIONS = new Dimension(400, 350);
        final List<TilePanel> boardTiles; 

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for(int i=0; i<BoardTools.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSIONS);
            validate();
        }
    }

    private class TilePanel extends JPanel {
        private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
        private final int tileID;
        private final Color lightTileColour = Color.decode("#F0D9B7");
        private final Color darkTileColour = Color.decode("#B48866");

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            validate();
        }

        private void assignTileColour() {
            
            boolean isLightTile = ((tileID + tileID / 8) % 2 == 0);

            setBackground(isLightTile ? lightTileColour : darkTileColour);
        }
    }

}
