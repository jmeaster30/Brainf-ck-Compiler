#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Target.h"
#include "Interpreter.h"

int main(int argc, char** argv)
{
  char* filename = NULL;
  char* outputname = NULL;
  char* program = NULL;
  char* targetname = NULL;

  Target target = C;

  int comp = 0;
  int interp = 0;

  FILE* in = NULL;

  int i = 0;
  for(i = 0; i < argc; i++)
  {
    char* arg = argv[i];
    if(i == argc - 1)
    {
      filename = (char*)malloc(strlen(arg) + 1);
      memset(filename, 0, strlen(arg) + 1);
      memcpy(filename, arg, strlen(arg));
    }
    if(strcmp(arg, "-o") == 0)
    {
      outputname = (char*)malloc(strlen(argv[i + 1]) + 1);
      memset(outputname, 0, strlen(argv[i + 1]) + 1);
      memcpy(outputname, argv[i + 1], strlen(argv[i + 1]));
    }
    else if(strcmp(arg, "-c") == 0){comp = 1;}
    else if(strcmp(arg, "-i") == 0){interp = 1;}
    else if(strcmp(arg, "-t") == 0)
    {
      targetname = (char*)malloc(strlen(argv[i + 1]) + 1);
      memset(targetname, 0, strlen(argv[i + 1]) + 1);
      memcpy(targetname, argv[i + 1], strlen(argv[i + 1]));
    }
  }

  if((comp == 0 && interp == 0) || filename == NULL)
  {
    printf("Usage: bfc -t (C | MIPS) -c -i -o (FILENAME) (SOURCE)\n"
           "\t-t TARGET will compile into TARGET(by default this will compile into C)\n"
           "\t\tTARGETS: C, MIPS\n"
           "\t-c flag to specify you would like to compile the brainfuck code\n"
           "\t-i flag to specify you would like to interpret the brainfuck code\n"
           "\t-o FILENAME specifies you would like the output file to be of name FILENAME\n"
           "NOTE: always end the command with the brainfuck source file. However the other flags can be in any order you want.\n");
    return 0;
  }

  in = fopen(filename, "r");
  if(in == NULL)
  {
    printf("ERROR: There was a problem opening the file %s\n", filename);
    return 1;
  }

  int programSize = 0;
  int c = 0;
  while(c != -1)
  {
    c = fgetc(in);
    if(c == '+' || c == '-' || c == '.' || c == ',' ||
       c == '[' || c == ']' || c == '<' || c == '>')
    {
      char* newprog = (char *)malloc(programSize + 1);
      memset(newprog, 0, programSize + 1);
      memcpy(newprog, program, programSize);
      newprog[programSize] = (char)c;
      program = newprog;
      programSize += 1;
    }
  }

  //printf("program: %s\nsize: %i\n", program, programSize);
  if(interp == 1)
  {
    interpret(program, programSize);
  }
}
