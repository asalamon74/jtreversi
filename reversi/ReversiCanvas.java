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
    
    public ReversiCanvas(J2MEReversi boss, Display display) {
        this.boss = boss;
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
        drawBoard(g);
        drawTable(g, boss.table);
        drawSelectionBox(g);
    }

    protected void drawBoard(Graphics g) {
        g.setColor(0x000000);
        for( int i=0; i<SIZE; ++i ) {
            g.drawLine(0, i*sizey, width, i*sizey);
            g.drawLine(i*sizex, 0, i*sizex, height);
        }
    }

    protected void drawTable(Graphics g, ReversiTable t) {
        for( int i=0; i<SIZE; ++i ) {
            for( int j=0; j<SIZE; ++j ) {
                if( t.getItem(i,j) != 0 ) {
                    drawPiece(g, i, j, t.getItem(i,j));
                }
            }
        }
    }

    protected void drawPiece(Graphics g,int row, int col, int player) {
        int x = row*sizex+sizex/6;
        int y = col*sizey+sizey/6;
        int w = 2*sizex/3;
        int h = 2*sizey/3;
        int sa = 0;
        int aa = 360;
        if( player == 1 ) {
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
        case Canvas.FIRE: 
            System.out.println("Fire");
            boss.nextTurn(selx, sely);
            repaint();
            break;
        }
    }

    protected J2MEReversi boss;
    protected Display display;
    int width, height;
    int sizex, sizey;
    int selx, sely;
    
} // ReversiCanvas
