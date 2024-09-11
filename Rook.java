public class Rook extends Piece {
    private boolean hasMoved;
    public Rook(int row, int col, boolean isWhite){
        super(row,col,isWhite);
        this.hasMoved = false;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
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
