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

    public static final int SIZE = 8;
        
    private Command exitCommand; // The exit command
    private Display display;    // The display for this MIDlet
    private ReversiCanvas canvas;
    private boolean []isHuman = {true, true};
    private int actPlayer;
    private int turnNum;
    public  ReversiTable table;
    private ReversiGame rgame = new ReversiGame();
    
    public J2MEReversi() {
        System.out.println("constructor");
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.SCREEN, 2);
        canvas = new ReversiCanvas(this, display);
        canvas.addCommand(exitCommand);
        canvas.setCommandListener(this);
    }
    
    /**
     * Start up the Hello MIDlet by creating the TextBox and associating
     * the exit command and listener.
     */
    public void startApp() {
        TextBox t = new TextBox("Hello MIDlet", "Test string", 256, 0);
        
        t.addCommand(exitCommand);
        t.setCommandListener(this);
        
        //        display.setCurrent(t);
        display.setCurrent(canvas);
        actPlayer = 0;
        turnNum = 1;
        table = new ReversiTable(SIZE);
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

    public void nextTurn(int row, int col) {
        ReversiMove move = new ReversiMove(row, col);
        ReversiTable newTable = (ReversiTable)rgame.turn( table, actPlayer, move );
        if( newTable == null ) {
            System.out.println("Invalid Move");
        } else {
            int point = rgame.point(newTable, actPlayer);
            System.out.println("point:"+point);
            if( point > 9000 || point < -9000 ) {
                System.out.println("end");
            }
            table = newTable;
            actPlayer = 1 - actPlayer;
            ++turnNum;
            ReversiMove[] nextMoves = (ReversiMove[])rgame.possibleMoves(table, actPlayer);
            if( nextMoves.length == 0 ) {
                table.setPassNum(table.getPassNum()+1);
                ++turnNum;
                actPlayer = 1 - actPlayer;
            } 
        }
    }
    
}
