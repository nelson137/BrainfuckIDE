===============================================================================
Title  : Max Cell Value
Author : Nelson Earle
===============================================================================

[ This implementation of Brainfuck limits cell values to the interval [0,255].
  If a cell somehow obtains a value that is outside of this range (likely
  through the read command) the resulting behavior of the program is undefined.

  Cell values will also wrap, meaning that if a cell has a value of 0 and is
  decremented, then it will wrap up to 255. Conversely if a cell has a value of
  255 and is incremented, then it will wrap down to 0.

  This program demonstrates cell value wrapping by:

    A) Incrementing a cell to 255 then one more time so that it wraps down to 0

    B) Decrementing the cell so that it wraps back up to 255 then continues
       decrementing until it reaches 0
]

+[+]    A
-[-]    B
