package reversi;

/*
 *
 * Created on July 25, 2002, 8:52 PM
 */

import minimax.*;
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
    private boolean []isHuman = {true, false};
    private short actPlayer;
    private int turnNum;
    public  ReversiTable table;
    private ReversiGame rgame = new ReversiGame();
    private Minimax minimax = new Minimax(100);
    private boolean gameEnded;

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

    protected ReversiMove computerTurn() {
        ReversiMove move = (ReversiMove)minimax.minimax(2, table, actPlayer, rgame, false, 0, false, null);
        System.out.println("computer point: "+move.getPoint());        
        System.out.println("computer move: "+move);        
        return move;
    
    }

    protected void processMove(ReversiMove move) {
        ReversiTable newTable = (ReversiTable)rgame.turn( table, actPlayer, move );
        if( newTable == null ) {
            System.out.println("Invalid Move");
        } else {
            boolean nonPass = false;
            while( !nonPass ) {
                int point = rgame.point(newTable, actPlayer);
                System.out.println("point:"+point);
                if( point > 9000 || point < -9000 ) {
                    System.out.println("end");
                    gameEnded = true;
                }
                table = newTable;
                actPlayer = (short)(1 - actPlayer);
                ++turnNum;
                if( !rgame.hasPossibleMove(table, actPlayer) ) {
                    table.setPassNum(table.getPassNum()+1);
                    ++turnNum;
                    actPlayer = (short)(1 - actPlayer);
                } else {
                    nonPass = true;
                }
            }
        }
    }

    public void nextTurn(int row, int col) {
        ReversiMove move = new ReversiMove(row, col);
        processMove(move);
        canvas.repaint();        
        if( !gameEnded && !isHuman[actPlayer] ) {
            move = computerTurn();
            processMove(move);
        }
    }
    
}
