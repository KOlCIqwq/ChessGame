import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Cell extends JPanel {
    private int row;
    private int col;
    private Piece piece;
    private ImagePanel imagePanel;  // The image of the piece
    private Point initialClick;     // To track the starting point of the drag
    private boolean dragging = false; // Flag to track if dragging is happening
    private Board board;            // Reference to the main Board object
    private Cell fromCell;

    // Constructor
    public Cell(int row, int col, Board board) {
        this.row = row;
        this.col = col;
        this.board = board; // Save a reference to the main Board for updating it
        setLayout(new BorderLayout());  
        setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);  // Set alternating colors

        // Add MouseListeners for drag and drop
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (imagePanel != null) {
                    initialClick = e.getLocationOnScreen();
                    dragging = true;
                    // Capture the starting cell (fromCell) at the beginning of the drag
                    Point boardLocation = SwingUtilities.convertPoint(Cell.this, e.getPoint(), board);
                    fromCell = board.getCellAtLocation(boardLocation);
                    if (fromCell != null && !board.isRightColor(fromCell.getpiece())){
                        dragging = false;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragging) {
                    Point releaseLocation = SwingUtilities.convertPoint(Cell.this, e.getPoint(), board);
                    Cell targetCell = board.getCellAtLocation(releaseLocation);
                    boolean sameColor = board.checkSameColor(fromCell, targetCell);
                    if (targetCell != null && fromCell != null && !sameColor) {
                        Piece movingPiece = fromCell.getpiece();
                        // Handle Castle
                        if (movingPiece instanceof King && Math.abs(targetCell.getCol() - fromCell.getCol()) == 2){
                            if(board.canCastle() && (board.canCastleKingSide() || board.canCastleQueenSide())){
                                movePieceTo(targetCell);
                                performCastle(fromCell, targetCell);
                                board.swichTurn();
                            }
                        } else if (movingPiece != null && movingPiece.isValidMove(targetCell.getRow(), targetCell.getCol(), board) && board.isRightColor(movingPiece)) {
                            movePieceTo(targetCell);
                            board.swichTurn();
                        } else{
                            resetPiecePosition();
                        }
                    } else {
                    resetPiecePosition();  // Snap back if the move is invalid
                    }
                    dragging = false;
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && imagePanel != null) {
                    Point locationOnScreen = e.getLocationOnScreen();
                    imagePanel.setLocation(locationOnScreen.x - initialClick.x, locationOnScreen.y - initialClick.y);
                }
            }
        });
    }

    public Piece getpiece(){
        return piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    // Setters and Getters for ImagePanel
    public void setImagePanel(ImagePanel newImagePanel) {
        this.imagePanel = newImagePanel;
        removeAll();

        if (newImagePanel != null) {
            add(newImagePanel, BorderLayout.CENTER); 
        }
        revalidate();
        repaint();
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    // Move the piece to the target cell
    private void movePieceTo(Cell targetCell) {
            // Move the piece visually
            targetCell.setImagePanel(this.getImagePanel());
            this.setImagePanel(null);

            // Update the board array
            board.updateBoardArray(this, targetCell);
            String newFEN = board.toFEN();
            board.redrawBoard(newFEN);
    }

    private void performCastle(Cell fromCell, Cell targetCell){
        Piece movingKing = targetCell.getpiece(); // Moved king to targetcell, so the cell should point to targetCell
        int kingStartCol = fromCell.getCol();
        int rookStartCol = targetCell.getCol() > kingStartCol ? 7 : 0; // Determine rook position based on castling direction
        int rookTargetCol = targetCell.getCol() > kingStartCol ? 5 : 3; // Determine where the rook should go
        
        // Move the King
        targetCell.setImagePanel(fromCell.getImagePanel());
        fromCell.setImagePanel(null);
        board.updateBoardArray(fromCell, targetCell);
        board.updateCastling(movingKing);
        String kingFEN = board.toFEN();
        board.redrawBoard(kingFEN);

        // Move the Rook
        Cell rookStartCell = board.getCell(fromCell.getRow(), rookStartCol);
        Cell rookTargetCell = board.getCell(fromCell.getRow(), rookTargetCol);
        Piece movingRook = rookStartCell.getpiece();

        rookTargetCell.setImagePanel(rookStartCell.getImagePanel());
        rookStartCell.setImagePanel(null);
        board.updateBoardArray(rookStartCell, rookTargetCell);
        board.updateCastling(movingKing);
        String newFEN = board.toFEN();
        // Redraw the board after the move
        board.redrawBoard(newFEN);

        // Set them to moved, so don't perform agian the castle
        ((King) movingKing).setHasMoved(true);
        ((Rook) movingRook).setHasMoved(true);

    }
    // Reset the piece back to its original position if the move is invalid
    private void resetPiecePosition() {
        // If the move is invalid, reset the piece to its original position
        revalidate();
        repaint();
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
}