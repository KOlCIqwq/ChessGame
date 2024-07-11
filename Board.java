import javax.swing.JPanel;

import java.awt.*;

public class Board extends JPanel{
    public Board(String fen){
        setLayout(new GridLayout(8,8));
        char[][] Board = parseFEN(fen);
        boolean isWhite = true;

        // Loop the Board
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                Cell cell = new Cell(row,col);
                cell.setLayout(new BorderLayout());
                
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
                add(cell);
                isWhite = !isWhite;

                //cell.add(new ImagePanel("C:/Users/liu12/Desktop/Useful/Chess/Resources/wr.png"));
                // Add mouse listener to print coordinates
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Clicked cell at: (" + cell.getRow() + ", " + cell.getCol() + ")");
                    }
                });
            }
            isWhite = !isWhite;
            
        }
    }
    // Convert FEN notation into a String table
    public char[][] parseFEN(String fen){
        String[] rows = fen.split("/"); // Split he String into pieces by /
        char[][] Board = new char[8][8];

        for (int row = 0; row < 8; row++){
            String fenRow = rows[row];
            int col = 0;
            for (int i = 0; i < fenRow.length(); i++){
                char c = fenRow.charAt(i);
                if (Character.isDigit(c)){
                    int cells = Character.getNumericValue(c);
                    for (int j = 0; j < cells; j++){
                        Board[row][col] = ' ';
                        col++;
                    }
                }else{
                    Board[row][col] = c;
                    col++;
                }
            }
        }
        return Board;
    }

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
}
