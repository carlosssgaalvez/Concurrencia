#include "ListaJugadores.h"
#include <stdio.h>
#include <stdlib.h>

//crea una lista vac�a (sin ning�n nodo)
void crear(TListaJugadores *lc){
    *lc = NULL;
}

//inserta un nuevo jugador en la lista de jugadores, poniendo 1 en el n�mero de goles marcados.
//Si ya existe a�ade 1 al n�mero de goles marcados.
void insertar(TListaJugadores *lj,unsigned int id){
    if(*lj == NULL){    // lista vacia
        TListaJugadores nueva = malloc(sizeof(struct Nodo));
        if(nueva == NULL){
            perror("Error al solicitar memoria");
            exit(-1);
        }
        *lj = nueva;
        nueva->nGoles = 1;
        nueva->nJugador = id;
        nueva->sig = NULL;
    } else {            // lista no vacia
        TListaJugadores aux = *lj;
        TListaJugadores ant = aux;
        aux = aux->sig;
        int encontrado = 0;
        while(encontrado == 0){     
            if(ant->nJugador == id){    // encontramos jugador, sumamos gol
                ant->nGoles++;
                encontrado = 1;
            } else if (aux == NULL || aux->nJugador > id){         //añadimos al final de la lista
                TListaJugadores nueva = malloc(sizeof(struct Nodo));
                if(nueva == NULL){
                perror("Error al solicitar memoria");
                exit(-1);
                }
                ant->sig = nueva;
                nueva->nGoles = 1;
                nueva->nJugador = id;
                nueva->sig = aux;
                encontrado = 1;
            } else {
                ant = aux;
                aux = aux->sig;
            }
        }
    }
}

//recorre la lista circular escribiendo los identificadores y los goles marcados
void recorrer(TListaJugadores lj){
    if(lj == NULL){
        printf("Lista de jugadores vacia\n");
    }
    while(lj != NULL){
        printf("Número de jugador: %i --> %i Goles\n", lj->nJugador, lj->nGoles);
        lj = lj->sig;
    }
}

//devuelve el n�mero de nodos de la lista
int longitud(TListaJugadores lj){
    int longi = 0;
    while(lj != NULL){
        longi++;
        lj = lj->sig;
    }
    return longi;
}

//Eliminar. Toma un número de goles como par�metro y
//elimina todos los jugadores que hayan marcado menos que ese n�mero de goles
void eliminar(TListaJugadores *lj,unsigned int n){
    if(*lj != NULL){                //Lista no vacia
        TListaJugadores ant = *lj;
        TListaJugadores aux = NULL;
        while (ant->nGoles < n){   //borramos los primero de la lista
            *lj = ant->sig;
            free(ant);
            ant = *lj;
        }
        if(ant!= NULL){
            aux = ant->sig;
            while(aux != NULL){
                if(aux->nGoles < n){
                    ant->sig = aux->sig;
                    free(aux);
                    aux = ant->sig;
                } else {
                    ant = aux;
                    aux = aux->sig;
                }
            }
        }
    }
}

// Devuelve el ID del m�ximo jugador. Si la lista est� vac�a devuelve 0. Si hay m�s de un jugador con el mismo n�mero de goles que el m�ximo devuelve el de mayor ID
// Hay que devolver el identificador, no el n�mero de goles que ha marcado
unsigned int maximo(TListaJugadores lj){
    unsigned int goles = 0;
    unsigned int resultado = 0;
    while(lj != NULL){
        if(lj->nGoles > goles){
            goles = lj->nGoles;
            resultado = lj->nJugador;
        } else if(goles == lj->nGoles){
            resultado = (lj->nJugador > resultado)? lj->nJugador : resultado;
        }
        lj = lj->sig;
    }
    return resultado;
}


//Destruye la lista y libera la memoria)
void destruir(TListaJugadores *lj){
    TListaJugadores aux = *lj;
    while(aux != NULL){
        *lj = aux->sig;
        free(aux);
        aux = *lj;
    }
}

