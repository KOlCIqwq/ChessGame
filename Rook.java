public class Rook extends Piece {
    public Rook(int row, int col, boolean isWhite){
        super(row,col,isWhite);
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        if (row == tgtRow || col == tgtCol){
            return true;
        }
        return false;
    }
    public char getFENchar() {
        return isWhite ? 'R' : 'r';
    }
}
