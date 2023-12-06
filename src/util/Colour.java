package util;

import Player.Player;

public enum Colour {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public Player choosePlayer(Player white, Player black) {
            return white;
        }

        @Override
        public Colour Opposite() {
            return BLACK;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public Player choosePlayer(Player white, Player black) {
            return black;
        }

        @Override
        public Colour Opposite() {
            return WHITE;
        }
    };

    public abstract int getDirection();
    public abstract Player choosePlayer(Player white, Player black);
    public abstract Colour Opposite();
    }

