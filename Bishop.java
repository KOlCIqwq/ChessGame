public class Bishop extends Piece{
    public Bishop(int row, int col, boolean isWhite){
        super(row,col,isWhite); 
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        int dRow = Math.abs(tgtRow - row);
        int dCol = Math.abs(tgtCol - col);

        return dRow == dCol;
    }
    public char getFENchar() {
        return isWhite ? 'B' : 'b';
    }
}
