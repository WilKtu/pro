from json import load,dumps

def separador():
    print("================================")

while True: 
    separador()
    print("+-+-+-+-+ Bienvenido +-+-+-+-+")
    separador()
    print("1. Añadir elemento a la colección")
    print("2. Listar elementos de la colección")
    print("3. Buscar elementos")
    print("4. Editar elementos")
    print("5. Eliminar elementos")
    print("6. Salir")
    
    separador()
    
    op=int(input("Ingrese un opcion: "))
    
    separador()
    
    if op ==1:
        while True:
            separador()
            print("*** AÑADIR ***")
            separador()
            print("1. añadir titulo")
            print("2. añadir autor, director, artista")
            print("3. genero")
            print("4. valoracion")
            print("5. volver")
            separador()
            sub_op=int(input("Eliga una opcion: "))
            if sub_op==1:
                print("*")
            elif sub_op==2:
                print("d")
            elif sub_op==3:
                print("s")
            elif sub_op==4:
                print(" ")
            elif sub_op==5:
                print ("Regresando...")
                break
            else:
                print("opcion invalida")
    elif op==2:
        while True:
            separador()
            print("*** MOSTRAR ***")
            separador()
            print("1.  Elementos de la colecion")
            print("2. Listas por categoria")
            print("3. volver")
            separador()
            sub_op=int(input("Eliga una opcion: "))
            if sub_op==1:
                print("*")
            elif sub_op==2:
                print("d")
            elif sub_op==3:
                print ("Regresando...")
                break
            else:
                print("opcion invalida")
    elif op==3:
        while True:
            separador()
            print("*** BUSCAR ELEMENTO ***")
            separador()
            print("1. titulo ")
            print("2. autor o genero")
            print("3. volver")
            separador()
            sub_op=int(input("Eliga una opcion: "))
            if sub_op==1:
                print("*")
            elif sub_op==2:
                print("d")
            elif sub_op==3:
                print ("Regresando...")
                break
            else:
                print("opcion invalida")
    elif op==4:
        while True:
            separador()
            print("*** Editar elemento ***")
            separador()
            print("1. Editar elemento")
            print("2. voler")
            separador()
            sub_op=int(input("Eliga una opcion: "))
            if sub_op==1:
                print("*")
            elif sub_op==2:
                print ("Regresando...")
                break
            else:
                print("opcion invalida")
    elif op==5:
        while True:
            separador()
            print("*** ELINAR ELEMENTO ***")
            separador()
            print("1. eliminar un elemento")
            print("2. volver")
            separador()
            sub_op=int(input("Eliga una opcion: "))
            if sub_op==1:
                print("*")
            elif sub_op==2:
                print ("Regresando...")
                break
            else:
                print("opcion invalida")   
    elif op==6:
        print("Saliendo...")
        break
    else:
        print("Opcion invalida ")