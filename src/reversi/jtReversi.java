package reversi;

import minimax.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class jtReversi extends MIDlet implements CommandListener {

    private static final String aboutImageName = "/icons/jataka.png";

    private Command exitCommand; // The exit command
    private Command optionsCommand;
    private List skillList;
    private List mainMenu;
    private List optionsMenu;
    private Display display;    // The display for this MIDlet
    private ReversiCanvas canvas;
    private boolean []isHuman = {true, false};
    private boolean twoplayer;
    private byte actPlayer;
    private int turnNum;
    public  ReversiTable table;
    private ReversiGame rgame;
    private boolean gameEnded = true;
    private int skill = 1;
    private Image logoImage;
    private Image selectedMutableImage = Image.createImage(16,16);
    private Image selectedImage;
    private Image unselectedMutableImage = Image.createImage(16,16);
    private Image unselectedImage;
    private MinimaxTimerTask mtt;
    private Timer timer = new Timer();
    protected int[][] heurMatrix = { 
                                  {500 ,-240, 85, 69, 69, 85,-240, 500},
				  {-240,-130, 49, 23, 23, 49,-130,-240},
				  {  85,  49,  1,  9,  9,  1,  49,  85},
				  {  69,  23,  9, 32, 32,  9,  23,  69},
				  {  69,  23,  9, 32, 32,  9,  23,  69},
				  {  85,  49,  1,  9,  9,  1,  49,  85},
				  {-240,-130, 49, 23, 23, 49,-130,-240},
                                  {500 ,-240, 85, 69, 69, 85,-240, 500}};
    private static final String[] mainMenuItems = {
        "Start 1P", 
        "Start 2P", 
        "Skill", 
        "About",
        "Exit game"
    };

    private static final String[] optionItems = {
        "Continue", 
        "Skill", 
        "About"};

    private static final String[] skillItems = new String[4];

    protected void startGame() {
        display.setCurrent(canvas);
        canvas.setMessage("Good Luck");
        actPlayer = 0;
        turnNum = 1;
        gameEnded = false;
        table = new ReversiTable();
    }

    public void initMidlet() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 99);
        optionsCommand = new Command("Options", Command.SCREEN, 5);
        mainMenu = new List( "jtReversi", List.IMPLICIT, mainMenuItems, null);
        mainMenu.setCommandListener(this);
        optionsMenu = new List( "Options", List.IMPLICIT, optionItems, null);
        optionsMenu.setCommandListener(this);
        selectedMutableImage.getGraphics().fillRect(4,4, 8,8);
        selectedImage = Image.createImage(selectedMutableImage);
        unselectedMutableImage.getGraphics().fillRect(7,7, 2,2);
        unselectedImage = Image.createImage(unselectedMutableImage);
        Image []unseletedImages = new Image[skillItems.length];
        for( int i=0; i<skillItems.length; ++i ) {
            skillItems[i] = "Level "+(i+1);
            unseletedImages[i] = unselectedImage;
        }
        skillList = new List("Skill", List.IMPLICIT, skillItems, unseletedImages);
        skillList.addCommand(exitCommand);
        skillList.setCommandListener(this);
        canvas = new ReversiCanvas(this, display);
        canvas.addCommand(exitCommand);
        canvas.addCommand(optionsCommand);
        canvas.setCommandListener(this);
        rgame = new ReversiGame();
        rgame.setEvaluationFunction(
            new ReversiHeuristicEvaluation(heurMatrix,10,18,true));
        try {
            logoImage  = Image.createImage(aboutImageName);
        } catch (IOException e) {
            System.out.println("Invalid logo"+e);
        }
        new SplashScreen( display, mainMenu );
    }

    public void startApp() {
        Minimax.clearPrecalculatedMoves();
        mtt = null;
        Minimax.cancel(false);
        if( display == null ) {
            initMidlet();
        } else {
            display.setCurrent( mainMenu );
        }
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
    public void commandAction(Command c, Displayable d) {
        if( d.equals(canvas) ) {
            if (c == exitCommand) {
                destroyApp(false);
                notifyDestroyed();
            } else if( c == optionsCommand ) {
                display.setCurrent(optionsMenu);
            }       
        } else if( d.equals(skillList) ) { 
            if( c == exitCommand ) {
                Alert alert = new Alert("Level");
                alert.setTimeout(1000);
                alert.setString("Level Not Changed\nLevel:"+skill);
                if( !gameEnded ) {
                    display.setCurrent(canvas);
                } else {
                    display.setCurrent(mainMenu);
                }                
                display.setCurrent(alert);                
            } else if( c == List.SELECT_COMMAND ) {
                skillList.set(skill-1, skillList.getString(skill-1), unselectedImage);
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
        } else if( d.equals(mainMenu) ) {
            if( c == List.SELECT_COMMAND ) {
                int pos = mainMenu.getSelectedIndex();
                switch( pos ) {
                case 0:
                    isHuman[0] = true;
                    isHuman[1] = false;
                    twoplayer = false;
                    startGame();
                    break;
                case 1:
                    isHuman[0] = true;
                    isHuman[1] = true;
                    twoplayer = true;
                    startGame();
                    break;
                case 2:
                    skillList.set(skill-1, skillList.getString(skill-1), selectedImage);
                    display.setCurrent(skillList);
                    break;                    
                case 3:
                    showAbout();
                    break;
                case 4:
                    destroyApp(false);
                    notifyDestroyed();
                    break;
                }
            }
        } else if( d.equals(optionsMenu) ) {
            if( c == List.SELECT_COMMAND ) {
                int pos = optionsMenu.getSelectedIndex();
                switch( pos ) {
                case 0:
                    display.setCurrent(canvas);
                    break;
                case 1:
                    skillList.set(skill-1, skillList.getString(skill-1), selectedImage);
                    display.setCurrent(skillList);
                    break;                    
                case 2:
                    showAbout();
                    break;
                }
            }
        }
    }

    protected void showAbout() {
        Alert alert = new Alert("About jtReversi 0.81");
        alert.setTimeout(Alert.FOREVER);
        alert.setString("Simple board game\nby\nJataka Ltd.");
        alert.setType(AlertType.INFO);
        alert.setImage(logoImage);
        display.setCurrent(alert);
    }

    protected int getActSkill() {
        int actSkill = skill;
        if( turnNum > 50 ) {
            ++actSkill;
        }
        if( turnNum > 55 ) {
            ++actSkill;
        }        
        return actSkill;
    }

    protected ReversiMove computerTurn(ReversiMove prevMove) {
        canvas.setMessage("Thinking");
        canvas.repaint();
        canvas.serviceRepaints();
        ReversiMove move=(ReversiMove)Minimax.precalculatedBestMove(prevMove);
        if( move == null ) {
            //            System.out.println("no precalculated move");
            move = (ReversiMove)Minimax.minimax(getActSkill(), table, actPlayer, rgame, true, 0, true, true, null, true);
        } else {
            //            System.out.println("Precalculated move");
        }
        //        System.out.println(move.getPoint());
        //        System.out.println("evalNum:"+rgame.getEvalNum());
        canvas.stopWait();
        rgame.resetEvalNum();
        return move;
    
    }

    protected void processMove(ReversiMove move) {
        //        System.out.println("move:"+move);
        ReversiTable newTable = new ReversiTable();
        boolean goodMove = rgame.turn( table, actPlayer, move, newTable );
        if( !goodMove ) {
            //            System.out.println("invalid move:"+move);
            canvas.setMessage("Invalid Move",2000);
        } else {
            boolean nonPass = false;
            table = newTable;
            while( !nonPass && !gameEnded) {
                rgame.process(newTable, actPlayer);
                if( rgame.isGameEnded() ) {
                    int result = rgame.getGameResult();
                    String endMessage="";
                    boolean firstWin = 
                        (result == EvaluationFunction.LOSS && actPlayer == 0) || 
                        (result == EvaluationFunction.WIN  && actPlayer == 1);
                    int winner = firstWin ? 1 : 0;
                    if( !twoplayer && firstWin ) {
                        endMessage = "Computer won";
                    } else if( result == EvaluationFunction.DRAW ) {
                        endMessage = "Draw";
                    } else {
                        if( twoplayer ) {
                            endMessage = canvas.playerNames[winner] + " won";
                        } else {
                            endMessage = "You won";
                        }
                    } 
                    int firstNum = rgame.firstPlayerNum();
                    int secondNum = rgame.secondPlayerNum();
                    endMessage += "\n"+canvas.playerNames[0]+": "+firstNum+"\n"+
                        canvas.playerNames[1]+": "+secondNum;
                    canvas.setMessage(endMessage);
                    gameEnded = true;
                } else {
                    actPlayer = (byte)(1 - actPlayer);
                    ++turnNum;
                    if( !rgame.hasPossibleMove(table, actPlayer) ) {
                        String message = "";
                        if( isHuman[actPlayer] ) {
                            if( twoplayer ) {
                                message = canvas.playerNames[actPlayer];
                            } else {
                                message = "Human";
                            }
                        } else {
                            message = "Computer";
                        }
                        canvas.setMessage(message + " Pass", 2000);
                        table.setPassNum(table.getPassNum()+1);
                        
                    } else {
                        nonPass = true;
                    }
                }
            }
        }
    }

    void nextTurn(int row, int col) {
        //        System.out.println("nextTurn"+mtt);
        if( mtt != null ) {
            mtt.cancel();
            while( mtt.ended == false ) {
                //                System.out.println("wait nt");
                synchronized(this) {
                    try {
                        wait(50);
                    } catch(Exception e) {
                        System.out.println("Synchronization problem"+e);
                    }
                }
            }
        }
        if( gameEnded ) {
            startApp();
            return;
        }
        ReversiMove move = new ReversiMove(row, col);
        processMove(move);
        canvas.repaint();
        canvas.serviceRepaints();        
        while( !gameEnded && !isHuman[actPlayer] ) {
            ReversiMove computerMove = computerTurn(move);
            processMove(computerMove);
            canvas.repaint();
            canvas.serviceRepaints();        
            mtt = new MinimaxTimerTask();
            timer.schedule(mtt, 0);            
        }
    }

    public byte getActPlayer() {
        return actPlayer;
    }

    public Move[] possibleMoves() {
        return rgame.possibleMoves(table, actPlayer);
    }

    public class SplashScreen extends Canvas {
        private Display     display;
        private Displayable next;
        private Timer       timer = new Timer();

        public SplashScreen( Display display, Displayable next ){
            this.display = display;
            this.next    = next;

            display.setCurrent( this );
        }

        protected void keyPressed( int keyCode ){
            dismiss();
        }

        protected void paint( Graphics g ){
            // do your drawing here
            g.setColor(0xffffff);
            g.fillRect(0,0, getWidth(), getHeight());
            Font f = g.getFont();            
            Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
            g.setFont(largeFont);
            g.setColor(0x000000);
            g.drawImage( logoImage, getWidth()/2 , 0, g.TOP|g.HCENTER );
            String str = "jtReversi";
            g.drawString(str, getWidth()/2, logoImage.getHeight(), g.TOP|g.HCENTER);
            g.setFont(f);
            str = "(c) 2002";
            g.drawString(str, getWidth(), getHeight(), g.RIGHT|g.BOTTOM);
        }

        protected void pointerPressed( int x, int y ){
            dismiss();
        }

        protected void showNotify(){
            timer.schedule( new CountDown(), 5000 );
        }

        private void dismiss(){
            timer.cancel();
            display.setCurrent( next );
        }

        private class CountDown extends TimerTask {
            public void run(){
                dismiss();
            }
        }
    }

    class MinimaxTimerTask extends TimerTask {
        
        public boolean ended;

        public boolean cancel() {
            Minimax.cancel(true);
            return true;
        }
        
        public void run() {
            ended = false;
            //            System.out.println("start");
            Minimax.foreMinimax(getActSkill(), table, (byte)(1-actPlayer), rgame, true, 0, true, true);
            //            System.out.println("end");
            ended = true;
        }
    }


}
