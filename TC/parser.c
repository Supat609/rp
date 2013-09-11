#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXTHREADS 50
#define MAXTOTKENS 500
#define MAXPAIR 50

typedef struct {
	string syscallname;
	string syscallval;
} pair;

typedef struct {
	pair mainpair;
	pair[MAXPAIRS] subpair;
} token;

typedef token[MAXTOKENS] tokens;
typedef tokens[MAXTHREADS] threads;

int main(int argc, char* argv[]) {
	
	return(0);
}