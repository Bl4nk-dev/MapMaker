
import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MenuBar extends JFrame implements MenuListener, ActionListener {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    JMenuBar menuBar;
    static MapLayout mapLayout;
    static MapTilesMenu mapMenu;
    static JScrollPane panelPane;
    static JFrame fr;
    JMenu File, saveM, Edit, Help;
    JMenuItem newMap, load, saveBrowse, saveDef, saveImage, clear, about;
    JCheckBoxMenuItem grid, pixels1, pixels4, pixels9;
    ButtonGroup check;
    static String filename = "maps/LastOpenedMap.txt", filenameShort = "LastOpenedMap.txt";
    static String Previousfilename = "maps/LastOpenedMap.txt";
    static boolean cleared = false;

    static int[][][] cMap = mapLayout.getcMap();

    public static void main(String[] args) {

        fr = new MenuBar();
        mapMenu = new MapTilesMenu();
        mapLayout = new MapLayout();
        readMap(filename);

        panelPane = new JScrollPane(mapLayout);

        //panelPane.setPreferredSize(new Dimension(800,800));
        panelPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelPane.getVerticalScrollBar().setUnitIncrement(16);
        panelPane.getHorizontalScrollBar().setUnitIncrement(16);
//        JViewport view= new JViewport();
//        view.setViewPosition(new Point(500, 500));
//      
//        view.setViewSize(new Dimension(100, 100));
//        
//        panelPane.setViewport(view);
        MapLayout.view = panelPane.getViewport();
        mapLayout.repaint();
        fr.setLayout(new BorderLayout());
        fr.add(mapMenu, BorderLayout.WEST);
        fr.add(panelPane, BorderLayout.CENTER);

        fr.setSize(1000, 600);
        fr.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        fr.setLocationRelativeTo(null);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
        }

        fr.setVisible(true);

    }

    public MenuBar() {

        setLayout(new FlowLayout());

        setTitle("Map maker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();

        File = new JMenu("File");
        //File.addMenuListener(new thisMenuListener());
        menuBar.add(File);

        //newMap, load, saveBrowse,saveDef
        newMap = new JMenuItem("New Map",
                new ImageIcon("images/New_map.png"));
        newMap.addActionListener(this);
        File.add(newMap);

        load = new JMenuItem("Load Map",
                new ImageIcon("images/browse.png"));
        load.addActionListener(this);
        File.add(load);

        saveM = new JMenu("Save");
        // saveM.addMenuListener(new thisMenuListener());
        File.add(saveM);

        saveBrowse = new JMenuItem("Save As...",
                new ImageIcon("images/browse.png"));
        saveBrowse.addActionListener(this);
        saveM.add(saveBrowse);

        saveDef = new JMenuItem("Save",
                new ImageIcon("images/Save.png"));
        saveDef.addActionListener(this);
        saveM.add(saveDef);

        saveImage = new JMenuItem("Save Image",
                new ImageIcon("images/Save.png"));
        saveImage.addActionListener(this);
        saveM.add(saveImage);

        Edit = new JMenu("Edit");
        // Edit.addMenuListener(new thisMenuListener());
        menuBar.add(Edit);

        clear = new JMenuItem("Clear");
        clear.addActionListener(this);
        Edit.add(clear);

        grid = new JCheckBoxMenuItem("Grid");
        grid.setSelected(true);
        grid.addActionListener(this);
        Edit.add(grid);

        pixels1 = new JCheckBoxMenuItem("1 x 1 brush");
        pixels1.setSelected(true);
        pixels1.addActionListener(this);
        Edit.add(pixels1);

        pixels4 = new JCheckBoxMenuItem("2 x 2 brush");
        pixels4.setSelected(false);
        pixels4.addActionListener(this);
        Edit.add(pixels4);

        pixels9 = new JCheckBoxMenuItem("3 x 3 brush");
        pixels9.setSelected(false);
        pixels9.addActionListener(this);
        Edit.add(pixels9);

        check = new ButtonGroup();
        check.add(pixels1);
        check.add(pixels4);
        check.add(pixels9);

        Help = new JMenu("Help");
        Help.addMenuListener(this);
        menuBar.add(Help);

        about = new JMenuItem("About",
                new ImageIcon("images/info.png"));
        about.addActionListener(this);

        Help.add(about);

        //////////////////////////////MOVE LATER////////////////
        //newMap();
        this.setJMenuBar(menuBar);
    }

    private void saveImage() {
        BufferedImage imagebuf = null;
        String directory = null;

        //getFileSystemView().getDefaultDirectory()
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        jfc.setDialogTitle("Select a file");

        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Map PNG & JPEG Files", "png","jpeg");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showDialog(null, "Save");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            directory = jfc.getSelectedFile().getAbsolutePath();
            //filenameShort = jfc.getSelectedFile().getName().toString();
//			System.out.println(directory);
            char[] dir = directory.toCharArray();
            for (int i = 0; i < dir.length; i++) {
                if (dir[i] == '\\') {
                    dir[i] = '/';
                }
            }
            directory = new String(dir);

//                        System.out.println(directory);
        }

        MapLayout.drawAll = true;
        mapLayout.repaint();
        try {
            Rectangle bounds = new Rectangle(mapLayout.width * mapLayout.tileSize,
                    mapLayout.height * mapLayout.tileSize);
            imagebuf = new Robot().createScreenCapture(bounds);
        } catch (AWTException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Graphics graphics = imagebuf.createGraphics();
        mapLayout.paint(graphics);
        try {
            ImageIO.write(imagebuf, "png", new File(directory));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }

        MapLayout.drawAll = false;
        mapLayout.repaint();
    }

    public static void writeMap(int[][][] arr, String cFilename) {
        Formatter map;

        try {
            map = new Formatter(cFilename);

            map.format(Integer.toString(arr.length) + " "
                    + Integer.toString(arr[0][0].length)
                    + " " + Integer.toString(arr[0].length) + "\n");

            //int[] IDs = getFileID(MapTilesMenu.ImagesList);
            for (int d = 0; d < MapLayout.dimension; d++) {
                for (int i = 0; i < MapLayout.height; i++) {
                    for (int j = 0; j < MapLayout.width; j++) {

                        map.format(Integer.toString(MapLayout.cMap[d][i][j]) + " ");

                        if (j == MapLayout.width - 1) {
                            map.format("\n");
                        }
                    }
                }
                map.format("\n");
            }
            map.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found" + ex);
        }

    }

    public static void readMap(String FileName) {

        filename = FileName;
        try {
            Scanner input = new Scanner(new File(filename));
            MapLayout.dimension = input.nextInt();
            MapLayout.width = input.nextInt();
            MapLayout.height = input.nextInt();
            mapLayout.setPreferredSize(new Dimension(MapLayout.width * MapLayout.tileSize,
                    MapLayout.height * MapLayout.tileSize));

            mapMenu.Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
            mapLayout.revalidate();
            cMap = new int[MapLayout.dimension][MapLayout.height][MapLayout.width];
            for (int d = 0; d < MapLayout.dimension; d++) {
                for (int i = 0; i < MapLayout.height; i++) {

                    for (int j = 0; j < MapLayout.width; j++) {
                        cMap[d][i][j] = input.nextInt();

                    }
                    //System.out.println(Arrays.toString(MapLayout.cMap[i]));
                }
            }
            mapLayout.setcMap(cMap);
            mapLayout.repaint();

        } catch (FileNotFoundException ex) {
            System.err.println("File not found\n" + ex);

        }

    }

    public void newMap() {
        cMap = new int[MapLayout.dimension][MapLayout.height][MapLayout.width];

//            for(int i =0; i < MapLayout.height;i++){
//                System.out.println(Arrays.toString(cMap[i]));
//            } 
        mapLayout.setcMap(cMap);
        mapLayout.repaint();

    }

    public void fileChooserOpen() {

        //getFileSystemView().getDefaultDirectory()
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        jfc.setDialogTitle("Select a file");

        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Map Text Files", "txt");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            //			filename = jfc.getSelectedFile().getName();
            //			System.out.println(filename);

            String directory = jfc.getSelectedFile().getAbsolutePath();
            filenameShort = jfc.getSelectedFile().getName().toString();
//                            System.out.println(directory);
            char[] dir = directory.toCharArray();
            for (int i = 0; i < dir.length; i++) {
                if (dir[i] == '\\') {
                    dir[i] = '/';
                }
            }
            directory = new String(dir);

//                            System.out.println(directory);
            filename = directory;
        }
        readMap(filename);
        mapLayout.setcMap(cMap);
        mapLayout.repaint();
    }

    public void fileChooserSave() {

        //getFileSystemView().getDefaultDirectory()
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        jfc.setDialogTitle("Select a file");

        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Map Text Files", "txt");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showDialog(null, "Save");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String directory = jfc.getSelectedFile().getAbsolutePath();
            filenameShort = jfc.getSelectedFile().getName().toString();
//			System.out.println(directory);
            char[] dir = directory.toCharArray();
            for (int i = 0; i < dir.length; i++) {
                if (dir[i] == '\\') {
                    dir[i] = '/';
                }
            }
            directory = new String(dir);

//                        System.out.println(directory);
            filename = directory;
        }

        writeMap(MapLayout.cMap, filename);

    }

    public void Clear() {

        cMap = new int[MapLayout.dimension][MapLayout.height][MapLayout.width];

        mapLayout.setcMap(cMap);
        mapLayout.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(newMap)) {
            newMap();
        }

        if (e.getSource().equals(clear)) {

            Clear();

        }
        if (e.getSource().equals(grid)) {
            if (grid.isSelected()) {
                MapLayout.grid = true;
                mapLayout.repaint();
            } else {
                MapLayout.grid = false;
                mapLayout.repaint();
            }
        }

        if (e.getSource().equals(pixels1) | e.getSource().equals(pixels4) | e.getSource().equals(pixels9)) {
            if (pixels1.isSelected()) {
                MapLayout.pixels1 = true;
                MapLayout.pixels4 = false;
                MapLayout.pixels9 = false;
                mapLayout.repaint();
            } else if (pixels4.isSelected()) {
                MapLayout.pixels4 = true;
                MapLayout.pixels1 = false;
                MapLayout.pixels9 = false;
                mapLayout.repaint();
            } else if (pixels9.isSelected()) {
                MapLayout.pixels9 = true;
                MapLayout.pixels1 = false;
                MapLayout.pixels4 = false;
                mapLayout.repaint();
            }
        }

        if (e.getSource().equals(load)) {

            fileChooserOpen();
            writeMap(MapLayout.cMap, Previousfilename);
            MapTilesMenu.file.setText(filenameShort);

        }

        if (e.getSource().equals(saveDef)) {
            writeMap(cMap, filename);
            writeMap(MapLayout.cMap, Previousfilename);
        }

        if (e.getSource().equals(saveImage)) {
            System.out.println("Image Saved");
            if (grid.isSelected()) {
                MapLayout.grid = false;
                mapLayout.repaint();
                saveImage();
                MapLayout.grid = true;
            } else {
                mapLayout.repaint();
                saveImage();
            }
        }

        if (e.getSource().equals(saveBrowse)) {
            fileChooserSave();
            writeMap(MapLayout.cMap, Previousfilename);
            MapTilesMenu.file.setText(filenameShort);
        }

        if (e.getSource().equals(about)) {
            JFrame Infobox = new JFrame("About");
            JPanel info = new JPanel();

            JScrollPane infoScroll = new JScrollPane(info,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            Infobox.add(infoScroll);
            JLabel information = new JLabel("<html>Use the panel to the left to change the selected tile<br/>"
                    + "You can use the + and - to change the size of the map<br/>"
                    + "For load directory you need to select a png file and it will select the current folder<br/>"
                    + "The set Size box requires input in the format \"width x height\" "
                    + "<br/><br/><br/>Version 2.0 changes: <br/>*Load directory button added<br/>*Scrollbars were added<br/>"
                    + "*Set Size (You can now set any size you want without the + and -)<br/>"
                    + "**Pls don't exceed 1000 tiles though<br/>"
                    + "<br/><br/>Version 3.0 changes: <br/>*Now you can never exceed 1000x1000<br/>"
                    + "*Drag functionality added<br/>"
                    + "<br/><br/>Version 4.0 changes: <br/>*You can now name image files however you want<br/>"
                    + "*Size limit for the smallest possible map is now 4x4<br/>"
                    + "**After directory change, buttons will adjust and the map will change.<br/>"
                    + "   If the new directory has less image files than the previous directory<br/>"
                    + "   some tiles may become invisible due to the lack of png files<br/>"
                    + "<br/><br/>Version 5.0 changes: <br/>*Added ability to change the amount of buttons seen in the button menu<br/>"
                    + " (Use the + and - buttons to adjuts this amount (min 1 column, max 5))<br/>"
                    + "*Current file name is now displayed above the buttons menu<br/>"
                    + "*Now when no texture is available in the directory the tile will have a \"No Texture\" sign<br/>"
                    + "*Also when a texture is too big the \"Too Big\" sign will be visible<br/>"
                    + "<br/><br/>Version 6.0 changes: <br/>*Code was optimised to accomodate 3d arrays used to display different layers<br/>"
                    + "*Added a new buttons menu for foreground objects<br/>"
                    + "*Grid can now be disabled in the Edit Option<br/>"
                    + "*Scrollbar for map was fixed<br/>"
                    + "*Right click functionality returns!!!<br/>"
                    + "**Now for left click the selected tile is placed and for right click a clear tile is placed<br/>"
                    + "*Great Performance boost !!!!!!!<br/>"
                    + " (Instead of drawing all the tiles at once the code now only draws the tiles visible on the screen)<br/>"
                    + "*Map size limit was lifted, however 17500x17500 seems to be the limit.<br/>"
                    + " (don't try to save it btw, the app will be unusable until the file write is done,<br/>"
                    + "  might also crash Netbeans, also that file will be ususable and LastOpenedMap.txt will have to be<br/>"
                    + "  manualy replaced with a map of usable sizes)<br/>"
                    + "<br/><br/><br/>In Upcoming updates:<br/>*Simulation mode (As if)<br/>*Auto map generation (nope)<br/>"
                    + "*Depression progress bar!!!!  (I might actually do this one xD)"
                    + "<html>");
            info.add(information);

            Infobox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            Infobox.setSize(new Dimension(500, 600));
            Infobox.setLocationRelativeTo(null);
            Infobox.setVisible(true);

        }

    }

    @Override
    public void menuSelected(MenuEvent e) {

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }

    public static MapTilesMenu getMapMenu() {
        return mapMenu;
    }

}
