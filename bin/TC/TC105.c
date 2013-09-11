// File: TC105.c
// Test Case 5: A Copy of TC104. Still running Multiple Threads, but instead of calling 
//                    "execve" to run "mirror" at the command line, TC105 is calling the 
//                    mirror function. In the TC104, it's using fork(), while TC105 is 
//                    using pthread_create(), the heavy weight process duplication.
//
// Parameters : 
// 1. @NOF as a Number of Input File (Not larger than 10)
//
// Sample : ./TC105 3
//
// Author: Supat R.

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define PN 10

void *mirror(void *vptr_value) {
	char *filename = (char *)vptr_value;
	char str[256];
	
	FILE *in = fopen(filename,"r");
	while (!feof(in)) {
		fscanf(in,"%s",str);
		printf("%s\n",str);
	}
        fclose(in);
        return NULL;
}


int main(int argc, char* argv[]) {
	int i, j, status, N;
	char fn[PN][20];
	char tempFn[20];
	pthread_t pid[PN];

	if (argc < 1) {
		fprintf(stderr,"Reqests a parameters <Number of Input File>\"\n");
		exit(EXIT_FAILURE);
	} else {
  		N = atoi(argv[1]);
		if (N > 10) {
		  	fprintf(stderr,"Number of Input File is out of bound (Not larger than 10)\n");
      		  	exit(EXIT_FAILURE);
		}

		for (i=0; i<N; i++) {
      	  		sprintf(fn[i], "%d", i);
	  		strcat(fn[i],".txt");
			if (pthread_create(&(pid[i]), NULL, &mirror, (void *)fn[i])) {
				fprintf(stderr,"Unable to create thread\n");
				exit(EXIT_FAILURE);
			}
		}

		for (i=0; i<N; i++) {
			if (pthread_join(pid[i], NULL)) {
				printf("Could not join thread\n");
				exit(EXIT_FAILURE);
			}
		}
	}

	return(0);
}