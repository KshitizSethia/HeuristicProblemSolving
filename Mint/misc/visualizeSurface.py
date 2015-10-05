import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import axes3d
import matplotlib.cm as cm

multiples = np.ndarray(shape=(239,239))
file = open("multiples.csv")
lineNum=-1
for line in file:
	lineNum= lineNum+1
	colNum = -1
	for entry in line.split(", "):
		colNum = colNum+1
		multiples[lineNum,colNum] = float(entry)
		
nonMultiples = np.ndarray(shape=(239,239))
file = open("nonMultiples.csv")
lineNum=-1
for line in file:
	lineNum= lineNum+1
	colNum = -1
	for entry in line.split(", "):
		colNum = colNum+1
		nonMultiples[lineNum,colNum] = float(entry)

n = 100.0
cost = np.multiply(n, multiples) + nonMultiples
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
base = np.arange(1,240)
X,Y = np.meshgrid(base, base)
ax.plot_surface(X,Y, cost, rstride=2, cstride=2, cmap=cm.get_cmap("hot"))
#X, Y, Z = axes3d.get_test_data(0.05)
#print X.shape
#print Y.shape
#print Z.shape
#ax.plot_wireframe(X, Y, Z, rstride=10, cstride=10)
plt.show()