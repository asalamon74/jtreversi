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
    private Image offscreen = null;
    private static int ASPECT_LIMIT = 15; // 1.5
    
    public ReversiCanvas(jtReversi boss, Display display) {
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
        sizex = width / 8;
        sizey = height / 8;
        if( 10*sizex / sizey > ASPECT_LIMIT ) {
            sizex = sizey * ASPECT_LIMIT / 10;
            width = sizex * 8;
        }
        if( 10*sizey / sizex > ASPECT_LIMIT ) {
            sizey = sizex * ASPECT_LIMIT / 10;
            height = sizey * 8;
        }

        small = sizex < SIZE_LIMIT || sizey < SIZE_LIMIT;
        selx = 0;
        sely = 0;
        //        if( !isDoubleBuffered() ) { 
        // there are phones which claims to have a doubleBuffered Canvas
        // although they don't have
            offscreen = Image.createImage(width, height);
        //        }
    }

    protected void paint(Graphics g) {
        Graphics saved = g;

        if( offscreen != null ) {
            g = offscreen.getGraphics();
        }
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
        drawPossibleMoves(g, boss.table);
        drawMessage(g);
        if( g != saved ) {
            saved.drawImage( offscreen, 0, 0, Graphics.LEFT | Graphics.TOP );
        }
    }

    protected void drawBoard(Graphics g) {
        if( colored ) {
            g.setColor(BG_COLOR);
            g.fillRect(0,0, 8*sizex, 8*sizey);
        }
        g.setColor(0x000000);
        if( small ) {
            int x,y;
            for( int i=0; i<8; ++i ) {
                x = i * sizex + sizex/2;                
                for( int j=0; j<8; ++j ) {
                    if( boss.table.getItem(i,j)  == 0 ) {
                        y = j * sizey + sizey/2;
                        g.drawLine(x,y,x,y);
                    }
                }
            }
        } else {
            for( int i=0; i<=8; ++i ) {
                g.drawLine(0, i*sizey, 8*sizex, i*sizey);
                g.drawLine(i*sizex, 0, i*sizex, 8*sizey);
            }
        }
    }

    protected void drawTable(Graphics g, ReversiTable t) {
        for( int i=0; i<8; ++i ) {
            for( int j=0; j<8; ++j ) {
                if( t.getItem(i,j) != 0 ) {
                    drawPiece(g, i, j, t.getItem(i,j));
                }
            }
        }
    }

    protected void drawPossibleMoves(Graphics g, ReversiTable t) {
        ReversiMove []moves = (ReversiMove [])boss.possibleMoves(); // t is not used 
        if( moves == null ) {
            // end of the game
            return;
        }
        int row,col;
        int x,y;
        for( int i=0; i<moves.length; ++i ) {
            row = moves[i].row;
            col = moves[i].col;
            x = row * sizex + sizex/2;
            y = col * sizey + sizey/2;
            g.fillArc(x,y,2,2,0,360);
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
                if( small ) {
                    g.fillRect(x,y,w,h);
                } else {
                    g.fillArc(x,y,w,h,sa,aa);
                }
            } else {
                if( small ) {
                    g.drawRect(x,y,w,h);
                } else {
                    g.drawArc(x,y,w,h,sa,aa);
                }
            }
        } else {
            if( colored ) {
                g.setColor(P2_COLOR );
                if( small ) {
                    g.fillRect(x,y,w,h);
                } else {
                    g.fillArc(x,y,w,h,sa,aa);
                }
            } else {
                g.setColor(0x000000);
                if( small ) {
                    g.fillRect(x,y,w,h);
                } else {
                    g.fillArc(x,y,w,h,sa,aa);
                }
            }
        }
    }

    protected void drawSelectionBox(Graphics g) {
        if( colored ) {
            if( boss.getActPlayer() == 0 ) {            
                g.setColor(BOX_P1_COLOR);
            } else {
                g.setColor(BOX_P2_COLOR);
            }
        } else {
            if( boss.getActPlayer() == 0 ) {
                g.setColor(LIGHT_BOX_COLOR);
            } else {
                g.setColor(DARK_BOX_COLOR);
            }
        }
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
            g.fillRect(cornerX-1, cornerY-1, maxWidth, (breaks+1) * messageHeight+6);
            g.setColor(0x000000);
            g.drawRect(cornerX-1, cornerY-1, maxWidth, (breaks+1) * messageHeight + 6);
            g.drawRect(cornerX, cornerY, maxWidth-2, (breaks+1) * messageHeight + 4);
            while( endIndex < message.length() ) {
                startIndex = endIndex+1;
                endIndex = message.indexOf('\n', startIndex);
                if( endIndex == -1 ) {
                    endIndex = message.length();
                }
                String submessage = message.substring(startIndex, endIndex);
                g.drawString(submessage, cornerX+5, cornerY+2, g.TOP|g.LEFT);
                cornerY += messageHeight;
            }
        }
    }
    public void keyPressed(int keyCode) {
        int oldselx = selx;
        int oldsely = sely;
        switch( getGameAction(keyCode) ) {
        case Canvas.UP: 
            sely = (sely + 8 -1) % 8;
            if( message != null ) {
                message = null;
                repaint();
            } else {
                repaint(selx*sizex-1, oldsely*sizey-1,sizex+2, sizey+2);
                repaint(selx*sizex, sely*sizey,sizex, sizey);
            }
            break;
        case Canvas.DOWN: 
            sely = (sely + 1) % 8;
            message = null;
            repaint();
            break;
        case Canvas.LEFT: 
            selx = (selx + 8 -1) % 8;
            message = null;
            repaint();
            break;
        case Canvas.RIGHT: 
            selx = (selx + 1) % 8;
            message = null;
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

    public void startWait(String message) {
        this.message = message + "  ";
        repaint();
        new Timer().schedule(new PleaseWaitTimerTask(), 500, 500);
    }

    public void stopWait() {
        this.message = null;
    }

    public class DiscardMessageTimerTask extends TimerTask {
        public void run() {
            message = null;
            repaint();
        }
    }

    // Note: currently it's not used
    public class PleaseWaitTimerTask extends TimerTask {
        int state = 0;
        char []flag = {'|', '/', '-', '\\'};

        public void run() {
            state = (state + 1) % 4;
            message = message.substring(0, message.length() - 1) + flag[state];
            repaint();
        }
    }

    protected jtReversi boss;
    protected Display display;
    protected String message;
    protected boolean colored;
    protected boolean small;
    public static final int P1_COLOR = 0xff0000;
    public static final int P2_COLOR = 0x0000ff;
    public static final int LIGHT_BOX_COLOR = 0x008f00;
    public static final int DARK_BOX_COLOR = 0x000000;
    public static final int BOX_P1_COLOR = P1_COLOR;
    public static final int BOX_P2_COLOR = P2_COLOR;;
    public static final int BG_COLOR = 0xffffd0;
    int width, height;
    int sizex, sizey;
    int selx, sely;
    
} // ReversiCanvas
