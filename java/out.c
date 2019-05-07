#include <stdlib.h> 
#include <stdio.h>
#include <string.h>
unsigned char* mem;
int mempos = 0;
int main(){
	mem = (unsigned char*)malloc(sizeof(unsigned char) * 22);
	memset(mem, 0, sizeof(unsigned char) * 22);
	mem[mempos] += 8;
	while(mem[mempos] != 0){
		mempos += 1;
		mem[mempos] += 4;
		while(mem[mempos] != 0){
			mempos += 1;
			mem[mempos] += 2;
			mempos += 1;
			mem[mempos] += 3;
			mempos += 1;
			mem[mempos] += 3;
			mempos += 1;
			mem[mempos] += 1;
			mempos -= 4;
			mem[mempos] -= 1;
		}
		mempos += 1;
		mem[mempos] += 1;
		mempos += 1;
		mem[mempos] += 1;
		mempos += 1;
		mem[mempos] -= 1;
		mempos += 2;
		mem[mempos] += 1;
		while(mem[mempos] != 0){
			mempos -= 1;
		}
		mempos -= 1;
		mem[mempos] -= 1;
	}
	mempos += 2;
	printf("%c", mem[mempos]);
	mempos += 1;
	mem[mempos] -= 3;
	printf("%c", mem[mempos]);
	mem[mempos] += 7;
	printf("%c", mem[mempos]);
	printf("%c", mem[mempos]);
	mem[mempos] += 3;
	printf("%c", mem[mempos]);
	mempos += 2;
	printf("%c", mem[mempos]);
	mempos -= 1;
	mem[mempos] -= 1;
	printf("%c", mem[mempos]);
	mempos -= 1;
	printf("%c", mem[mempos]);
	mem[mempos] += 3;
	printf("%c", mem[mempos]);
	mem[mempos] -= 6;
	printf("%c", mem[mempos]);
	mem[mempos] -= 8;
	printf("%c", mem[mempos]);
	mempos += 2;
	mem[mempos] += 1;
	printf("%c", mem[mempos]);
	mempos += 1;
	mem[mempos] += 2;
	printf("%c", mem[mempos]);
	return 0;
}