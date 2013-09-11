// File: TC107.c
// Test Case 7: Multiple fork() process calling "read_write_then_delete" function to read 
//                    a text file named "xr.txt" running 0 to 9 (limited) then fseek with 
//                    the SEEK_END and fwrite the whole record (struct) into an output 
//                    file (defined by user). Finally, these input files will be deleted 
//                    (by function unlink()) at the end of "read_write_then_delete" 
//                    function. After running these multiple process synchronously, 
//                    function "report" will fread the record or struct from the output 
//                    file to printf out to the screen.
//
// Parameters : 
// 1. @NOF as a Number of Input File (Not larger than 10)
// 2. @OutputFilename as a filename of the Output File
//
// Sample : ./TC107 3 a.txt
//
// Author: Supat R.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define PN 11

typedef struct {
	int month;
	int day;
	int year;
} date;

typedef struct {
	int id;
	char name[20];
	date birthdate;
} record;

void report(char *inFN) {
	record data;
	FILE *in = fopen(inFN,"r");
	while (fread(&data, sizeof(record),1,in) > 0) {
		printf("%-3d : %-20s :%2d/%2d/%-4d\n", data.id, data.name, data.birthdate.month, 
			data.birthdate.day, data.birthdate.year);
	}
	fclose(in);
}

void read_write_then_delete(char *inFN, char *outFN) {
	record data;
	FILE *in = fopen(inFN,"r");
	if (in == NULL) {
		fprintf(stderr,"Unable to open input file %s\n",inFN);
		exit(EXIT_FAILURE);
	}
	FILE *out = fopen(outFN,"a");
	while (!feof(in)) {
		fscanf(in,"%d %s %d/%d/%d\n", &data.id, data.name, &data.birthdate.month, 
			&data.birthdate.day, &data.birthdate.year);
		fseek(out, 0L, SEEK_END);
		fwrite(&data,sizeof(record),1,out);
	}
	fclose(out);
	fclose(in);
	unlink(inFN);
}

int main(int argc, char* argv[]) {
	int N, i, status;
	pid_t pid[PN], child_pid[PN];
	char outFN[30], fn[30];
	
	if (argc < 2) {
		fprintf(stderr,"Requires two parameters <Number of Input File> and <Output Filename>\"\n");
		exit(EXIT_FAILURE);
	} else {
  		N = atoi(argv[1]);
		if (N > 10) {
		  	fprintf(stderr,"Number of Input File is out of bound (Not larger than 10)\n");
      		  	exit(EXIT_FAILURE);
		}
		
		strcpy(outFN,argv[2]);
		unlink(outFN);
		
		for (i=0; i<N; i++) {
      	  		sprintf(fn, "%d", i);
	  		strcat(fn,"r.txt");
			pid[i] = fork();
			if (pid[i] < 0) {
				fprintf (stderr, "Fork failed.\n");
				exit(EXIT_FAILURE);
			} else if (pid[i] == 0) {
				read_write_then_delete (fn, outFN);
				return(0);
			}
		}

		for (i=0; i<N; i++) {
			child_pid[i] = wait(&status);
		}
	}
	
	report(outFN);
	
	return(0);
}