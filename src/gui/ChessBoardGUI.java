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
import Pieces.Piece.Name;
import util.BoardTools;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;



public class ChessBoardGUI {
    
    private final JFrame gameFrame;

    private final BoardPanel boardPanel;

    private static final Dimension FRAME_DIMENSIONS = new Dimension(600,650);

    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;

    private Piece playerMovedPiece;

    private BoardDirection boardDirection;

    private TakenPiecesGUI takenPiecesGUI;

    private MoveListener chessEngine;

    private final Log moveLog = new Log();

    private Boolean debugOptions;
    private Boolean isActiveGame;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    

    public ChessBoardGUI(final Board board) {
        this.chessBoard = board;
        this.boardDirection = BoardDirection.NORMAL;
        this.debugOptions = false;
        this.isActiveGame = true;
        this.chessEngine = new MoveListener();
        addPropertyChangeListener(chessEngine);

        gameFrame = new JFrame("kChess");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JMenuBar tableMenuBar = createMenuBar();

        gameFrame.setJMenuBar(tableMenuBar);
        gameFrame.setSize(FRAME_DIMENSIONS);

        this.takenPiecesGUI = new TakenPiecesGUI();
        this.boardPanel = new BoardPanel(chessBoard);

        

        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.add(takenPiecesGUI, BorderLayout.SOUTH);

        gameFrame.setVisible(true);

        notifyGameStart();

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void notifyMove(Move move) {
        pcs.firePropertyChange("move", null, move);
    }

    public void notifyOpponentHasMoved() {
        pcs.firePropertyChange("opponent_has_moved", null, null);
    }

    public void notifyGameStart() {
        pcs.firePropertyChange("game_start", null, null);
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
        final JMenuItem debugToolsMenuItem = new JMenuItem("Debug Tools");
        debugToolsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debugOptions = !debugOptions;
                boardPanel.drawBoard();
            }
        });
        preferencesMenu.add(debugToolsMenuItem);
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

        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard = Board.create();
                boardPanel.drawBoard();
                moveLog.clear();
                takenPiecesGUI.redraw(moveLog);
                isActiveGame = true;
            }
        });
        fileMenu.add(newGameMenuItem);

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

    private class MoveListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("move".equals(evt.getPropertyName())) {
                if (chessBoard.getCurrentPlayer().isHuman()) {
                    Move move = (Move) evt.getNewValue();
                    final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);

                    if (transition.getMoveStatus() == MoveStatus.DONE) {
                        chessBoard = transition.getBoard();
                        moveLog.addMove(move);
                        takenPiecesGUI.redraw(moveLog);
                    }
                    sourceTile = null;
                    destinationTile = null;
                    playerMovedPiece = null;
                    boardPanel.drawBoard();
                    System.out.println("drawn");
                    notifyOpponentHasMoved();
                }
                
                
              //  checkEndgame();
            } else if ("opponent_has_moved".equals(evt.getPropertyName())) {
                
                if (!chessBoard.getCurrentPlayer().isHuman() && isActiveGame) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    final Move move = chessBoard.getCurrentPlayer().promptMove();
                    final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                    
                    System.out.println(move);
                
                    if (transition.getMoveStatus() == MoveStatus.DONE) {
                        chessBoard = transition.getBoard();
                        moveLog.addMove(move);
                        takenPiecesGUI.redraw(moveLog);
                    }
                    checkEndgame();
                    boardPanel.drawBoard();
                    notifyOpponentHasMoved();
                    
                } 
            } else if ("game_start".equals(evt.getPropertyName())) {
                if (!chessBoard.getCurrentPlayer().isHuman() && isActiveGame) {
                    final Move move = chessBoard.getCurrentPlayer().promptMove();
                    final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                    
                    System.out.println(move);
                
                    if (transition.getMoveStatus() == MoveStatus.DONE) {
                        chessBoard = transition.getBoard();
                        moveLog.addMove(move);
                        takenPiecesGUI.redraw(moveLog);
                    }
                    checkEndgame();
                    notifyOpponentHasMoved();
                }
            }
        }

        public void checkEndgame() {
            if (chessBoard.getCurrentPlayer().isInCheckmate()) {
                isActiveGame = false;
                System.out.println("Checkmate!");
                JDialog endMenuGUI = new EndMenuGUI(chessBoard.getCurrentPlayer().colour().Opposite().toString() + " wins!", chessBoard);
                endMenuGUI.setVisible(true);
            } else if (chessBoard.getCurrentPlayer().isInStalemate()) {
                isActiveGame = false;
                System.out.println("Stalemate!");
                JDialog endMenuGUI = new EndMenuGUI(chessBoard.getCurrentPlayer().colour().toString() + " is in Stalemate!", chessBoard);
                endMenuGUI.setVisible(true);
            }
        }

    }

    private class BoardPanel extends JPanel {
        private static final Dimension BOARD_PANEL_DIMENSIONS = new Dimension(400, 350);
        final List<TilePanel> boardTiles; 

        BoardPanel(final Board board) {
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

        public void drawBoard(int tileID) {
            
            for (final TilePanel tile : boardDirection.traverse(boardTiles)) {
                if (tile.tileID == tileID) {
                    tile.drawTile();
                    add(tile);
                    tile.deselect();
                }
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
                    
                    if (isActiveGame && chessBoard.getCurrentPlayer().isHuman()) {
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
                                    isSelected = true;
                                    sourceTile = chessBoard.getTile(tileID);
                                    playerMovedPiece = sourceTile.getPiece();
                                    if (playerMovedPiece == null) {
                                        sourceTile = null;
                                        isSelected = false;
                                    }
                                } else {
                                    final Move move = Move.Constructor.createMove(chessBoard, sourceTile.getCoordinates(), destinationTile.getCoordinates());
                                 //   executeMove(move);
                                    notifyMove(move);
                                   
                                }
                            } else {
                                final Move move = Move.Constructor.createMove(chessBoard, sourceTile.getCoordinates(), destinationTile.getCoordinates());
                                //executeMove(move);
                                notifyMove(move);
                            }
                        }
                        
                        
                        
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                boardPanel.drawBoard();
                                //runTurn();
                            }

                        });
                    }
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

        
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

        public void notifyMove(Move move) {
            pcs.firePropertyChange("move", null, move);
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
                if (move.getDestinationCoordinate() == this.tileID) {
                    if (isLightTile) {
                        setBackground(selectedLightTileColour);
                    } else {
                        setBackground(selectedDarkTileColour);
                    }
                }
            
            }
            
        }

        private Collection<Move> pieceLegalMoves(Board board) {
            List<Move> legalMoves = new ArrayList<>();         
            if (playerMovedPiece != null && playerMovedPiece.getColour() == board.getCurrentPlayer().colour()) {
                legalMoves = playerMovedPiece.calculateLegalMoves(board);
                if(playerMovedPiece.getName() == Name.KING) {
                    legalMoves.addAll(board.getPlayer(playerMovedPiece.getColour()).getKingCastles());
                }
                
            }
            return Collections.unmodifiableCollection(legalMoves);
        }

        public void drawTile() {
            assignTileColour();
            assignTilePieceIcon();
            drawLegalMoves(chessBoard);

            
            if (debugOptions) {
                JLabel Tile = new JLabel(Integer.toString(tileID));
                Tile.setSize(ICON_DIMENSIONS);
                add(Tile);
            }

            validate();
        }

        private void assignTilePieceIcon() {
            //redraw
            removeAll();
            if(chessBoard.getTile(tileID).isOccupied()) {
                final Piece pieceOnTile = chessBoard.getTile(tileID).getPiece();
                
                try {
                    final BufferedImage image = ImageIO.read(new File(BoardTools.getPiecePath(pieceOnTile)));
                    Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    JLabel icon = new JLabel(new ImageIcon(scaledImage));
                    icon.setSize(ICON_DIMENSIONS);
                    add(icon);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        

        private void assignTileColour() {
            setBackground(isLightTile ? lightTileColour : darkTileColour);
        }
    }

    public static class Log {
        private final List<Move> moves;

        public Log() {
            this.moves = new ArrayList<>();
        }

        public  void addMove(final Move move) {
            if (move != null) {
                moves.add(move);
            }   
        }

        public void clear() {
            moves.clear();
        }

        public List<Move> getMoves() {
            return moves;
        }

        @Override
        public String toString() {
            return moves.toString();
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
