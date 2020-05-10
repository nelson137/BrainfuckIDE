===============================================================================
Title  : Hello World
Author : Nelson Earle
===============================================================================

This program demonstrates a common mechanism used to print data in Brainfuck

Line 'A' increments a cell to be used as a counter for the loop

Lines 'B' through 'E' increment a group of cells next to the counter in each
iteration of the loop

Line 'F' moves the cursor back to the counter cell and decrements it once

Lines 'B' through 'F' are repeated until the counter reaches 0 and the cells
are about 60 or 90 for uppercase and lowercase letters respectively

Remember that a loop terminates if and only if the closing bracket (]) of the
loop is reached and the current cell is 0

By now the cells are as follows with the cursor denoted by parentheses:

    { (0) 10*7 10*10 10*3 10*1 }

The rest of the code moves between the cells incrementing and decrementing them
as necessary to create the letters "Hello World!" and print them out

===============================================================================

+++++ +++++ [        A
    > +++++ ++       B
    > +++++ +++++    C
    > +++            D
    > +              E
    <<<< -           F
]
> ++ .
> + .
+++++ ++ .
.
+++ .
> ++ .
<< +++++ +++++ +++++ .
> .
+++ .
----- - .
----- --- .
> + .
> .
