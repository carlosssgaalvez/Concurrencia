#include "Lista.h"
#include <stdio.h>
#include <stdlib.h>

/*
 * Inicializa la lista de puntos creando una lista vacía
 *
 */
void crearLista(TLista *lista){
    *lista = NULL;
}

/**
 * Inserta el punto de forma ordenada (por el valor de la abscisa x)
 * en la lista siempre que no esté repetida la abscisa.
 *  En ok, se devolverá un 1 si se ha podido insertar, y  0 en caso contrario.
 *  Nota: utiliza una función auxiliar para saber
 *   si ya hay un punto en la lista con la misma abscisa punto.x
 *
 */
int encontrarAbscisa(TLista lista, float X){
    int encontrado = 0;
    while(lista != NULL){
        if(X == (lista->punto).x){
            encontrado = 1;
            break;
        }else {
            lista = lista->sig;
        }
    }
    return encontrado;
}

void insertarPunto(TLista *lista, struct Punto punto, int * ok){
    *ok = 0;
    if(*lista == NULL){     //No hay elementos en la lista
        TLista Nodo = malloc(sizeof(struct Nodo));
        if(Nodo == NULL){
            perror("No se ha encontrado memoria");
            exit(-1);
        }else{
            *lista = Nodo;
            Nodo->punto = punto;
            Nodo->sig = NULL;
            *ok = 1;
        }
    } else {                //La lista no está vacia
        if(encontrarAbscisa(*lista,punto.x) == 0){
            TLista ant = *lista;
            TLista aux = ant->sig;
            TLista Nodo = malloc(sizeof(struct Nodo));
            Nodo->punto = punto;
            Nodo->sig = NULL;
            if(Nodo == NULL){
                perror("No se ha encontrado memoria");
                exit(-1);
            } else {
                if(ant->punto.x > punto.x){ // Solo hay un elemento y es mayor que el dado
                    *lista = Nodo;
                    Nodo->sig = ant;
                } else {
                    while((aux != NULL) && (aux->punto.x < punto.x)){   // puede haber un elemento menor o mas de 1
                        ant = aux;
                        aux = aux->sig;
                    }
                    ant->sig = Nodo;
                    Nodo->sig = aux;
                }
            }
            *ok =1;
        }
    }
}

/*
 * Elimina de la lista el punto con abscisa x de la lista.
 * En ok devolverá un 1 si se ha podido eliminar,
 * y un 0 si no hay ningún punto en la lista con abscisa x
 *
 */
void eliminarPunto(TLista *lista,float x,int* ok){
    *ok = 0;
    if(encontrarAbscisa(*lista,x) == 1){
        TLista ant = *lista;
        TLista aux = ant->sig;
        if(ant->punto.x == x){ //Elemento a borrar es el primero
            *lista = aux;
            free(ant);
        } else {                        //recorremos la lista hasta encontrarlo
            while(aux->punto.x != x){
                ant = aux;
                aux = aux->sig;
            }
            ant->sig = aux->sig;
            free(aux);
        }
        *ok = 1;     
    }
}

 /**
 * Muestra en pantalla el listado de puntos
 */
void mostrarLista(TLista lista){
    if(lista == NULL){
        printf("La lista está vacía\n");
    } else{
        while(lista != NULL){
            printf("Punto: (%.2f,%.2f)\n", lista->punto.x, lista->punto.y);
            lista = lista->sig;
        }
    }
}

/**
 * Destruye la lista de puntos, liberando todos los nodos de la memoria.
 */
void destruir(TLista *lista){
    TLista aux = *lista;
    while(aux != NULL){
        *lista = (*lista)->sig;
        free(aux);
        aux = *lista;
    }
}

/*
 * Lee el contenido del archivo binario de nombre nFichero,
 * que contiene una secuencia de puntos de una función polinómica,
 *  y lo inserta en la lista.
 *
 */
void leePuntos(TLista *lista,char * nFichero){
    FILE *f = fopen(nFichero, "rb");
    if(f == NULL){
        perror("No se ha abierto el archivo");
        exit(-1);
    }
    struct Punto p;
    while(fread(&p, sizeof(struct Punto), 1, f)==1){
        insertarPunto(lista, p, 0);
    }
}

