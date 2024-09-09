public class Knight extends Piece{
    public Knight(int row, int col, boolean isWhite){
        super(row, col, isWhite);
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        int dRow = Math.abs(tgtRow - row);
        int dCol = Math.abs(tgtCol - col);
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }
    public char getFENchar() {
        return isWhite ? 'N' : 'n';
    }
}
