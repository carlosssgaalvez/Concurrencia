/*
 * Principal.c
 *
 *  Created on: 14/6/2016
 *      Author: esc
 */

#include "ListaJugadores.h"
#include <stdio.h>
#include <stdlib.h>



// Lee el fichero y lo introduce en la lista
void cargarFichero (char * nombreFich, TListaJugadores *lj)
{
	FILE *f = fopen(nombreFich,"rb");
	if(f == NULL){
		perror("error al abrir el fichero");
		exit(-1);
	}
	unsigned int id[3];
	while(fread(&id,sizeof(unsigned int), 3, f) == 3){
		insertar(lj, id[1]);
	}
	fclose(f);
}


int main(){

	TListaJugadores lj;
	crear(&lj);
    unsigned int num_goles = 2;
	cargarFichero ("goles.bin",&lj);
	printf("Hay un total de %d jugadores\n",longitud(lj));
	fflush(stdout);

	recorrer(lj);
	fflush(stdout);
	printf("Introduce un numero de goles: \n");
	fflush(stdout);
	scanf("%d",&num_goles);

	eliminar(&lj,num_goles);
	printf("--------------------------------------\n");
	recorrer(lj);
	printf("Hay un total de %d jugadores\n",longitud(lj));
	fflush(stdout);

	printf ("El jugador que más goles ha marcado es el que tiene ID: %d",maximo(lj));
	fflush(stdout);
	destruir (&lj);

	return 0;
}
