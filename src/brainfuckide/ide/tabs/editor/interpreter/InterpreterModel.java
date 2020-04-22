package brainfuckide.ide.tabs.editor.interpreter;

import brainfuckide.interpreter.Interpreter;
import brainfuckide.util.MVCModel;
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

    private final Timeline timeline;
    private static final double TICK_RATE = 1_000;

    private Interpreter interpreter;

    private boolean isInterpreting;

    public static final String CURSOR_MOVE_RIGHT = "CURSOR_MOVE_RIGHT";
    public static final String CURSOR_MOVE_LEFT = "CURSOR_MOVE_LEFT";
    public static final String SET_VALUE = "SET_VALUE";
    public static final String PRINT_CHAR = "PRINT_CHAR";
    public static final String READ_CHAR = "READ_CHAR";
    public static final String DONE = "DONE";

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

    public void startNewInterpreter(String code, String input) {
        // Only start new interpreter if stopped
        if (this.timeline.getStatus() == Status.STOPPED)
            this.interpreter = new Interpreter(code, input);
        this.playInterpreter();
        this.isInterpreting = true;
    }

    public void playInterpreter() {
        this.timeline.play();
    }

    public void pauseInterpreter() {
        // Does nothing if not running
        this.timeline.pause();
    }

    private void setWaitingForInput(boolean value) {
        this.isInterpreting = value;
        System.out.println("waitingForInput(" + value + ")");
    }

    public void stopInterpreter() {
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

        if (frame.isDone()) {
            this.firePropertyChange(DONE, null);
            return;
        }

        switch (frame.command) {

            case '>':
                this.firePropertyChange(CURSOR_MOVE_RIGHT, frame.cursorPos);
                break;

            case '<':
                this.firePropertyChange(CURSOR_MOVE_LEFT, frame.cursorPos);
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
                    this.pauseInterpreter();
                    this.setWaitingForInput(true);
                    this.firePropertyChange(READ_CHAR, null);
                }
                break;

        }
    }

}
