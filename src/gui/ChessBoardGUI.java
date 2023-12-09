package gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import Board.Board;
import Pieces.Piece;
import util.BoardTools;
import util.Colour;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardGUI {
    
    private final JFrame gameFrame;

    private final BoardPanel boardPanel;

    private static final Dimension FRAME_DIMENSIONS = new Dimension(600,600);

    private final Board chessBoard;

    public ChessBoardGUI(final Board board) {
        this.chessBoard = board;
        gameFrame = new JFrame("kChess");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JMenuBar tableMenuBar = createMenuBar();

        gameFrame.setJMenuBar(tableMenuBar);
        gameFrame.setSize(FRAME_DIMENSIONS);

        boardPanel = new BoardPanel(chessBoard);
        gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        gameFrame.setVisible(true);
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

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
        
    }

    private class BoardPanel extends JPanel {
        private static final Dimension BOARD_PANEL_DIMENSIONS = new Dimension(400, 350);
        final List<TilePanel> boardTiles; 
        final Board board;

        BoardPanel(final Board board) {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            this.board = board;
            for(int i=0; i<BoardTools.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, board, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSIONS);
            validate();
        }
    }

    private class TilePanel extends JPanel {
        private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
        private static final Dimension ICON_DIMENSIONS = new Dimension(5, 5);
        private final int tileID;
        private final Color lightTileColour = Color.decode("#BDCFEA");
        private final Color darkTileColour = Color.decode("#6E91AC");

        private final Board board;

        TilePanel(final BoardPanel boardPanel, final Board board, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            this.board = board;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceIcon(board);

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
                }
                
            });

            validate();
        }

        private void assignTilePieceIcon(final Board board) {
            //redraw
            removeAll();
            if(board.getTile(tileID).isOccupied()) {
                final Piece pieceOnTile = board.getTile(tileID).getPiece();
                
                try {
                    final BufferedImage image = ImageIO.read(new File(getPiecePath(pieceOnTile)));
                    Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    JLabel icon = new JLabel(new ImageIcon(scaledImage));
                    icon.setSize(ICON_DIMENSIONS);
                    add(icon);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getPiecePath(final Piece piece) {
            String colourPrefix = (piece.getColour() == Colour.WHITE) ? "w" : "b";
            return "graphics\\" + colourPrefix + "_" + piece.getFullName().toLowerCase() + ".png";
        }

        private void assignTileColour() {
            
            boolean isLightTile = ((tileID + tileID / 8) % 2 == 0);

            setBackground(isLightTile ? lightTileColour : darkTileColour);
        }
    }

}
