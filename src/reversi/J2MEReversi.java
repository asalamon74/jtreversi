package reversi;

import minimax.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.IOException;

public class J2MEReversi extends MIDlet implements CommandListener {

    public static final int SIZE = 8;
    private static final String aboutImageName = "/icons/jataka_logo_c_small.png";
        
    private Command exitCommand; // The exit command
    private Command optionsCommand;
    private List skillList;
    private List mainMenu;
    private List optionsMenu;
    private Display display;    // The display for this MIDlet
    private ReversiCanvas canvas;
    private boolean []isHuman = {true, false};
    private byte actPlayer;
    private int turnNum;
    public  ReversiTable table;
    private ReversiGame rgame;
    private Minimax minimax = new Minimax(100);
    private boolean gameEnded = true;
    private int skill = 1;
    private static final String[] mainMenuItems = {
        "Start 1P", 
        "Start 2P", 
        "Skill", 
        "About"};

    private static final String[] optionItems = {
        "Continue", 
        "Skill", 
        "About"};

    private static final String[] skillItems = {
        "Level 1", 
        "Level 2", 
        "Level 3",
        "Level 4",
        "Level 5"};


    public J2MEReversi() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 99);
        optionsCommand = new Command("Options", Command.SCREEN, 5);
        mainMenu = new List( "J2MEReversi", List.IMPLICIT, mainMenuItems, null);
        mainMenu.setCommandListener(new MainCommandListener());
        optionsMenu = new List( "Options", List.IMPLICIT, optionItems, null);
        optionsMenu.setCommandListener(new OptionsCommandListener());
        skillList = new List("Skill", List.IMPLICIT, skillItems, null);
        skillList.addCommand(exitCommand);
        skillList.setCommandListener(new SkillCommandListener());
        canvas = new ReversiCanvas(this, display);
        canvas.addCommand(exitCommand);
        canvas.addCommand(optionsCommand);
        canvas.setCommandListener(this);
        rgame = new ReversiGame();
        rgame.setEvaluationFunction(new ReversiHeuristicEvaluation());            
    }

    protected void startGame() {
        display.setCurrent(canvas);
        canvas.setMessage("Good Luck");
        actPlayer = 0;
        turnNum = 1;
        gameEnded = false;
        table = new ReversiTable(SIZE);
//         int flip = 3;
//         for( int i=0; i<SIZE; ++i) {
//             for( int j=0; j<SIZE-1; ++j ) {
//                 byte value = (byte)(1 + i % 2);
//                 if( value == 1 && flip > 0 ) {
//                     value = 2;
//                     --flip;
//                 }
//                 table.setItem(i,j,value );
//             }
//         }
    }

    public void startApp() {
        display.setCurrent(mainMenu);
        //        startGame();
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
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } else if( c == optionsCommand ) {
            display.setCurrent(optionsMenu);
        }       
    }

    protected void showAbout() {
        Alert alert = new Alert("About J2ME_Reversi");
        alert.setTimeout(Alert.FOREVER);
        alert.setString("Simple board game\nby\nAndras Salamon\n(Jataka Ltd.)");
        alert.setType(AlertType.INFO);
        try {
            Image image = Image.createImage(aboutImageName);
            alert.setImage(image);
        } catch (IOException e) {
            System.out.println("Invalid about logo"+e);
        }
        display.setCurrent(alert);
    }


    protected ReversiMove computerTurn() {
        ReversiMove move = (ReversiMove)minimax.minimax(skill, table, actPlayer, rgame, true, 0, true, true, null);
        //        System.out.println("eval: "+ReversiGame.getEvalNum());
        rgame.resetEvalNum();
        return move;
    
    }

    protected void processMove(ReversiMove move) {
        ReversiTable newTable = new ReversiTable(J2MEReversi.SIZE);
        boolean goodMove = rgame.turn( table, actPlayer, move, newTable );
        if( !goodMove ) {
            canvas.setMessage("Invalid Move",2000);
        } else {
            boolean nonPass = false;
            table = newTable;
            while( !nonPass && !gameEnded) {
                int point = rgame.point(newTable, actPlayer);
                if( rgame.isGameEnded() ) {
                    int result = rgame.getGameResult();
                    String endMessage="";
                    if( (result == EvaluationFunction.LOSS && actPlayer == 0) || 
                        (result == EvaluationFunction.WIN  && actPlayer == 1)) {
                        endMessage = "Computer won";
                    } else if( result == EvaluationFunction.DRAW ) {
                        endMessage = "Draw";
                    } else {
                        endMessage = "You won";
                    } 
                    int firstNum = rgame.firstPlayerPoint();
                    int secondNum = rgame.secondPlayerPoint();
                    endMessage += "\n"+canvas.playerNames[0]+": "+firstNum+"\n"+
                        canvas.playerNames[1]+": "+secondNum;
                    canvas.setMessage(endMessage);
                    gameEnded = true;
                } else {
                    actPlayer = (byte)(1 - actPlayer);
                    ++turnNum;
                    if( !rgame.hasPossibleMove(table, actPlayer) ) {
                        if( isHuman[actPlayer] ) {
                            canvas.setMessage("Human Pass", 2000);
                        } else {
                            canvas.setMessage("Computer Pass", 2000);
                        }
                        table.setPassNum(table.getPassNum()+1);
                    } else {
                        nonPass = true;
                    }
                }
            }
        }
    }

    void nextTurn(int row, int col) {
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
                if( !gameEnded ) {
                    display.setCurrent(canvas);
                } else {
                    display.setCurrent(mainMenu);
                }
                
                display.setCurrent(alert);
            }
        }
    }

    private class MainCommandListener implements CommandListener {

        public void commandAction(Command c, Displayable d) {
            if( c == List.SELECT_COMMAND ) {
                int pos = mainMenu.getSelectedIndex();
                switch( pos ) {
                case 0:
                    isHuman[0] = true;
                    isHuman[1] = false;
                    startGame();
                    break;
                case 1:
                    isHuman[0] = true;
                    isHuman[1] = true;
                    startGame();
                    break;
                case 2:
                    display.setCurrent(skillList);
                    break;                    
                case 3:
                    showAbout();
                    break;
                }
            }
        }
    }

    private class OptionsCommandListener implements CommandListener {

        public void commandAction(Command c, Displayable d) {
            if( c == List.SELECT_COMMAND ) {
                int pos = optionsMenu.getSelectedIndex();
                switch( pos ) {
                case 0:
                    display.setCurrent(canvas);
                    break;
                case 1:
                    display.setCurrent(skillList);
                    break;                    
                case 2:
                    showAbout();
                    break;
                }
            }
        }
    }

}
