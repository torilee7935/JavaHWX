import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private Piece[][] board = new Piece[8][8];
    private boolean isPlayer1Turn = true;
    private JButton selectedButton = null;

    public ChessGame() {
        initializeBoard();
        initializeUI();
    }

    private class Piece {
        private String symbol;
        private boolean isPlayer1;

        public Piece(String symbol, boolean isPlayer1) {
            this.symbol = symbol;
            this.isPlayer1 = isPlayer1;
        }

        public String getSymbol() {
            return symbol;
        }

        public boolean isPlayer1() {
            return isPlayer1;
        }

        public boolean isEmpty() {
            if (this.symbol == "")
                return true;
            else
                return false;
        }
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
        board[1][0] = new Piece("P", true);
        board[1][1] = new Piece("P", true);
        board[1][2] = new Piece("P", true);
        board[1][3] = new Piece("P", true);
        board[1][4] = new Piece("P", true); // Pawns
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
        board[6][0] = new Piece("p", false);
        board[6][1] = new Piece("p", false);
        board[6][2] = new Piece("p", false);
        board[6][3] = new Piece("p", false);
        board[6][4] = new Piece("p", false); // Pawns
        board[6][5] = new Piece("p", false);
        board[6][6] = new Piece("p", false);
        board[6][7] = new Piece("p", false);

    }

    private void initializeUI() {
        setTitle("Chess Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        // Create buttons for the chess board
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

            if (selectedButton == null) {
                // If no piece is selected, highlight the button if it contains a piece of the
                // current player
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
                // If a piece is already selected, move it to the new position
                int selectedRow = getButtonRow(selectedButton);
                int selectedCol = getButtonCol(selectedButton);

                // Check if the move is valid
                if (isValidMove(selectedRow, selectedCol, row, col, board[selectedRow][selectedCol])) {
                    // Move the piece to the new position
                    board[row][col] = board[selectedRow][selectedCol];
                    board[selectedRow][selectedCol] = null;
                    clickedButton.setText(board[row][col].getSymbol());

                    selectedButton.setText("");

                    // Reset the background color of the selected button
                    selectedButton.setBackground(null);

                    boolean flag = false;

                    if (isKingInCheck(isPlayer1Turn)) {
                        flag = true;
                        JOptionPane.showMessageDialog(ChessGame.this, "Moving there puts you in Check.");
                        board[selectedRow][selectedCol] = board[row][col];
                        board[row][col] = null;
                        clickedButton.setText("");

                        if (isPlayer1Turn)
                            selectedButton.setText("K");
                        else
                            selectedButton.setText("k");
                        isPlayer1Turn = !isPlayer1Turn;
                    }

                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);

                    // Switch turns
                    isPlayer1Turn = !isPlayer1Turn;

                    boolean isOpponentInCheck = isKingInCheck(isPlayer1Turn);

                    if (isOpponentInCheck && !flag) {
                        JOptionPane.showMessageDialog(ChessGame.this, "You placed your opponent in check.");
                    }

                    selectedButton.setBackground(null);
                    selectedButton = null;
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

            // Check if the destination is empty or has an opponent's piece
            if (board[toRow][toCol] == null
                    || (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1())) {
                // Implement specific rules for each piece
                switch (p.getSymbol()) {
                    case "P":
                        // Pawn's basic movement and capturing
                        int forwardDirectionP = p.isPlayer1() ? 1 : -1;
                        int initialRowP = p.isPlayer1() ? 1 : 6;

                        if ((toCol == fromCol && toRow == fromRow + forwardDirectionP) ||
                                (fromRow == initialRowP && toCol == fromCol
                                        && toRow == fromRow + 2 * forwardDirectionP)) {
                            // Check for capturing
                            if (board[toRow][toCol] == null) {
                                return true; // Valid move (no capture)
                            } else {
                                return false; // Invalid move (occupied destination, no capture)
                            }
                        } else if ((toCol == fromCol + 1 || toCol == fromCol - 1)
                                && toRow == fromRow + forwardDirectionP) {
                            // Check for capturing diagonally
                            if (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                                return true; // Valid move (capture)
                            } else {
                                return false; // Invalid move (no piece to capture)
                            }
                        } else {
                            return false; // Invalid move (other conditions)
                        }
                    case "p":
                        int backwardDirectionP = p.isPlayer1() ? 1 : -1;
                        int initialRowPBlack = p.isPlayer1() ? 1 : 6;

                        // Basic movement conditions
                        if ((toCol == fromCol && toRow == fromRow + backwardDirectionP) ||
                                (fromRow == initialRowPBlack && toCol == fromCol
                                        && toRow == fromRow + 2 * backwardDirectionP)) {
                            // Check for moving forward
                            if (board[toRow][toCol] == null) {
                                return true; // Valid move (no capture)
                            }
                        }

                        // Capturing diagonally
                        if (toCol == fromCol + 1 || toCol == fromCol - 1) {
                            if (toRow == fromRow + backwardDirectionP) {
                                // Check for capturing diagonally
                                if (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1()) {
                                    return true; // Valid move (capture)
                                }
                            }
                        }

                        return false; // Invalid move
                    case "N":
                    case "n":
                        // Knight's L-shaped movement
                        int rowDiff = Math.abs(toRow - fromRow);
                        int colDiff = Math.abs(toCol - fromCol);
                        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
                    case "R":
                    case "r":
                        // Rook's movement (vertical or horizontal)
                        if (toRow == fromRow) {
                            // Horizontal movement
                            int direction = (toCol - fromCol) > 0 ? 1 : -1;
                            for (int col = fromCol + direction; col != toCol; col += direction) {
                                if (board[toRow][col] != null) {
                                    return false; // Invalid move if there is a piece in the path
                                }
                            }
                        } else if (toCol == fromCol) {
                            // Vertical movement
                            int direction = (toRow - fromRow) > 0 ? 1 : -1;
                            for (int row = fromRow + direction; row != toRow; row += direction) {
                                if (board[row][toCol] != null) {
                                    return false; // Invalid move if there is a piece in the path
                                }
                            }
                        } else {
                            return false; // Invalid move if not moving vertically or horizontally
                        }

                        // Check for capturing
                        return board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1();
                    case "B":
                    case "b":
                        // Bishop's movement (diagonal)
                        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
                            return false; // Invalid move if not moving diagonally
                        }

                        // Determine the direction of movement
                        int rowDirection = (toRow - fromRow) > 0 ? 1 : -1;
                        int colDirection = (toCol - fromCol) > 0 ? 1 : -1;

                        // Check for pieces in the diagonal path
                        for (int i = 1; i < Math.abs(toRow - fromRow); i++) {
                            int row = fromRow + i * rowDirection;
                            int col = fromCol + i * colDirection;
                            if (board[row][col] != null) {
                                return false; // Invalid move if there is a piece in the path
                            }
                        }

                        // Check for capturing
                        return board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1();
                    case "Q":
                    case "q":
                        // Queen's movement (combination of rook and bishop)
                        if (!(toRow == fromRow || toCol == fromCol
                                || Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol))) {
                            return false; // Invalid move if not moving vertically, horizontally, or diagonally
                        }

                        // Determine the direction of movement
                        int rowDirectionQ = Integer.compare(toRow, fromRow);
                        int colDirectionQ = Integer.compare(toCol, fromCol);

                        // Check for pieces in the path
                        if (toRow == fromRow || toCol == fromCol) {
                            // Rook-like movement (vertical or horizontal)
                            for (int i = 1; i < Math.abs(toRow - fromRow) + Math.abs(toCol - fromCol); i++) {
                                int row = fromRow + i * rowDirectionQ;
                                int col = fromCol + i * colDirectionQ;
                                if (board[row][col] != null) {
                                    return false; // Invalid move if there is a piece in the path
                                }
                            }
                        } else {
                            // Bishop-like movement (diagonal)
                            for (int i = 1; i < Math.abs(toRow - fromRow); i++) {
                                int row = fromRow + i * rowDirectionQ;
                                int col = fromCol + i * colDirectionQ;
                                if (board[row][col] != null) {
                                    return false; // Invalid move if there is a piece in the path
                                }
                            }
                        }

                        // Check for capturing
                        return board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1();
                    case "K":
                    case "k":
                        // King's movement (one square in any direction)
                        int rowDiffKing = Math.abs(toRow - fromRow);
                        int colDiffKing = Math.abs(toCol - fromCol);

                        // Check for a valid move (one square in any direction)
                        if (rowDiffKing <= 1 && colDiffKing <= 1) {
                            // Check for capturing
                            return board[toRow][toCol] == null || board[toRow][toCol].isPlayer1() != p.isPlayer1();
                        }
                        return false; // Invalid move
                    default:
                        return false;
                }

            }

            return false;
        }

        private boolean isKingInCheck(boolean isPlayer1) {
            int kingRow = -1;
            int kingCol = -1;

            // Find the position of the king
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

            System.out.println("King is on" + kingRow + ", " + kingCol);

            // Check if any opponent piece can move to the king's new position
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece opponentPiece = board[i][j];
                    if (opponentPiece != null && opponentPiece.isPlayer1() != isPlayer1) {
                        if (isValidMove(i, j, kingRow, kingCol, opponentPiece)) {
                            System.out.println("King is attacked by" + i + ", " + j);

                            return true; // King is in check
                        }
                    }
                }
            }

            return false; // King is not in check
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame());
    }
}
