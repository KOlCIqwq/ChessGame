import java.awt.*;
import javax.swing.*;

public class Board extends JLayeredPane {
    private char[][] boardArray;  // Store board state (8x8 grid)
    private Cell[][] cells;       // 2D array of Cell objects
    private char activeColor;
    private String castling;
    private String enPSquare;
    private int halfMoveClock;
    private int fullMoveClock;
    
    
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
                char pieceChar = boardArray[row][col];
                if (pieceChar != ' ') {
                    Piece piece = createPiece(pieceChar, row, col);
                    cell.setPiece(piece);
                    String imageName = getImageName(pieceChar);
                    ImagePanel imagePanel = new ImagePanel("Resources/" + imageName + ".png", isWhite ? Color.WHITE : Color.BLACK);
                    cell.setImagePanel(imagePanel);
                }
                
                add(cell);
                isWhite = !isWhite;
            }
            isWhite = !isWhite; // Toggle color every row
        }
    }

    private Piece createPiece(char pieceChar, int row, int col){
        boolean isWhite = Character.isUpperCase(pieceChar);
        switch (Character.toLowerCase(pieceChar)) {
            case 'r': return new Rook(row, col, isWhite);
            case 'n': return new Knight(row, col, isWhite);
            case 'b': return new Bishop(row, col, isWhite);
            case 'q': return new Queen(row, col, isWhite);
            case 'k': return new King(row, col, isWhite);
            case 'p': return new Pawn(row, col, isWhite);
            default: return null;
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
        Piece movingPiece = fromCell.getpiece();
        if (movingPiece != null){
            toCell.setPiece(movingPiece);
            fromCell.setPiece(null);
            movingPiece.setPosition(toCell.getRow(), toCell.getCol());
        }
    }

    // Redraw the board visually after the move
    public void redrawBoard(String fen) {
        this.boardArray = parseFEN(fen);
        removeAll();
        initializeBoard();
        revalidate();
        repaint();
    }

    // Convert FEN notation into a String table
    public char[][] parseFEN(String fen){
        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/"); // Split the String into pieces by /
        char[][] board = new char[8][8];

        for (int row = 0; row < 8; row++){
            // Get current row
            String fenRow = rows[row];
            int col = 0;
            // Loop all chars in the row
            for (int i = 0; i < fenRow.length(); i++){
                char c = fenRow.charAt(i);
                // If it's a number 
                if (Character.isDigit(c)){
                    // Convert from char to int
                    int cells = Character.getNumericValue(c);
                    // Jump number of tiles leaving them empty
                    for (int j = 0; j < cells; j++){
                        board[row][col] = ' ';
                        col++;
                    }
                }else{
                    // Else just fill the board with its piece's char
                    board[row][col] = c;
                    col++;
                }
            }
        }
        // The second part is the active color (w or b)
        activeColor = parts[1].charAt(0);

        // The third part is the castling rights (KQkq or -)
        castling = parts[2];

        // The fourth part is the en passant target square (e.g., e3 or -)
        enPSquare = parts[3];

        // The fifth part is the halfmove clock (integer)
        halfMoveClock = Integer.parseInt(parts[4]);

        // The sixth part is the fullmove number (integer)
        fullMoveClock = Integer.parseInt(parts[5]);
        return board;
    }

    // Function that construct the FEN notation of current board
    public String toFEN(){
        StringBuilder fen = new StringBuilder();
        for(int row = 0; row < 8; row ++){
            int counte = 0;
            for(int col = 0; col < 8; col++){
                Piece piece = cells[row][col].getpiece();
                if (piece == null){
                    counte++;
                } else{
                    if (counte > 0){
                        fen.append(counte);
                        counte = 0;
                    }
                    fen.append(piece.getFENchar());
                }
            }
            if (counte > 0){
                fen.append(counte);
            }
            if (row < 7){
                fen.append('/');
            }
        }
        // Append active color
        fen.append(" ").append(activeColor);

        // Append castling rights
        fen.append(" ").append(castling);

        // Append en passant target square
        fen.append(" ").append(enPSquare);

        // Append halfmove clock and fullmove number
        fen.append(" ").append(halfMoveClock).append(" ").append(fullMoveClock);
        System.err.println(fen.toString());
        return fen.toString();
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

        if ((Character.isUpperCase(fromPiece) && Character.isUpperCase(toPiece)) || 
            (Character.isLowerCase(fromPiece) && Character.isLowerCase(toPiece))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRightColor(Piece piece){
        if (piece == null){
            return false;
        }
        // If the piece is white and it's white's turn, return true
        // If the piece is black and it's black's turn, return true
        return (piece.isWhite() && activeColor == 'w') || (!piece.isWhite() && activeColor == 'b');
    }

    public void swichTurn(){
        activeColor = (activeColor == 'w') ? 'b' : 'w'; // Switch to b if the current is w otherwise w
    }

    public boolean canCastle(){
        if (activeColor == 'w' && (castling.contains("K") || castling.contains("Q"))){
            return true;
        }
        if (activeColor == 'b' && (castling.contains("k") || castling.contains("q"))){
            return true;
        }
        return false;
    }

    public boolean canCastleKingSide(){
        int row = 0;
        if (activeColor == 'w'){
            row = 7;
        }
        return boardArray[row][5] == ' ' && boardArray[row][6] == ' ';
    }
    public boolean canCastleQueenSide(){
        int row = 0;
        if (activeColor == 'w'){
            row = 7;
        }
        return boardArray[row][1] == ' ' && boardArray[row][2] == ' ' && boardArray[row][3] == ' ';
    }
    public void updateCastling(Piece piece) {
        if (piece instanceof King) {
            if (piece.isWhite()) {
                castling = castling.replace("K", "").replace("Q", "");
            } else {
                castling = castling.replace("k", "").replace("q", "");
            }
        } else if (piece instanceof Rook) {
            if (piece.isWhite() && piece.getRow() == 7 && piece.getCol() == 0) {
                castling = castling.replace("Q", "");
            } else if (piece.isWhite() && piece.getRow() == 7 && piece.getCol() == 7) {
                castling = castling.replace("K", "");
            } else if (!piece.isWhite() && piece.getRow() == 0 && piece.getCol() == 0) {
                castling = castling.replace("q", "");
            } else if (!piece.isWhite() && piece.getRow() == 0 && piece.getCol() == 7) {
                castling = castling.replace("k", "");
            }
        }
    }
}