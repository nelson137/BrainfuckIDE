package brainfuckide.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public final class Tape {

    private int segmentSize;

    private LinkedList<ArrayList<Cell>> segments;

    Tape() {
        this(64);
    }

    Tape(int segmentSize) {
        this.segmentSize = segmentSize;
        this.segments = new LinkedList<>();
        this.addSegment();
    }

    public void addSegment() {
        Cell segment[] = new Cell[this.segmentSize];
        for (int i=0; i<this.segmentSize; i++)
            segment[i] = new Cell();
        this.segments.add(new ArrayList<>(Arrays.asList(segment)));
    }

    public int getSegmentSize() {
        return this.segmentSize;
    }

    public int getNumSegments() {
        return this.segments.size();
    }

    public int size() {
        return this.segments.size() * this.segmentSize;
    }

    public Cell get(int index) {
        int segmentIndex = index / this.segmentSize;
        int cellIndex = index % this.segmentSize;
        while (this.segments.size() <= segmentIndex)
            this.addSegment();
        return this.segments.get(segmentIndex).get(cellIndex);
    }

}
