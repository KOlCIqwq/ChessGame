import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel{
    private Cell selectedCell;
    private char[][] Board;

    public Board(String fen){
        setLayout(new GridLayout(8,8));
        char[][] Board = parseFEN(fen);
        boolean isWhite = true;
        
        // Loop the Board
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                
                Cell cell = new Cell(row,col);
                cell.setLayout(new BorderLayout());
                
                // The piece is the current Board cell
                char piece = Board[row][col];
                if (piece != ' '){
                    String image = getImageName(piece);
                    ImagePanel imagePanel = new ImagePanel("C:/Users/liu12/Desktop/Useful/Chess/Resources/" + image + ".png", isWhite ? Color.WHITE : Color.BLACK);
                    cell.add(imagePanel, BorderLayout.CENTER);
                } else{
                    if (isWhite){
                        cell.setBackground(Color.WHITE);
                    } else{
                        cell.setBackground(Color.BLACK);
                    }
                }

                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Clicked cell at: (" + cell.getRow() + ", " + cell.getCol() + ")");
                        handleClick(cell);
                    }
                });

                add(cell);
                isWhite = !isWhite;

                //cell.add(new ImagePanel("C:/Users/liu12/Desktop/Useful/Chess/Resources/wr.png"));
                // Add mouse listener to print coordinates
                /* 
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Clicked cell at: (" + cell.getRow() + ", " + cell.getCol() + ")");
                    }
                });*/

                
            }
            isWhite = !isWhite;
            
        }
    }

    private void Draw(){
        boolean isWhite = true;
        
        // Loop the Board
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                
                Cell cell = new Cell(row,col);
                cell.setLayout(new BorderLayout());
                
                // The piece is the current Board cell
                char piece = Board[row][col];
                if (piece != ' '){
                    String image = getImageName(piece);
                    ImagePanel imagePanel = new ImagePanel("C:/Users/liu12/Desktop/Useful/Chess/Resources/" + image + ".png", isWhite ? Color.WHITE : Color.BLACK);
                    cell.add(imagePanel, BorderLayout.CENTER);
                } else{
                    if (isWhite){
                        cell.setBackground(Color.WHITE);
                    } else{
                        cell.setBackground(Color.BLACK);
                    }
                }
                

                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Clicked cell at: (" + cell.getRow() + ", " + cell.getCol() + ")");
                        handleClick(cell);
                    }
                });

                add(cell);
                isWhite = !isWhite;

                //cell.add(new ImagePanel("C:/Users/liu12/Desktop/Useful/Chess/Resources/wr.png"));
                // Add mouse listener to print coordinates
                /* 
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Clicked cell at: (" + cell.getRow() + ", " + cell.getCol() + ")");
                    }
                });*/

                
            }
            isWhite = !isWhite;
            
        }
        revalidate();
        repaint();
    }

    // Convert FEN notation into a String table
    public char[][] parseFEN(String fen){
        String[] rows = fen.split("/"); // Split he String into pieces by /
        char[][] Board = new char[8][8];

        for (int row = 0; row < 8; row++){
            // Get current row
            String fenRow = rows[row];
            int col = 0;
            // Loop all char in row
            for (int i = 0; i < fenRow.length(); i++){
                char c = fenRow.charAt(i);
                // If it's a number 
                if (Character.isDigit(c)){
                    // Convert from char to int
                    int cells = Character.getNumericValue(c);
                    // Jump number of tiles leaving it empty
                    for (int j = 0; j < cells; j++){
                        Board[row][col] = ' ';
                        col++;
                    }
                }else{
                    // Else just fill the board with it's piece's char
                    Board[row][col] = c;
                    col++;
                }
            }
        }
        return Board;
    }

    // Convert the char of the cell into it's png format
    private String getImageName(char piece){
        
        switch (piece) {
            case 'r': return "br";
            case 'n': return "bn";
            case 'b': return "bb";
            case 'q': return "bq";
            case 'k': return "bk";
            case 'p': return "bp";
            case 'R': return "wr";
            case 'N': return "wn";
            case 'B': return "wb";
            case 'Q': return "wq";
            case 'K': return "wk";
            case 'P': return "wp";
            default: return "";
        }
    }

    // Handle the cellclick
    private void handleClick(Cell cell){
        
        if (selectedCell == null){
            System.out.println("1");
            //if (cell.getImagePanel() != null){
                selectedCell = cell;
                cell.setBackground(Color.YELLOW);
            
        } else{
            System.out.println("2");
            if (cell != selectedCell){
                cell.setImagePanel(selectedCell.getImagePanel());
                Board[cell.getRow()][cell.getCol()] = Board[selectedCell.getRow()][selectedCell.getCol()];
                selectedCell.setImagePanel(null);
                Board[selectedCell.getRow()][selectedCell.getCol()] = ' ';
                // Update FEN string and redraw the board
                String newFEN = updateFEN();
                removeAll();
                setLayout(new GridLayout(8, 8));
                Board = parseFEN(newFEN);
                Draw();
            }
            selectedCell.setBackground((selectedCell.getRow() + selectedCell.getCol()) % 2 == 0 ? Color.WHITE : Color.BLACK);
            selectedCell = null;
        }
    }

    private String updateFEN() {
        StringBuilder fen = new StringBuilder();
    
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
    
            for (int col = 0; col < 8; col++) {
                char piece = Board[row][col];
    
                if (piece == ' ') {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
                }
            }
    
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
    
            if (row < 7) {
                fen.append('/');
            }
        }
        System.err.println(fen.toString());
        return fen.toString();
    }
}
