package reversi;

import minimax.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class jtReversi extends MIDlet implements CommandListener, ItemStateListener {

    private static final String aboutImageName = "/icons/jataka.png";

    private Command exitCommand; // The exit command
    private Command optionsCommand;
    private Form skillForm;
    private ChoiceGroup skillChoiceGroup;
    private List mainMenu;
    private List optionsMenu;
    private Display display;    // The display for this MIDlet
    private ReversiCanvas canvas;
    private Form aboutForm;
    private boolean []isHuman = {true, false};
    private boolean twoplayer;
    private byte actPlayer;
    private int turnNum;
    private boolean gameLoaded = false;
    public  ReversiTable table;
    private ReversiGame rgame;
    private boolean gameEnded = true;
    public  int skill = 1;
    private Image logoImage;
    private MinimaxTimerTask mtt;
    private Timer timer = new Timer();
    private boolean animation = true;
    private Table[] tables;
    private RecordStore rs;
    private byte []record;
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
        if( !gameLoaded ) {
            table = new ReversiTable();
        }
        canvas.updatePossibleMoves();
    }

    public void initMidlet() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 99);
        optionsCommand = new Command("Options", Command.SCREEN, 5);
        mainMenu = new List( "jtReversi", List.IMPLICIT, mainMenuItems, null);
        mainMenu.setCommandListener(this);
        optionsMenu = new List( "Options", List.IMPLICIT, optionItems, null);
        optionsMenu.setCommandListener(this);
        for( int i=0; i<skillItems.length; ++i ) {
             skillItems[i] = "Level "+(i+1);
        }
        skillForm = new Form("Skill");
        skillChoiceGroup = new ChoiceGroup("Skill", 
					   ChoiceGroup.EXCLUSIVE, 
					   skillItems, 
					   null);
        skillForm.append(skillChoiceGroup);
        skillForm.addCommand(exitCommand);
        skillForm.setCommandListener(this);
        skillForm.setItemStateListener(this);
	skill = 1; // default skill is 1

        canvas = new ReversiCanvas(this, display);
        canvas.addCommand(exitCommand);
        canvas.addCommand(optionsCommand);
        canvas.setCommandListener(this);
        rgame = new ReversiGame(heurMatrix,10,18,true);
        try {
            logoImage  = Image.createImage(aboutImageName);
        } catch (IOException e) {
            System.out.println("Invalid logo"+e);
        }
        aboutForm = new Form("About jtReversi 0.85");
	aboutForm.append(logoImage);
	aboutForm.append("\n");
	aboutForm.append("www.jataka.hu\n");
	aboutForm.append("Simple board game by Jataka Ltd.\n");
	aboutForm.append("Contact: mail@jataka.hu\n");
        aboutForm.addCommand(exitCommand);
        aboutForm.setCommandListener(this);
        loadRecordStore();
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
        saveRecordStore();
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
        } else if( d.equals(skillForm) ) {
            // only one command
            Alert alert = new Alert("Level");
            alert.setString("Level not changed\n Level: "+skill);
            // setting next
            // after timeout this will be the next screen
            display.setCurrent(alert, mainMenu);
            alert.setTimeout(1000);
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
                    skillChoiceGroup.setSelectedIndex(skill-1, true);
                    display.setCurrent(skillForm);
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
                    skillChoiceGroup.setSelectedIndex(skill-1, true);
                    display.setCurrent(skillForm);
                    break;                    
                case 2:
                    showAbout();
                    break;
                }
            }
        } else if( d.equals(aboutForm) ) {
            //            if( c == exitCommand ) {
            display.setCurrent(mainMenu);
            //            }
        }
    }


    public void itemStateChanged(Item item) {
	Alert alert = new Alert("Skill");
        skill = ((ChoiceGroup)item).getSelectedIndex()+1;
        alert.setString("Level changed\n New level:"+skill);
	display.setCurrent(alert, mainMenu);
	alert.setTimeout(1000);
    }

    protected void showAbout() {
        display.setCurrent(aboutForm);
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
        ReversiMove move=(ReversiMove)Minimax.precalculatedBestMove(prevMove);
        if( move == null ) {
            canvas.setMessage("Thinking");
            canvas.repaint();
            canvas.serviceRepaints();
            Minimax.cancel(false);
            move = (ReversiMove)Minimax.minimax(getActSkill(), table, actPlayer, rgame, true, 0, true, true, null, true);
        }
        canvas.stopWait();
        rgame.resetEvalNum();
        return move;   
    }

    protected void processMove(ReversiMove move, boolean startForeThinking) {
        //        System.out.println("move:"+move);
        ReversiTable newTable = new ReversiTable();
        //        boolean goodMove = rgame.turn( table, actPlayer, move, newTable );
        tables = rgame.animatedTurn(table, actPlayer, move, newTable);
        boolean goodMove = (tables != null);
        if( !goodMove ) {
            // System.out.println("actPlayer:"+actPlayer+" invalid move:"+move);
            canvas.setMessage("Invalid Move",2000);
        } else {
            if( startForeThinking ) {
                mtt.setStartTable(tables[tables.length-1]);
                timer.schedule(mtt, 0);            
            }
            synchronized(this) {
                for( int i=0; i<tables.length; ++i ) {
                    table = (ReversiTable)tables[i];
                    canvas.repaint();
                    canvas.serviceRepaints();        
                    if( i<tables.length-1 ) {
                        try {
                            wait(300);
                        } catch( InterruptedException e ) {
                            // do something
                        }
                    }
                }
            }
            boolean nonPass = false;
            table = newTable;
            while( !nonPass && !gameEnded) {
                rgame.process(newTable, actPlayer);
                if( rgame.isGameEnded() ) {
                    int result = rgame.getGameResult();
                    String endMessage="";
                    boolean firstWin = 
                        (result == TwoPlayerGame.LOSS && actPlayer == 0) || 
                        (result == TwoPlayerGame.WIN  && actPlayer == 1);
                    int winner = firstWin ? 1 : 0;
                    if( !twoplayer && firstWin ) {
                        endMessage = "Computer won";
                    } else if( result == TwoPlayerGame.DRAW ) {
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
                        // just to be sure
                        Minimax.clearPrecalculatedMoves();
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
        processMove(move, false);
        canvas.updatePossibleMoves();
        canvas.repaint();
        canvas.serviceRepaints();        
        while( !gameEnded && !isHuman[actPlayer] ) {
            mtt = new MinimaxTimerTask();
            ReversiMove computerMove = computerTurn(move);
            canvas.selx = computerMove.row;
            canvas.sely = computerMove.col;
            processMove(computerMove, true);
            canvas.updatePossibleMoves();
            canvas.repaint();
            canvas.serviceRepaints();        
            if( isHuman[actPlayer] ) {
                //                timer.schedule(mtt, 0);            
            } else {
                // just to be sure
                Minimax.clearPrecalculatedMoves();
            }
        }
    }

    public byte getActPlayer() {
        return actPlayer;
    }

    public Move[] possibleMoves() {
        return rgame.possibleMoves(table, actPlayer);
    }


    /**
     * Loads data from recordstore.
     *
     */
    public void loadRecordStore() {
        int recordNum;
        try {
            rs = RecordStore.openRecordStore("R", true);
            recordNum = rs.getNumRecords();
            if( recordNum > 0 ) {
                record = rs.getRecord(1);
                if( record != null ) {
                    skill = record[0];
                    System.out.println("loaded skill:"+skill);
                }            
            } else {
                System.out.println("create record 1");
                record = new byte[1];
                record[0] = 1;
                try {
                    rs.addRecord(record, 0, 1);
                } catch (Exception e) {
                    // what to do?
                }
            }
            if( recordNum > 1 ) {
                byte []tableArray = rs.getRecord(2);
                System.out.println(tableArray.length+" bytes loaded");
                for( int i=0; i<tableArray.length; ++i ) {
                    System.out.println("load["+i+"]:"+tableArray[i]);
                }           
                System.out.println("saved game");
                table = new ReversiTable(tableArray);
                gameLoaded = true;
                System.out.println("table:"+table);
            } else {
                // create the board record
                System.out.println("create record 2");
                record = new byte[16];
                try {
                    rs.addRecord(record, 0, 16);
                } catch (Exception e) {
                    // what to do?
                }
            }
        } catch( Exception e) {
            //            System.out.println("e:"+e);
        }
    }

    /**
     * Saves data into recordstore
     *
     */
    public void saveRecordStore() {
        try {
            byte []record = new byte[1];
            record[0] = (byte)skill;
            rs.setRecord(1, record, 0, 1);
           
            System.out.println("save table:\n"+table);
            byte []tableByteArray = table.toByteArray();
            for( int i=0; i<tableByteArray.length; ++i ) {
                System.out.println("save["+i+"]:"+tableByteArray[i]);
            }
            //            tableByteArray[0] = (byte)188;
            rs.setRecord(2, tableByteArray, 0, tableByteArray.length );
            rs.closeRecordStore();

        } catch( Exception e ) {
            //            System.out.println("e:"+e);
        }
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
            str = "(c) 2003";
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

        Table startTable;

        public void setStartTable(Table startTable) {
            this.startTable = startTable;
        }
        
        public boolean ended;

        public boolean cancel() {
            Minimax.cancel(true);
            return true;
        }
        
        public void run() {
            ended = false;
            //            System.out.println("start");
            Minimax.foreMinimax(getActSkill(), startTable, actPlayer, rgame, true, 0, true, true);
            //             System.out.println("end");
            System.gc();
            ended = true;
        }
    }

}
