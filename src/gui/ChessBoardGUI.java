package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.SwingUtilities.*;

import Board.Board;
import Board.Move;
import Board.MoveStatus;
import Board.MoveTransition;
import Board.Tile;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChessBoardGUI {
    
    private final JFrame gameFrame;

    private final BoardPanel boardPanel;

    private static final Dimension FRAME_DIMENSIONS = new Dimension(600,600);

    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;

    private Piece playerMovedPiece;

    private BoardDirection boardDirection;

    private Boolean holdingPiece;

    

    public ChessBoardGUI(final Board board) {
        this.chessBoard = board;
        this.boardDirection = BoardDirection.NORMAL;
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
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard();
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        return preferencesMenu;
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
     //   final Board board;

        BoardPanel(final Board board) {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
        //    this.board = board;
            for(int i=0; i<BoardTools.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSIONS);
            validate();
        }

        public void drawBoard() {
            removeAll();
            for (final TilePanel tile : boardDirection.traverse(boardTiles)) {
                
                tile.drawTile();
                add(tile);
                tile.deselect();
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
        private static final Dimension ICON_DIMENSIONS = new Dimension(5, 5);
        private final int tileID;
        private final Color lightTileColour = Color.decode("#BDCFEA");
        private final Color darkTileColour = Color.decode("#6E91AC");
        private final Color selectedLightTileColour = Color.decode("#EAD8BD");
        private final Color selectedDarkTileColour = Color.decode("#AC896E");
        
        private final Boolean isLightTile;
        private Boolean isSelected;

        

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            this.isLightTile = ((tileID + tileID / 8) % 2 == 0);
            this.isSelected = false;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceIcon();

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)) {
                        //right click will clear
                        sourceTile = null;
                        destinationTile = null;
                        playerMovedPiece = null;
                        isSelected = false;
                    } else if (isLeftMouseButton(e)) {
                        
                        //left click will select
                        isSelected = true;
                        if (sourceTile == null) { //if we have nothing selected
                            sourceTile = chessBoard.getTile(tileID);
                            playerMovedPiece = sourceTile.getPiece();
                            if (playerMovedPiece == null) {
                                sourceTile = null;
                                isSelected = false;
                            }
                        } else { //we have something already selected, next selection will be a destination tile
                            destinationTile = chessBoard.getTile(tileID);
                            isSelected = false;
                            if (destinationTile.isOccupied()) {//if we have selected our own colour, reselect.
                                if (destinationTile.getPiece().getColour() == playerMovedPiece.getColour()) {
                                    sourceTile = chessBoard.getTile(tileID);
                                    playerMovedPiece = sourceTile.getPiece();
                                    if (playerMovedPiece == null) {
                                        sourceTile = null;
                                        isSelected = false;
                                    }
                                } else {
                                    final Move move = Move.Constructor.createMove(chessBoard, sourceTile.getCoordinates(), destinationTile.getCoordinates());
                                
                                    final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                                    if (transition.getMoveStatus() == MoveStatus.DONE) {
                                        chessBoard = transition.getBoard();
                                    }
                                    sourceTile = null;
                                    destinationTile = null;
                                    playerMovedPiece = null;
                                }
                            } else {
                                final Move move = Move.Constructor.createMove(chessBoard, sourceTile.getCoordinates(), destinationTile.getCoordinates());
                                
                                final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                                if (transition.getMoveStatus() == MoveStatus.DONE) {
                                    chessBoard = transition.getBoard();
                                }
                                sourceTile = null;
                                destinationTile = null;
                                playerMovedPiece = null;
                            }
                        }
                        
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                boardPanel.drawBoard();
                            }

                        });
                    }
                    

                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
                
            });

            validate();
        }

        public void deselect() {
            isSelected = false;
        }

        private void drawLegalMoves(final Board board) {
            if (isSelected) {
                if (isLightTile) {
                    setBackground(selectedLightTileColour);
                } else {
                    setBackground(selectedDarkTileColour);
                }
            } 
            for (final Move move : pieceLegalMoves(board)) {
                if (move.getDestinationCoordinates() == this.tileID) {
                    if (isLightTile) {
                        setBackground(selectedLightTileColour);
                    } else {
                        setBackground(selectedDarkTileColour);
                    }
                }
            }
            
        }

        private Collection<Move> pieceLegalMoves(Board board) {
            if (playerMovedPiece != null && playerMovedPiece.getColour() == board.getCurrentPlayer().colour()) {
                return playerMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        public void drawTile() {
            assignTileColour();
            assignTilePieceIcon();
            drawLegalMoves(chessBoard);
            validate();
        }

        private void assignTilePieceIcon() {
            //redraw
            removeAll();
            if(chessBoard.getTile(tileID).isOccupied()) {
                final Piece pieceOnTile = chessBoard.getTile(tileID).getPiece();
                
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
            
        //    boolean isLightTile = ((tileID + tileID / 8) % 2 == 0);

            setBackground(isLightTile ? lightTileColour : darkTileColour);
        }
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

        List<TilePanel> reverse(final List<TilePanel> boardTiles) {
            List<TilePanel> reversedList = new ArrayList<>(boardTiles);
            Collections.reverse(reversedList);
            return reversedList;
        }
    }

}
