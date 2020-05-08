package bfide.interpreter;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Interpreter {

    public static class MismatchedBracketException extends Exception {
        public MismatchedBracketException(String msg) {
            super(msg);
        }
    }

    public static final MismatchedBracketException MBE_OPEN =
        new MismatchedBracketException(
            "Opening bracket ([) has no matching closing bracket (])");

    public static final MismatchedBracketException MBE_CLOSE =
        new MismatchedBracketException(
            "Closing bracket (]) has no matching opening bracket ([)");

    /**************************************************************************
     * Fields & Constructor & Utility Methods
     *************************************************************************/

    private final String code;
    private int codeIndex;

    private LinkedList<Character> input;

    private final HashMap<Integer, Integer> bracemap;

    private final Tape tape;
    private final Cursor cursor;

    private boolean outOfInput;

    public Interpreter(String rawCode, String input) throws MismatchedBracketException {
        this.tape = new Tape();
        this.cursor = new Cursor(this.tape);

        Stack braceStack = new Stack();
        this.bracemap = new HashMap<>();
        StringBuilder codeBuilder = new StringBuilder();

        int ci = 0;
        int start;
        CharacterIterator it = new StringCharacterIterator(rawCode);
        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
            switch (c) {
                case '>': case '<': case '+': case '-': case '.': case ',':
                    break;
                case '[':
                    braceStack.push(ci);
                    break;
                case ']':
                    if (braceStack.empty())
                        throw MBE_CLOSE;
                    start = (int) braceStack.pop();
                    bracemap.put(start, ci);
                    bracemap.put(ci, start);
                    break;
                default:
                    // Skip non-brainfuck character
                    continue;
            }
            codeBuilder.append(c);
            ci++;
        }

        if (braceStack.empty() == false)
            throw MBE_OPEN;

        this.code = codeBuilder.toString();
        this.codeIndex = -1;

        this.input = new LinkedList<>(
            input.chars().mapToObj(i -> (char) i).collect(Collectors.toList())
        );

        this.outOfInput = false;
    }

    public boolean isOutOfInput() {
        return this.outOfInput;
    }

    /**************************************************************************
     * Commands
     *************************************************************************/

    private void jumpToMatchingBracket() {
        this.codeIndex = this.bracemap.get(this.codeIndex) - 1;
    }

    public void pushInput(Character c) {
        this.outOfInput = false;
        this.cursor.setValue(c);
    }

    public class Frame {

        public final char command;
        public final int cursorPos;
        public final int cursorValue;

        Frame(char command, int pos, int value) {
            this.command = command;
            this.cursorPos = pos;
            this.cursorValue = value;
        }

        public boolean isFinished() {
            return this.cursorPos == -1 || this.cursorValue == -1;
        }

    }

    public boolean hasNextCommand() {
        return this.codeIndex + 1 < this.code.length();
    }

    public Frame interpretNextCommand() {
        if (this.hasNextCommand() == false)
            return new Frame('\0', -1, -1);

        if (this.outOfInput == false)
            this.codeIndex++;
        final Character command = this.code.charAt(this.codeIndex);

        switch (command) {

            case '>': this.cursor.right(); break;

            case '<': this.cursor.left(); break;

            case '+': this.cursor.inc(); break;

            case '-': this.cursor.dec(); break;

            case '[':
                if (this.cursor.getValue() == 0)
                    this.jumpToMatchingBracket();
                break;

            case ']':
                if (this.cursor.getValue() != 0)
                    this.jumpToMatchingBracket();
                break;

            case '.': break;

            case ',':
                Character c = this.input.pollFirst();
                if (c == null)
                    this.outOfInput = true;
                else
                    this.pushInput(c);
                break;

        }

        return new Frame(
            command,
            this.cursor.getPos(),
            this.cursor.getValue()
        );
    }

}
