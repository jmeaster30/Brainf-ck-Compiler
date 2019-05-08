PROG = bfc
CC = gcc
CFLAGS = 
LDFLAGS = 
OBJS = Interpreter.o BFC.o 

$(PROG) : $(OBJS)
	$(CC) $(LDFLAGS) -o $(PROG) $(OBJS)
Interpreter.o : Interpreter.h
	$(CC) $(CFLAGS) -c Interpreter.c
BFC.o : 
	$(CC) $(CFLAGS) -c BFC.c

clean:
	rm -f core $(OBJS)