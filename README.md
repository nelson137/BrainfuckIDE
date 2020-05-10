# BrainfuckIDE
                                                                                            
Brainfuck is an esoteric and turing complete language created by Urban Müller in 1993. It consists of eight simple instructions  and a code pointer. The memory is represented as a tape of integer cells whose values can be incremented, decremented, set by user input, and printed to the console.

The instructions are as follows:

 Instruction | Description
-------------+------------
      >      | Increment the code pointer (move right)
      <      | Decrement the code pointer (move left)
      +      | Increment the current cell
      -      | Decrement the current cell
      ,      | Store a character from user input in the current cell
      .      | Print the current cell's ASCII value
      [      | Denotes the beginning of a loop
      ]      | Denotes the end of a loop

Program execution starts with all cells initialized to 0 and the cursor (or code pointer) on the first cell. The move, increment, and decrement instructions (>, <, +, and -) all behave as expected -- moving the cursor along the tape or changing the value of the cell that the cursor points to. Input/output is achieved with the read (,) and print (.) instructions, acting on the current cell much like the increment and decrement instructions.

Brainfuck also provides a looping mechanism where the body of the loop is surrounded by an opening ([) and closing (]) square bracket. When the program reaches an open-loop instruction ([) it checks the current cell value to determine if the body should be executed. If the value is zero it skips to the matching close-loop instruction (]) and continues execution. Otherwise, the value is non-zero, and the body of the loop is executed. After the body is executed and the program reaches the end of the loop it jumps back to the beginning of the loop and compares the current cell value to 0 again for another potential iteration.

This document serves as a brief introduction to Brainfuck, but a full description can be found on [Wikipedia](https://en.wikipedia.org/wiki/Brainfuck).
