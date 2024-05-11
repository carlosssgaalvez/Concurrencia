/*
 ============================================================================
 Name        : practica1.c
 Author      : 
 Version     :
 Copyright   : Your copyright notice
 Description :
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include "gestion_memoria.h"

int main(void) {

	T_Manejador manej;
	unsigned ok;
	unsigned dir;
	setvbuf(stdout, NULL, _IONBF,0);

	crear(&manej);
	mostrar(manej);

	obtener(&manej,500,&dir,&ok); /* Se ha hecho una foto. Se necesita memoria */
	if (ok) {
		mostrar(manej);
		printf("Se han reservado 500 bloques desde la direccion: %d\n", dir);
	} else {
		printf("No es posible reservar 500 bloques de memoria\n");
	}

	devolver(&manej, 200,0); /* Se ha enviado parte de la foto. Ya se puede borrar */
	mostrar (manej);


	obtener(&manej,250,&dir,&ok); /* Se ha hecho otra foto */
	if (ok) {
		mostrar(manej);
		printf("Se han reservado 250 bloques desde la direccion: %d\n", dir);
	} else {
		printf("No es posible reservar 250 bloques de memoria\n");
	}

	devolver(&manej,100,500); /* Se ha enviado parte de la foto. Ya se puede borrar */
	mostrar(manej);

 	obtener(&manej,250,&dir,&ok); /* Se ha hecho otra foto */
	if (ok) {
		mostrar(manej);
		printf("Se han reservado 250 bloques desde la direccion: %d\n", dir);
	} else {
		printf("No es posible reservar 250 bloques memoria\n");
	}

	devolver(&manej, 200,750); /* Se ha enviado parte de la foto. Ya se puede borrar */
	mostrar(manej);

	//vamos a devolver memoria que falta para que al compactar salga un único bloque
	devolver(&manej, 300, 200);
	devolver(&manej, 150, 600);
	devolver(&manej,50, 950);
	mostrar(manej);

	destruir(&manej);
	mostrar (manej);

	printf("Fin Programa\n");

	return EXIT_SUCCESS;
}
