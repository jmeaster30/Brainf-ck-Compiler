import java.util.*;
import java.io.*;

public class Interpreter
{
	private String program;
	
	private int pc;
	private int programLength;
	
	private ArrayList<Character> memory;
	private int pointerPosition;
	
	private Deque<Integer> loopStack;
	private int[] loopArray;
	
	public Interpreter(String pg)
	{
		program = pg;
		pc = 0;
		programLength = program.length();
		pointerPosition = 0;
		
		memory = new ArrayList<Character>(); 
		loopStack = new ArrayDeque<Integer>();
		loopArray = makeLoopArray();
	}
	
	private int[] makeLoopArray()
	{
		int[] arr = new int[programLength];
		Deque<Integer> loopStartIndex = new ArrayDeque<Integer>();
		for(int i = 0; i < programLength; i++)
		{
			char c = program.charAt(i);
			if(c == '[')
			{
				loopStartIndex.addFirst(i);
				arr[i] = 0;
			}
			else if(c == ']')
			{
				int index = loopStartIndex.removeFirst();
				arr[index] = i;
				arr[i] = index;
			}
			else
			{
				arr[i] = 0;
			}
		}
		
		return arr;
	}
	
	private int interpret(char inst)
	{
		int error = 0;
		switch(inst)
		{
			case '<':
				if(pointerPosition > 0) 
					pointerPosition -= 1;
				//else
				//	pointerPosition = 0;
				break;
			case '>':
				if(pointerPosition < memory.size() - 1) 
					pointerPosition += 1;
				else// if(pointerPosition >= memory.size() - 1)
				{
					memory.add((char)0);
					pointerPosition += 1;
				}
				break;
			case '+':
				if(pointerPosition >= memory.size())
					memory.add((char)(1));
				else
					memory.set(pointerPosition, (char)(memory.get(pointerPosition) + 1));
				break;
			case '-':
				if(pointerPosition >= memory.size())
					memory.add((char)(-1));
				else
					memory.set(pointerPosition, (char)(memory.get(pointerPosition) - 1));
				break;
			case '[':
				if(memory.get(pointerPosition).equals((char)0))//go to end of loop
					pc = loopArray[pc];
				//else
				//	loopStack.addFirst(pc);
				break;
			case ']':
				if(!memory.get(pointerPosition).equals((char)0))//go back to start of loop
					pc = loopArray[pc];
				//else
				//	loopStack.removeFirst();
				break;
			case '.':
				//output
				if(pointerPosition < memory.size())
					System.out.print(memory.get(pointerPosition));
				break;
			case ',':
				//input
				Scanner sc = new Scanner(System.in);
				char b = (char)sc.nextByte();
				memory.set(pointerPosition, b);
				sc.close();
				break;
			default:
			//noop
				break;
		}
	
		pc++;
		return error;
	}
	
	public int interpret()
	{
		int error = 0;
		while(pc < programLength)
			error = interpret(program.charAt(pc));
		return error;
	}
}