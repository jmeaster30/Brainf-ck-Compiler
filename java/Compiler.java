import java.util.*;
import java.io.*;

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

  public void clearOp(ArrayList<Instruction> list)
  {
    //look for [-] sequence
    int size = list.size();
    int matchIndex = 0;
    ArrayList<Instruction> clear = new ArrayList<Instruction>();
    clear.add(new Instruction(Command.START, 0, 0));
    clear.add(new Instruction(Command.SUB, 0, 0));
    clear.add(new Instruction(Command.END, 0, 0));
    for(int i = 0; i < size; i++)
    {
      Instruction inst = list.get(i);
      if(inst.command == clear.get(matchIndex).command)
        matchIndex++;
      else
        matchIndex = 0;
      if(matchIndex == 3)
      {
        list.remove(i);
        list.remove(i - 1);
        list.set(i - 2, new Instruction(Command.CLEAR, 0, 0));
        i -= 2;
        size = list.size();
        matchIndex = 0;
      }
    }
  }

  public void multOp2(ArrayList<Instruction> list)
  {
    int size = list.size();
    int matchIndex = 0;
    int startIndex = -1;
    ArrayList<Instruction> mult = new ArrayList<Instruction>();
    mult.add(new Instruction(Command.START, 0, 0));
    mult.add(new Instruction(Command.RIGHT, 0, 0));
    mult.add(new Instruction(Command.ADD, 0, 0));
    mult.add(new Instruction(Command.LEFT, 0, 0));
    mult.add(new Instruction(Command.SUB, 0, 0));
    mult.add(new Instruction(Command.END, 0, 0));
    ArrayList<Integer> offsetList = new ArrayList<Integer>();
    ArrayList<Integer> multList = new ArrayList<Integer>();
    for(int i = 0; i < size; i++)
    {
      Instruction inst = list.get(i);
      if(inst.command == Command.START) startIndex = i;
      if(inst.command == mult.get(matchIndex).command)
      {
        matchIndex++;
        if(matchIndex == 3)
        {
          //i is multval
          //i-1 is offset
          int last = 0;
          if(offsetList.size() != 0) last = offsetList.get(offsetList.size() - 1);
          offsetList.add(last + list.get(i - 1).value);
          multList.add(list.get(i).value);
        }
      }
      else if(inst.command == Command.RIGHT && matchIndex == 3)
      {
        matchIndex = 2;
      }
      else
      {
        matchIndex = 0;
        startIndex = -1;
        offsetList.clear();
        multList.clear();
        if(inst.command == Command.START)
        {
          startIndex = i;
          matchIndex++;
        }
      }
      if(matchIndex == mult.size())
      {
        if(list.get(i - 1).value == 1 && list.get(i - 2).value == offsetList.get(offsetList.size() - 1))
        {
          for(int j = i; j >= startIndex; j--)
          {
            list.remove(j);
          }
          for(int j = 0; j < offsetList.size(); j++)
          {
            list.add(startIndex + j, new Instruction(Command.MULT, multList.get(j), offsetList.get(j)));
          }
          list.add(startIndex + offsetList.size(), new Instruction(Command.CLEAR, 0, 0));
        }
        i = startIndex + offsetList.size() - 1;
        size = list.size();
        matchIndex = 0;
      }
    }
  }

  public void multOp(ArrayList<Instruction> list)
  {
    int size = list.size();
    int matchIndex = 0;
    int startIndex = -1;
    ArrayList<Instruction> mult = new ArrayList<Instruction>();
    mult.add(new Instruction(Command.START, 0, 0));
    mult.add(new Instruction(Command.SUB, 0, 0));
    mult.add(new Instruction(Command.RIGHT, 0, 0));
    mult.add(new Instruction(Command.ADD, 0, 0));
    mult.add(new Instruction(Command.LEFT, 0, 0));
    mult.add(new Instruction(Command.END, 0, 0));
    ArrayList<Integer> offsetList = new ArrayList<Integer>();
    ArrayList<Integer> multList = new ArrayList<Integer>();
    for(int i = 0; i < size; i++)
    {
      Instruction inst = list.get(i);
      if(inst.command == Command.START) startIndex = i;
      if(inst.command == mult.get(matchIndex).command)
      {
        matchIndex++;
        if(matchIndex == 4)
        {
          //i is multval
          //i-1 is offset
          int last = 0;
          if(offsetList.size() != 0) last = offsetList.get(offsetList.size() - 1);
          offsetList.add(last + list.get(i - 1).value);
          multList.add(list.get(i).value);
        }
      }
      else if(inst.command == Command.RIGHT && matchIndex == 4)
      {
        matchIndex = 3;
      }
      else
      {
        matchIndex = 0;
        startIndex = -1;
        if(inst.command == Command.START)
        {
          startIndex = i;
          matchIndex++;
        }
      }
      if(matchIndex == mult.size())
      {
        if(list.get(startIndex + 1).value == 1 && list.get(i - 1).value == offsetList.get(offsetList.size() - 1))
        {
          for(int j = i; j >= startIndex; j--)
          {
            list.remove(j);
          }
          for(int j = 0; j < offsetList.size(); j++)
          {
            list.add(startIndex + j, new Instruction(Command.MULT, multList.get(j), offsetList.get(j)));
          }
          list.add(startIndex + offsetList.size(), new Instruction(Command.CLEAR, 0, 0));
        }
        i = startIndex + offsetList.size() - 1;
        size = list.size();
        matchIndex = 0;
      }
    }
  }

  public void optimize(ArrayList<Instruction> list)
  {
    ArrayList<Instruction> prev = (ArrayList<Instruction>)list.clone();
    foldInstructions(list);
    while(prev.equals(list))
    {
      prev = (ArrayList<Instruction>)list.clone();
      foldInstructions(list);
      clearOp(list);
      multOp(list);
      multOp2(list);
    }
  }

	//this function will give an over estimate of the memory needed to run the program
	//I believe it is impossible to determine the max memory require since the memory is thoretically infinite
	public int memoryNeeded(ArrayList<Instruction> list)
	{
		int mem = 1;
		int i = 1;
    Instruction prevMult = null;
		for(Instruction inst : list)
		{
      if(prevMult != null && inst.command != Command.MULT)
      {
        if(mem < i + prevMult.offset)
				{
					mem += prevMult.offset - (mem - i);
					i += prevMult.offset;
				}
				else
				{
					i += prevMult.offset;
				}
        prevMult = null;
      }
			if(inst.command == Command.RIGHT)
			{
				if(mem < i + inst.value)
				{
					mem += inst.value - (mem - i);
					i += inst.value;
				}
				else
				{
					i += inst.value;
				}
			}
			else if(inst.command == Command.LEFT)
			{
				if(i != 0)
        {
          i -= inst.value;
          if(i < 0) i = 0;
        }
			}
      else if(inst.command == Command.MULT)
      {
        //find last in string of copy statements
        prevMult = inst;
      }
		}
    //just in case we have a copy instruction at the end of the program
    if(prevMult != null)
    {
      if(mem < i + prevMult.offset)
      {
        mem += prevMult.offset - (mem - i);
        i += prevMult.offset;
      }
      else
      {
        i += prevMult.offset;
      }
    }
		return mem;
	}

	public void write(String str, BufferedWriter out)
	{
		try
		{
			for(int i = 0; i < str.length(); i++)
				out.write((int)(str.charAt(i)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//this function outputs the optimized instructions into a file
	//for now we will output into c code
	public int outputC(ArrayList<Instruction> list, int memRequired)
	{
		BufferedWriter out = null;
		BufferedReader in = null;

		try
		{
			out = new BufferedWriter(new FileWriter(outputFilename + ".c"));
      //takes in the base c code needed
			in = new BufferedReader(new FileReader("base.base"));
			int c;
			while ((c = in.read()) != -1)
			{
				out.write(c);
				//out.flush();
			}
			in.close();
			int looplevel = 1;
			String str = "\tmem = (unsigned char*)malloc(sizeof(unsigned char) * " + Integer.toString(memRequired) + ");\n\tmemset(mem, 0, sizeof(unsigned char) * " + Integer.toString(memRequired) + ");\n";
			//write(str, out);
			for(Instruction inst : list)
			{
				switch(inst.command)
				{
					case ADD:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "mem[mempos] += " + Integer.toString(inst.value) + ";\n";
						break;
					case SUB:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "mem[mempos] -= " + Integer.toString(inst.value) + ";\n";
						break;
					case LEFT:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "mempos -= " + Integer.toString(inst.value) + ";\n";
						break;
					case RIGHT:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "mempos += " + Integer.toString(inst.value) + ";\n";
						break;
					case START:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "while(mem[mempos] != 0){\n";
						looplevel++;
						break;
					case END:
						looplevel--;
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "}\n";
						break;
					case OUTPUT:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "printf(\"%c\", mem[mempos]);\n";
						break;
					case INPUT:
						for(int i = 0; i < looplevel; i++)
							str += "\t";
						str += "scanf(\" %c\", &(mem[mempos]));\n";
						break;
          case CLEAR:
            for(int i = 0; i < looplevel; i++)
              str += "\t";
            str += "mem[mempos] = 0;\n";
            break;
          case MULT:
            for(int i = 0; i < looplevel; i++)
              str += "\t";
            str += "mem[mempos + " + Integer.toString(inst.offset) + "] += mem[mempos] * " + Integer.toString(inst.value) + ";\n";
            break;
					default:
						break;
				}
			}
			str += "\treturn 0;\n}";
			write(str, out);
			out.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

  public void outputMIPS(ArrayList<Instruction> list, int memRequired)
  {

  }

	//compiles the program
	//for now it will just generate c code
	public int compile(Target t)
	{
		ArrayList<Instruction> instructionList = generateInstructionList(program);

		System.out.println("Instruction count (before optimizations): " + instructionList.size());
    //for(Instruction inst : instructionList)
    //  System.out.println(inst.command + ", " + inst.value + ", " + inst.offset);
    int memRequired = memoryNeeded(instructionList);
    System.out.println("Memory required: " + memRequired);

    optimize(instructionList);
    System.out.println("Instruction count (after optimizations): " + instructionList.size());
    //for(Instruction inst : instructionList)
    //  System.out.println(inst.command + ", " + inst.value + ", " + inst.offset);
    switch(t)
    {
      case C:
        outputC(instructionList, 2 * memRequired);
        break;
      case MIPS:
        outputMIPS(instructionList, memRequired);
        break;
      default:
        break;
    }

		return 0;
	}
}
