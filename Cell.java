import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Cell extends JPanel {
    private int row;
    private int col;
    private ImagePanel imagePanel;  // The image of the piece
    private Point initialClick;     // To track the starting point of the drag
    private boolean dragging = false; // Flag to track if dragging is happening
    private Board board;            // Reference to the main Board object

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
                    initialClick = e.getPoint();
                    dragging = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragging) {
                    Point releaseLocation = e.getLocationOnScreen();
                    Cell targetCell = board.getCellAtLocation(releaseLocation); // Get the cell at the mouse release position
                    Cell fromCell = board.getCellAtLocation(initialClick);
                    boolean sameColor = board.checkSameColor(fromCell, targetCell);
                    
                    
                    if (targetCell != null && targetCell.getImagePanel() == null) {
                        movePieceTo(targetCell);
                    } else {
                        if (sameColor){
                            // Invalid move, snap back
                        resetPiecePosition();
                        } else{
                            movePieceTo(targetCell);
                        }
                    }
                    dragging = false;
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    Point locationOnScreen = e.getLocationOnScreen();
                    // Adjust the imagePanel to move with the mouse
                    imagePanel.setLocation(locationOnScreen.x - initialClick.x, locationOnScreen.y - initialClick.y);
                }
            }
        });
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

        // Redraw the board to reflect the changes
        board.redrawBoard();
    }

    // Reset the piece back to its original position if the move is invalid
    private void resetPiecePosition() {
        // You can implement this by revalidating the original cell's content
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