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
    
    public ReversiCanvas(J2MEReversi boss, Display display) {
        this.boss = boss;
        this.display = display;
        width = getWidth();
        height = getHeight();
        sizex = width / J2MEReversi.SIZE;
        sizey = height / J2MEReversi.SIZE;
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
        drawMessage(g);
    }

    protected void drawBoard(Graphics g) {
        g.setColor(0x000000);
        for( int i=0; i<=J2MEReversi.SIZE; ++i ) {
            g.drawLine(0, i*sizey, J2MEReversi.SIZE*sizex, i*sizey);
            g.drawLine(i*sizex, 0, i*sizex, J2MEReversi.SIZE*sizey);
        }
    }

    protected void drawTable(Graphics g, ReversiTable t) {
        for( int i=0; i<J2MEReversi.SIZE; ++i ) {
            for( int j=0; j<J2MEReversi.SIZE; ++j ) {
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

    protected void drawMessage(Graphics g) {
        if( message != null ) {
            int messageWidth = g.getFont().stringWidth(message)+10;
            int messageHeight = g.getFont().getHeight();
            g.setColor(0xeeeeee);
            int cornerX = (width - messageWidth)/2;
            int cornerY = (height - messageHeight)/2;
            // TODO: break line if too long
            g.fillRect(cornerX, cornerY, messageWidth, messageHeight);
            g.setColor(0x000000);
            g.drawRect(cornerX, cornerY, messageWidth, messageHeight);
            g.drawString(message, cornerX+5, cornerY, g.TOP|g.LEFT);                
        }
    }
    public void keyPressed(int keyCode) {
        switch( getGameAction(keyCode) ) {
        case Canvas.UP: 
            sely = (sely + J2MEReversi.SIZE -1) % J2MEReversi.SIZE;
            repaint();
            break;
        case Canvas.DOWN: 
            sely = (sely + 1) % J2MEReversi.SIZE;
            repaint();
            break;
        case Canvas.LEFT: 
            selx = (selx + J2MEReversi.SIZE -1) % J2MEReversi.SIZE;
            repaint();
            break;
        case Canvas.RIGHT: 
            selx = (selx + 1) % J2MEReversi.SIZE;
            repaint();
            break;
        case Canvas.FIRE: 
            if( message != null ) {
                message = null;
            } else {
                boss.nextTurn(selx, sely);
            }
            repaint();
            break;
        }
    }

    public void setMessage(String message) {
        this.message = message;
        repaint();
    }

    protected J2MEReversi boss;
    protected Display display;
    protected String message;
    int width, height;
    int sizex, sizey;
    int selx, sely;
    
} // ReversiCanvas
