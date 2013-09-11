#include <stdio.h>

int main(int argc, char *argv[]) {
	char str[256];
	FILE *in = fopen(argv[1],"r");
	while (!feof(in)) {
		fscanf(in,"%s",str);
		printf("%s\n",str);
	}
        fclose(in);
	return 0;
}