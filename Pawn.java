public class Pawn extends Piece{
    public Pawn(int row, int col, boolean isWhite){
        super(row,col,isWhite);
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        // Pawn moves forward by 1 square (or 2 squares if it's the first move)
        int direction = isWhite ? -1 : 1;
        if (tgtCol == col && tgtRow == row + direction) {
            // Normal move
            return true;
        } else if (tgtCol == col && row == (isWhite ? 6 : 1) && tgtRow == row + 2 * direction) {
            // Initial 2-square move
            return true;
        } else if (Math.abs(tgtCol - col) == 1 && tgtRow == row + direction) {
            // Capture move (diagonal)
            return true;
        }
        return false;
    }
}
