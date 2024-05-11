/*
 * Examen Septiembre 2022 PSC - todos los grupos.
 * Implementación Tren.c
*/

#include "Tren.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/// @brief Inicializa el tren creado en el Principal que tiene maximoVagones (Vagon *tren) con todos los vagones vacío. 
/// @param tren Array que representa el tren. 
/// @param maximoVagones Número de vagones que tiene el tren (tamaño del array). 
/// 0.25 pts 
void inicializarTren(Vagon * tren, int maximoVagones){
    for(int i = 0; i < maximoVagones; i++){
        tren[i] = NULL;
    }
} 

/// @brief Inserta los datos de un nuevo pasajero. Sí el asiento está libre se lo asigna y si está ocupado ignora la petición. 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente, en el que se debe insertar. 
/// @param numeroVagon Vagón en el que se quiere sentar el pasajero. 
/// @param numeroAsiento Asiento dentro del vagón en el que se quiere sentar el pasajero. 
/// @param nombre Nombre del pasajero. 
/// @return Si el asiento ya está ocupado, devuelve -1, si no, devuelve 0. 
/// 1.75 pts 
int entraPasajero(Vagon * tren, unsigned numeroVagon, unsigned numeroAsiento, char * nombre){
    int sentado = -1;
    if(tren[numeroVagon]== NULL){ // vagon libre entero
        Vagon nodo = malloc(sizeof(struct Asiento));
        if(nodo == NULL){
            perror("Error al solicitar memoria");
            exit(-1);
        }
        tren[numeroVagon] = nodo;
        strcpy(nodo->nombre,nombre);
        nodo->num = numeroAsiento;
        sentado = 0;
    } else {            // hay gente sentada en el vagon
        Vagon ant = tren[numeroVagon];
        if(ant->num > numeroAsiento){       //se coloca en primera posicion
            Vagon nodo = malloc(sizeof(struct Asiento));
            if(nodo == NULL){
                perror("Error al solicitar memoria");
                exit(-1);
            }
            tren[numeroVagon] = nodo;
            strcpy(nodo->nombre,nombre);
            nodo->num = numeroAsiento;
            nodo->sig = ant;
            sentado = 0;
        } else if(ant->num != numeroAsiento){  //comprobamos que el primer asiento no sea el suyo
            Vagon aux = ant->sig;
            while(aux != NULL && ant->num < numeroAsiento && aux->num <= numeroAsiento){    //buscamos su posicion 
                ant = aux;
                aux = aux->sig;
            }
            if(ant->num < numeroAsiento){
                Vagon nodo = malloc(sizeof(struct Asiento));
                if(nodo == NULL){
                    perror("Error al solicitar memoria");
                    exit(-1);
                }
                strcpy(nodo->nombre,nombre);
                nodo->num = numeroAsiento;
                ant->sig = nodo;
                nodo->sig = aux;
                sentado = 0;
            }
        }
    }
    return sentado;
} 
 
/// @brief Muestra por pantalla los vagones ocupados mostrando en cada línea los datos de un pasajero. Por ejemplo: 
/* 
Vagon 0:  
Asiento 2, Carlos García 
Asiento 4, Ismael Canario 
Vagon 2: 
Asiento 1, Macarena Sol 
*/ 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param maximoVagones Máximo de vagones que tiene el tren. 
/// 0.75 pt 
void imprimeTren(Vagon * tren, unsigned maximoVagones){
    for(unsigned i = 0; i < maximoVagones; i++){
        if(tren[i]!= NULL){     // hay gente en el vagon
            printf("Vagon %i:\n", i);
            Vagon nodo = tren[i];
            while(nodo != NULL){
                printf("Asiento %i, %s\n", nodo->num, nodo->nombre);
                nodo = nodo->sig;
            }
        }
    }
} 
 
/// @brief El pasajero abandona el tren (es eliminado de la estructura).  
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param numeroVagon Vagón en el que se está el pasajero que abandona el tren. 
/// @param numeroAsiento Asiento en el que se está el pasajero que abandona el tren. 
/// @return Devuelve 0 si el pasajero abandona el tren y -1 si no había pasajero en el vagón y asiento indicados. 
/// 1.5 pts 
int salePasajero(Vagon * tren, unsigned numeroVagon, unsigned numeroAsiento){
    int abandona = -1;
    Vagon nodo = tren[numeroVagon];
    if(nodo != NULL){   // confirmamos que hay gente en el vagon
        if(nodo->num == numeroAsiento){     // es el primer pasajero sentado
            tren[numeroVagon] = nodo->sig;
            free(nodo);
            abandona = 0;
        }
        Vagon aux = nodo->sig;
        while(aux != NULL && aux->num < numeroAsiento){
            nodo = aux;
            aux = aux->sig;
        }
        if(aux != NULL && aux->num == numeroAsiento){
            nodo->sig = aux->sig;
            free(aux);
            abandona = 0;
        }
    }
    return abandona;
} 
 
/// @brief Intercambia dos pasajeros. Para ello es suficiente intercambiar el NOMBRE del viajero que está en el asiento \p numeroAsiento1 del vagón\p numeroVagon1, con el del viajero que está en el asiento \p numeroAsiento2 del vagón \p numeroVagon2. 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param numeroVagon1 Vagón en el que se está el pasajero 1 que quiere cambiarse de sitio. 
/// @param numeroAsiento1 Asiento en el que se está el pasajero 1 que quiere cambiarse de sitio. 
/// @param numeroVagon2 Vagón en el que se está el pasajero 2 que quiere cambiarse de sitio. 
/// @param numeroAsiento2 Asiento en el que se está el pasajero 2 que quiere cambiarse de sitio. 
/// @return Si algún asiento no está ocupado, devuelve -1. Si se puede realizar el cambio, devuelve 0. 
/// 1.75 pts 
int intercambianAsientos(Vagon * tren, unsigned numeroVagon1, unsigned numeroAsiento1,unsigned numeroVagon2, unsigned numeroAsiento2){
    int cambio = -1;
    if (tren[numeroVagon1] != NULL && tren[numeroVagon2] != NULL){ // hay personas en ambos vagones
        Vagon persona1 = tren[numeroVagon1];
        while(persona1 != NULL && persona1->num < numeroAsiento1){ // buscamos persona1
            persona1 = persona1->sig;
        }
        if(persona1 != NULL && persona1->num == numeroAsiento1){ // hemos encontrado la persona1
            Vagon persona2 = tren[numeroVagon2];
            while(persona2 != NULL && persona2->num < numeroAsiento2){ // buscamos persona1
                persona2 = persona2->sig;
            }
            if(persona2 != NULL && persona2->num == numeroAsiento2){ // ambos pasajeros encontrados
                char aux[30];
                strcpy(aux,persona1->nombre);
                strcpy(persona1->nombre,persona2->nombre);
                strcpy(persona2->nombre, aux);
                cambio = 0;
            }
        }
    }
    return cambio;
} 
 
/// @brief El tren llega a la última parada y bajan todos los pasajeros del tren. El tren debe quedar vacío. 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param maximoVagones Maximo de vagones que tiene el tren. 
/// 1 pt 
void ultimaParada(Vagon * tren, unsigned maximoVagones){
    for(unsigned i = 0; i < maximoVagones; i++){
        Vagon nodo;
        while(tren[i] != NULL){
            nodo = tren[i];
            tren[i]= nodo->sig;
            free(nodo);
        }
    }
} 
 
/// @brief Guarda en fichero de TEXTO los datos de los pasajeros en el tren. El formato del fichero de texto será el siguiente, una primera línea con el siguiente texto: 
// Vagon;Asiento;Nombre 
// Tras esta línea, incluirá una línea por cada pasajero, ordenados por vagón primero y luego por número de asiento.  
// 0;2;Carmen Garcia 
// 0;3;Pepe Perez 
// 1;5;Adela Gamez 
// 1;7;Josefa Valverde 
/// @param filename Nombre del fichero en el que se van a almacenar los datos de los pasajeros del tren. 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param maximoVagones Máximo de vagones que tiene el tren. 
/// 1.5 pts 
void almacenarRegistroPasajeros(char *filename, Vagon * tren, unsigned maximoVagones){
    FILE *f = fopen(filename, "wt");
    if(f == NULL){
        perror("Error al abrir el fichero");
        exit(-1);
    }
    for(unsigned i = 0; i < maximoVagones; i++){
        if(tren[i]!= NULL){     // hay gente en el vagon
            Vagon nodo = tren[i];
            while(nodo != NULL){
                fprintf(f,"%i;%i;%s", i, nodo->num, nodo->nombre);
                nodo = nodo->sig;
            }
        }
    }
    fclose(f);
} 
 
/// @brief Algunas estaciones están automatizadas y proporcionan un fichero con los pasajeros que van a entrar en un vagón en su parada. 
// Esta función carga los pasajeros que están en el fichero BINARIO filename en el  
// vagón numeroVagon. Se asume que los pasajeros almacenados en el fichero no van a  
// sentarse en asientos previamente ocupados.  
// El fichero binario almacena la información de cada pasajero con la siguiente  
// estructura:  
// - Un entero con el número de asiento. 
// - La cadena de caracteres con el nombre. 
/// @param filename Nombre del fichero que contiene los datos de los pasajeros del vagon. 
/// @param tren Array con los vagones y pasajeros que tiene el tren actualmente. 
/// @param numeroVagon Vagon del que se van a importar los pasajeros.  
/// 1.5 pts 
void importarPasajerosVagon(char *filename, Vagon * tren,unsigned numeroVagon){
    FILE *f = fopen(filename,"rb");
    if(f == NULL){
        perror("Error al abrir el fichero");
        exit(-1);
    }
    int nAsiento;
    char nombre[30];
    int leido = 0;
    while(leido == 0){
        if(fread(&nAsiento,sizeof(int),1,f) != 1){
            leido = 1;
        }
        if(leido == 0 && fread(nombre,sizeof(nombre),1,f) != sizeof(nombre)){
            leido = 1;
        }
        if(leido == 0){
            entraPasajero(tren,numeroVagon,nAsiento,nombre);
        }
    }
    fclose(f);
} 


