import sys
import random

lines = []
file = open(sys.argv[1], "r")
for line in file:
	#line = raw_input()
	#if not line:
	#	break
	lines.append(line)
	
#print lines
random.shuffle(lines)
for line in lines:
	print line,