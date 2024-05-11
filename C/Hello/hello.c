// Prueba a compilar a mano, y mira que funciona. 
// gcc hello.c -o hello.out
// ./hello.out para ejecutarlo
#include <stdio.h> 

int main(int argc, char const *argv[])
{
    int s = 10;
    printf("EL valor de s es: %d\n", s);;

    char cadena[15] = "hola";
    printf("La cadena es: %s\n", cadena);

    return 0;
}