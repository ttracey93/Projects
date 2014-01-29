This project was built to assess a text file and read it into a hash table.

If the line in the text file contains a string followed by an integer,
i.e. "Moe 18", then hash Moe and store it with an address value of 18.
If the line contains just a name, report its entry location in the table,
unless it does not exist in the table, then report an error.

The makefile for this project will compile it by using the make command.
p1 is an executable that will run the program easily as well. 
>make
>p1 dat
will show you sample output.

dat is a file I created as sample input to demonstrate this project.

This project was step one in creating an assembler to take Assembly code
and translate it into hexidecimal instructions.