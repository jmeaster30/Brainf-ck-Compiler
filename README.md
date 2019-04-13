# bf-compiler

This is an interpreter/compiler for the Brainf*ck esoteric programming language.

Once you build either the java or the c version* of this program run it like any other program but there are flags to control the program a bit:


the file you want to interpret or compile will be the last parameter


the -i flag means you want to interpret the file

the -c flag means you want to compile the file

--both of these flags can be put in you will just recieve the interpreted output right away and a compiled executable


the -o \<filename\> flag will cause the compiled executable to be named "\<filename\>"


<h4>Notes:</h4>
http//calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html --- I found this blog post talking about some opltimizations you can make for brainfuck compilers and I got some ideas of how to make the optimizations more general.
