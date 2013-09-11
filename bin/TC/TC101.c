// File: TC101.c
// Test Case 1: Simple Copy File
// Parameters : 
// 1. @Source as a source File Name
// 2. @Destination as a destination File Name
// Author: Supat R.

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
	FILE *in, *out;
	char buffer[256];
	size_t bufferSize;
	char mode[] = "0755";
	int modeNo = strtol(mode, 0, 8);

	if (argc < 2) {
	   	fprintf(stderr,"Reqests 2 parameters <Source> and <Destination>\n");
	   	exit(EXIT_FAILURE);
	} else {
		in = fopen(argv[1],"r");
		if (!in) {
		   fprintf(stderr,"Cannot Open Source File: %s\n",argv[1]);
		   exit(EXIT_FAILURE);
		}
		
		out = fopen(argv[2],"w");
		if (!out) {
		   fprintf(stderr,"Cannot Open Destination File: %s\n",argv[2]);
		   exit(EXIT_FAILURE);
		}
		
		bufferSize = fread(buffer,1,sizeof(buffer), in);
		printf("%lu\n", bufferSize);
		while (bufferSize > 0) {
			fwrite(buffer,1,bufferSize, out);
			bufferSize = fread(buffer,1, sizeof(buffer), in);
		printf("%lu\n", bufferSize);
		}
		
		fclose(in);
		fclose(out);
		
		if (chmod(argv[2],modeNo) < 0) {
			fprintf(stderr,"Change Mode for Destination File\n");
			exit(EXIT_FAILURE);
		}
	}   
	return(0);
}