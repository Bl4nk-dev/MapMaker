
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuDragMouseEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author piotr
 */
public class MapLayout extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ChangeListener {

    public static int tileSize = 60;

    public static int SelectedTile = 0, SelectedObject = 0;
    public static boolean drawTile = true, drawAll = false, grid = true, pixels1 = true, pixels4 = false, pixels9 = false;
    BufferedImage panel;
    static int[][][] cMap = new int[2][13][23];
    public static JViewport view = new JViewport();
    public static int width = cMap[0][0].length, height = cMap[0].length, dimension = cMap.length;

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;

    public MapLayout() {
        setPreferredSize(new Dimension(width * tileSize, height * tileSize));
        addMouseListener(this);
        view.addChangeListener(this);
        addMouseMotionListener(this);
        //addMouseWheelListener(this);

//        
//                new MouseMotionAdapter() // anonymous inner class
//        {
//            // store drag coordinates and repaint
//            public void mouseDragged(MouseEvent e) {
//                // System.out.println(SelectedTile);
//                
//            }
//                    for (int d = 0; d < dimension - 1; d++) {
//                        for (int i = 0; i < height; i++) {
//                            for (int j = 0; j < width; j++) {
//
//                                if (e.getX() <= (j + 1) * 60 & e.getX() > 0
//                                        & e.getY() <= (i + 1) * 60 & e.getY() > 0) {
//
//                                    cMap[d][i][j] = SelectedTile;
//
//                                    repaint();
//                                    return;
//                                }
//
//                            }
//                        }
//
//                    }
//                    for (int d = 1; d < dimension; d++) {
//                        for (int i = 0; i < height; i++) {
//                            for (int j = 0; j < width; j++) {
//
//                                if (e.getX() <= (j + 1) * 60 & e.getX() > 0
//                                        & e.getY() <= (i + 1) * 60 & e.getY() > 0) {
//
//                                    cMap[d][i][j] = SelectedObject;
//
//                                    repaint();
//                                    return;
//                                }
//
//                            }
//                        }
//                    }
        // end method mouseDragged
        // end anonymous inner class
        // end call to addMouseMotionListener
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        int viewXcords = Math.abs(view.getView().getX());
        int viewYcords = Math.abs(view.getView().getY());
        int viewX = (int) Math.floor(viewXcords / tileSize);
        int viewY = (int) Math.floor(viewYcords / tileSize);
        int viewXmax = (int) Math.floor((viewXcords + view.getWidth()) / tileSize);
        int viewYmax = (int) Math.floor((viewYcords + view.getHeight()) / tileSize);
//        System.out.println(viewX + "     " +viewY + "     " +viewXmax + "     " +viewYmax 
//                + "     ");
        int k = 0;

        Graphics2D g2 = (Graphics2D) g;
        if (zoomer) {
            AffineTransform at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            g2.transform(at);

            zoomer = false;

        }
        if (drawAll == false) {
            for (int i = viewY; i < height & i <= viewYmax; i++) {
                int d = 0;
                for (int j = viewX; j < width & j <= viewXmax; j++) {

                    k = cMap[d][i][j];

                    for (int p = 0; p < MapTilesMenu.ImagesList.length; p++) {
                        if (k == Integer.parseInt(MapTilesMenu.ImagesList[p].substring(0, 3))) {

                            Image image = new ImageIcon(MapTilesMenu.path + MapTilesMenu.ImagesList[p]).getImage();
                            g.drawImage(image, image.getWidth(null) * j, image.getHeight(null) * i, this);
                            break;

//                        if (image.getWidth(null) == tileSize & image.getHeight(null) == tileSize) {
//                            g.drawImage(image, image.getWidth(null) * j, image.getHeight(null) * i, this);
//                        } else {
//                            g.setColor(Color.red);
//                            g.drawOval(j * 60, i * 60, 60, 60);
//                            g.drawString("Too Big", j * 60 + 10, i * 60 + 35);
//                        }
                        } else if (k > Integer.parseInt(MapTilesMenu.ImagesList[p].substring(0, 3))) {

                            g.setColor(Color.red);
                            g.setFont(new Font("Ariel", Font.BOLD, 10));

                            String top = "   No Tile";
                            String bot = "     ID: " + k;

                            g.drawString(top, j * 60 + 2, i * 60 + 30);
                            g.drawString(bot, j * 60 + 2, i * 60 + 45);

                        }

                    }

                }

            }

            for (int i = viewY; i < height & i <= viewYmax; i++) {
                int d = 1;
                for (int j = viewX; j < width & j >= viewX & j <= viewXmax; j++) {

                    k = cMap[d][i][j];

                    for (int p = 0; p < MapTilesMenu.ImagesList2.length; p++) {
                        if (k == Integer.parseInt(MapTilesMenu.ImagesList2[p].substring(0, 3))) {
                            Image image = new ImageIcon(MapTilesMenu.tiles2Path + MapTilesMenu.ImagesList2[p]).getImage();
                            g.drawImage(image, (int) (tileSize * j - ((Math.abs(image.getWidth(null) - tileSize)) / 2)),
                                    (int) (tileSize * i - (Math.abs(image.getHeight(null) - tileSize))), this);
                            break;
                        } else if (p < MapTilesMenu.NpcList.length) {
                            if (k == Integer.parseInt(MapTilesMenu.NpcList[p].substring(0, 3))) {

                                Image image = new ImageIcon(MapTilesMenu.NpcPath + MapTilesMenu.NpcList[p]).getImage();
                                g.drawImage(image, (int) (tileSize * j - ((Math.abs(image.getWidth(null) - tileSize)) / 2)),
                                        (int) (tileSize * i - (Math.abs(image.getHeight(null) - tileSize))), this);
                                break;
                            }
                        } else if (p == MapTilesMenu.ImagesList2.length - 1) {

                            g.setColor(Color.red);
                            g.setFont(new Font("Ariel", Font.BOLD, 10));
                            String top = " No Object";
                            String bot = "     ID: " + k;
                            g.drawString(top, j * 60 + 2, i * 60 + 30);
                            g.drawString(bot, j * 60 + 2, i * 60 + 45);

                        }
                    }

                }
            }
        } else {
            for (int i = 0; i < height ; i++) {
                int d = 0;
                for (int j = 0; j < width ; j++) {

                    k = cMap[d][i][j];

                    for (int p = 0; p < MapTilesMenu.ImagesList.length; p++) {
                        if (k == Integer.parseInt(MapTilesMenu.ImagesList[p].substring(0, 3))) {

                            Image image = new ImageIcon(MapTilesMenu.path + MapTilesMenu.ImagesList[p]).getImage();
                            g.drawImage(image, image.getWidth(null) * j, image.getHeight(null) * i, this);
                            break;

//                        if (image.getWidth(null) == tileSize & image.getHeight(null) == tileSize) {
//                            g.drawImage(image, image.getWidth(null) * j, image.getHeight(null) * i, this);
//                        } else {
//                            g.setColor(Color.red);
//                            g.drawOval(j * 60, i * 60, 60, 60);
//                            g.drawString("Too Big", j * 60 + 10, i * 60 + 35);
//                        }
                        } else if (k > Integer.parseInt(MapTilesMenu.ImagesList[p].substring(0, 3))) {

                            g.setColor(Color.red);
                            g.setFont(new Font("Ariel", Font.BOLD, 10));

                            String top = "   No Tile";
                            String bot = "     ID: " + k;

                            g.drawString(top, j * 60 + 2, i * 60 + 30);
                            g.drawString(bot, j * 60 + 2, i * 60 + 45);

                        }

                    }

                }

            }

            for (int i = 0; i < height; i++) {
                int d = 1;
                for (int j = 0; j < width ; j++) {

                    k = cMap[d][i][j];

                    for (int p = 0; p < MapTilesMenu.ImagesList2.length; p++) {
                        if (k == Integer.parseInt(MapTilesMenu.ImagesList2[p].substring(0, 3))) {
                            Image image = new ImageIcon(MapTilesMenu.tiles2Path + MapTilesMenu.ImagesList2[p]).getImage();
                            g.drawImage(image, (int) (tileSize * j - ((Math.abs(image.getWidth(null) - tileSize)) / 2)),
                                    (int) (tileSize * i - (Math.abs(image.getHeight(null) - tileSize))), this);
                            break;
                        } else if (p < MapTilesMenu.NpcList.length) {
                            if (k == Integer.parseInt(MapTilesMenu.NpcList[p].substring(0, 3))) {

                                Image image = new ImageIcon(MapTilesMenu.NpcPath + MapTilesMenu.NpcList[p]).getImage();
                                g.drawImage(image, (int) (tileSize * j - ((Math.abs(image.getWidth(null) - tileSize)) / 2)),
                                        (int) (tileSize * i - (Math.abs(image.getHeight(null) - tileSize))), this);
                                break;
                            }
                        } else if (p == MapTilesMenu.ImagesList2.length - 1) {

                            g.setColor(Color.red);
                            g.setFont(new Font("Ariel", Font.BOLD, 10));
                            String top = " No Object";
                            String bot = "     ID: " + k;
                            g.drawString(top, j * 60 + 2, i * 60 + 30);
                            g.drawString(bot, j * 60 + 2, i * 60 + 45);

                        }
                    }

                }
            }
        }

//        for (int i = viewY; i < height & i <= viewYmax; i++) {
//            int d = 1;
//            for (int j = viewX; j < width & j >= viewX & j <= viewXmax; j++) {
//
//                k = cMap[d][i][j];
//
//                for (int p = 0; p < MapTilesMenu.NpcList.length; p++) {
//                    if (k == Integer.parseInt(MapTilesMenu.NpcList[p].substring(0, 3))) {
//                        
//                        Image image = new ImageIcon(MapTilesMenu.pathObjects + MapTilesMenu.NpcList[p]).getImage();
//                        g.drawImage(image, (int) (tileSize * j - ((Math.abs(image.getWidth(null) - tileSize)) / 2)),
//                                (int) (tileSize * i - (Math.abs(image.getHeight(null) - tileSize))), this);
//                        break;
//                    }else if (p == MapTilesMenu.NpcList.length -1 ) {
//                       
//                        g.setColor(Color.red);
//                        g.setFont(new Font("Ariel",Font.BOLD,10));
//                        String top =  "  No NPC";
//                        String bot = "     ID: "+ k;
//                        g.drawString(top, j * 60 + 2, i * 60 + 30);
//                        g.drawString(bot, j * 60 + 2, i * 60 + 45);
//                        
//                    }
//                }
//
//            }
//        }
        if (grid) {
            g.setColor(Color.LIGHT_GRAY);
            for (int i = viewY; i <= height & i <= viewYmax; i++) {

                g.drawLine(0, i * tileSize, width * tileSize, i * tileSize);
            }
            for (int j = viewX; j <= width & j <= viewXmax; j++) {
                g.drawLine(j * tileSize, 0, j * tileSize, height * tileSize);
            }

        }

    }

    public void setcMap(int[][][] cMap) {
        MapLayout.cMap = cMap;
    }

    public static int[][][] getcMap() {
        return cMap;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (SwingUtilities.isMiddleMouseButton(e)) {
            zoomFactor = 1;

            repaint();
        }

        if (drawTile) {

            int d = 0;
            if (e.getY() < tileSize * cMap[0].length
                    & e.getY() >= 0 & e.getX() >= 0
                    & e.getX() < tileSize * cMap[0][0].length) {
                int j = (int) Math.floor(e.getX() / tileSize);
                int i = (int) Math.floor(e.getY() / tileSize);

                if (pixels1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        cMap[d][i][j] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        cMap[d][i][j] = 0;
                    }
                } else if (pixels4) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = SelectedTile;
                        cMap[d][i][j + 1] = SelectedTile;
                        cMap[d][i + 1][j] = SelectedTile;
                        cMap[d][i + 1][j + 1] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                } else if (pixels9) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = SelectedTile;
                        cMap[d][i - 1][j] = SelectedTile;
                        cMap[d][i - 1][j + 1] = SelectedTile;
                        cMap[d][i][j - 1] = SelectedTile;
                        cMap[d][i][j] = SelectedTile;
                        cMap[d][i][j + 1] = SelectedTile;
                        cMap[d][i + 1][j - 1] = SelectedTile;
                        cMap[d][i + 1][j] = SelectedTile;
                        cMap[d][i + 1][j + 1] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = 0;
                        cMap[d][i - 1][j] = 0;
                        cMap[d][i - 1][j + 1] = 0;
                        cMap[d][i][j - 1] = 0;
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j - 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                }
                repaint();
                return;
            }
        } else {

            int d = 1;
            if (e.getY() < tileSize * cMap[0].length
                    & e.getY() >= 0 & e.getX() >= 0
                    & e.getX() < tileSize * cMap[0][0].length) {
                int j = (int) Math.floor(e.getX() / tileSize);
                int i = (int) Math.floor(e.getY() / tileSize);

                if (pixels1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        cMap[d][i][j] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        cMap[d][i][j] = 0;
                    }
                } else if (pixels4) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = SelectedObject;
                        cMap[d][i][j + 1] = SelectedObject;
                        cMap[d][i + 1][j] = SelectedObject;
                        cMap[d][i + 1][j + 1] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                } else if (pixels9) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = SelectedObject;
                        cMap[d][i - 1][j] = SelectedObject;
                        cMap[d][i - 1][j + 1] = SelectedObject;
                        cMap[d][i][j - 1] = SelectedObject;
                        cMap[d][i][j] = SelectedObject;
                        cMap[d][i][j + 1] = SelectedObject;
                        cMap[d][i + 1][j - 1] = SelectedObject;
                        cMap[d][i + 1][j] = SelectedObject;
                        cMap[d][i + 1][j + 1] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = 0;
                        cMap[d][i - 1][j] = 0;
                        cMap[d][i - 1][j + 1] = 0;
                        cMap[d][i][j - 1] = 0;
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j - 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                }

                repaint();
                return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Useful:
        // SwingUtilities.isLeftMouseButton(aMouseEvent);
        // SwingUtilities.isRightMouseButton(aMouseEvent);
        // SwingUtilities.isMiddleMouseButton(aMouseEvent);

        if (drawTile) {

            int d = 0;
            if (e.getY() < tileSize * cMap[0].length & e.getY() > 0
                    & e.getY() >= 0 & e.getX() >= 0
                    & e.getX() < tileSize * cMap[0][0].length & e.getX() > 0) {
                int j = (int) Math.floor(e.getX() / tileSize);
                int i = (int) Math.floor(e.getY() / tileSize);

                if (pixels1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        cMap[d][i][j] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        cMap[d][i][j] = 0;
                    }
                } else if (pixels4) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = SelectedTile;
                        cMap[d][i][j + 1] = SelectedTile;
                        cMap[d][i + 1][j] = SelectedTile;
                        cMap[d][i + 1][j + 1] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                } else if (pixels9) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = SelectedTile;
                        cMap[d][i - 1][j] = SelectedTile;
                        cMap[d][i - 1][j + 1] = SelectedTile;
                        cMap[d][i][j - 1] = SelectedTile;
                        cMap[d][i][j] = SelectedTile;
                        cMap[d][i][j + 1] = SelectedTile;
                        cMap[d][i + 1][j - 1] = SelectedTile;
                        cMap[d][i + 1][j] = SelectedTile;
                        cMap[d][i + 1][j + 1] = SelectedTile;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = 0;
                        cMap[d][i - 1][j] = 0;
                        cMap[d][i - 1][j + 1] = 0;
                        cMap[d][i][j - 1] = 0;
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j - 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                }

                repaint();
                return;
            }
        } else {

            int d = 1;
            if (e.getY() < tileSize * cMap[0].length
                    & e.getY() >= 0 & e.getX() >= 0
                    & e.getX() < tileSize * cMap[0][0].length) {
                int j = (int) Math.floor(e.getX() / tileSize);
                int i = (int) Math.floor(e.getY() / tileSize);

                if (pixels1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        cMap[d][i][j] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        cMap[d][i][j] = 0;
                    }
                } else if (pixels4) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = SelectedObject;
                        cMap[d][i][j + 1] = SelectedObject;
                        cMap[d][i + 1][j] = SelectedObject;
                        cMap[d][i + 1][j + 1] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1) {
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                } else if (pixels9) {
                    if (SwingUtilities.isLeftMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = SelectedObject;
                        cMap[d][i - 1][j] = SelectedObject;
                        cMap[d][i - 1][j + 1] = SelectedObject;
                        cMap[d][i][j - 1] = SelectedObject;
                        cMap[d][i][j] = SelectedObject;
                        cMap[d][i][j + 1] = SelectedObject;
                        cMap[d][i + 1][j - 1] = SelectedObject;
                        cMap[d][i + 1][j] = SelectedObject;
                        cMap[d][i + 1][j + 1] = SelectedObject;
                    } else if (SwingUtilities.isRightMouseButton(e) & i < cMap[0].length - 1 & j < cMap[0][0].length - 1
                            & j > 0 & i > 0) {
                        cMap[d][i - 1][j - 1] = 0;
                        cMap[d][i - 1][j] = 0;
                        cMap[d][i - 1][j + 1] = 0;
                        cMap[d][i][j - 1] = 0;
                        cMap[d][i][j] = 0;
                        cMap[d][i][j + 1] = 0;
                        cMap[d][i + 1][j - 1] = 0;
                        cMap[d][i + 1][j] = 0;
                        cMap[d][i + 1][j + 1] = 0;
                    }

                }

                repaint();
                return;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        view = MenuBar.panelPane.getViewport();
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomer = true;
        //Zoom in

        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

}
