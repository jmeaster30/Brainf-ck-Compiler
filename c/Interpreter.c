#include "Interpreter.h"

char* memory = NULL;
int* looparray = NULL;

int memsize = 0;
int mempos = 0;
int pc = 0;

int makeLoopArray(char* program, int programSize)
{
  looparray = (int*)malloc(programSize * sizeof(int));
  memset(looparray, -1, programSize * sizeof(int));
  //max amount of start loop operands must be half of the program size since if there were more we wouldnt have enough matching end loop
  int* loopstack = (int*)malloc((programSize * sizeof(int) / 2) + 1);
  memset(loopstack, 0, (programSize * sizeof(int) / 2) + 1);
  int top = 0;//points to the empty spot above the stack
  int i = 0;
  for(i = 0; i < programSize; i++)
  {
    char c = program[i];
    if(c == '[')
    {
      loopstack[top++] = i;
      looparray[i] = 0;
    }
    else if(c == ']')
    {
      if(top == 0) return -1;
      int index = loopstack[--top];
      looparray[index] = i;
      looparray[i] = index;
    }
  }
  free(loopstack);
  if(top != 0) return -1;
  return 0;
}

int interpret(char* program, int programSize)
{
  //initialize memory with 30000 spots
  memsize = 30000;
  memory = (char*)malloc(memsize);
  memset(memory, 0, memsize);
  //make loop array that keeps track of matching brackets
  if(makeLoopArray(program, programSize) == -1)
  {
    printf("ERROR: Unmatched loop brackets\n");
    return -1;
  }
  //start interpreting
  for(pc = 0; pc < programSize; pc++)
  {
    char inst = program[pc];
    char c;
    switch(inst)
    {
      case '<':
        if(mempos > 0) mempos--;
        break;
      case '>':
        if(mempos < memsize - 1)
          mempos++;
        else
        {
          //allocate more memory
          memsize *= 2;
          memory = realloc(memory, memsize);
          if(memory == NULL) return -1;
          mempos++;
        }
        break;
      case '+':
        c = memory[mempos];
        c += 1;
        memory[mempos] = c;
        break;
      case '-':
        c = memory[mempos];
        c -= 1;
        memory[mempos] = c;
        break;
      case '[':
        if(memory[mempos] == 0) pc = looparray[pc];
        break;
      case ']':
        if(memory[mempos] != 0) pc = looparray[pc];
        break;
      case '.':
        putchar(memory[mempos]);
        break;
      case ',':
        memory[mempos] = getchar();
        break;
      default:
        break;
    }
  }
  return 0;
}
