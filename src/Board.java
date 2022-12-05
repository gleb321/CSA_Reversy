import java.util.ArrayList;

public class Board {
    public static final int height = 8;
    public static final int width = 8;
    public static final int firstPlayerFieldCode = -1;
    public static final int secondPlayerFieldCode = 1;

    public Board() {
        int middleHeight = height / 2;
        int middleWidth = width / 2;
        board = new int[height][width];
        board[middleHeight][middleWidth - 1] = board[middleHeight - 1][middleWidth] = firstPlayerFieldCode;
        board[middleHeight][middleWidth] = board[middleHeight - 1][middleWidth - 1] = secondPlayerFieldCode;
        firstPlayerBoard = getFieldCopy(board);
        secondPlayerBoard = getFieldCopy(board);
    }

    public void print(int playerCode) {
        char symbol;
        Color color;
        System.out.print("   ");
        for (int i = 0; i < width; ++i) {
            System.out.printf("%s[%s%c%s]", Color.GREEN, Color.GREEN, 'A' + i, Color.GREEN);
        }

        System.out.println();
        for (int i = 0; i < height; ++i) {
            System.out.printf("%s[%s%d%s]", Color.GREEN, Color.GREEN, i + 1, Color.GREEN);
            for (int j = 0; j < width; ++j) {
                symbol = '#';
                color = Color.WHITE;
                switch (board[i][j]) {
                    case firstPlayerFieldCode -> color = Color.RED;
                    case secondPlayerFieldCode -> color = Color.BLUE;
                    case availableFieldCode -> {
                        symbol = '?';
                        color = playerCode == firstPlayerFieldCode ? Color.RED : Color.BLUE;
                    }
                    default -> symbol = ' ';
                }

                System.out.printf("%s[%s%c%s]", Color.WHITE, color, symbol, Color.WHITE);
            }

            System.out.println();
        }
    }

    public void printPlayersScore() {
        System.out.println("\t\t\tSCORE");
        System.out.printf("\t%sRED%s: %d\t", Color.RED, Color.WHITE, getPlayerScore(firstPlayerFieldCode));
        System.out.printf("\t  %sBLUE%s: %d\n", Color.BLUE, Color.WHITE, getPlayerScore(secondPlayerFieldCode));
    }

    public boolean prepareForMove(int playerCode) {
        boolean moveIsPossible = false;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (board[i][j] == playerCode || board[i][j] == getOpponentCode(playerCode)) {
                    continue;
                }

                if (isAvailableToPlace(playerCode, getOpponentCode(playerCode), i, j)) {
                    board[i][j] = availableFieldCode;
                    moveIsPossible = true;
                } else {
                    board[i][j] = emptyFieldCode;
                }
            }
        }

        return moveIsPossible;
    }

    public boolean tryPlaceMove(int playerCode, int i, int j) {
        if (i < 0 || i >= height || j < 0 || j >= width) {
            return false;
        }

        if (board[i][j] != availableFieldCode) {
            return false;
        }

        if (playerCode == firstPlayerFieldCode) {
            firstPlayerBoard = getFieldCopy(board);
        } else {
            secondPlayerBoard = getFieldCopy(board);
        }

        board[i][j] = playerCode;
        recolourBoard(playerCode, getOpponentCode(playerCode), i, j);
        return true;
    }

    public ArrayList<int[]> getAvailableMoves() {
        ArrayList<int[]> availableMoves = new ArrayList<int[]>(4);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (board[i][j] == availableFieldCode) {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }

        return availableMoves;
    }

    public int getNumberOfRecolouredFigures(int playerCode, int i, int j) {
        if (!isAvailableToPlace(playerCode, getOpponentCode(playerCode), i, j)) {
            return 0;
        }

        int counter = 0;
        for (var coord : coords) {
            if (i + coord[0] >= 0 && i + coord[0] < height && j + coord[1] >= 0 && j + coord[1] < width) {
                counter += getNumberOfOpponentsFiguresInLine(playerCode, getOpponentCode(playerCode), i, j, coord);
            }
        }

        return counter;
    }

    public int getPlayerScore(int playerCode) {
        int counter = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (board[i][j] == playerCode) {
                    ++counter;
                }
            }
        }

        return counter;
    }

    public void undoMove(int playerCode) {
        if (playerCode == firstPlayerFieldCode) {
            board = getFieldCopy(firstPlayerBoard);
        } else {
            board = getFieldCopy(secondPlayerBoard);
        }
    }

    public int getOpponentCode(int playerCode) {
        return playerCode == firstPlayerFieldCode ? secondPlayerFieldCode : firstPlayerFieldCode;
    }

    private static final int emptyFieldCode = 0;
    private static final int availableFieldCode = 2;
    final private int[][] coords = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 1}};

    private int[][] board;
    private int[][] firstPlayerBoard;
    private int[][] secondPlayerBoard;

    private boolean isAvailableToPlace(int playerCode, int opponentCode, int i, int j) {
        for (var coord : coords) {
            if (i + coord[0] >= 0 && i + coord[0] < height && j + coord[1] >= 0 && j + coord[1] < width) {
                if (board[i + coord[0]][j + coord[1]] == opponentCode) {
                    if (getNumberOfOpponentsFiguresInLine(playerCode, opponentCode, i, j, coord) != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int getNumberOfOpponentsFiguresInLine(int playerCode, int opponentCode, int i, int j, int[] coord) {
        int counter = 0;
        while (i + coord[0] >= 0 && i + coord[0] < height && j + coord[1] >= 0 && j + coord[1] < width) {
            i += coord[0];
            j += coord[1];
            if (board[i][j] == opponentCode) {
                ++counter;
                continue;
            }

            if (board[i][j] == playerCode) {
                return counter;
            } else {
                return 0;
            }
        }

        return 0;
    }

    private void recolourBoard(int playerCode, int opponentCode, int i, int j) {
        for (var coord : coords) {
            if (i + coord[0] >= 0 && i + coord[0] < height && j + coord[1] >= 0 && j + coord[1] < width) {
                if (board[i + coord[0]][j + coord[1]] == opponentCode) {
                    if (getNumberOfOpponentsFiguresInLine(playerCode, opponentCode, i, j, coord) != 0) {
                        recolourLine(playerCode, opponentCode, i, j, coord);
                    }
                }
            }
        }
    }

    private void recolourLine(int playerCode, int opponentCode, int i, int j, int[] coord) {
        while (i + coord[0] >= 0 && i + coord[0] < height && j + coord[1] >= 0 && j + coord[1] < width) {
            i += coord[0];
            j += coord[1];
            if (board[i][j] == playerCode) {
                return;
            }

            if (board[i][j] == opponentCode) {
                board[i][j] = playerCode;
            }
        }
    }

    private int[][] getFieldCopy(int[][] boardToCopy) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                copy[i][j] = boardToCopy[i][j];
            }
        }

        return copy;
    }
}
