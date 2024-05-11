#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Componentes.h"


/*
typedef struct elemLista {
	long codigoComponente;
	char textoFabricante[MAX_CADENA];
	struct elemLista * sig;
} Componente;
*/


/*
La funcion Lista_Crear crea una lista enlazada vacia
de nodos de tipo Componente.
*/
Lista Lista_Crear(){    
    Lista lista = NULL;
    return lista;
}

/*
La rutina Adquirir_Componente se encarga de recibir los datos de un nuevo 
componente (codigo y texto) que se le introducen por teclado y devolverlos 
por los parametros pasados por referencia "codigo" y "texto".
*/
void Adquirir_Componente(long *codigo,char *texto){
    printf("Introduce un código y su descripcion: ");
    scanf("%ld", codigo);
    fgets(texto, MAX_CADENA, stdin);
}

/*
La funcion Lista_Agregar toma como parametro un puntero a una lista,
el código y el texto de un componente y  anyade un nodo al final de 
la lista con estos datos.
*/
void Lista_Agregar(Lista *lista, long codigo, char* textoFabricante){
    Lista Nodo = malloc(sizeof(Componente));
    if(Nodo == NULL){
        perror("Error al reservar memoria");
        exit(-1);
    }
    Nodo->codigoComponente = codigo;
    strcpy(Nodo->textoFabricante, textoFabricante);
    Nodo->sig = NULL;
    if(*lista == NULL){
        *lista = Nodo;
    } else {
        Lista aux = *lista;
        while (aux->sig != NULL){
            aux = aux->sig;
        }
        aux->sig = Nodo;
    }
}

/*
La funcion Lista_Imprimir se encarga de imprimir por pantalla la lista 
enlazada completa que se le pasa como parametro.
*/
void Lista_Imprimir( Lista lista){
    if(lista!=NULL) {
        while(lista!=NULL) {
            printf("Codigo: %ld\n Texto: %s \n\n", lista->codigoComponente, lista->textoFabricante);
            lista=lista->sig;
        }
    } else {
        printf("La lista está vacía\n");
    }
}

/*
La rutina Lista_Vacia devuelve 1 si la lista que se le pasa
como parametro esta vacia y 0 si no lo esta.
*/
int Lista_Vacia(Lista lista){
    return (lista==NULL)? 1 : 0;
}

/*Num_Elementos es una funcion a la que se le pasa un puntero a una lista 
y devuelve el numero de elementos de dicha lista.
*/
int Num_Elementos(Lista  lista){
    int resultado = 0;
        while(lista!=NULL) {
            resultado++;
            lista=lista->sig;
        }
    return resultado;
}

/*
Lista_Extraer toma como parametro un puntero a una Lista y elimina el
Componente que se encuentra en su ultima posicion.
*/
void Lista_Extraer(Lista *lista){
    if(*lista != NULL){             // Lista no vacia
        Lista anterior = *lista;
        if(anterior->sig == NULL){  // Solo un elemento
            *lista = NULL;
            free(anterior);
        }else {                     // Más de un elemento
            Lista aux = anterior->sig;
            while(aux->sig != NULL){
                anterior = aux;
                aux = aux->sig;
            }
            anterior->sig = NULL;
            free(aux);
        }
    }
}

/*
Lista_Vaciar es una funcion que toma como parametro un puntero a una Lista
y elimina todos sus Componentes.
*/
void Lista_Vaciar(Lista *lista){
    while(*lista != NULL){
        Lista_Extraer(lista);
    }
}

/*
La funcion Lista_Salvar se encarga de guardar en el fichero binario 
"examen.dat" la lista enlazada completa que se le pasa como parametro. 
Para cada nodo de la lista, debe almacenarse en el fichero
el código y el texto de la componente correspondiente.
*/
void Lista_Salvar( Lista  lista){
    FILE *f = fopen("examen.dat", "wb");
    if(f== NULL){
        perror("Error al abrir el fichero");
        exit(-1);
    }
    while(lista != NULL){
        fwrite(&lista, sizeof(Componente), 1, f);
        lista = lista->sig;
    }
    fclose(f);
}

