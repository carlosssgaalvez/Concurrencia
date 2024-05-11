#include "arbolbb.h"
#include <stdio.h>
#include <stdlib.h>

// gcc principal.c arbolbb.c -o programa.out
// ./programa.out  

// *arbol --> arbol = direccion de memoria  |  *arbol = contenido
// &arbol --> &arbol = direccion de memoria |  arbol = contenido 

// Crea la estructura utilizada para gestionar el árbol.
void crear(T_Arbol* arbol){
    *arbol = NULL;             // ponemos el contenido a nulo
}

// Destruye la estructura utilizada y libera la memoria.
void destruir(T_Arbol* arbol){  // entra un puntero llamado arbol
    if(*arbol != NULL){
        destruir(&((*arbol)->izq));   // ponemos & porque necesitamos un puntero
        destruir(&((*arbol)->der));
        free(*arbol);                 // liberamos un contenido
        *arbol = NULL;
    }
}

// Inserta num en el árbol. Si ya está insertado, no hace nada
void insertar(T_Arbol* arbol,unsigned num){
    if(*arbol == NULL){
        *arbol = malloc(sizeof(struct T_Nodo));
        if(*arbol == NULL){
            printf("\nNo se ha podido crear correctamente el arbol");
            exit(-1);
        } else {
            (*arbol)->dato = num;
            (*arbol)->der = NULL;
            (*arbol)->izq = NULL;
        }
    } else if((*arbol)->dato > num){
        insertar(&((*arbol)->izq), num);
    } else if((*arbol)->dato < num){
        insertar(&((*arbol)->der), num);
    }
}

// Muestra el contenido del árbol en InOrden
void mostrar(T_Arbol arbol){
    if(arbol != NULL){
        mostrar(arbol->izq);
        printf("%u ",arbol->dato);
        mostrar(arbol->der);
    }   
}

// Guarda en disco el recorrido inOrden del árbol
void salvar(T_Arbol arbol, FILE* fichero){
    if(fichero == NULL){
        printf("\nFichero no está abierto");
        exit(-1);
    } else {
        if(arbol !=NULL){
            salvar(arbol->izq,fichero);
            fwrite(&(arbol->dato), sizeof(unsigned int), 1, fichero);
            salvar(arbol->der,fichero);
        }
    }
}

// Guarda en fichero de texto el recorrido inOrden del árbol
// Pasar los datos del fichero arbol
void salvarTexto(T_Arbol arbol, FILE* fichero){
     if(fichero == NULL){
        printf("\nFichero no está abierto");
        exit(-1);
     } else {
        if(arbol !=NULL){
            salvarTexto(arbol->izq,fichero);
            fprintf(fichero, "%u ", arbol->dato);
            salvarTexto(arbol->der,fichero);
        }
     }
}