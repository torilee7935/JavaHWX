import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons = new JButton[8][8]; // UI board
    private Piece[][] board = new Piece[8][8]; // Logic board
    private boolean isPlayer1Turn = true; // Checks if its white(true) or black(false) turn
    private JButton selectedButton = null; // piece selected for move
    private boolean gameOver = false;
    private int iconWidth = 0; // for image sizing
    private int iconHeight = 0;

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

    public ChessGame() {
        initializeBoard();
        initializeUI();
    }

    private void initializeBoard() {
        board[0][0] = new Piece("R", true, "PieceImages/WRook.png"); // Rook
        board[0][1] = new Piece("N", true, "PieceImages/WKnight.png"); // Knight
        board[0][2] = new Piece("B", true, "PieceImages/WBishop.png"); // Bishop
        board[0][3] = new Piece("Q", true, "PieceImages/WQueen.png"); // Queen
        board[0][4] = new Piece("K", true, "PieceImages/WKing.png"); // King
        board[0][5] = new Piece("B", true, "PieceImages/WBishop.png"); // Bishop
        board[0][6] = new Piece("N", true, "PieceImages/WKnight.png"); // Knight
        board[0][7] = new Piece("R", true, "PieceImages/WRook.png"); // Rook
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Piece("P", true, "PieceImages/WPawn.png"); // Pawns
        }
        for (int i = 2; i < 6; i++) { // middle rows
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
        board[7][6] = new Piece("n", false, "PieceImages/BKnight.png"); // Rook
        board[7][7] = new Piece("r", false, "PieceImages/BRook.png"); // Knight
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece("p", false, "PieceImages/BPawn.png");// Pawns
        }

    }

    private void initializeUI() {
        setTitle("Chess Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        iconWidth = getWidth() / 8;
        iconHeight = getHeight() / 8;

        // Create buttons array
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j] = new JButton(); // New button on UI board
                if (board[i][j] != null) {
                    resizeImage(boardButtons[i][j], board[i][j]); // Resizes image to fit screen
                }
                boardButtons[i][j].addActionListener(new ChessButtonListener(i, j));
                add(boardButtons[i][j]);
            }
        }
        updateBoardPattern(); // Sets the checkered board pattern
        setVisible(true);
    }

    private void resizeImage(JButton buttonImageBoard, Piece pieceBoard) {
        Image originalImage = pieceBoard.image.getImage(); // Gets image associated with this piece
        Image resizedImage = originalImage.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH); // Resize
        pieceBoard.image = new ImageIcon(resizedImage); // Uses resized image to update piece image
        buttonImageBoard.setIcon(pieceBoard.image); // Sets image icon
    }

    // Updates UI board
    private void updateButtonText(int row, int col, String symbol) {
        if (symbol != "") {
            boardButtons[row][col].setIcon(board[row][col].image);
        } else {
            boardButtons[row][col].setIcon(null);
        }
    }

    // Makes the checkered board pattern
    private void updateBoardPattern() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || i % 2 == 1 && j % 2 == 1)
                    boardButtons[i][j].setBackground(new Color(210, 180, 140));
                else
                    boardButtons[i][j].setBackground(new Color(255, 253, 208));
            }
        }
    }

    private class ChessButtonListener implements ActionListener {
        private int row;
        private int col;

        public ChessButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
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

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            boolean flag = false;

            if (selectedButton == null) { // Highlights a yellow box for selected piece
                if (!board[row][col].isEmpty() && isPlayer1Turn
                        && Character.isUpperCase(board[row][col].getSymbol().charAt(0))) {
                    selectedButton = clickedButton; // Saves the piece that is going to be moved
                    selectedButton.setBackground(Color.YELLOW);
                } else if (!board[row][col].isEmpty() && !isPlayer1Turn
                        && Character.isLowerCase(board[row][col].getSymbol().charAt(0))) {
                    selectedButton = clickedButton; // Saves the piece that is going to be moved
                    selectedButton.setBackground(Color.YELLOW);
                }
            } else {
                // Piece that will be moved
                int selectedRow = getButtonRow(selectedButton);
                int selectedCol = getButtonCol(selectedButton);
                Piece fromTemp = board[selectedRow][selectedCol]; // From the button selected
                Piece toTemp = board[row][col]; // To where the piece will be move
                boolean occupied = false; // occupied flag if there is a piece

                // if its a valid move it will update the piece board/UI board and also handle
                // special cases(king in check/checkMate, upgrade pawn to king, etc)
                if (isValidMove(selectedRow, selectedCol, row, col, board[selectedRow][selectedCol])) {
                    if (toTemp != null)
                        occupied = true;

                    board[row][col] = fromTemp; // Updates the logic board with correct piece
                    board[selectedRow][selectedCol] = null; // Removes piece from previous spot
                    clickedButton.setIcon(board[row][col].image); // Puts updated image on button
                    selectedButton.setIcon(null); // Removes image from where piece moved
                    updateBoardPattern();

                    flag = false;

                    if (isKingInCheck(isPlayer1Turn)) { // if next move places you in check, move is revoked
                        flag = true;
                        JOptionPane.showMessageDialog(ChessGame.this, "Moving there puts you in Check.");
                        board[selectedRow][selectedCol] = board[row][col]; // Resets the moved piece to original spot
                        boardButtons[selectedRow][selectedCol].setIcon(fromTemp.image); // Resets the image
                        board[row][col] = toTemp; // Resets logic board of the spot it was supposed to be moved to

                        if (occupied) { // if there was a piece, resets that image
                            boardButtons[selectedRow][selectedCol].setIcon(fromTemp.image);
                            clickedButton.setIcon(toTemp.image);
                        } else {
                            boardButtons[row][col].setIcon(null);
                            selectedButton.setBackground(Color.YELLOW);
                        }

                    }
                    // If pawn reaches other side it becomes a queen
                    if (board[row][col].getSymbol().equalsIgnoreCase("p") && isPlayer1Turn && row == 7) {
                        updateButtonText(row, col, "Q");
                        board[row][col] = new Piece("Q", true, "PieceImages/WQueen.png");
                        resizeImage(boardButtons[row][col], board[row][col]);
                    } else if (board[row][col].getSymbol().equalsIgnoreCase("p") && !isPlayer1Turn && row == 0) {
                        updateButtonText(row, col, "q");
                        board[row][col] = new Piece("q", false, "PieceImages/BQueen.png");
                        resizeImage(boardButtons[row][col], board[row][col]);
                    }
                    selectedButton = clickedButton;
                    selectedButton.setBackground(Color.YELLOW);
                    // Switch turns
                    if (!flag)
                        isPlayer1Turn = !isPlayer1Turn;
                    // Checks for checkmate
                    if (isKingInCheckmate(isPlayer1Turn)) {
                        if (isPlayer1Turn) {
                            JOptionPane.showMessageDialog(ChessGame.this,
                                    "You have been placed in checkmate, Black Wins!");
                            gameOver = true;
                        } else {
                            JOptionPane.showMessageDialog(ChessGame.this,
                                    "You have been placed in checkmate, White Wins!");
                            gameOver = true;
                        }
                    }
                    // Checks if opponent got put in check
                    if (isKingInCheck(isPlayer1Turn) && !flag) {
                        JOptionPane.showMessageDialog(ChessGame.this, "You placed your opponent in check.");
                    }
                    selectedButton = null;
                    updateBoardPattern();
                } else if (gameOver) {
                    JOptionPane.showMessageDialog(ChessGame.this, "Game is over.");
                } else {
                    if (selectedButton == clickedButton) {
                        selectedButton = null;
                        updateBoardPattern();
                    } else { // if invalid move, try again
                        JOptionPane.showMessageDialog(ChessGame.this, "Invalid move!");
                    }
                }
            } // End of else (selectedButton NOT null)
        } // End of chessPieceListner

        private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece p) {
            if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
                return false;
            }

            if (gameOver)
                return false;

            // Checks if the destination is empty or has an opponent's piece
            if (board[toRow][toCol] == null
                    || (board[toRow][toCol] != null && board[toRow][toCol].isPlayer1() != p.isPlayer1())) {
                // Finds which piece p is
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

    // canCastle____ checks specific placements on the board depending on king/queen
    // and white/black
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
