package reversi;

import javax.microedition.lcdui.*;
import java.util.Timer;
import java.util.TimerTask;


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

    public String []playerNames;
    private static final int SIZE_LIMIT = 8;
    
    public ReversiCanvas(J2MEReversi boss, Display display) {
        this.boss = boss;
        this.display = display;
        colored = display.isColor() && display.numColors() > 127;
        playerNames = new String[2];
        if( colored ) {
            playerNames[0] = "Red";
            playerNames[1] = "Blue";
        } else {
            playerNames[0] = "White";
            playerNames[1] = "Black";
        }            

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
        if( sizex < SIZE_LIMIT || sizey < SIZE_LIMIT ) {
            int x,y;
            for( int i=0; i<J2MEReversi.SIZE; ++i ) {
                for( int j=0; j<J2MEReversi.SIZE; ++j ) {
                    x = i * sizex + sizex/2;
                    y = j * sizey + sizey/2;
                    g.drawLine(x,y,x,y);
                }
            }
        } else {
            for( int i=0; i<=J2MEReversi.SIZE; ++i ) {
                g.drawLine(0, i*sizey, J2MEReversi.SIZE*sizex, i*sizey);
                g.drawLine(i*sizex, 0, i*sizex, J2MEReversi.SIZE*sizey);
            }
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
            if( colored ) {
                g.setColor(P1_COLOR );
                g.fillArc(x,y,w,h,sa,aa);
            } else {
                g.drawArc(x,y,w,h,sa,aa);
            }
        } else {
            if( colored ) {
                g.setColor(P2_COLOR );
                g.fillArc(x,y,w,h,sa,aa);
            } else {
                g.setColor(0x000000);
                g.fillArc(x,y,w,h,sa,aa);
            }
        }
    }

    protected void drawSelectionBox(Graphics g) {
        g.setColor(BOX_COLOR);
        g.drawRect( selx*sizex, sely*sizey,sizex, sizey);
        g.drawRect( selx*sizex+1, sely*sizey+1,sizex-2, sizey-2);
    }

    protected int lineBreaks(String message) {
        int breaks = 0;
        int index = 0;
        while( (index = message.indexOf('\n', index)) != -1 ) {
            ++breaks;
            ++index;
        }
        return breaks;
    }

    protected int maxSubWidth(Graphics g, String message) {
        int startIndex;
        int endIndex = -1;
        int maxWidth = 0;
        int messageWidth;
        int breaks = lineBreaks(message);
        while( endIndex < message.length() ) {
            startIndex = endIndex+1;
            endIndex = message.indexOf('\n', startIndex);
            if( endIndex == -1 ) {
                endIndex = message.length();
            }
            String submessage = message.substring(startIndex, endIndex);
            messageWidth = g.getFont().stringWidth(submessage);
            if( maxWidth < messageWidth ) {
                maxWidth = messageWidth;
            }
        }
        return maxWidth;
    }

    protected void drawMessage(Graphics g) {
        // TODO: automagically break line if too long
        int startIndex;
        int endIndex = -1;
        int subIndex = 0;
        if( message != null ) {
            int breaks = lineBreaks(message);
            int messageHeight = g.getFont().getHeight();
            int maxWidth = maxSubWidth(g, message)+10;
            int cornerX = (width - maxWidth)/2;
            int cornerY = (height - (breaks+1) * messageHeight)/2;
            g.setColor(0xeeeeee);
            g.fillRect(cornerX, cornerY, maxWidth, (breaks+1) * messageHeight);
            g.setColor(0x000000);
            g.drawRect(cornerX, cornerY, maxWidth, (breaks+1) * messageHeight);
            while( endIndex < message.length() ) {
                startIndex = endIndex+1;
                endIndex = message.indexOf('\n', startIndex);
                if( endIndex == -1 ) {
                    endIndex = message.length();
                }
                String submessage = message.substring(startIndex, endIndex);
                int messageWidth = g.getFont().stringWidth(submessage)+10;
                g.drawString(submessage, cornerX+5, cornerY, g.TOP|g.LEFT);
                cornerY += messageHeight;
            }
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

    public void setMessage(String message, int delay) {
        this.message = message;
        repaint();
        new Timer().schedule(new DiscardMessageTimerTask(), delay);
    }

    public class DiscardMessageTimerTask extends TimerTask {
        public void run() {
            message = null;
            repaint();
        }
    }

    protected J2MEReversi boss;
    protected Display display;
    protected String message;
    protected boolean colored;
    public static final int P1_COLOR = 0xff0000;
    public static final int P2_COLOR = 0x0000ff;
    public static final int BOX_COLOR = 0x008f00;
    int width, height;
    int sizex, sizey;
    int selx, sely;
    
} // ReversiCanvas
