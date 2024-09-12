public class Pawn extends Piece{
    public Pawn(int row, int col, boolean isWhite){
        super(row,col,isWhite);
    }

    @Override
    public boolean isValidMove(int tgtRow, int tgtCol, Board board){
        // Pawn moves forward by 1 square (or 2 squares if it's the first move)
        int direction = isWhite ? -1 : 1;
        if (tgtCol == col && tgtRow == row + direction) {
            // 1 square, check if the target cell is null
            return board.getCell(tgtRow,tgtCol).getpiece() == null;
        } else if (tgtCol == col && row == (isWhite ? 6 : 1) && tgtRow == row + 2 * direction) {
            // 2 moves, check if midcell and target cell are null
            Cell interCell = board.getCell(row + direction, col);
            Cell tgtCell = board.getCell(tgtRow, tgtCol);
            return interCell.getpiece() == null && tgtCell.getpiece() == null;
        } else if (Math.abs(tgtCol - col) == 1 && tgtRow == row + direction) {
            // If target piece is not null and they're not the same color, it can move
            Piece targetPiece = board.getCell(tgtRow, tgtCol).getpiece();
            return targetPiece != null && targetPiece.isWhite() != this.isWhite();
        }
        return false;
    }
    public char getFENchar() {
        return isWhite ? 'P' : 'p';
    }
}
