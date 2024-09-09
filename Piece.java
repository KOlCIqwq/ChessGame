public abstract class Piece {
    protected int row;
    protected int col;
    protected boolean isWhite;

    public Piece(int row, int col, boolean isWhite){
        this.row = row;
        this.col = col;
        this.isWhite = isWhite;
    }

    public abstract boolean isValidMove(int tgtRow, int tgtCol, Board board);

    public abstract char getFENchar();

    public boolean isWhite(){
        return isWhite;
    }

    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
}
