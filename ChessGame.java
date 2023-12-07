import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private Piece[][] board = new Piece[8][8];
    private boolean isPlayer1Turn = true;
    private JButton selectedButton = null;
    private boolean gameOver = false;

    public ChessGame() {
        initializeBoard();
        initializeUI();
    }

    private class Piece {
        private String symbol;
        private boolean isPlayer1;
        private boolean hasMoved;

        public Piece(String symbol, boolean isPlayer1) {
            this.symbol = symbol;
            this.isPlayer1 = isPlayer1;
            this.hasMoved = false;
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
        boardButtons[row][col].setText(symbol);
    }

    private void initializeBoard() {
        board[0][0] = new Piece("R", true); // Rook
        board[0][1] = new Piece("N", true); // Knight
        board[0][2] = new Piece("B", true); // Bishop
        board[0][3] = new Piece("Q", true); // Queen
        board[0][4] = new Piece("K", true); // King
        board[0][5] = new Piece("B", true); // Bishop
        board[0][6] = new Piece("N", true); // Rook
        board[0][7] = new Piece("R", true); // Knight
        board[1][0] = new Piece("P", true); // Pawns
        board[1][1] = new Piece("P", true);
        board[1][2] = new Piece("P", true);
        board[1][3] = new Piece("P", true);
        board[1][4] = new Piece("P", true);
        board[1][5] = new Piece("P", true);
        board[1][6] = new Piece("P", true);
        board[1][7] = new Piece("P", true);

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        board[7][0] = new Piece("r", false); // Rook
        board[7][1] = new Piece("n", false); // Knight
        board[7][2] = new Piece("b", false); // Bishop
        board[7][3] = new Piece("q", false); // Queen
        board[7][4] = new Piece("k", false); // King
        board[7][5] = new Piece("b", false); // Bishop
        board[7][6] = new Piece("n", false); // Rook
        board[7][7] = new Piece("r", false); // Knight
        board[6][0] = new Piece("p", false);// Pawns
        board[6][1] = new Piece("p", false);
        board[6][2] = new Piece("p", false);
        board[6][3] = new Piece("p", false);
        board[6][4] = new Piece("p", false);
        board[6][5] = new Piece("p", false);
        board[6][6] = new Piece("p", false);
        board[6][7] = new Piece("p", false);

    }

    private void initializeUI() {
        setTitle("Chess Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        // Create buttons array
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    boardButtons[i][j] = new JButton(board[i][j].getSymbol());
                } else {
                    boardButtons[i][j] = new JButton();
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

                    clickedButton.setText(board[row][col].getSymbol());

                    selectedButton.setText("");

                    selectedButton.setBackground(null);

                    boolean flag = false;

                    if (isKingInCheck(isPlayer1Turn)) {
                        flag = true;
                        JOptionPane.showMessageDialog(ChessGame.this, "Moving there puts you in Check.");

                        board[selectedRow][selectedCol] = board[row][col];

                        board[row][col] = toTemp;

                        if (occupied)
                            clickedButton.setText(toTemp.getSymbol());
                        else
                            clickedButton.setText("");

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

                switch (p.getSymbol()) {
                    case "P":
                        int forwardDirectionP = p.isPlayer1() ? 1 : -1;
                        int initialRowP = p.isPlayer1() ? 1 : 6;

                        if ((toCol == fromCol && toRow == fromRow + forwardDirectionP) ||
                                (fromRow == initialRowP && toCol == fromCol
                                        && toRow == fromRow + 2 * forwardDirectionP)) {
                            // Check for foward move
                            if (board[toRow][toCol] == null) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if ((toCol == fromCol + 1 || toCol == fromCol - 1)
                                && toRow == fromRow + forwardDirectionP) {
                            // Check for capturing diagonally
                            if (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
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
                    case "N":
                    case "n":
                        // Knight's movement
                        int rowDiff = Math.abs(toRow - fromRow);
                        int colDiff = Math.abs(toCol - fromCol);
                        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
                            return true;
                        } else {
                            return false;
                        }
                    case "R":
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
                    case "B":
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
                    case "Q":
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
                    case "K":
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
