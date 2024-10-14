import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Editor");
        window.setResizable(false);

        Panel panel = new Panel();
        window.add(panel);
        window.pack();
        window.setLocation(325, 0);
        window.setVisible(true);

        MapSettings settings = new MapSettings(panel);
        TileList tile_list = new TileList(panel);
        panel.start_clock();

        
    }
}