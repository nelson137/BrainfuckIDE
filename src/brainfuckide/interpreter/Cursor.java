package brainfuckide.interpreter;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Cursor {

    private final Tape tape;

    private int index;
    private Cell cell;

    Cursor(Tape tape) {
        this.tape = tape;
        this.index = 0;
        this.cell = this.tape.get(index);
    }

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    public int getPos() {
        return this.index;
    }

    public int getValue() {
        return this.cell.get();
    }

    public void setValue(char value) {
        this.cell.set(value);
    }

    /**************************************************************************
     * Movement
     *************************************************************************/

    public void right() {
        this.cell = this.tape.get(++this.index);
    }

    public void left() {
        if (this.index > 0)
            this.cell = this.tape.get(--this.index);
    }

    /**************************************************************************
     * Cell Manipulation
     *************************************************************************/

    public void inc() {
        this.cell.inc();
    }

    public void dec() {
        this.cell.dec();
    }

}
