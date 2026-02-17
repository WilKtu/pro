from json import load,dumps

def separador():
    print("==========================")

while True: 
    print("+-+-+-+-+ Bienvenido +-+-+-+-+")
    print("1. Añadir elemento a la colección")
    print("2. Listar elementos de la colección")
    print("3. Buscar elementos")
    print("4. Editar elementos")
    print("5. Eliminar elementos")
    print("6. Cargar elementos")
    print("7. Salir")
    
    separador()
    
    op=int(input("Ingrese un opcion: "))
    
    if op ==1:
        while True:
            print("*** AÑADIR ***")
            print("1. añadir titulo")
            print("2. añadir autor, director, artista")
            print("3. genero")
            print("4. valoracion")
            print("5. volver")
            sub_op=int(input("Eliga una opcion"))
    elif op==2:
        while True:
            print("*** MOSTRAR ***")
            print("1.  Elementos de la colecion")
            print("2. Listas por categoria")
            print("3. volver")
            sub_op=int(input("Eliga una opcion"))
    elif op==3:
        while True:
            print("*** BUSCAR ELEMENTO ***")
            print("1. titulo ")
            print("2. autor o genero")
            print("3. volver")
            
    elif op==4:
        print(" ")
    elif op==5:
        print(" ")
    elif op==6:
        print(" ")        
    elif op==7:
        print("Saliendo...")
        break
    else:
        print("Opcion invalida ")