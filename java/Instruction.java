public class Instruction
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
	
	public String getString()
	{
		return command.name() + " " + Integer.toString(value);
	}
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		if(!(o instanceof Instruction)) return false;
		if(command == ((Instruction)o).command && value == ((Instruction)o).value) return true;
		return false;
	}
}