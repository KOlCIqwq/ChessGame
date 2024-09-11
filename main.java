import java.awt.*;
import javax.swing.*;

public class main {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            // Initialize the board with JFrame
            JFrame frame = new JFrame("chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Prevent heavy ram
            frame.setSize(600,600);

            String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
            // Get board class to give the board
            Board board = new Board(fen);
            frame.add(board);
            
            frame.setVisible(true);
        });    
    }
}

// Draw the board
class ImagePanel extends JPanel{
    private Image im;
    private Color backgroundColor;

    public ImagePanel(String path, Color backgroundColor){
        this.im = new ImageIcon(path).getImage();
        this.backgroundColor = backgroundColor;
        setOpaque(false); // Ensure the background is not transparent
    }

    @Override
    /* Draw first the board then the image of pieces, to let the image appear transparent background*/
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        // Draw the background color
        //g.setColor(getParent().getBackground());
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Draw the image
        g.drawImage(im,0,0,this.getWidth(),this.getHeight(),this);
    }
}
