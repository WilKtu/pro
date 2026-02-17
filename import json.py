import json

def separador():
    print("==========================")

# Función para cargar datos desde un archivo JSON
def cargar_datos(nombre_archivo):
    try:
        with open(nombre_archivo, 'r') as archivo:
            return json.load(archivo)  # Cargar datos existentes
    except FileNotFoundError:
        print("Archivo no encontrado. Se creará uno nuevo.")
        return []  # Si no existe el archivo, devolvemos una lista vacía
    except json.JSONDecodeError:
        print("El archivo JSON está vacío o tiene un formato incorrecto. Se creará uno nuevo.")
        return []

# Función para guardar datos en un archivo JSON
def guardar_datos(nombre_archivo, datos):
    with open(nombre_archivo, 'w') as archivo:
        json.dump(datos, archivo, indent=4)  # Guardar datos en formato JSON con indentación

# Nombre del archivo JSON
nombre_archivo = 'coleccion.json'

# Cargar datos iniciales desde el archivo JSON
coleccion = cargar_datos(nombre_archivo)

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
    
    try:
        op = int(input("Ingrese una opción: "))
    except ValueError:
        print("Por favor, ingrese un número válido.")
        continue
    
    if op == 1:
        # Añadir un nuevo elemento a la colección
        elemento = input("Ingrese el elemento a añadir: ")
        coleccion.append(elemento)  # Agregar el nuevo elemento a la lista
        guardar_datos(nombre_archivo, coleccion)  # Guardar la colección actualizada en el archivo JSON
        print("Elemento añadido con éxito.")
    elif op == 2:
        # Listar todos los elementos de la colección
        print("Elementos en la colección:")
        for i, elem in enumerate(coleccion, start=1):
            print(f"{i}. {elem}")
    elif op == 3:
        # Buscar un elemento en la colección
        busqueda = input("Ingrese el elemento a buscar: ")
        if busqueda in coleccion:
            print(f"El elemento '{busqueda}' está en la colección.")
        else:
            print(f"El elemento '{busqueda}' no se encontró.")
    elif op == 4:
        # Editar un elemento de la colección
        print("Elementos en la colección:")
        for i, elem in enumerate(coleccion, start=1):
            print(f"{i}. {elem}")
        try:
            indice = int(input("Ingrese el número del elemento a editar: ")) - 1
            if 0 <= indice < len(coleccion):
                nuevo_valor = input("Ingrese el nuevo valor: ")
                coleccion[indice] = nuevo_valor
                guardar_datos(nombre_archivo, coleccion)  # Guardar cambios en el archivo JSON
                print("Elemento editado con éxito.")
            else:
                print("Índice fuera de rango.")
        except ValueError:
            print("Por favor, ingrese un número válido.")
    elif op == 5:
        # Eliminar un elemento de la colección
        print("Elementos en la colección:")
        for i, elem in enumerate(coleccion, start=1):
            print(f"{i}. {elem}")
        try:
            indice = int(input("Ingrese el número del elemento a eliminar: ")) - 1
            if 0 <= indice < len(coleccion):
                eliminado = coleccion.pop(indice)
                guardar_datos(nombre_archivo, coleccion)  # Guardar cambios en el archivo JSON
                print(f"Elemento '{eliminado}' eliminado con éxito.")
            else:
                print("Índice fuera de rango.")
        except ValueError:
            print("Por favor, ingrese un número válido.")
    elif op == 6:
        # Cargar elementos desde el archivo JSON
        coleccion = cargar_datos(nombre_archivo)
        print("Elementos cargados desde el archivo.")
    elif op == 7:
        # Salir del programa
        print("Saliendo...")
        break
    else:
        print("Opción inválida.")