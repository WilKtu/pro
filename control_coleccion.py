from json import load, dump

archivo_datos = "datos.json"

def cargar_datos():
    try:
        with open(archivo_datos, "r") as archivo:
            return load(archivo)
    except FileNotFoundError:
        print("Archivo no encontrado. Se creará uno nuevo.")
        return []
    except Exception as e:
        print(f"Error al cargar los datos: {e}")
        return []

def guardar_datos(coleccion):
    try:
        with open(archivo_datos, "w") as archivo:
            dump(coleccion, archivo, indent=4)
    except Exception as e:
        print(f"Error al guardar los datos: {e}")

def separador():  
    print("==="*15)

def mostrar_coleccion(coleccion):
    if not coleccion:
        print("La colección está vacía.")
    else:
        for i, item in enumerate(coleccion, start=1):
            print(f"{i}. {item['tipo']}: {item['titulo']} - {item['autor']} - {item['genero']} - {item['valoracion']}")

while True:
    try:
        separador()
        print("        +-+-+-+-+ Bienvenido +-+-+-+-+")
        separador()
        print("1. Añadir elemento a la colección")
        print("2. Listar elementos de la colección")
        print("3. Buscar elementos")
        print("4. Editar elementos")
        print("5. Eliminar elementos")
        print("6. Salir")
        separador()

        op = int(input("Ingrese una opción: "))
        separador()

        if op == 1:
            while True:
                try:
                    separador()
                    print("*** AÑADIR ***")
                    separador()
                    print("1. Añadir libro")
                    print("2. Añadir película")
                    print("3. Añadir música")
                    print("4. Volver")
                    separador()
                    sub_op = int(input("Elija una opción: "))
                    if sub_op in [1, 2, 3]:
                        tipo = "Libro" if sub_op == 1 else "Película" if sub_op == 2 else "Música"
                        titulo = input("Ingrese el título: ")
                        autor = input("Ingrese el autor/director/artista: ")
                        genero = input("Ingrese el género: ")
                        valoracion = input("Ingrese la valoración: ")
                        coleccion = cargar_datos()
                        coleccion.append({
                            "tipo": tipo,
                            "titulo": titulo,
                            "autor": autor,
                            "genero": genero,
                            "valoracion": valoracion
                        })
                        guardar_datos(coleccion)  
                        print(f"{tipo} añadido exitosamente.")
                    elif sub_op == 4:
                        print("Regresando...")
                        break
                    else:
                        print("Opción inválida")
                except ValueError:
                    print("Por favor, ingrese un número válido.")
        elif op == 2:
            while True:
                try:
                    separador()
                    print("*** MOSTRAR ***")
                    separador()
                    print("1. Elementos de la colección")
                    print("2. Listas por categoría")
                    print("3. Volver")
                    separador()
                    sub_op = int(input("Elija una opción: "))
                    if sub_op == 1:
                        coleccion = cargar_datos()
                        separador()
                        mostrar_coleccion(coleccion)
                        separador()
                    elif sub_op == 2:
                        coleccion = cargar_datos()
                        libros = [item for item in coleccion if item['tipo'] == "Libro"]
                        peliculas = [item for item in coleccion if item['tipo'] == "Película"]
                        musica = [item for item in coleccion if item['tipo'] == "Música"]
                        separador()
                        print("=== Libros ===")
                        mostrar_coleccion(libros)
                        separador()
                        print("=== Películas ===")
                        mostrar_coleccion(peliculas)
                        separador()
                        print("=== Música ===")
                        mostrar_coleccion(musica)
                    elif sub_op == 3:
                        print("Regresando...")
                        break
                    else:
                        print("Opción inválida")
                except ValueError:
                    print("Por favor, ingrese un número válido.")
        elif op == 3:
            while True:
                try:
                    separador()
                    print("*** BUSCAR ELEMENTO ***")
                    separador()
                    print("1. Título")
                    print("2. Autor o género")
                    print("3. Volver")
                    separador()
                    sub_op = int(input("Elija una opción: "))
                    if sub_op == 1:
                        criterio = input("Ingrese el título a buscar: ").lower()
                        coleccion = cargar_datos()
                        resultados = [item for item in coleccion if criterio in item['titulo'].lower()]
                        mostrar_coleccion(resultados)
                    elif sub_op == 2:
                        criterio = input("Ingrese el autor o género a buscar: ").lower()
                        coleccion = cargar_datos()
                        resultados = [item for item in coleccion if criterio in item['autor'].lower() or criterio in item['genero'].lower()]
                        mostrar_coleccion(resultados)
                    elif sub_op == 3:
                        print("Regresando...")
                        break
                    else:
                        print("Opción inválida")
                except ValueError:
                    print("Por favor, ingrese un número válido.")
        elif op == 4: 
            while True:
                try:
                    separador()
                    print("*** EDITAR ELEMENTO ***")
                    separador()
                    coleccion = cargar_datos()
                    mostrar_coleccion(coleccion)
                    separador()
                    print("1. Editar elemento")
                    print("2. Volver")
                    separador()
                    sub_op = int(input("Elija una opción: "))
                    if sub_op == 1:
                        if not coleccion:
                            print("La colección está vacía.")
                        else:
                            indice = int(input("Ingrese el número del elemento a editar: ")) - 1
                            if 0 <= indice < len(coleccion):
                                item = coleccion[indice]
                                print(f"Editando: {item['titulo']} (deja en blanco para no cambiar)")
                                titulo = input(f"Nuevo título [{item['titulo']}]: ")
                                autor = input(f"Nuevo autor [{item['autor']}]: ")
                                genero = input(f"Nuevo género [{item['genero']}]: ")
                                valoracion = input(f"Nueva valoración [{item['valoracion']}]: ")
                                if titulo: item['titulo'] = titulo
                                if autor: item['autor'] = autor
                                if genero: item['genero'] = genero
                                if valoracion: item['valoracion'] = valoracion
                                guardar_datos(coleccion)
                                print("Elemento editado exitosamente.")
                            else:
                                print("Número inválido.")
                    elif sub_op == 2:
                        print("Regresando...")
                        break
                    else:
                        print("Opción inválida")
                except ValueError:
                    print("Por favor, ingrese un número válido.")
        elif op == 5:  
            while True:
                try:
                    separador()
                    print("*** ELIMINAR ELEMENTO ***")
                    separador()
                    coleccion = cargar_datos()
                    mostrar_coleccion(coleccion)
                    separador()
                    print("1. Eliminar un elemento")
                    print("2. Volver")
                    separador()
                    sub_op = int(input("Elija una opción: "))
                    if sub_op == 1:
                        if not coleccion:
                            print("La colección está vacía.")
                        else:
                            indice = int(input("Ingrese el número del elemento a eliminar: ")) - 1
                            if 0 <= indice < len(coleccion):
                                eliminado = coleccion.pop(indice)
                                guardar_datos(coleccion)
                                print(f"'{eliminado['titulo']}' eliminado exitosamente.")
                            else:
                                print("Número inválido.")
                    elif sub_op == 2:
                        print("Regresando...")
                        break
                    else:
                        print("Opción inválida")
                except ValueError:
                    print("Por favor, ingrese un número válido.")
        elif op == 6:
            print("Saliendo...")
            break
        else:
            print("Opción inválida")
    except ValueError:
        print("Por favor, ingrese un número válido.")
