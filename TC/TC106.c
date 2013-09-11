// File: TC106.c
// Test Case 6: One reader process receives data (read_from_pipe) from one pipeline 
//                     sharing by multiple fork() processes writing into the same pipe 
//                     (write_to_pipe).
//
// Parameters : 
// 1. @NOF as a Number of Input File (Not larger than 10)
// 2. @NumberToRun as a number to be run by Writer to Loop.
//
// Sample : ./TC106 2 25
//
// Author: Supat R.

#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#define PN 11
 
int limit = 10;

void read_from_pipe (int file) {
	FILE *stream;
	int c;
	stream = fdopen (file, "r");
	while ((c = fgetc (stream)) != EOF) {
		putchar (c);
	}
	fclose (stream);
}

void write_to_pipe (int file) {
	FILE *stream;
	int i;
	stream = fdopen (file, "w");
	for (i=0; i<limit; i++) {
		fprintf (stream, "%3d", i+1);
		if (((i+1)%10 == 0) || (i == (limit-1)))
			fprintf(stream, "\n");
	}
	fprintf (stream, "\n");	  
	fclose (stream);
}

int main (int argc, char* argv[]) {
	int mypipe[2];
	int i,status, N;
	char fn[20];
	pid_t pid[PN];
	int child_pid[PN];

	if (argc < 2) {
		fprintf(stderr,"Reqests two parameters <Number of Input File> and <Number to run>\n");
		exit(EXIT_FAILURE);
	} else {
  		N = atoi(argv[1]);
  		limit = atoi(argv[2]);
		if (N > 10) {
		  	fprintf(stderr,"Number of Input File is out of bound (Not larger than 10)\n");
      		  	exit(EXIT_FAILURE);
		}
		
		/* Create the pipe.  */
		if (pipe (mypipe)) {
			fprintf (stderr, "Pipe failed.\n");
			return EXIT_FAILURE;
		}
		
		pid[0] = fork();
		if (pid[0] < 0) {
			fprintf (stderr, "Fork failed.\n");
			exit(EXIT_FAILURE);
		} else if (pid[0] == 0) {
			read_from_pipe (mypipe[0]);
			return(0);
		}
		
		for (i=1; i<=N; i++) {
			pid[i] = fork();
			if (pid[i] < 0) {
				fprintf (stderr, "Fork failed.\n");
				exit(EXIT_FAILURE);
			} else if (pid[i] == 0) {
				write_to_pipe (mypipe[1]);
				return(0);
			}
		}

		for (i=1; i<=N; i++) {
			child_pid[i] = wait(&status);
		}
	}

	return(0);
	
}