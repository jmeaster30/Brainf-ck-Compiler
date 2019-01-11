import java.util.ArrayList;
import java.io.*;

public class BFC
{
	public static enum Command
	{
		START,
		END,
		LEFT,
		RIGHT,
		ADD,
		SUB,
		OUTPUT,
		INPUT;
	}
	
	public static class Instruction
	{
		public Command command;
		public int value;
		
		public Instruction()
		{
			command = null;
			value = 0;
		}
		
		public Instruction(Command c, int val)
		{
			command = c;
			value = val;
		}
	}
	
	public static int interpret(String program)
	{
		return 0;
	}
	
	public static void foldInstructions(ArrayList<Instruction> list)
	{
		int loopLevel = 0;
		int listLength = list.size();
		for(int i = listLength - 1; i >= 0; i--)
		{
			boolean fold = false;
			Instruction curr = list.get(i);
			Instruction next = list.get(i - 1);
			switch(curr.command)
			{
				case START:
					curr.value = loopLevel;
					loopLevel -= 1;
					break;
				case END:
					loopLevel += 1;
					curr.value = loopLevel;
					break;
				case LEFT:
				case RIGHT:
					if(next.command == curr.command) fold = true;
					break;
				case ADD:
				case SUB:
					if(next.command == curr.command) fold = true;
					break;
				case OUTPUT:
				case INPUT:
					//i can maybe fold these to input and output more bytes
					break;
				default:
					break;
			}
			if(fold)
			{
				next.value += curr.value;
				list.remove(i);
			}
		}
	}
	
	public static ArrayList<Instruction> generateInstructionList(String program)
	{
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		int stringLength = program.length();
		for(int i = 0; i < stringLength; i++)
		{
			Instruction item = new Instruction();
			item.value = 1;
			char c = program.charAt(i);
			switch(c)
			{
				case '<':
					item.command = Command.LEFT;
					break;
				case '>':
					item.command = Command.RIGHT;
					break;
				case '+':
					item.command = Command.ADD;
					break;
				case '-':
					item.command = Command.SUB;
					break;
				case '[':
					item.command = Command.START;
					break;
				case ']':
					item.command = Command.END;
					break;
				case '.':
					item.command = Command.OUTPUT;
					break;
				case ',':
					item.command = Command.INPUT;
					break;
				default:
					//shouldn't get here since the cases cover every possible char in the program string
					break;
			}
		}
		return list;
	}
	
	public static int compile(String program)
	{
		ArrayList<Instruction> instructionList = generateInstructionList(program);
		return 0;
	}	
	
	public static void main(String[] args)
	{
		//-o <filename> is the output executable default is the same name as the input filename
		//-i the bf code will be interpreted
		//-c the bf code will be compiled
		String filename = null;
		String outputname = null;
		String program = "";
		
		boolean comp = false;
		boolean interp = false;
		
		FileInputStream in = null;
		
		int i = 0;
		for(String arg : args)
		{
			//System.out.println(arg + " == " + args[i]);
			if(arg.equals("-o")) outputname = args[i + 1];
			if(arg.equals("-c")) comp = true;
			if(arg.equals("-i")) interp = true;
			if(i == args.length - 1) filename = arg;
			i++;
		}
		
		try 
		{
			in = new FileInputStream(filename);
			
			int c;
			while ((c = in.read()) != -1) 
			{
				if(c == '+' || c == '-' || c == '.' || c == ',' ||
				   c == '[' || c == ']' || c == '<' || c == '>')
					program += (char)c;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}