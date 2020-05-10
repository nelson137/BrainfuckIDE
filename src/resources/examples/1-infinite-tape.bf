===============================================================================
Title  : Infinite Tape
Author : Nelson Earle
===============================================================================

[ In this implementation of Brainfuck, the start of the tape (the left side) is
  fixed but the right side grows indefinitely and on demand such that the
  cursor cannot go off the end of the tape.

  Likewise, the visualizer will add cells as needed. When the cursor is on the
  last cell and attempts to move to the right, another cell is added to the
  visualizer.

  If the cursor attempts to move left when already on the left-most cell
  nothing will happen.

  This program demonstrates the "infinite" visualizer tape.
]

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
