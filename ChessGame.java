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

        board[7][0] = new Piece("r", true); // Rook
        board[7][1] = new Piece("n", true); // Knight
        board[7][2] = new Piece("b", true); // Bishop
        board[7][3] = new Piece("q", true); // Queen
        board[7][4] = new Piece("k", true); // King
        board[7][5] = new Piece("b", true); // Bishop
        board[7][6] = new Piece("n", true); // Rook
        board[7][7] = new Piece("r", true); // Knight
        board[6][0] = new Piece("p", true);
        board[6][1] = new Piece("p", true);
        board[6][2] = new Piece("p", true);
        board[6][3] = new Piece("p", true);
        board[6][4] = new Piece("p", true); // Pawns
        board[6][5] = new Piece("p", true);
        board[6][6] = new Piece("p", true);
        board[6][7] = new Piece("p", true);

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

                // Check if the move is valid (you need to implement this logic)
                if (isValidMove(selectedRow, selectedCol, row, col, board[selectedRow][selectedCol])) {
                    // Move the piece to the new position
                    board[row][col] = board[selectedRow][selectedCol];
                    board[selectedRow][selectedCol] = null;
                    clickedButton.setText(board[row][col].getSymbol());

                    selectedButton.setText("");

                    // Reset the background color of the selected button
                    selectedButton.setBackground(null);

                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);

                    // Switch turns
                    isPlayer1Turn = !isPlayer1Turn;

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

        // Implement your own logic for checking the validity of the move
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
                        } else if (toCol == fromCol + 1 || toCol == fromCol - 1) {
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
                        int backwardDirectionP = p.isPlayer1() ? -1 : 1;
                        int initialRowPBlack = p.isPlayer1() ? 6 : 1;

                        // Basic movement and capturing conditions
                        if ((toCol == fromCol && toRow == fromRow + backwardDirectionP) ||
                                (fromRow == initialRowPBlack && toCol == fromCol
                                        && toRow == fromRow + 2 * backwardDirectionP)) {
                            // Check for capturing (diagonally)
                            if (toCol == fromCol + 1 && board[toRow][toCol] != null
                                    && !board[toRow][toCol].isPlayer1()) {
                                return true; // Valid move (capture)
                            } else if (toCol == fromCol - 1 && board[toRow][toCol] != null
                                    && !board[toRow][toCol].isPlayer1()) {
                                return true; // Valid move (capture)
                            } else if (board[toRow][toCol] == null) {
                                return true; // Valid move (no capture)
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
                        return toRow == fromRow || toCol == fromCol;
                    case "B":
                    case "b":
                        // Bishop's movement (diagonal)
                        return Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol);
                    case "Q":
                    case "q":
                        // Queen's movement (combination of rook and bishop)
                        return toRow == fromRow || toCol == fromCol
                                || Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol);
                    case "K":
                    case "k":
                        // King's movement (one square in any direction)
                        int rowDiffKing = Math.abs(toRow - fromRow);
                        int colDiffKing = Math.abs(toCol - fromCol);
                        return rowDiffKing <= 1 && colDiffKing <= 1;
                    default:
                        return false;
                }

            }

            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame());
    }
}
