package bfide.ide.tabs.editor;

import bfide.interpreter.Interpreter;
import bfide.interpreter.Interpreter.MismatchedBracketException;
import bfide.util.BfLogger;
import bfide.util.MVCModel;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class InterpreterModel extends MVCModel {

    /**************************************************************************
     * Fields & Constructor
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    private final Timeline timeline;
    private static final double TICK_RATE = 1_000;

    private Interpreter interpreter;

    private boolean isInterpreting;

    public static final String CURSOR_LEFT = "CURSOR_LEFT";
    public static final String CURSOR_RIGHT = "CURSOR_RIGHT";
    public static final String EXCEPTION = "EXCEPTION";
    public static final String PAUSE = "PAUSE";
    public static final String PLAY = "PLAY";
    public static final String PRINT_CHAR = "PRINT_CHAR";
    public static final String READ_CHAR = "READ_CHAR";
    public static final String SET_VALUE = "SET_VALUE";
    public static final String START = "START";
    public static final String STOP = "STOP";

    public InterpreterModel() {
        this.timeline = new Timeline(
            new KeyFrame(Duration.millis(TICK_RATE), event -> {
                this.interpretNextCommand();
            })
        );
        this.timeline.setRate(1);
        this.timeline.setCycleCount(Animation.INDEFINITE);

        this.isInterpreting = false;
    }

    // </editor-fold>

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public Status getStatus() {
        Status status = this.timeline.getStatus();
        if (status == Status.PAUSED && this.isInterpreting)
            return Status.RUNNING;
        return status;
    }

    public boolean isOutOfInput() {
        return this.interpreter.isOutOfInput();
    }

    public double getRate() {
        return this.timeline.getRate();
    }

    public void setExecutionRate(Number rate) {
        this.timeline.setRate((double) rate);
    }

    // </editor-fold>

    /**************************************************************************
     * Control Methods
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public void startNewInterpreter(String code, String input) {
        new BfLogger("interpreter").logMethod();
        try {
            this.interpreter = new Interpreter(code, input);
            this.playInterpreter();
            this.isInterpreting = true;
            this.firePropertyChange(START, null);
        } catch (MismatchedBracketException ex) {
            this.firePropertyChange(EXCEPTION, ex);
            this.firePropertyChange(STOP, null);
        }
    }

    public void playInterpreter() {
        new BfLogger("interpreter").logMethod();
        this.timeline.play();
        this.isInterpreting = true;
        this.firePropertyChange(PLAY, null);
    }

    public void pauseInterpreter() {
        new BfLogger("interpreter").logMethod();
        // Does nothing if not running
        this.timeline.pause();
        this.isInterpreting = false;
        this.firePropertyChange(PAUSE, null);
    }

    private void setWaitingForInput(boolean value) {
        this.isInterpreting = value;
    }

    public void stopInterpreter() {
        new BfLogger("interpreter").logMethod();
        // Does nothing if not running
        this.timeline.stop();
        this.interpreter = null;
        this.isInterpreting = false;
    }

    public void pushInput(Character c) {
        this.interpreter.pushInput(c);
        this.setWaitingForInput(false);
        this.playInterpreter();
        this.firePropertyChange(SET_VALUE, (int) c);
    }

    private void interpretNextCommand() {
        Interpreter.Frame frame = this.interpreter.interpretNextCommand();

        if (frame.isFinished()) {
            this.firePropertyChange(STOP, null);
            return;
        }

        switch (frame.command) {

            case '>':
                this.firePropertyChange(CURSOR_RIGHT, frame.cursorPos);
                break;

            case '<':
                this.firePropertyChange(CURSOR_LEFT, frame.cursorPos);
                break;

            case '+':
            case '-':
                this.firePropertyChange(SET_VALUE, frame.cursorValue);
                break;

            case '.':
                this.firePropertyChange(PRINT_CHAR, (char) frame.cursorValue);
                break;

            case ',':
                if (this.interpreter.isOutOfInput()) {
                    // Using this.pauseInterpreter() will fire the PAUSE event
                    // causing the Play/Pause button text to change
                    this.timeline.pause();
                    this.setWaitingForInput(true);
                    this.firePropertyChange(READ_CHAR, null);
                } else {
                    this.firePropertyChange(SET_VALUE, frame.cursorValue);
                }
                break;

        }
    }

    // </editor-fold>

}
