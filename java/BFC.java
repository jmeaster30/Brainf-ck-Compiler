import java.util.*;
import java.io.*;

public class BFC
{
	public static void main(String[] args)
	{
		//-o <filename> is the output executable default is the same name as the input filename
		//-i the bf code will be interpreted
		//-c the bf code will be compiled
		String filename = null;
		String outputname = null;
		String program = "";
    String targetname = "";

    Target target = Target.C;

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
      if(arg.equals("-t")) targetname = args[i + 1];
			if(i == args.length - 1) filename = arg;
			i++;
		}

    if(targetname.toLowerCase().equals("c")) target = Target.C;
    if(targetname.toLowerCase().equals("mips")) target = Target.MIPS;

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
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//System.out.println(program);
		if(interp)
		{
			Interpreter interpreter = new Interpreter(program);
			interpreter.interpret();
		}
		if(comp)
		{
			Compiler compiler = new Compiler(program, (outputname == null) ? "out" : outputname);
			compiler.compile(target);
		}
	}
}
