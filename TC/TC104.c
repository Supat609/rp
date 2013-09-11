// File: TC104.c
// Test Case 4: Multiple Threads calling "execve" to run "mirror" (Simple read file and 
//                    write to screen program) with running number of file name based on 
//                    the thread number such as Thread 0 calls for 0.txt and/or Thread 1 
//                    calls for 1.txt. 
//
// Parameters : 
// 1. @NOF as a Number of Input File (Not larger than 10)
//
// Sample : ./TC104 3
//
// Author: Supat R.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define PN 10

int main(int argc, char* argv[]) {
	int i, status, N;
	char fn[20];
	pid_t pid[PN];
	int child_pid[PN];
        char *newargv[] = { "mirror", NULL, NULL, NULL };
        char *newenviron[] = { NULL };

	if (argc < 1) {
		fprintf(stderr,"Requires a parameters <Number of Input File>\"\n");
		exit(EXIT_FAILURE);
	} else {
  		N = atoi(argv[1]);
		if (N > 10) {
		  	fprintf(stderr,"Number of Input File is out of bound (Not larger than 10)\n");
      		  	exit(EXIT_FAILURE);
		}
		
		for (i=0; i<N; i++) {
			pid[i] = fork();
			if (pid[i] < 0) {
		  		fprintf(stderr,"Failed to fork a new process\n");
      		  		exit(EXIT_FAILURE);
      	  		} else if (pid[i] == 0) {
	      	  		sprintf(fn, "%d", i);
      		  		strcat(fn,".txt");
				newargv[1] = fn;
				execve("mirror", newargv, newenviron);
				fprintf(stderr,"Unable to run 'execve' function\n");
				exit(EXIT_FAILURE);
			}
		}

		for (i=0; i<N; i++) {
			child_pid[i] = wait(&status);
		}
	}

	return(0);
}