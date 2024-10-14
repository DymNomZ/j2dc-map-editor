import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TileList extends JFrame {

    private final JButton add_btn;
    private JFileChooser file_chooser = null;
    private final ActionListener click_listener;
    private Tile tile = null;
    private JLabel tile_image = null, tile_name = null, idx_label, solid_label;
    private JPanel main_panel = null, new_panel = null, mini_grid = null;
    private JTextField idx_input = null;
    private JCheckBox solid_check = null;
    private JScrollPane scroll_pane = null;
    private String texture_name = " ";
    private int dot_idx = -1;
    private final Panel panel;
    public File selected_folder;
    public File[] files;
    public ArrayList<JPanel> cards = new ArrayList<>();
    
    public TileList(Panel panel){

        this.panel = panel;

        main_panel = new JPanel();
        main_panel.setLayout(new GridLayout(0, 1));

        add_btn = new JButton("Add tiles");
        add_btn.setBackground(Color.BLACK);
        add_btn.setForeground(Color.WHITE);

        main_panel.add(add_btn);

        //just learned 0 is for multiple columns, I was used to assuming -1 for infinites
        this.setLayout(new GridLayout(0, 1));

        //lamdaed, handle adding of tiles to list
        click_listener = (ActionEvent e) -> {
            file_chooser = new JFileChooser();
            file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int return_value = file_chooser.showOpenDialog(null);
            if (return_value == JFileChooser.APPROVE_OPTION) {
                
                selected_folder = file_chooser.getSelectedFile();
                System.out.println("Selected folder: " + selected_folder);
                
                refresh_list();
                
            }
        };

        add_btn.addActionListener(click_listener);

        //make list scrollable when list gets bigger
        scroll_pane = new JScrollPane(main_panel);

        add(scroll_pane);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Tile List");
        this.setSize(400, 600);
        this.getContentPane().setBackground(Color.BLACK);
        this.setLocation(1135, 200);
        this.setResizable(true);
        this.setFocusable(true);
        //pack messes things up
        //this.pack();
        this.setVisible(true);
    }

    public void refresh_list(){
        System.out.println("Refreshing");
        files = selected_folder.listFiles();
        
        load_cards();
    }

    public void load_cards(){
        if (files != null) {
            for (File file : files) {
                if(file.getName().endsWith(".png")){

                    //System.out.println(file.getName());

                    tile = new Tile(file.getAbsolutePath(), 0);

                    new_panel = new JPanel();
                    new_panel.setBackground(Color.BLACK);
                    new_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 10));

                    dot_idx = file.getName().lastIndexOf('.');
                    texture_name = file.getName().substring(0, dot_idx);

                    tile_image = new JLabel(new ImageIcon(tile.image));
                    tile_name = new JLabel(texture_name);
                    tile_name.setForeground(Color.WHITE);

                    idx_label = new JLabel("idx");
                    idx_label.setForeground(Color.WHITE);

                    //only 2 digits maluoy ta
                    idx_input = new JTextField(2);
                    idx_input.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char c = e.getKeyChar();
                            // Ignore non-digit characters
                            if (!Character.isDigit(c) || c == '0') {
                                e.consume();
                            }
                        }
                    });

                    solid_label = new JLabel("solid");
                    solid_label.setForeground(Color.WHITE);

                    solid_check = new JCheckBox();
                    solid_check.setBackground(Color.BLACK);

                    //lamdaed again, handle checking
                    solid_check.addItemListener((ItemEvent e) -> {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            System.out.println("Tile is solid");
                        } else {
                            System.out.println("Tile is not solid");
                        }
                    });

                    new_panel.add(tile_image);
                    new_panel.add(tile_name);

                    mini_grid = new JPanel();
                    mini_grid.setBackground(Color.BLACK);
                    mini_grid.setLayout(new GridLayout(2, 2));
                    mini_grid.add(idx_label);
                    mini_grid.add(solid_label);
                    mini_grid.add(idx_input);
                    mini_grid.add(solid_check);

                    new_panel.add(mini_grid);

                    //Handles what tile is selecting for placing on grid
                    new_panel.addMouseListener(
                        new CardHandler(
                            panel, tile, 
                            texture_name, 
                            idx_input
                        )
                    );

                    //add to tile data array in panel for finalizing purposes
                    panel.add_tile_data(tile, idx_input);
                    cards.add(new_panel);
                }
            }

            //display selection of tiles on window
            for(JPanel p : cards){
                main_panel.add(p);
            }

            revalidate();
            //repaint();

        } else {
            System.out.println("Cannot initialize, directory is empty");
        }

        //System.out.println(cards.size());
    }
}
