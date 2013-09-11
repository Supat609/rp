// File: TC108.c
// Test Case 8: A Batch File Runner based on only some basic operations like adding, 
//                    deleting, copying, renaming, echoing. The tool allows users to run 
//                    only a batch or multiple batches in the same time. Using the fork() 
//                    to create separated process to run each batch concurrently. (Maximum 
//                    number of batch files to run is not more than 10 simultaneously.)
 //
// Parameters : 
// 1. @Batch File Name collecting basic commands in the following syntax.
//     - Adding  : a <file> <data>
//     - Deleting: d <file>
//     - Copying: c <file> <file>
//     - Renaming: r <file> <file>
//     - Echoing: e <file> 
//
// Sample : ./TC108 a.bat
//            : ./TC108 a.bat b.bat c.bat d.bat e.bat f.bat g.bat h.bat i.bat j.bat
//
// Author: Supat R.

#include <stdio.h>
#include <stdlib.h>
#define PN 11

void running (int processNo, char *transaction) {
	FILE *tfn, *in, *out;
	char command;
	char fn[20];
	char tn[20];
	int data;
	
	if ((tfn = fopen(transaction, "r")) == NULL) {
		fprintf(stderr,"%d: Transaction file not found : %s\n",processNo,transaction);
		exit(EXIT_FAILURE);
	} else {
		while (!(feof(tfn))) {
			fscanf(tfn,"%c", &command);
			
			switch(command) {
			case 'a' :	{	// append data at the end
						fscanf(tfn,"%s %d\n",fn, &data);
						if ((out = fopen(fn,"a")) == NULL) {
							fprintf(stderr,"%d: Error to add %d to %s\n",processNo,data,fn);
							exit(EXIT_FAILURE);
						} else {
							printf("%d: add: %d to %s\n",processNo, data,fn);
							fprintf(out, "%d\n", data);
							fclose(out);
						}
						break;
					}
					
			case 'd' :	{	// delete a file
						fscanf(tfn,"%s\n", fn);
						if ((unlink(fn)) != 0) {
							fprintf(stderr,"%d: Error to del %s\n",processNo,fn);
							exit(EXIT_FAILURE);
						} else 
							printf("%d: del: %s\n",processNo, fn);
						break;	
					}
					
			case 'r' :	{	// rename a file
						fscanf(tfn,"%s %s\n", fn, tn);
						if ((rename(fn, tn)) != 0) {
							fprintf(stderr,"%d: Error to ren %s to %s\n",processNo,fn, tn);
							exit(EXIT_FAILURE);
						} else 
							printf("%d: ren: %s to %s\n",processNo,fn,tn);
						break;
					}
					
			case 'c' :	{	// copy a file	
						fscanf(tfn,"%s %s\n",fn, tn);
						if ((in = fopen(fn,"r")) == NULL) {
							fprintf(stderr,"%d: Error to cpy (Open source) %s\n",processNo,fn);
							exit(EXIT_FAILURE);
						} else {
							if ((out = fopen(tn,"w")) == NULL) {
								fprintf(stderr,"%d: Error to cpy (Open destination) %s\n",processNo,tn);
								exit(EXIT_FAILURE);
							} else {
								printf("%d: cpy: %s to %s\n",processNo, fn, tn);
								while (!(feof(in))) {
									fscanf(in,"%d\n", &data);
									fprintf(out, "%d\n", data);
								}
								fclose(out);
							}
							fclose(in);
						}
						break;	
					}
					
			case 'e' :	{	// echo a file	
						fscanf(tfn,"%s\n",fn);
						if ((in = fopen(fn,"r")) == NULL) {
							fprintf(stderr,"%d: Error to eco %s\n",processNo,fn);
							exit(EXIT_FAILURE);
						} else {
							printf("%d: eco: %s\n",processNo, fn);
							while (!feof(in)) {
								fscanf(in, "%d\n", &data);
								printf("%d\n",data);
							}
							fclose(in);
						}
						break;	
					}
			}
		}
		fclose(tfn);
	}
}

int main(int argc, char* argv[]) {
	pid_t pid[PN];
	int status, i;
	int N = argc;
	
	if (N < 2) {
		fprintf(stderr,"Requires at least one parameters <Transaction File> [<Transaction File>]*\n");
		exit(EXIT_FAILURE);
	} else if (N > 11) {
		fprintf(stderr,"Cannot have more than 10 parameters\n");
		exit(EXIT_FAILURE);
	} else {
		for (i=0; i<N; i++) {
			pid[i] = fork();
			if (pid[i] < 0) {
				fprintf (stderr, "Fork failed.\n");
				exit(EXIT_FAILURE);
			} else if (pid[i] == 0) {
				if (argv[i+1] != NULL)
					running(i+1, argv[i+1]);
				return(0);
			}
		}

		for (i=0; i<N; i++)
			pid[i] = wait(&status);
	}

	return(0);
}