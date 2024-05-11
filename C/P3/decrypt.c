
#include <stdio.h>
#include <stdlib.h>

/* Parte 1: algoritmo de descifrado
 * 	v: puntero a un bloque de 64 bits.
 * 	k: puntero a la clave para descifrar.
 * 	Sabiendo que unsiged int equivale a 4 bytes (32 bits)
 * 	Podemos usar la notación de array con v y k
 * 	v[0] v[1] --- k[0] ... k[3]
 */
void decrypt(unsigned int *v, unsigned int *k)
{
	//Definir variables e inicializar los valores de delta y sum
	unsigned int delta = 0x9e3779b9;
	unsigned int sum = 0xC6EF3720;

	//Repetir 32  veces (usar un bucle) la siguiente secuencia de operaciones de bajo nivel
	for(int i = 0; i < 32; i++){

		//Restar a v[1] el resultado de la operacion :
			// (v[0] desplazado a la izquierda 4 bits +k[2]) XOR (v[0] + sum)  XOR (v[0] desplazado a la derecha 5 bits)+k[3]
		v[1] -= ((v[0] << 4) + k[2]) ^ (v[0] + sum) ^ ((v[0] >> 5) + k[3]);

		//Restar a v[0] el resultado de la operacion:
			// (v[1] desplazado a la izquierda 4 bits + k[0]) XOR (v[1]+ sum)  XOR (v[1] desplazado a la derecha 5 bits)+k[1]
		v[0] -= ((v[1] << 4) + k[0]) ^ (v[1] + sum) ^ ((v[1] >> 5) + k[1]);

		// Restar a sum el valor de delta
		sum -= delta;
	}
}

/* Parte 2: Metodo main. Tenemos diferentes opciones para obtener el nombre del fichero cifrado y el descifrado
 * 1. Usar los argumentos de entrada (argv)
 * 2. Pedir que el usuario introduzca los nombres por teclado
 * 3. Definir arrays de caracteres con los nombres
 */
int main(int argc, char *argv[] )
{
	/*Declaraci�n de las variables necesarias, por ejemplo:
	* variables para los descriptores de los ficheros ( FILE * fCif, *fDes)
	* la constante k inicializada con los valores de la clave
	* buffer para almacenar los datos (puntero a unsigned int, más adelante se reserva memoria dinámica */
	FILE *fCif, *fDes;
	unsigned int k[] = {128,129,130,131};
	unsigned int *buffer, imgSize;

	/*Abrir fichero encriptado fCif en modo lectura binario
	 * nota: comprobar que se ha abierto correctamente*/
    fCif = fopen("image01.png.enc", "rb");
	if(fCif == NULL){
		printf("Error al abrir el fichero cifrado\n");
		exit(-1);
	}

	/*Abrir/crear fichero fDes en modo escritura binario
	 * nota: comprobar que se ha abierto correctamente*/
    fDes = fopen("image01.png", "wb");
	if(fDes == NULL){
		printf("Error al abrir el fichero cifrado\n");
		exit(-1);
	}

   /*Al comienzo del fichero cifrado esta almacenado el tama�o en bytes que tendr� el fichero descifrado.
    * Leer este valor (imgSize)*/
    fread(&imgSize, sizeof(unsigned int), 1, fCif);

	/*Reservar memoria din�mica para el buffer que almacenara el contenido del fichero cifrado
	 * nota1: si el tama�o del fichero descifrado (imgSize) no es m�ltiplo de 8 bytes,
	 * el fichero cifrado tiene adem�s un bloque de 8 bytes incompleto, por lo que puede que no coincida con imgSize
	 * nota2: al reservar memoria din�mica comprobar que se ha realiz� de forma correcta */
	int nBlocks = (imgSize %8)==0? imgSize/8: (imgSize/8)+1;
	buffer = malloc(nBlocks*8);
	if(buffer == NULL){
		fclose(fCif);
		fclose(fDes);
		exit(-1);
	}
	/*Leer la informaci�n del fichero cifrado, almacenado el contenido en el buffer*/
	fread(buffer, 8, nBlocks, fCif);
	/*Para cada bloque de 64 bits (8 bytes o dos unsiged int) del buffer, ejectuar el algoritmo de descifrado*/
	for(int i = 0; i < nBlocks; i++){
		decrypt(&(buffer[i*2]), k);
	}
    /*Guardar el contenido del buffer en el fichero fDes
     * nota: en fDes solo se almacenan tantos bytes como diga imgSize */
	fwrite(buffer, 1, imgSize, fDes);
	/*Cerrar los ficheros*/
	fclose(fCif);
	fclose(fDes);
	free(buffer);
}


