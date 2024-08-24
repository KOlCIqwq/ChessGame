public class King extends Piece{
    public King(int row, int col, boolean isWhite){
        super(row,col,isWhite);
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        int dRow = Math.abs(tgtRow - row);
        int dCol = Math.abs(tgtCol - col);
        return (dRow <= 1 && dCol <= 1);
    }
}

