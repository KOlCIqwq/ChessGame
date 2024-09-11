public class King extends Piece{
    private boolean hasMoved;
    public King(int row, int col, boolean isWhite){
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
        int dRow = Math.abs(tgtRow - row);
        int dCol = Math.abs(tgtCol - col);
        return (dRow <= 1 && dCol <= 1);
    }
    public char getFENchar() {
        return isWhite ? 'K' : 'k';
    }
}

