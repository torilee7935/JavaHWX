import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8];
    private String[][] board = new String[8][8];
    private boolean isPlayer1Turn = true;
    private JButton selectedButton = null;

    public ChessGame() {
        initializeBoard();
        initializeUI();
    }

    private void initializeBoard() {
        // Initialize the chess board with pieces in their initial positions
        board[0] = new String[] { "R", "N", "B", "Q", "K", "B", "N", "R" };
        board[1] = new String[] { "P", "P", "P", "P", "P", "P", "P", "P" };
        for (int i = 2; i < 6; i++) {
            board[i] = new String[] { "", "", "", "", "", "", "", "" };
        }
        board[6] = new String[] { "p", "p", "p", "p", "p", "p", "p", "p" };
        board[7] = new String[] { "r", "n", "b", "q", "k", "b", "n", "r" };
    }

    private void initializeUI() {
        setTitle("Chess Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        // Create buttons for the chess board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j] = new JButton(board[i][j]);
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
                if (!board[row][col].isEmpty() && isPlayer1Turn && Character.isUpperCase(board[row][col].charAt(0))) {
                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);
                } else if (!board[row][col].isEmpty() && !isPlayer1Turn
                        && Character.isLowerCase(board[row][col].charAt(0))) {
                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);
                }
            } else {
                // If a piece is already selected, move it to the new position
                int selectedRow = getButtonRow(selectedButton);
                int selectedCol = getButtonCol(selectedButton);

                // Check if the move is valid (you need to implement this logic)
                if (isValidMove(selectedRow, selectedCol, row, col)) {
                    // Move the piece to the new position
                    board[row][col] = board[selectedRow][selectedCol];
                    board[selectedRow][selectedCol] = "";
                    clickedButton.setText(board[row][col]);

                    selectedButton.setText("");

                    // Reset the background color of the selected button
                    selectedButton.setBackground(null);
                    selectedButton = null;

                    // Switch turns
                    isPlayer1Turn = !isPlayer1Turn;
                } else {
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
        private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
            // You need to implement this based on the rules of chess
            return true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame());
    }
}
