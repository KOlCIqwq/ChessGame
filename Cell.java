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
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
    if (dragging) {
        Point releaseLocation = SwingUtilities.convertPoint(Cell.this, e.getPoint(), board);
        Cell targetCell = board.getCellAtLocation(releaseLocation);

        if (targetCell != null && fromCell != null) {
            boolean sameColor = board.checkSameColor(fromCell, targetCell);

            if (!sameColor) {
                Piece movingPiece = fromCell.getpiece();
                if (movingPiece != null && movingPiece.isValidMove(targetCell.getRow(), targetCell.getCol(), board)) {
                    movePieceTo(targetCell);
                } else{
                    resetPiecePosition();
                }
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

        targetCell.revalidate();
        targetCell.repaint();
        this.revalidate();
        this.repaint();

        board.redrawBoard();
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