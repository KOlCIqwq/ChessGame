public class Queen extends Piece{
    public Queen(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol, Board board) {
        // Queen moves horizontally, vertically, or diagonally
        int dRow = Math.abs(targetRow - row);
        int dCol = Math.abs(targetCol - col);
        return (row == targetRow || col == targetCol) || (dRow == dCol);
    }
}
