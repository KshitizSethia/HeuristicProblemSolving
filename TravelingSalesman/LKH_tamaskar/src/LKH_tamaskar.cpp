#include<iostream>
#include<vector>
#include <fstream>

#include "LKMatrix.h"

vector<int> id;
vector<vector<double> > coord;

int main() {
	ifstream infile("small.txt",ios::in);
	int n;
	double x, y, z;
	while (infile >> n) {
		id.push_back(n);
		infile >> x >> y >> z;
		double thisCoordArray[] = { x, y, z };
		vector<double> thisCoord(thisCoordArray,
				thisCoordArray + sizeof(thisCoordArray) / sizeof(double));
		coord.push_back(thisCoord);
	}

	LKMatrix mat(coord, id);

	mat.optimizeTour();

	//cout << mat.getCurrentTourDistance() << endl;
	mat.printTourIds();
}
