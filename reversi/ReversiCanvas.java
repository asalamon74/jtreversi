package reversi;

import javax.microedition.lcdui.*;

/**
 * ReversiCanvas.java
 *
 *
 * Created: Sun Jul 28 11:32:06 2002
 *
 * @author Salamon Andras
 * @version
 */

public class ReversiCanvas extends Canvas {
    
    public static final int SIZE = 8;
    
    public ReversiCanvas(Display display) {
        this.display = display;
        width = getWidth();
        height = getHeight();
        sizex = width / SIZE;
        sizey = width / SIZE;
        selx = 0;
        sely = 0;
    }

    protected void paint(Graphics g) {
        int x = g.getClipX();
        int y = g.getClipY();
        int w = g.getClipWidth();
        int h = g.getClipHeight();

        // Draw the frame 
        g.setColor(0xffffff);
        g.fillRect(x, y, w, h);    
        drawTable(g);
        drawPiece(g,3,3,0);
        drawPiece(g,4,4,0);
        drawPiece(g,3,4,1);
        drawPiece(g,4,3,1);
        drawSelectionBox(g);
    }

    protected void drawTable(Graphics g) {
        g.setColor(0x000000);
        for( int i=0; i<SIZE; ++i ) {
            g.drawLine(0, i*sizey, width, i*sizey);
            g.drawLine(i*sizex, 0, i*sizex, height);
        }
    }

    protected void drawPiece(Graphics g,int row, int col, int player) {
        int x = row*sizex+sizex/6;
        int y = col*sizey+sizey/6;
        int w = 2*sizex/3;
        int h = 2*sizey/3;
        int sa = 0;
        int aa = 360;
        if( player == 0 ) {
            g.drawArc(x,y,w,h,sa,aa);
        } else {
            g.fillArc(x,y,w,h,sa,aa);
        }
    }

    protected void drawSelectionBox(Graphics g) {
        g.setColor(0x008f00);
        g.drawRect( selx*sizex, sely*sizey,sizex, sizey);
        g.drawRect( selx*sizex+1, sely*sizey+1,sizex-2, sizey-2);
    }

    public void keyPressed(int keyCode) {
        switch( getGameAction(keyCode) ) {
        case Canvas.UP: 
            sely = (sely + SIZE -1) % SIZE;
            repaint();
            break;
        case Canvas.DOWN: 
            sely = (sely + 1) % SIZE;
            repaint();
            break;
        case Canvas.LEFT: 
            selx = (selx + SIZE -1) % SIZE;
            repaint();
            break;
        case Canvas.RIGHT: 
            selx = (selx + 1) % SIZE;
            repaint();
            break;
        }
    }

    Display display;
    int width, height;
    int sizex, sizey;
    int selx, sely;
    
} // ReversiCanvas
