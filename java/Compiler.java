import java.util.*;

public class Compiler
{	
	private String program;
	private String outputFilename;
	
	
	public Compiler(String p, String o)
	{
		program = p;
		outputFilename = o;
	}
	
	//public void matchLoopBrackets(ArrayList<Instruction> list)
	//{
	//	Deque<Integer> loopStartIndex = new ArrayDeque<Integer>();
	//	for(int i = 0; i < list.size(); i++)
	//	{
	//		Instruction inst = list.get(i);
	//		if(inst.command == Command.START)
	//		{
	//			loopStartIndex.addFirst(i);
	//		}
	//		else if(inst.command == Command.END)
	///		{
	//			int index = loopStartIndex.removeFirst();
	//			Instruction start = list.get(index);
	//			inst.value = index;
	//			start.value = i;
	//		}
	//	}
	//}
	
	//this functions folds instructions that are repeated.
	//the resulting list is shorter and thus when it is compiled it is optimized slightly
	public void foldInstructions(ArrayList<Instruction> list)
	{
		int loopLevel = 0;
		int listLength = list.size();
		for(int i = listLength - 1; i > 0; i--)
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
	
	//turns the string into a list of instructions
	public ArrayList<Instruction> generateInstructionList(String program)
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
			list.add(item);
		}
		return list;
	}
	
	//this function will give an over estimate of the memory needed to run the program
	//I believe it is impossible to determine the max memory require since the memory is thoretically infinite
	public int minimumMemory(ArrayList<Instruction> list)
	{
		int mem = 1;
		int i = 1;
		for(Instruction inst : list)
		{
			if(inst.command == Command.RIGHT)
			{
				if(i == mem)
				{
					mem++;
					i++;
				}
				else
				{
					i++;
				}
			}
			else if(inst.command == Command.LEFT)
			{
				if(i != 0) i--;
			}
		}
		return mem;
	}
	
	//compiles the program
	//for now it will just generate c code
	public int compile()
	{
		ArrayList<Instruction> instructionList = generateInstructionList(program);
		foldInstructions(instructionList);
		for(Instruction inst : instructionList)
		{
			//System.out.println(inst.getString());
		}
		int memRequired = minimumMemory(instructionList);
		//System.out.println(memRequired);
		return 0;
	}
}