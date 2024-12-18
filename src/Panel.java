import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel {
    
    public final int SCREEN_WIDTH = 1500;
    public final int SCREEN_HEIGHT = 800;
    public final int DEF_TILE_SIZE = 14;
    public final int DEF_TILE_COL = 25, DEF_TILE_ROW = 25;

    public int scale = 1;
    public int tile_size = DEF_TILE_SIZE * scale;

    private Tile tile = null;
    private Tile blank = null;

    private Cursor cursor;
    public  DataHandler data_handler;

    public MouseHandler mouse;
    public Camera cam;
    public final Grid grid;
    
    public Settings settings;
    public TileList tile_list;
    
    public Panel(){

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(new BorderLayout());

        cam = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, tile_size, scale, DEF_TILE_COL, DEF_TILE_ROW);
        grid =  new Grid(DEF_TILE_COL, DEF_TILE_ROW);
        mouse = new MouseHandler(SCREEN_WIDTH, SCREEN_HEIGHT, this);
        cursor = new Cursor(SCREEN_WIDTH, SCREEN_HEIGHT, tile_size, scale, DEF_TILE_COL, DEF_TILE_ROW);

        data_handler = new DataHandler(this);
        settings = new Settings(data_handler);
        tile_list = new TileList(data_handler);

        this.add(settings, BorderLayout.WEST);
        this.add(tile_list, BorderLayout.EAST);

        tile = new Tile("void.png", "void", 0, false, false);
        blank = new Tile("void.png", "void", 0, false, false);

        this.addMouseMotionListener(mouse);
        this.addMouseListener(mouse);
        this.addMouseWheelListener(mouse);
        this.addKeyListener(new WindowHandler(this));
    }

    public void updateSelectedTile(Tile selected_tile, boolean is_editing){
        //check if received tile is the same as previous, meaning, you are deselecting
        if(this.tile == selected_tile && !is_editing){
            this.tile = blank;
        }
        //else assign the new one
        else{
            this.tile = selected_tile;
        }
    }

    private final ActionListener timer_listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){

            cam.updatePosition(mouse, scale, DEF_TILE_SIZE);
            cursor.updatePosition(mouse, scale, DEF_TILE_SIZE);

            //dictate how much scale will change when mouse wheel is scrolled
            scale = mouse.get_scale_factor();
            tile_size = scale * DEF_TILE_SIZE;

            repaint();
        }
    };

    public void startClock(){
        Timer timer = new Timer(10, timer_listener);
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        grid.display(g, cam, scale, DEF_TILE_SIZE, tile, mouse);
        cursor.displayTile(g, tile);
    }

}
