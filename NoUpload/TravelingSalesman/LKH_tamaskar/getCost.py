import math
import sys

cityIds = []
coords = []

def readCoords():
	#print sys.argv	
	for line in open(sys.argv[1], "r"):
		data = line.split()
		cityIds.append(int(data[0]))
		coords.append([float(data[1]), float(data[2]), float(data[3])])
	#print "cities"
	#print cityIds
	#print "coords"
	#print coords

def calcDistance():
	cities = map(int, open(sys.argv[2], "r").readlines())
	totalDist = 0.0
	for index in range(0, len(cityIds)-1):
		totalDist += distance(index, index+1)
		
	print totalDist
	
def distance(index1, index2):
	coord1 = coords[index1]
	coord2 = coords[index2]
	distance = math.pow(coord1[0]-coord2[0], 2) +math.pow(coord1[1]-coord2[1],2) +math.pow(coord1[2]-coord2[2],2)
	
	distance = math.sqrt(distance)
	return distance
	
if __name__ =="__main__":
	readCoords()
	calcDistance()
