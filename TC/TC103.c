// File: TC103.c
// Test Case 3: Multiple Threads copying numbers from multiple input files
//                    (named "?.txt") to a same output file. 
//
// Parameters : 
// 1. @NOF as a Number of Input File (Not larger than 10)
// 2. @OFileName as Output File name.
//
// Sample : ./TC103 3 mix.txt
//
// Author: Supat R.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define PN 10

int main(int argc, char* argv[]) {
	int i, status;
	char fn[20];
	pid_t pid[PN];
	int child_pid[PN];
	FILE *in, *out;
	char outfn[100];
	int data, N;

	if (argc < 2) {
		fprintf(stderr,"Reqests 2 parameters <Number of Input File> and \"<Output File Name>\"\n");
		exit(EXIT_FAILURE);
	} else {
  		N = atoi(argv[1]);
		strcpy(outfn,argv[2]);
		if (N > 10) {
		  	fprintf(stderr,"Number of Input File is out of bound (Not larger than 10)\n");
      		  	exit(EXIT_FAILURE);
		}
		
		if (access(outfn, F_OK) == 0)
			unlink(outfn);
	
		for (i=0; i<N; i++) {
			pid[i] = fork();
			if (pid[i] < 0) {
		  		fprintf(stderr,"Failed to fork a new process\n");
      		  		exit(EXIT_FAILURE);
      	  		} else if (pid[i] == 0) {
	      	  		sprintf(fn, "%d", i);
      		  		strcat(fn,".txt");
  				if (access(fn, R_OK) == 0)
	  				in = fopen(fn,"r");
		  		else {
		  			fprintf(stderr, "Input File Not Found\n");
	  				exit(EXIT_FAILURE);
	    			}
	    		
	  			out = fopen(outfn,"a");

				while (!(feof(in))) {
					fscanf(in,"%d",&data);
					fprintf(out,"%d\n",data);
				}
			
      		  		fclose(out);
      	  			fclose(in);
				exit(0);
			}
		}
	
		for (i=0; i<N; i++) {
			child_pid[i] = wait(&status);
		}
	}

	return(0);
}