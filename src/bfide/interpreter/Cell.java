package bfide.interpreter;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Cell {

    private char value = 0;

    public int get() {
        return this.value;
    }

    public void set(char c) {
        this.value = c > 255 ? 0 : c;
    }

    public char getChar() {
        return this.value;
    }

    public void inc() {
        if (this.value >= 255)
            this.value = 0;
        else
            this.value++;
    }

    public void dec() {
        if (this.value <= 0)
            this.value = 255;
        else
            this.value--;
    }

}
