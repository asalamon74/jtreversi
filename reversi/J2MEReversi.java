package reversi;

import minimax.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class J2MEReversi extends MIDlet implements CommandListener {

    public static final int SIZE = 8;
        
    private Command exitCommand; // The exit command
    private Command aboutCommand;
    private Command optionsCommand;
    private List skillList;
    private Display display;    // The display for this MIDlet
    private ReversiCanvas canvas;
    private boolean []isHuman = {true, false};
    private byte actPlayer;
    private int turnNum;
    public  ReversiTable table;
    private ReversiGame rgame = new ReversiGame();
    private Minimax minimax = new Minimax(100);
    private boolean gameEnded;
    private int skill = 3;

    public J2MEReversi() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 99);
        aboutCommand =  new Command("About", Command.HELP, 10);
        optionsCommand =  new Command("Options", Command.SCREEN, 5);
        skillList = new List("Skill", List.IMPLICIT);
        skillList.append("Level 1" , null);
        skillList.append("Level 2" , null);
        skillList.append("Level 3" , null);
        skillList.addCommand(exitCommand);
        skillList.setCommandListener(new SkillCommandListener());
        canvas = new ReversiCanvas(this, display);
        canvas.addCommand(exitCommand);
        canvas.addCommand(aboutCommand);
        canvas.addCommand(optionsCommand);
        canvas.setCommandListener(this);
    }
    
    public void startApp() {
        display.setCurrent(canvas);
        canvas.setMessage("Good Luck");
        actPlayer = 0;
        turnNum = 1;
        gameEnded = false;
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
        System.out.println("c:"+c.getLabel());
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } else if( c == aboutCommand ) {
            showAbout();
        } else if( c == optionsCommand ) {
            display.setCurrent(skillList);
        }
    }

    protected void showAbout() {
        Alert alert = new Alert("About J2ME_Reversi");
        alert.setTimeout(Alert.FOREVER);
        alert.setString("Simple board game\nby\nAndras Salamon");
        display.setCurrent(alert);
    }


    protected ReversiMove computerTurn() {
        ReversiMove move = (ReversiMove)minimax.minimax(skill, table, actPlayer, rgame, false, 0, false, null);
        //        System.out.println("computer point: "+move.getPoint());        
        //        System.out.println("computer move: "+move);        
        return move;
    
    }

    protected void processMove(ReversiMove move) {
        ReversiTable newTable = (ReversiTable)rgame.turn( table, actPlayer, move );
        if( newTable == null ) {
            canvas.setMessage("Invalid Move");
        } else {
            boolean nonPass = false;
            while( !nonPass ) {
                int point = rgame.point(newTable, actPlayer);
                //                System.out.println("point:"+point);
                if( point > 9000 || point < -9000 ) {                    
                    String endMessage="";
                    if( (point < 0 && actPlayer == 0) || 
                        (point > 0 && actPlayer == 1)) {
                        endMessage = "You win";
                    } else {
                        endMessage = "Computer win";
                    }
                    canvas.setMessage(endMessage);
                    //          canvas.setMessage("End Point:"+point);
                    gameEnded = true;
                }
                table = newTable;
                actPlayer = (byte)(1 - actPlayer);
                ++turnNum;
                if( !rgame.hasPossibleMove(table, actPlayer) ) {
                    table.setPassNum(table.getPassNum()+1);
                    ++turnNum;
                    actPlayer = (byte)(1 - actPlayer);
                } else {
                    nonPass = true;
                }
            }
        }
    }

    public void nextTurn(int row, int col) {
        if( gameEnded ) {
            startApp();
            return;
        }
        ReversiMove move = new ReversiMove(row, col);
        processMove(move);
        canvas.repaint();
        canvas.serviceRepaints();        
        if( !gameEnded && !isHuman[actPlayer] ) {
            move = computerTurn();
            processMove(move);
        }
    }

    private class SkillCommandListener implements CommandListener {

        public void commandAction(Command c, Displayable d) {
            if( c == List.SELECT_COMMAND ) {
                skill = skillList.getSelectedIndex()+1;
                Alert alert = new Alert("Level");
                alert.setTimeout(1000);
                alert.setString("Level Changed\nNew Level:"+skill);
                display.setCurrent(canvas);
                display.setCurrent(alert);
            }
        }
    }
}
