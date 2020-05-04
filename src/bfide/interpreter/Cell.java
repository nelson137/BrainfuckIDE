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
        this.value = c;
    }

    public char getChar() {
        return this.value;
    }

    public void inc() {
        this.value++;
    }

    public void dec() {
        this.value--;
    }

}
