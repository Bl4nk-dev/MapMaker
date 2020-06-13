

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MapTilesMenu extends JPanel implements ActionListener {

    
    GridBagConstraints gbc, gbc2;
    JButton plusHeight, plusWidth, minusHeight, minusWidth, Load, plusButtons, minusButtons;
    static JLabel Height, Width, Size, file;
    JPanel tilesMenuButtons, ObjectButtons, ButtonMenus,ButtonMenustop,ButtonMenusbot;
    JTextField NewSize;
    JComboBox combo,combo2;
    String[] options = {"tiles"};
    String[] options2 = {"objects","npc"};
    int buttonsWidth = 3, buttonsHeight = 50;
    public static int SelectedTile = 0;
    public static String path, pathObjects = ("images/tiles2/"),
            tilesPath= "images/tiles2/",tiles2Path= "images/tiles2/",NpcPath = "images/npc/";
    JButton[][] buttons = new JButton[buttonsWidth][buttonsHeight], buttonsOB;
    public static String[] ImagesList, ImagesList2,NpcList;
//    public static void main(String[] args) {
//        MapTilesMenu m = new MapTilesMenu();
//        JFrame a = new JFrame();
//        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        a.add(m);
//        a.setVisible(true);
//                
//    }

    public MapTilesMenu() {
        setLayout(new BorderLayout());

        gbc = new GridBagConstraints();
        gbc2 = new GridBagConstraints();
//    box1 = new JButton(new ImageIcon("images/Save.png"));
//    box2 = new JButton(new ImageIcon("images/Save.png"));
//    box3 = new JButton(new ImageIcon("images/Save.png"));
//    box4 = new JButton(new ImageIcon("images/Save.png"));
        Insets ins = new Insets(1, 1, 1, 1);
        gbc.insets = ins;
        gbc2.insets = ins;
        JPanel topMenuButtons = new JPanel();

        topMenuButtons.setLayout(new GridBagLayout());
//        Load = new JButton("Load Directory");
//        Load.addActionListener(this);
//        Load.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

//        topMenuButtons.add(Load, gbc);
        add(topMenuButtons, BorderLayout.NORTH);

        file = new JLabel(MenuBar.filenameShort);
        gbc.gridx = 0;
        gbc.gridy = 3;

        topMenuButtons.add(file, gbc);

        tilesMenuButtons = new JPanel();
        JScrollPane scrollButtons1 = new JScrollPane(tilesMenuButtons,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollButtons1.getVerticalScrollBar().setUnitIncrement(16);

        tilesMenuButtons.setLayout(new GridBagLayout());

        ButtonMenus = new JPanel();
        ButtonMenus.setLayout(new GridLayout(2, 1, 0, 10));

        ButtonMenustop = new JPanel(new BorderLayout());
        ButtonMenustop.add(scrollButtons1,BorderLayout.CENTER);
        

        ObjectButtons = new JPanel();
        ObjectButtons.setLayout(new GridBagLayout());

        newButtonsObjects();
        JScrollPane scrollButtons2 = new JScrollPane(ObjectButtons,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollButtons2.getVerticalScrollBar().setUnitIncrement(16);
        
        ButtonMenusbot = new JPanel(new BorderLayout());
        ButtonMenusbot.add(scrollButtons2,BorderLayout.CENTER);
        
        
        ButtonMenus.add(ButtonMenustop);
        ButtonMenus.add(ButtonMenusbot);
        add(ButtonMenus, BorderLayout.CENTER);
        
        InitiateFilePaths();
        
        
        Combo();
        
        
        
        
        //add(scrollButtons2, BorderLayout.CENTER);  
        plusButtons = new JButton("+");
        minusButtons = new JButton("-");
        plusButtons.setPreferredSize(new Dimension(74, 30));
        minusButtons.setPreferredSize(new Dimension(74, 30));
        plusButtons.addActionListener(this);
        minusButtons.addActionListener(this);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        topMenuButtons.add(plusButtons, gbc);
        gbc.gridx = 1;
        topMenuButtons.add(minusButtons, gbc);
        
        
        SizeButtons();
        newButtons();

        path = "images/tiles/";
        IconChanger();
        IconChangerObjects();

//    buttons[0][0].setIcon(new ImageIcon("images/tiles/dirt.png"));
//    buttons[0][0].setText("");
//      
//    buttons[1][0].setIcon(new ImageIcon("images/tiles/grass.png"));
//    buttons[1][0].setText("");
//    
//    buttons[0][1].setIcon(new ImageIcon("images/tiles/sky.png"));
//    buttons[0][1].setHorizontalAlignment(SwingConstants.CENTER);
//    
//    buttons[0][1].setText("");
//    buttons[0][1].setBackground(Color.yellow);
        //buttons[0][1].setBorder(BorderFactory.createLineBorder(Color.yellow, 5));
//    box1.addActionListener(this);
//    
//    add(box1,gbc);
//    
//    gbc.gridy =1;
//    add(box2,gbc);
//    
//    gbc.gridy =2;
//    add(box3,gbc);
//    
//    gbc.gridy =3;
//    add(box4,gbc);
    }

    public void Combo(){        
        combo = new JComboBox(options);
        
        combo.addActionListener(this);
        ButtonMenustop.add(combo,BorderLayout.NORTH);
        
        combo2= new JComboBox(options2);
        combo2.addActionListener(this);
        ButtonMenusbot.add(combo2,BorderLayout.NORTH);
        
        }
    
    public void newButtons() {

        buttons = new JButton[buttonsWidth][buttonsHeight];
        for (int j = 0; j < buttons.length; j++) {

            for (int i = 0; i < buttons[j].length; i++) {
                buttons[j][i] = new JButton("");
                buttons[j][i].setPreferredSize(new Dimension(70, 70));
                buttons[j][i].addActionListener(this);

                gbc.gridx = j;
                gbc.gridy = i;

                tilesMenuButtons.add(buttons[j][i], gbc);
            }
        }

    }

    public void newButtonsObjects() {

        buttonsOB = new JButton[buttonsWidth][buttonsHeight];

        for (int j = 0; j < buttonsOB.length; j++) {

            for (int i = 0; i < buttonsOB[j].length; i++) {
                buttonsOB[j][i] = new JButton("");
                buttonsOB[j][i].setPreferredSize(new Dimension(70, 70));
                buttonsOB[j][i].addActionListener(this);

                gbc2.gridx = j;
                gbc2.gridy = i;

                ObjectButtons.add(buttonsOB[j][i], gbc2);
            }
        }

    }

    public void SizeButtons() {
        JPanel bottomTilesMenu = new JPanel();
        bottomTilesMenu.setLayout(new GridBagLayout());

        Height = new JLabel("Height");
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.gridwidth = 2;
        bottomTilesMenu.add(Height, gbc);
        gbc.gridwidth = 1;

        minusHeight = new JButton("-");
        minusHeight.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 0;
        gbc.gridy = 23;
        minusHeight.addActionListener(this);
        bottomTilesMenu.add(minusHeight, gbc);

        plusHeight = new JButton("+");
        plusHeight.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        gbc.gridy = 23;
        plusHeight.addActionListener(this);
        bottomTilesMenu.add(plusHeight, gbc);

        Height = new JLabel("Width");
        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.gridwidth = 2;
        bottomTilesMenu.add(Height, gbc);
        gbc.gridwidth = 1;

        minusWidth = new JButton("-");
        minusWidth.setPreferredSize(new Dimension(80, 30));

        gbc.gridx = 0;
        gbc.gridy = 25;
        minusWidth.addActionListener(this);
        bottomTilesMenu.add(minusWidth, gbc);

        plusWidth = new JButton("+");
        plusWidth.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        gbc.gridy = 25;
        plusWidth.addActionListener(this);
        bottomTilesMenu.add(plusWidth, gbc);

        Size = new JLabel("Map Size: " + MapLayout.width + " x " + MapLayout.height);
        gbc.gridx = 0;
        gbc.gridy = 27;
        gbc.gridwidth = 2;
        bottomTilesMenu.add(Size, gbc);
        gbc.gridwidth = 1;

        NewSize = new JTextField();
        JLabel setSize = new JLabel("Set Size");

        NewSize.setPreferredSize(new Dimension(80, 20));
        gbc.gridx = 0;
        gbc.gridy = 26;
        gbc.gridwidth = 1;

        NewSize.addActionListener(this);
        bottomTilesMenu.add(setSize, gbc);
        gbc.gridx = 1;
        bottomTilesMenu.add(NewSize, gbc);

        add(bottomTilesMenu, BorderLayout.SOUTH);

    }

    public void removeOldButtons() {
        for (int j = 0; j < buttons.length; j++) {

            for (int i = 0; i < buttons[j].length; i++) {
                buttons[j][i].setVisible(false);

            }
        }
        for (int j = 0; j < buttonsOB.length; j++) {

            for (int i = 0; i < buttonsOB[j].length; i++) {
                buttonsOB[j][i].setVisible(false);

            }
        }
//        tilesMenuButtons.removeAll();
//        ObjectButtons.removeAll();

    }

    public void copycMap() {
        int[][][] precMap = MapLayout.cMap.clone();
        MapLayout.cMap = new int[MapLayout.dimension][MapLayout.height][MapLayout.width];
        for (int d = 0; d < MapLayout.dimension & d < precMap.length; d++) {
            for (int i = 0; i < MapLayout.height & i < precMap[0].length; i++) {
                for (int j = 0; j < MapLayout.width & j < precMap[0][0].length; j++) {

                    MapLayout.cMap[d][i][j] = precMap[d][i][j];

                }
            }
            MenuBar.mapLayout.repaint();

        }
    }

    public void LoadImages() {

        //getFileSystemView().getDefaultDirectory()
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        jfc.setDialogTitle("Select a Folder");

        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Folder with PNG files", "png");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
//			filename = jfc.getSelectedFile().getName();
//			System.out.println(filename);

            String directory = jfc.getCurrentDirectory().toString();
//			System.out.println(directory);
            char[] dir = directory.toCharArray();
            for (int i = 0; i < dir.length; i++) {
                if (dir[i] == '\\') {
                    dir[i] = '/';
                }
            }
            directory = new String(dir);

//                        System.out.println(directory);
            path = directory + "/";
        }

    }

    public void IconChanger() {

        File folder = new File(path);
        String[] folderlist = folder.list();

        System.out.println("Images in tiles directory" + Arrays.toString(folderlist));

        int i = 0;
        int j = 0;
        int k = 0;
        for (; k < buttonsWidth * buttonsHeight;) {
            if (k < folderlist.length) {
                buttons[i][j].setIcon(new ImageIcon(path + folderlist[k]));
                //System.out.println(path + folderlist[k]);
            } else {
                buttons[i][j].setIcon(null);
            }

            k++;
            ImagesList = folderlist.clone();
            if (buttonsWidth == 5) {
                if (i % 5 == 0 | i % 5 == 1 | i % 5 == 2 | i % 5 == 3) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 4) {
                if (i % 4 == 0 | i % 4 == 1 | i % 4 == 2) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 3) {
                if (i % 3 == 0 | i % 3 == 1) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 2) {
                if (i % 2 == 0) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else {
                j++;
            }

        }
    }

    public static int[] getFileID(String[] filelist){
        int[] id = new  int[filelist.length] ;
        
        for(int i = 0;i<filelist.length;i++){
            String num =  filelist[i].substring(0, 3);
            //System.out.print(num + " ");
            
            id[i] = Integer.parseInt(num);
        }
        
        
        return id;
    }
    
    public void InitiateFilePaths(){
        File tilefolder = new File(tiles2Path);
        ImagesList = tilefolder.list();
        
        File tile2folder = new File(tilesPath);
        ImagesList2 = tile2folder.list();
        
        File Npcfolder = new File(NpcPath);
        NpcList = Npcfolder.list();
        System.out.println("Images in NPC directory" + Arrays.toString(NpcList));
        
    }
    
    public void IconChangerObjects() {

        File folder = new File(pathObjects);
        String[] folderlist = folder.list();

        System.out.println("Images in Objects directory" + Arrays.toString(folderlist));

        int i = 0;
        int j = 0;
        int k = 0;
        for (; k < buttonsWidth * buttonsHeight;) {
            if (k < folderlist.length) {
                buttonsOB[i][j].setIcon(new ImageIcon(pathObjects + folderlist[k]));
                //System.out.println(path + folderlist[k]);
            } else {
                buttonsOB[i][j].setIcon(null);
            }

            k++;
            if(pathObjects.equals("images/tiles2/")){
                ImagesList2 = folderlist.clone();
            }
            else if(pathObjects.equals("images/npc/")){
                NpcList = folderlist.clone();
            }
            
            
            if (buttonsWidth == 5) {
                if (i % 5 == 0 | i % 5 == 1 | i % 5 == 2 | i % 5 == 3) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 4) {
                if (i % 4 == 0 | i % 4 == 1 | i % 4 == 2) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 3) {
                if (i % 3 == 0 | i % 3 == 1) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else if (buttonsWidth == 2) {
                if (i % 2 == 0) {
                    i++;
                } else {
                    i = 0;
                    j++;
                }
            } else {
                j++;
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(combo)){
            if(combo.getSelectedItem().toString().equals("tiles")){
            path = "images/tiles/";
            IconChanger();
            MenuBar.mapLayout.repaint();
            }
            
        }
        else if(e.getSource().equals(combo2)){
            if(combo2.getSelectedItem().toString().equals("objects")){
            pathObjects = "images/tiles2/";
            IconChangerObjects();
            MenuBar.mapLayout.repaint();
            }else if(combo2.getSelectedItem().toString().equals("npc")){
                
                pathObjects = "images/npc/";
            IconChangerObjects();
            MenuBar.mapLayout.repaint();
            }
        }
        else
        if (e.getSource().equals(plusButtons)) {
            if (buttonsWidth < 5) {

                removeOldButtons();
                buttonsWidth++;
                newButtons();
                newButtonsObjects();
                IconChanger();
                IconChangerObjects();

                MenuBar.mapMenu.revalidate();
            }
        } else if (e.getSource().equals(minusButtons)) {
            if (buttonsWidth > 1) {

                removeOldButtons();
                buttonsWidth--;
                newButtons();
                newButtonsObjects();
                IconChanger();
                IconChangerObjects();

                MenuBar.mapMenu.revalidate();

            }

        } else if (e.getSource().equals(NewSize)) {
            String size = String.format(e.getActionCommand());
            String[] split = size.split("x", 2);

            if (Integer.parseInt(split[0].trim()) > 3 
                    & Integer.parseInt(split[1].trim()) > 3 ) {
                MapLayout.width = Integer.parseInt(split[0].trim());
                MapLayout.height = Integer.parseInt(split[1].trim());
                copycMap();
                Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
                MenuBar.mapLayout.setPreferredSize(new Dimension(MapLayout.width*MapLayout.tileSize,
                        MapLayout.height*MapLayout.tileSize));
                MenuBar.mapLayout.revalidate();
            }
//        } else if (e.getSource().equals(Load)) {
//            LoadImages();
//            IconChanger();
//            IconChangerObjects();
//            MenuBar.mapLayout.repaint();

        } else if (e.getSource().equals(plusHeight)) {
            
                MapLayout.height = MapLayout.height + 1;
                
                Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
                copycMap();
                MenuBar.mapLayout.setPreferredSize(new Dimension(MapLayout.width*MapLayout.tileSize,
                        MapLayout.height*MapLayout.tileSize));
                MenuBar.mapLayout.revalidate();
                
                MapLayout.view =  MenuBar.panelPane.getViewport();
                //System.out.println(MapLayout.view.getViewRect());
            

        } else if (e.getSource().equals(minusHeight)) {
            if (MapLayout.height > 4) {
                MapLayout.height = MapLayout.height - 1;
                
                Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
                copycMap();
                MenuBar.mapLayout.setPreferredSize(new Dimension(MapLayout.width*MapLayout.tileSize,
                        MapLayout.height*MapLayout.tileSize));
                MenuBar.mapLayout.revalidate();
                
                MapLayout.view =  MenuBar.panelPane.getViewport();
                //System.out.println(MapLayout.view.getViewRect());
                
            }
        } else if (e.getSource().equals(plusWidth)) {
           
                MapLayout.width = MapLayout.width + 1;
                Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
                copycMap();
                MenuBar.mapLayout.setPreferredSize(new Dimension(MapLayout.width*MapLayout.tileSize,
                        MapLayout.height*MapLayout.tileSize));
                MenuBar.mapLayout.revalidate();
                
                
                MapLayout.view =  MenuBar.panelPane.getViewport();
                //System.out.println(MapLayout.view.getViewRect());
            
        } else if (e.getSource().equals(minusWidth)) {
            if (MapLayout.width > 4) {
                MapLayout.width = MapLayout.width - 1;
                Size.setText("Map Size: " + MapLayout.width + " x " + MapLayout.height);
                copycMap();
                MenuBar.mapLayout.setPreferredSize(new Dimension(MapLayout.width*MapLayout.tileSize,
                        MapLayout.height*MapLayout.tileSize));
                MenuBar.mapLayout.revalidate();

                MapLayout.view =  MenuBar.panelPane.getViewport();
                //System.out.println(MapLayout.view.getViewRect());
            }
        }


            int[] IDs = getFileID(ImagesList);
            int[] IDs2= getFileID(ImagesList2);
            int[] IDsNpc = getFileID(NpcList);
        for (int i = 0; i < buttonsWidth; i++) {
            for (int j = 0; j < buttonsHeight; j++) {
                if (e.getSource().equals(buttons[i][j])) {
                    buttons[i][j].setBackground(Color.red);
                    buttonsOB[i][j].setBackground(null);
                    MapLayout.drawTile = true;
                    if(i + j * buttonsWidth< IDs.length)
                    SelectedTile = IDs[i + j * buttonsWidth];
                    else{
                        SelectedTile = 9999;
                    }
                    MapLayout.SelectedTile = SelectedTile;
                } else if (e.getSource().equals(buttonsOB[i][j])) {
                    MapLayout.drawTile = false;
                    
                    if(i + j * buttonsWidth< IDs2.length & pathObjects.equals(tiles2Path)){
                    MapLayout.SelectedObject = IDs2[i + j * buttonsWidth];
                    }
                    else if(i + j * buttonsWidth< IDs2.length & pathObjects.equals(NpcPath)){
                        MapLayout.SelectedObject = IDsNpc[i + j * buttonsWidth];
                    }
                    else{
                        MapLayout.SelectedObject = 9999;
                    }
                    
                    buttonsOB[i][j].setBackground(Color.red);
                    buttons[i][j].setBackground(null);

                } else {
                    buttons[i][j].setBackground(null);
                    buttonsOB[i][j].setBackground(null);
                }

            }
        }
    }
}
