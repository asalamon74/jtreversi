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
    
    public ReversiCanvas(Display display) {
        this.display = display;
        width = getWidth();
        height = getHeight();
    }

    protected void paint(Graphics g) {
        int x = g.getClipX();
        int y = g.getClipY();
        int w = g.getClipWidth();
        int h = g.getClipHeight();

        // Draw the frame 
        g.setColor(0xff0000);
        g.fillRect(x, y, w, h);    
    }

    Display display;
    int width, height;
    
} // ReversiCanvas
