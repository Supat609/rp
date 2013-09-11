// File: TC102.c
// Test Case 2: Zap File (Remove by unlink)
//
// Parameters : 
// 1. @Directory as a Path or Directory of File Deletion
// 2. @WildCard as a wildcard of file names to be deleted. (Note as the limitation of 
//     passing a wildcard through the command prompt, using "*.out" to pass as a 
//     parameter, linux will look for a list of file (*.out) and pick the first one to 
//     pass instead of "*.out". For example, Linux sees a.out, b.out, and c.out. It 
//     decides to pass "a.out" as the parameter. In order to pass "*.out" instead of 
//     "a.out", we need to use Double Quote(") to wrap around the wildcard.
//
// Sample: >> ./TC102 . "*.out" 
//
// Author: Supat R.

#include <dirent.h> 
#include <stdio.h> 
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int main(int argc, char* argv[]) {
	DIR *d;
	struct dirent *dir;
	char enter, ch = ' ';
	char path[100];

	if (argc < 2) {
		fprintf(stderr,"Reqests 2 parameters <Directory> and \"<WildCard>\"\n");
		exit(EXIT_FAILURE);
	} else {
		if (!(d = opendir(argv[1]))) {
			fprintf(stderr, "Cannot list from the target directory\n");
			exit(EXIT_FAILURE);
		} else {
			while ((dir = readdir(d)) != NULL) {
				if (!(fnmatch(argv[2],dir->d_name,0))) {
					if (toupper(ch) != 'A') {
						printf("Zap %s ?(Y/N/A): ", dir->d_name);
						scanf("%c%c", &ch, &enter);
					} else 
						printf("Zap %s ?(Y/N/A): A\n",dir->d_name);
					if ((toupper(ch) == 'A') || (toupper(ch) == 'Y')) {
						strcpy(path, argv[1]);
						strcat(path, "/");
						strcat(path, dir->d_name);
						unlink(path);
					}
				}
    			}
			closedir(d);
		}
	}
	return(0);
}