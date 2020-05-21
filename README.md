# bf-compiler

This is an interpreter/compiler for the Brainf*ck esoteric programming language.

## Usage:
Run the 'bfc' script in a command line followed by the brainf*ck source to compile or interpret it.

### Flags:

* -t <target>
Used for specifying what target you would like to compile to.
Currently there are only two options: C, MIPS  

* -i
Specifies that you would like to interpret the source file.

* -c
Specifies that you would like to compile the source file.

##### At least one of the -i and -c flags are required

* -o \<filename\> 
Specifies the name of the output file by default it is called "out"

## Notes:
http://calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html --- I found this blog post talking about some opltimizations you can make for brainfuck compilers and I got some ideas of how to make the optimizations more general.
