package reversi;

/*
 *
 * Created on July 25, 2002, 8:52 PM
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * An example MIDlet with simple "Hello" text and an Exit command.
 * Refer to the startApp, pauseApp, and destroyApp
 * methods so see how each handles the requested transition.
 *
 * @author  salamon
 * @version
 */
public class J2MEReversi extends MIDlet implements CommandListener {
    
    private Command exitCommand; // The exit command
    private Display display;    // The display for this MIDlet
    
    public J2MEReversi() {
        System.out.println("constructor");
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.SCREEN, 2);
    }
    
    /**
     * Start up the Hello MIDlet by creating the TextBox and associating
     * the exit command and listener.
     */
    public void startApp() {
        TextBox t = new TextBox("Hello MIDlet", "Test string", 256, 0);
        
        t.addCommand(exitCommand);
        t.setCommandListener(this);
        
        display.setCurrent(t);
    }
    
    /**
     * Pause is a no-op since there are no background activities or
     * record stores that need to be closed.
     */
    public void pauseApp() {
    }
    
    /**
     * Destroy must cleanup everything not handled by the garbage collector.
     * In this case there is nothing to cleanup.
     */
    public void destroyApp(boolean unconditional) {
    }
    
    /*
     * Respond to commands, including exit
     * On the exit command, cleanup and notify that the MIDlet has been destroyed.
     */
    public void commandAction(Command c, Displayable s) {
        System.out.println("c:"+c);
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
    
}
