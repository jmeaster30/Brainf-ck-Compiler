from os import listdir

program_name = "bfc"
compiler = "gcc"
cflags = ""
ldflags = ""

list_of_files = listdir("./")
#print(list_of_files)
list_of_headers = []
list_of_sources = []
for f in list_of_files:
	ext = f[-2:]
	file = f[0:-2]
	if ext == ".c":
		list_of_sources.append(file)
	elif ext == ".h":
		list_of_headers.append(file)
#print(list_of_sources)
#print(list_of_headers)

makefile = open('Makefile', 'w')
makefile.write("PROG = " + program_name + "\n")
makefile.write("CC = " + compiler + "\n")
makefile.write("CFLAGS = " + cflags + "\n")
makefile.write("LDFLAGS = " + ldflags + "\n")
#string of objects
objs = ""
for s in list_of_sources:
	objs += s + ".o "
makefile.write("OBJS = " + objs + "\n\n")
makefile.write("$(PROG) : $(OBJS)\n\t$(CC) $(LDFLAGS) -o $(PROG) $(OBJS)\n")
for o in list_of_sources:
	if o in list_of_headers:
		makefile.write(o + ".o : " + o + ".h\n\t$(CC) $(CFLAGS) -c " + o + ".c\n")
	else:
		makefile.write(o + ".o : \n\t$(CC) $(CFLAGS) -c " + o + ".c\n")
makefile.write("\nclean:\n\trm -f core $(OBJS)")
makefile.close()
