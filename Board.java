import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

public class Board extends JPanel {
    private char[][] boardArray;  // Store board state (8x8 grid)
    private Cell[][] cells;       // 2D array of Cell objects
    
    public Board(String fen) {
        setLayout(new GridLayout(8, 8));
        boardArray = parseFEN(fen);
        cells = new Cell[8][8];
        initializeBoard();
    }

    // Initialize the board with cells and pieces
    private void initializeBoard() {
        boolean isWhite = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Cell cell = new Cell(row, col, this);  // Pass reference of Board
                cells[row][col] = cell;
                
                // Add piece images based on board state
                char piece = boardArray[row][col];
                if (piece != ' ') {
                    String imageName = getImageName(piece);
                    ImagePanel imagePanel = new ImagePanel("Resources/" + imageName + ".png", isWhite ? Color.WHITE : Color.BLACK);
                    cell.setImagePanel(imagePanel);
                }
                
                add(cell);
                isWhite = !isWhite;
            }
            isWhite = !isWhite; // Toggle color every row
        }
    }

    // Get the Cell at a given mouse location
    public Cell getCellAtLocation(Point location) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Cell cell = cells[row][col];
                Rectangle bounds = cell.getBounds();
                if (bounds.contains(location)) {
                    return cell;
                }
            }
        }
        return null;  // Return null if no valid cell is found
    }

    // Update the board array after a piece has moved
    public void updateBoardArray(Cell fromCell, Cell toCell) {
        char piece = boardArray[fromCell.getRow()][fromCell.getCol()];
        boardArray[toCell.getRow()][toCell.getCol()] = piece;  // Move the piece to the new cell
        boardArray[fromCell.getRow()][fromCell.getCol()] = ' ';  // Empty the old cell
    }

    // Redraw the board visually after the move
    public void redrawBoard() {
        removeAll();  // Clear the current board
        initializeBoard();  // Reinitialize the board with updated state
        revalidate();
        repaint();
    }

    // Convert FEN notation into a String table
    public char[][] parseFEN(String fen){
        String[] rows = fen.split("/"); // Split he String into pieces by /
        char[][] Board = new char[8][8];

        for (int row = 0; row < 8; row++){
            // Get current row
            String fenRow = rows[row];
            int col = 0;
            // Loop all char in row
            for (int i = 0; i < fenRow.length(); i++){
                char c = fenRow.charAt(i);
                // If it's a number 
                if (Character.isDigit(c)){
                    // Convert from char to int
                    int cells = Character.getNumericValue(c);
                    // Jump number of tiles leaving it empty
                    for (int j = 0; j < cells; j++){
                        Board[row][col] = ' ';
                        col++;
                    }
                }else{
                    // Else just fill the board with it's piece's char
                    Board[row][col] = c;
                    col++;
                }
            }
        }
        return Board;
    }

    // Helper to get image names for pieces
    private String getImageName(char piece) {
        switch (piece) {
            case 'r': return "br";
            case 'n': return "bn";
            case 'b': return "bb";
            case 'q': return "bq";
            case 'k': return "bk";
            case 'p': return "bp";
            case 'R': return "wr";
            case 'N': return "wn";
            case 'B': return "wb";
            case 'Q': return "wq";
            case 'K': return "wk";
            case 'P': return "wp";
            default: return "";
        }
    }

    public boolean checkSameColor (Cell fromCell, Cell toCell){
        char fromPiece = boardArray[fromCell.getRow()][fromCell.getCol()];
        char toPiece = boardArray[toCell.getRow()][toCell.getCol()];

        System.out.println("From Piece: " + fromPiece + " at (" + fromCell.getRow() + ", " + fromCell.getCol() + ")");
        System.out.println("To Piece: " + toPiece + " at (" + toCell.getRow() + ", " + toCell.getCol() + ")");
        System.out.println("Full Board Array:");
        System.out.println(Arrays.deepToString(boardArray));

        if ((Character.isUpperCase(fromPiece) && Character.isUpperCase(toPiece)) || (Character.isLowerCase(fromPiece) && Character.isLowerCase(toPiece))){
            return true;
        } else{
            return false;
        }
    }

    public char getPiece(Cell cell){
        return boardArray[cell.getRow()][cell.getCol()];
    }
}

