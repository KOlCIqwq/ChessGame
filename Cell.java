import javax.swing.JPanel;
import java.awt.*;

class Cell extends JPanel{
    private int row;
    private int col;
    private ImagePanel image;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

    public void setImagePanel(ImagePanel image){
        this.image = image;
        removeAll();

        if (image != null){
            add(image, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    public ImagePanel getImagePanel(){
        return image;
    }
}