import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private Piece[][] board = new Piece[8][8];
    private boolean isPlayer1Turn = true;
    private JButton selectedButton = null;
    private boolean gameOver = false;

    public ImageIcon getImage(String symbol) {
        switch (symbol) {
            case "P":
                return new ImageIcon("PieceImages/WPawn.png");
            case "p":
                return new ImageIcon("PieceImages/BPawn.png");
            case "B":
                return new ImageIcon("PieceImages/WBishop.png");
            case "b":
                return new ImageIcon("PieceImages/BBishop.png");
            case "R":
                return new ImageIcon("PieceImages/WRook.png");
            case "r":
                return new ImageIcon("PieceImages/BRook.png");
            case "K":
                return new ImageIcon("PieceImages/WKing.png");
            case "k":
                return new ImageIcon("PieceImages/BKing.png");
            case "Q":
                return new ImageIcon("PieceImages/WQueen.png");
            case "q":
                return new ImageIcon("PieceImages/BQueen.png");
            case "N":
                return new ImageIcon("PieceImages/WKnight.png");
            case "n":
                return new ImageIcon("PieceImages/BKnight.png");
        }
        return null;

    }

    public ChessGame() {
        initializeBoard();
        initializeUI();
    }

    private class Piece {
        private String symbol;
        private boolean isPlayer1;
        private boolean hasMoved;
        private String imagePath;
        private ImageIcon image;

        public Piece(String symbol, boolean isPlayer1, String imagePath) {
            this.symbol = symbol;
            this.isPlayer1 = isPlayer1;
            this.hasMoved = false;
            this.imagePath = imagePath;
            this.image = new ImageIcon(imagePath);

        }

        public String getSymbol() {
            return symbol;
        }

        public boolean isPlayer1() {
            return isPlayer1;
        }

        public boolean hasMoved() {
            return hasMoved;
        }

        public void setMoved(boolean hasMoved) {
            this.hasMoved = hasMoved;
        }

        public boolean isEmpty() {
            if (this.symbol == "")
                return true;
            else
                return false;
        }
    }

    private void updateButtonText(int row, int col, String symbol) {
        boardButtons[row][col].setIcon(board[row][col].image);
        boardButtons[row][col].setText(symbol);
        boardButtons[row][col].setIcon(getImage(symbol));

    }

    private void initializeBoard() {
        board[0][0] = new Piece("R", true, "PieceImages/WRook.png"); // Rook
        board[0][1] = new Piece("N", true, "PieceImages/WKnight.png"); // Knight
        board[0][2] = new Piece("B", true, "PieceImages/WBishop.png"); // Bishop
        board[0][3] = new Piece("Q", true, "PieceImages/WQueen.png"); // Queen
        board[0][4] = new Piece("K", true, "PieceImages/WKing.png"); // King
        board[0][5] = new Piece("B", true, "PieceImages/WBishop.png"); // Bishop
        board[0][6] = new Piece("N", true, "PieceImages/WRook.png"); // Rook
        board[0][7] = new Piece("R", true, "PieceImages/WKnight.png"); // Knight
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Piece("P", true, "PieceImages/WPawn.png"); // Pawns
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        board[7][0] = new Piece("r", false, "PieceImages/BRook.png"); // Rook
        board[7][1] = new Piece("n", false, "PieceImages/BKnight.png"); // Knight
        board[7][2] = new Piece("b", false, "PieceImages/BBishop.png"); // Bishop
        board[7][3] = new Piece("q", false, "PieceImages/BQueen.png"); // Queen
        board[7][4] = new Piece("k", false, "PieceImages/BKing.png"); // King
        board[7][5] = new Piece("b", false, "PieceImages/BBishop.png"); // Bishop
        board[7][6] = new Piece("n", false, "PieceImages/BRook.png"); // Rook
        board[7][7] = new Piece("r", false, "PieceImages/BKnight.png"); // Knight
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece("p", false, "PieceImages/BPawn.png");// Pawns
        }

    }

    private void initializeUI() {
        setTitle("Chess Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        int iconWidth = getWidth() / 8;
        int iconHeight = getHeight() / 8;
        // Create buttons array
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j] = new JButton();
                if (board[i][j] != null) {
                    Image originalImage = board[i][j].image.getImage(); // Extract Image from ImageIcon
                    Image resizedImage = originalImage.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH); // Resize
                                                                                                                     // Image
                    board[i][j].image = new ImageIcon(resizedImage); // Create new ImageIcon from resized Image
                    boardButtons[i][j].setIcon(board[i][j].image);
                }
                boardButtons[i][j].addActionListener(new ChessButtonListener(i, j));
                add(boardButtons[i][j]);
            }
        }

        setVisible(true);
    }

    private class ChessButtonListener implements ActionListener {
        private int row;
        private int col;

        public ChessButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            if (selectedButton == null) { // Highlights selected piece
                if (!board[row][col].isEmpty() && isPlayer1Turn
                        && Character.isUpperCase(board[row][col].getSymbol().charAt(0))) {
                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);
                } else if (!board[row][col].isEmpty() && !isPlayer1Turn
                        && Character.isLowerCase(board[row][col].getSymbol().charAt(0))) {
                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);
                }
            } else {
                // Moves selected piece if valid
                int selectedRow = getButtonRow(selectedButton);
                int selectedCol = getButtonCol(selectedButton);

                if (isValidMove(selectedRow, selectedCol, row, col, board[selectedRow][selectedCol])) {
                    Piece toTemp = board[row][col];

                    boolean occupied = false;

                    if (toTemp != null)
                        occupied = true;

                    board[row][col] = board[selectedRow][selectedCol];
                    Piece fromTemp = board[selectedRow][selectedCol];
                    board[selectedRow][selectedCol] = null;

                    clickedButton.setIcon(board[row][col].image);
                    boardButtons[selectedRow][selectedCol].setIcon(null);

                    selectedButton.setIcon(null);
                    selectedButton.setText("");
                    selectedButton.setBackground(null);

                    boolean flag = false;

                    if (isKingInCheck(isPlayer1Turn)) {
                        flag = true;
                        JOptionPane.showMessageDialog(ChessGame.this, "Moving there puts you in Check.");

                        board[selectedRow][selectedCol] = board[row][col];
                        boardButtons[selectedRow][selectedCol].setIcon(getImage(fromTemp.getSymbol()));

                        board[row][col] = toTemp;

                        if (occupied) {
                            clickedButton.setText(toTemp.getSymbol());
                            boardButtons[row][col].setIcon(getImage(toTemp.getSymbol()));
                        } else {
                            clickedButton.setText("");
                            boardButtons[row][col].setIcon(null);
                        }
                        if (isPlayer1Turn)
                            selectedButton.setText(fromTemp.getSymbol());
                        else
                            selectedButton.setText(fromTemp.getSymbol());
                        isPlayer1Turn = !isPlayer1Turn;
                    }

                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);

                    // Switch turns
                    isPlayer1Turn = !isPlayer1Turn;

                    if (isKingInCheckmate(isPlayer1Turn))
                        if (isPlayer1Turn) {
                            JOptionPane.showMessageDialog(ChessGame.this,
                                    "You have been placed in checkmate, Black Wins!");
                            gameOver = true;
                        } else {
                            JOptionPane.showMessageDialog(ChessGame.this,
                                    "You have been placed in checkmate, White Wins!");
                            gameOver = true;
                        }

                    boolean isOpponentInCheck = isKingInCheck(isPlayer1Turn);

                    if (isOpponentInCheck && !flag) {
                        JOptionPane.showMessageDialog(ChessGame.this, "You placed your opponent in check.");
                    }

                    selectedButton.setBackground(null);
                    selectedButton = null;
                } else if (gameOver) {
                    JOptionPane.showMessageDialog(ChessGame.this, "Game is over.");

                } else {
                    if (selectedButton == clickedButton) {
                        selectedButton.setBackground(null);
                        selectedButton = null;
                    } else
                        JOptionPane.showMessageDialog(ChessGame.this, "Invalid move!");
                }
            }
        }

        private int getButtonRow(JButton button) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (boardButtons[i][j] == button) {
                        return i;
                    }
                }
            }
            return -1;
        }

        private int getButtonCol(JButton button) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (boardButtons[i][j] == button) {
                        return j;
                    }
                }
            }
            return -1;
        }

        private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece p) {
            if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
                return false;
            }

            if (gameOver)
                return false;

            // Check if the destination is empty or has an opponent's piece
            if (board[toRow][toCol] == null
                    || (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1())) {

                switch (p.getSymbol().toLowerCase()) {
                    case "p":
                        int backwardDirectionP = p.isPlayer1() ? 1 : -1;
                        int initialRowPBlack = p.isPlayer1() ? 1 : 6;

                        if ((toCol == fromCol && toRow == fromRow + backwardDirectionP) ||
                                (fromRow == initialRowPBlack && toCol == fromCol
                                        && toRow == fromRow + 2 * backwardDirectionP)) {
                            // Check for forward move
                            if (board[toRow][toCol] == null) {
                                return true;
                            }
                        }
                        // Check for capturing diagonally
                        if (toCol == fromCol + 1 || toCol == fromCol - 1) {
                            if (toRow == fromRow + backwardDirectionP) {
                                if (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                                    return true;
                                }
                            }
                        }

                        return false;
                    case "n":
                        // Knight's movement
                        int rowDiff = Math.abs(toRow - fromRow);
                        int colDiff = Math.abs(toCol - fromCol);
                        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
                            return true;
                        } else {
                            return false;
                        }
                    case "r":
                        // Rook's movement
                        if (toRow == fromRow) {
                            int direction = (toCol - fromCol) > 0 ? 1 : -1;
                            for (int col = fromCol + direction; col != toCol; col += direction) {
                                if (board[toRow][col] != null) {
                                    return false;
                                }
                            }
                        } else if (toCol == fromCol) {
                            int direction = (toRow - fromRow) > 0 ? 1 : -1;
                            for (int row = fromRow + direction; row != toRow; row += direction) {
                                if (board[row][toCol] != null) {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }

                        // Check for capturing
                        boolean canCapture = board[toRow][toCol] == null
                                || board[toRow][toCol].isPlayer1() != p.isPlayer1();

                        if (canCapture) {
                            p.setMoved(true);
                        }

                        return canCapture;
                    case "b":
                        // Bishop's movement
                        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
                            return false;
                        }

                        int rowDirection = (toRow - fromRow) > 0 ? 1 : -1;
                        int colDirection = (toCol - fromCol) > 0 ? 1 : -1;

                        for (int i = 1; i < Math.abs(toRow - fromRow); i++) {
                            int row = fromRow + i * rowDirection;
                            int col = fromCol + i * colDirection;
                            if (board[row][col] != null) {
                                return false;
                            }
                        }

                        // Check for capturing
                        if (board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                            return true;
                        } else {
                            return false;
                        }
                    case "q":
                        // Queen's movement
                        if (!(toRow == fromRow || toCol == fromCol
                                || Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol))) {
                            return false;
                        }

                        int rowDirectionQ = Integer.compare(toRow, fromRow);
                        int colDirectionQ = Integer.compare(toCol, fromCol);

                        if (toRow == fromRow || toCol == fromCol) {
                            for (int i = 1; i < Math.abs(toRow - fromRow) + Math.abs(toCol - fromCol); i++) {
                                int row = fromRow + i * rowDirectionQ;
                                int col = fromCol + i * colDirectionQ;
                                if (board[row][col] != null) {
                                    return false;
                                }
                            }
                        } else {
                            for (int i = 1; i < Math.abs(toRow - fromRow); i++) {
                                int row = fromRow + i * rowDirectionQ;
                                int col = fromCol + i * colDirectionQ;
                                if (board[row][col] != null) {
                                    return false;
                                }
                            }
                        }

                        // Check for capturing
                        if (board[toRow][toCol] == null || (board[toRow][toCol].isPlayer1() != p.isPlayer1())) {
                            return true;
                        } else {
                            return false;
                        }
                    case "k":
                        // King's movement
                        int rowDiffKing = Math.abs(toRow - fromRow);
                        int colDiffKing = Math.abs(toCol - fromCol);

                        if (rowDiffKing <= 1 && colDiffKing <= 1) {
                            if (board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                                return true;
                            } else {
                                return false;
                            }
                        }

                        if (p.getSymbol().equalsIgnoreCase("K") || p.getSymbol().equalsIgnoreCase("k")) {
                            // Castling to the king's side
                            if (canCastleKingSide(fromRow, fromCol, toRow, toCol, p)) {
                                return true;
                            }
                            // Castling to the queen's side
                            if (canCastleQueenSide(fromRow, fromCol, toRow, toCol, p)) {
                                return true;
                            }
                        }

                        return false;
                    default:
                        return false;
                }

            }

            return false;
        }

        private boolean isKingInCheck(boolean isPlayer1) {
            int kingRow = -1;
            int kingCol = -1;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece currentPiece = board[i][j];
                    if (currentPiece != null && currentPiece.getSymbol().equalsIgnoreCase("K")
                            && currentPiece.isPlayer1() == isPlayer1) {
                        kingRow = i;
                        kingCol = j;
                        break;
                    }
                }
                if (kingRow != -1 && kingCol != -1) {
                    break;
                }

            }

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece opponentPiece = board[i][j];
                    if (opponentPiece != null && opponentPiece.isPlayer1() != isPlayer1) {
                        if (isValidMove(i, j, kingRow, kingCol, opponentPiece)) {
                            System.out.println("King is attacked by" + i + ", " + j);

                            return true;
                        }
                    }
                }
            }

            return false;
        }

        private boolean isKingInCheckmate(boolean isPlayer1) {
            int kingRow = -1;
            int kingCol = -1;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece currentPiece = board[i][j];
                    if (currentPiece != null && currentPiece.getSymbol().equalsIgnoreCase("K")
                            && currentPiece.isPlayer1() == isPlayer1) {
                        kingRow = i;
                        kingCol = j;
                        break;
                    }
                }
                if (kingRow != -1 && kingCol != -1) {
                    break;
                }
            }

            if (isKingInCheck(isPlayer1)) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[kingRow][kingCol] != null && board[kingRow][kingCol].isPlayer1() == isPlayer1) {
                            if (isValidMove(kingRow, kingCol, i, j, board[kingRow][kingCol])) {
                                Piece temp = board[i][j];
                                board[i][j] = board[kingRow][kingCol];
                                board[kingRow][kingCol] = null;

                                if (!isKingInCheck(isPlayer1)) {
                                    board[kingRow][kingCol] = board[i][j];
                                    board[i][j] = temp;
                                    return false;
                                }

                                board[kingRow][kingCol] = board[i][j];
                                board[i][j] = temp;
                            }
                        }
                    }
                }
                return true;
            }

            return false;
        }
    }

    private boolean canCastleKingSide(int fromRow, int fromCol, int toRow, int toCol, Piece king) {
        if (king.isPlayer1() && fromRow == 0 && fromCol == 4 && toRow == 0 && toCol == 6) {
            if (board[0][5] == null && board[0][6] == null &&
                    board[0][7] != null && board[0][7].getSymbol().equalsIgnoreCase("R") &&
                    !hasPieceMoved(0, 7) && !hasPieceMoved(0, 4)) {
                board[0][5] = board[0][7];
                board[0][7] = null;

                board[0][5].setMoved(true);

                updateButtonText(0, 5, "R");
                updateButtonText(0, 7, "");

                return true;
            }
        } else if (!king.isPlayer1() && fromRow == 7 && fromCol == 4 && toRow == 7 && toCol == 6) {
            if (board[7][5] == null && board[7][6] == null &&
                    board[7][7] != null && board[7][7].getSymbol().equalsIgnoreCase("r") &&
                    !hasPieceMoved(7, 7) && !hasPieceMoved(7, 4)) {
                board[7][5] = board[7][7];
                board[7][7] = null;

                board[7][5].setMoved(true);

                updateButtonText(7, 5, "r");
                updateButtonText(7, 7, "");

                return true;
            }
        }
        return false;
    }

    private boolean canCastleQueenSide(int fromRow, int fromCol, int toRow, int toCol, Piece king) {
        if (king.isPlayer1() && fromRow == 0 && fromCol == 4 && toRow == 0 && toCol == 2) {
            if (board[0][1] == null && board[0][2] == null && board[0][3] == null &&
                    board[0][0] != null && board[0][0].getSymbol().equalsIgnoreCase("R") &&
                    !hasPieceMoved(0, 0) && !hasPieceMoved(0, 4)) {
                board[0][3] = board[0][0];
                board[0][0] = null;

                board[0][3].setMoved(true);

                updateButtonText(0, 3, "R");
                updateButtonText(0, 0, "");

                return true;
            }
        } else if (!king.isPlayer1() && fromRow == 7 && fromCol == 4 && toRow == 7 && toCol == 2) {
            if (board[7][1] == null && board[7][2] == null && board[7][3] == null &&
                    board[7][0] != null && board[7][0].getSymbol().equalsIgnoreCase("r") &&
                    !hasPieceMoved(7, 0) && !hasPieceMoved(7, 4)) {
                board[7][3] = board[7][0];
                board[7][0] = null;

                board[7][3].setMoved(true);

                updateButtonText(7, 3, "r");
                updateButtonText(7, 0, "");

                return true;
            }
        }
        return false;
    }

    private boolean hasPieceMoved(int row, int col) {
        if (board[row][col] != null) {
            return board[row][col].hasMoved();
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame());
    }
}
