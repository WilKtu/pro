"""
=======================================================
  ADMINISTRADOR DE COLECCIÓN DE LIBROS/PELÍCULAS/MÚSICA
=======================================================
Autor: Proyecto Python
Descripción: Aplicación de consola para gestionar una
             colección personal de elementos culturales.
Persistencia: Archivo JSON (coleccion.json)
=======================================================
"""

import json
import os
import uuid
from tabulate import tabulate

# ── CONSTANTES ──────────────────────────────────────────────────────────────
ARCHIVO_JSON = "coleccion.json"

TIPOS_VALIDOS = ["libro", "película", "música"]

GENEROS_SUGERIDOS = {
    "libro":    ["Ficción", "No Ficción", "Ciencia Ficción", "Fantasía",
                 "Terror", "Romance", "Histórico", "Biografía", "Otro"],
    "película": ["Acción", "Comedia", "Drama", "Terror", "Ciencia Ficción",
                 "Animación", "Documental", "Romance", "Thriller", "Otro"],
    "música":   ["Pop", "Rock", "Jazz", "Clásica", "Hip-Hop", "Electrónica",
                 "Reggaeton", "Country", "Metal", "Otro"],
}

COLORES = {
    "titulo":   "\033[1;96m",  # Cian brillante negrita
    "exito":    "\033[1;92m",  # Verde brillante
    "error":    "\033[1;91m",  # Rojo brillante
    "aviso":    "\033[1;93m",  # Amarillo
    "info":     "\033[1;94m",  # Azul
    "reset":    "\033[0m",
    "menu":     "\033[1;95m",  # Magenta
    "separador":"\033[90m",    # Gris
}

def c(texto, clave):
    """Aplica color ANSI al texto."""
    return f"{COLORES[clave]}{texto}{COLORES['reset']}"


# ── PERSISTENCIA JSON ────────────────────────────────────────────────────────

def cargar_coleccion() -> list[dict]:
    """
    Carga la colección desde el archivo JSON.
    Si el archivo no existe o está corrupto, devuelve lista vacía.
    """
    if not os.path.exists(ARCHIVO_JSON):
        return []
    try:
        with open(ARCHIVO_JSON, "r", encoding="utf-8") as f:
            datos = json.load(f)
        # Validamos que sea una lista
        return datos if isinstance(datos, list) else []
    except (json.JSONDecodeError, IOError):
        print(c(f"\n⚠ No se pudo leer '{ARCHIVO_JSON}'. Iniciando con colección vacía.", "aviso"))
        return []


def guardar_coleccion(coleccion: list[dict]) -> None:
    """
    Guarda la colección completa en el archivo JSON con formato legible.
    Se llama automáticamente después de cada operación de escritura.
    """
    try:
        with open(ARCHIVO_JSON, "w", encoding="utf-8") as f:
            json.dump(coleccion, f, ensure_ascii=False, indent=4)
    except IOError as e:
        print(c(f"\n✗ Error al guardar: {e}", "error"))


# ── UTILIDADES DE INTERFAZ ───────────────────────────────────────────────────

def limpiar_pantalla():
    os.system("cls" if os.name == "nt" else "clear")


def pausar():
    input(c("\n  Presiona Enter para continuar...", "separador"))


def imprimir_banner():
    limpiar_pantalla()
    sep = c("═" * 55, "titulo")
    print(f"""
{sep}
{c("   📚 ADMINISTRADOR DE COLECCIÓN CULTURAL 🎬🎵", "titulo")}
{sep}""")


def imprimir_separador(char="─", ancho=55):
    print(c(char * ancho, "separador"))


def solicitar_tipo() -> str:
    """Pide al usuario que elija el tipo de elemento."""
    print(c("\n  Tipo de elemento:", "info"))
    for i, t in enumerate(TIPOS_VALIDOS, 1):
        print(f"    {c(str(i), 'menu')}. {t.capitalize()}")
    while True:
        opcion = input(c("  Elige (1-3): ", "aviso")).strip()
        if opcion in {"1", "2", "3"}:
            return TIPOS_VALIDOS[int(opcion) - 1]
        print(c("  ✗ Opción inválida.", "error"))


def solicitar_genero(tipo: str) -> str:
    """Muestra géneros sugeridos para el tipo dado; permite ingresar uno libre."""
    sugeridos = GENEROS_SUGERIDOS.get(tipo, [])
    print(c(f"\n  Géneros sugeridos para {tipo}:", "info"))
    for i, g in enumerate(sugeridos, 1):
        print(f"    {c(str(i), 'menu')}. {g}")
    print(f"    {c('0', 'menu')}. Escribir género personalizado")
    while True:
        opcion = input(c(f"  Elige (0-{len(sugeridos)}): ", "aviso")).strip()
        if opcion == "0":
            genero = input(c("  Género personalizado: ", "aviso")).strip()
            return genero if genero else "Sin género"
        if opcion.isdigit() and 1 <= int(opcion) <= len(sugeridos):
            return sugeridos[int(opcion) - 1]
        print(c("  ✗ Opción inválida.", "error"))


def solicitar_valoracion() -> float | None:
    """Pide una valoración entre 0.0 y 10.0 (opcional)."""
    while True:
        entrada = input(c("  Valoración 0-10 (Enter para omitir): ", "aviso")).strip()
        if entrada == "":
            return None
        try:
            val = float(entrada)
            if 0.0 <= val <= 10.0:
                return round(val, 1)
            print(c("  ✗ Debe estar entre 0 y 10.", "error"))
        except ValueError:
            print(c("  ✗ Ingresa un número válido.", "error"))


def estrellas(valoracion: float | None) -> str:
    """Convierte valoración numérica a representación visual de estrellas."""
    if valoracion is None:
        return "Sin valorar"
    llenas = int(valoracion / 2)
    media  = 1 if (valoracion / 2 - llenas) >= 0.5 else 0
    vacias = 5 - llenas - media
    return "★" * llenas + ("½" * media) + "☆" * vacias + f" ({valoracion})"


# ── FUNCIONES PRINCIPALES ────────────────────────────────────────────────────

def añadir_elemento(coleccion: list[dict]) -> None:
    """
    FUNCIÓN 1 — Añadir Elemento
    Solicita todos los campos al usuario, genera un UUID único y
    añade el nuevo elemento a la lista, guardando de inmediato en JSON.
    """
    imprimir_banner()
    print(c("  ╔══ AÑADIR NUEVO ELEMENTO ══╗", "exito"))
    imprimir_separador()

    tipo = solicitar_tipo()

    titulo = ""
    while not titulo:
        titulo = input(c("\n  Título: ", "aviso")).strip()
        if not titulo:
            print(c("  ✗ El título es obligatorio.", "error"))

    campo_autor = {"libro": "Autor", "película": "Director", "música": "Artista"}
    autor = input(c(f"  {campo_autor[tipo]}: ", "aviso")).strip() or "Desconocido"

    genero     = solicitar_genero(tipo)
    valoracion = solicitar_valoracion()
    notas      = input(c("  Notas adicionales (opcional): ", "aviso")).strip()

    elemento = {
        "id":         str(uuid.uuid4())[:8],   # ID corto de 8 caracteres
        "tipo":       tipo,
        "titulo":     titulo,
        "autor":      autor,
        "genero":     genero,
        "valoracion": valoracion,
        "notas":      notas,
    }

    coleccion.append(elemento)
    guardar_coleccion(coleccion)

    print(c(f"\n  ✔ '{titulo}' añadido correctamente a tu colección.", "exito"))
    pausar()


def listar_elementos(coleccion: list[dict]) -> None:
    """
    FUNCIÓN 2 — Listar Elementos
    Muestra todos los elementos en formato tabla con tabulate.
    Permite filtrar por tipo para ver sólo libros, películas o música.
    """
    imprimir_banner()
    print(c("  ╔══ LISTAR COLECCIÓN ══╗", "info"))
    imprimir_separador()

    if not coleccion:
        print(c("\n  ℹ La colección está vacía.", "aviso"))
        pausar()
        return

    # Sub-menú de filtro
    print(c("\n  Filtrar por:", "info"))
    print(f"    {c('0', 'menu')}. Todos")
    for i, t in enumerate(TIPOS_VALIDOS, 1):
        print(f"    {c(str(i), 'menu')}. {t.capitalize()}s")

    opcion = input(c("  Elige (0-3): ", "aviso")).strip()
    if opcion in {"1", "2", "3"}:
        tipo_filtro = TIPOS_VALIDOS[int(opcion) - 1]
        lista_filtrada = [e for e in coleccion if e["tipo"] == tipo_filtro]
        titulo_tabla = f"Colección: {tipo_filtro.capitalize()}s"
    else:
        lista_filtrada = coleccion
        titulo_tabla = "Colección completa"

    if not lista_filtrada:
        print(c(f"\n  ℹ No hay elementos del tipo seleccionado.", "aviso"))
        pausar()
        return

    # Preparar filas para tabulate
    filas = []
    for e in lista_filtrada:
        filas.append([
            c(e["id"], "separador"),
            c(e["tipo"].capitalize(), "menu"),
            c(e["titulo"], "titulo"),
            e["autor"],
            e["genero"],
            estrellas(e["valoracion"]),
        ])

    headers = [
        c("ID",         "info"),
        c("Tipo",       "info"),
        c("Título",     "info"),
        c("Autor/Dir",  "info"),
        c("Género",     "info"),
        c("Valoración", "info"),
    ]

    print(f"\n  {c(titulo_tabla, 'exito')} — {c(str(len(lista_filtrada)), 'aviso')} elemento(s)\n")
    print(tabulate(filas, headers=headers, tablefmt="rounded_outline"))
    pausar()


def buscar_elemento(coleccion: list[dict]) -> None:
    """
    FUNCIÓN 3 — Buscar Elemento
    Filtra la colección por título, autor/director/artista o género,
    con búsqueda insensible a mayúsculas/minúsculas.
    """
    imprimir_banner()
    print(c("  ╔══ BUSCAR ELEMENTO ══╗", "info"))
    imprimir_separador()

    if not coleccion:
        print(c("\n  ℹ La colección está vacía.", "aviso"))
        pausar()
        return

    print(c("\n  Buscar por:", "info"))
    print(f"    {c('1', 'menu')}. Título")
    print(f"    {c('2', 'menu')}. Autor / Director / Artista")
    print(f"    {c('3', 'menu')}. Género")

    opcion = input(c("  Elige (1-3): ", "aviso")).strip()
    campos = {"1": "titulo", "2": "autor", "3": "genero"}

    if opcion not in campos:
        print(c("  ✗ Opción inválida.", "error"))
        pausar()
        return

    campo  = campos[opcion]
    termino = input(c(f"  Ingresa el término a buscar: ", "aviso")).strip().lower()

    if not termino:
        print(c("  ✗ El término no puede estar vacío.", "error"))
        pausar()
        return

    resultados = [e for e in coleccion if termino in str(e.get(campo, "")).lower()]

    if not resultados:
        print(c(f"\n  ✗ Sin resultados para '{termino}' en '{campo}'.", "aviso"))
        pausar()
        return

    print(c(f"\n  ✔ {len(resultados)} resultado(s) encontrado(s):\n", "exito"))

    filas = []
    for e in resultados:
        filas.append([
            c(e["id"], "separador"),
            c(e["tipo"].capitalize(), "menu"),
            c(e["titulo"], "titulo"),
            e["autor"],
            e["genero"],
            estrellas(e["valoracion"]),
        ])

    headers = [
        c("ID", "info"), c("Tipo", "info"), c("Título", "info"),
        c("Autor/Dir", "info"), c("Género", "info"), c("Valoración", "info"),
    ]
    print(tabulate(filas, headers=headers, tablefmt="rounded_outline"))
    pausar()


def editar_elemento(coleccion: list[dict]) -> None:
    """
    FUNCIÓN 4 — Editar Elemento
    Busca un elemento por ID o título y permite actualizar cualquier campo.
    Los campos vacíos en la edición conservan el valor original.
    """
    imprimir_banner()
    print(c("  ╔══ EDITAR ELEMENTO ══╗", "aviso"))
    imprimir_separador()

    if not coleccion:
        print(c("\n  ℹ La colección está vacía.", "aviso"))
        pausar()
        return

    termino = input(c("\n  Ingresa el ID o título del elemento a editar: ", "aviso")).strip().lower()

    # Buscar coincidencias por ID exacto o título parcial
    coincidencias = [
        e for e in coleccion
        if e["id"].lower() == termino or termino in e["titulo"].lower()
    ]

    if not coincidencias:
        print(c("  ✗ No se encontró ningún elemento.", "error"))
        pausar()
        return

    # Si hay varias coincidencias, mostrar lista y pedir selección
    if len(coincidencias) > 1:
        print(c(f"\n  Se encontraron {len(coincidencias)} coincidencias:\n", "aviso"))
        for i, e in enumerate(coincidencias, 1):
            print(f"  {c(str(i), 'menu')}. [{e['id']}] {e['titulo']} ({e['tipo']})")
        while True:
            sel = input(c(f"  Selecciona (1-{len(coincidencias)}): ", "aviso")).strip()
            if sel.isdigit() and 1 <= int(sel) <= len(coincidencias):
                elemento = coincidencias[int(sel) - 1]
                break
            print(c("  ✗ Opción inválida.", "error"))
    else:
        elemento = coincidencias[0]

    print(c(f"\n  Editando: {elemento['titulo']} [{elemento['id']}]", "exito"))
    print(c("  (Deja en blanco para conservar el valor actual)\n", "separador"))

    campo_autor = {"libro": "Autor", "película": "Director", "música": "Artista"}

    nuevo_titulo = input(c(f"  Título [{elemento['titulo']}]: ", "aviso")).strip()
    nuevo_autor  = input(c(f"  {campo_autor[elemento['tipo']]} [{elemento['autor']}]: ", "aviso")).strip()
    nuevo_genero = input(c(f"  Género [{elemento['genero']}]: ", "aviso")).strip()

    print(c(f"  Valoración actual: {estrellas(elemento['valoracion'])}", "info"))
    nueva_val_raw = input(c("  Nueva valoración 0-10 (Enter para conservar): ", "aviso")).strip()
    if nueva_val_raw == "":
        nueva_val = elemento["valoracion"]
    else:
        try:
            nueva_val = round(float(nueva_val_raw), 1)
            if not (0.0 <= nueva_val <= 10.0):
                print(c("  ✗ Fuera de rango, se conserva el valor actual.", "error"))
                nueva_val = elemento["valoracion"]
        except ValueError:
            print(c("  ✗ Valor inválido, se conserva el actual.", "error"))
            nueva_val = elemento["valoracion"]

    nuevas_notas = input(c(f"  Notas [{elemento.get('notas', '')}]: ", "aviso")).strip()

    # Aplicar cambios sólo si el usuario ingresó algo nuevo
    if nuevo_titulo:  elemento["titulo"]     = nuevo_titulo
    if nuevo_autor:   elemento["autor"]      = nuevo_autor
    if nuevo_genero:  elemento["genero"]     = nuevo_genero
    elemento["valoracion"] = nueva_val
    if nuevas_notas:  elemento["notas"]      = nuevas_notas

    guardar_coleccion(coleccion)
    print(c(f"\n  ✔ Elemento actualizado correctamente.", "exito"))
    pausar()


def eliminar_elemento(coleccion: list[dict]) -> None:
    """
    FUNCIÓN 5 — Eliminar Elemento
    Busca por ID o título y solicita confirmación antes de borrar.
    """
    imprimir_banner()
    print(c("  ╔══ ELIMINAR ELEMENTO ══╗", "error"))
    imprimir_separador()

    if not coleccion:
        print(c("\n  ℹ La colección está vacía.", "aviso"))
        pausar()
        return

    termino = input(c("\n  Ingresa el ID o título del elemento a eliminar: ", "aviso")).strip().lower()

    coincidencias = [
        (i, e) for i, e in enumerate(coleccion)
        if e["id"].lower() == termino or termino in e["titulo"].lower()
    ]

    if not coincidencias:
        print(c("  ✗ No se encontró ningún elemento.", "error"))
        pausar()
        return

    if len(coincidencias) > 1:
        print(c(f"\n  {len(coincidencias)} coincidencias:\n", "aviso"))
        for num, (_, e) in enumerate(coincidencias, 1):
            print(f"  {c(str(num), 'menu')}. [{e['id']}] {e['titulo']} ({e['tipo']})")
        while True:
            sel = input(c(f"  Selecciona (1-{len(coincidencias)}): ", "aviso")).strip()
            if sel.isdigit() and 1 <= int(sel) <= len(coincidencias):
                idx, elemento = coincidencias[int(sel) - 1]
                break
            print(c("  ✗ Opción inválida.", "error"))
    else:
        idx, elemento = coincidencias[0]

    print(c(f"\n  ⚠ ¿Eliminar '{elemento['titulo']}' [{elemento['id']}]? (s/n): ", "aviso"), end="")
    confirmacion = input().strip().lower()

    if confirmacion == "s":
        coleccion.pop(idx)
        guardar_coleccion(coleccion)
        print(c("  ✔ Elemento eliminado correctamente.", "exito"))
    else:
        print(c("  Operación cancelada.", "separador"))

    pausar()


def mostrar_estadisticas(coleccion: list[dict]) -> None:
    """
    FUNCIÓN EXTRA — Estadísticas
    Resumen rápido: totales por tipo y promedio de valoraciones.
    """
    imprimir_banner()
    print(c("  ╔══ ESTADÍSTICAS ══╗", "info"))
    imprimir_separador()

    if not coleccion:
        print(c("\n  ℹ La colección está vacía.", "aviso"))
        pausar()
        return

    total = len(coleccion)
    por_tipo = {t: sum(1 for e in coleccion if e["tipo"] == t) for t in TIPOS_VALIDOS}
    valorados = [e["valoracion"] for e in coleccion if e["valoracion"] is not None]
    promedio  = round(sum(valorados) / len(valorados), 2) if valorados else None

    filas_stats = [
        ["Total de elementos", c(str(total), "exito")],
        [c("📚 Libros", "titulo"),       c(str(por_tipo["libro"]),    "aviso")],
        [c("🎬 Películas", "titulo"),    c(str(por_tipo["película"]), "aviso")],
        [c("🎵 Música", "titulo"),       c(str(por_tipo["música"]),   "aviso")],
        ["Elementos valorados",  c(str(len(valorados)), "info")],
        ["Promedio de valoración", c(str(promedio) if promedio else "N/A", "exito")],
    ]
    print()
    print(tabulate(filas_stats, tablefmt="rounded_outline"))
    pausar()


# ── MENÚ PRINCIPAL ───────────────────────────────────────────────────────────

def menu_principal(coleccion: list[dict]) -> None:
    """Bucle principal del programa que muestra el menú y despacha funciones."""
    while True:
        imprimir_banner()
        total = len(coleccion)
        print(c(f"  📂 Colección cargada: {total} elemento(s)", "separador"))
        imprimir_separador()
        print(f"""
  {c("1", "menu")}. ➕  Añadir elemento
  {c("2", "menu")}. 📋  Listar elementos
  {c("3", "menu")}. 🔍  Buscar elemento
  {c("4", "menu")}. ✏   Editar elemento
  {c("5", "menu")}. 🗑   Eliminar elemento
  {c("6", "menu")}. 📊  Estadísticas
  {c("0", "error")}. 🚪  Salir
""")
        imprimir_separador()
        opcion = input(c("  Elige una opción: ", "aviso")).strip()

        acciones = {
            "1": lambda: añadir_elemento(coleccion),
            "2": lambda: listar_elementos(coleccion),
            "3": lambda: buscar_elemento(coleccion),
            "4": lambda: editar_elemento(coleccion),
            "5": lambda: eliminar_elemento(coleccion),
            "6": lambda: mostrar_estadisticas(coleccion),
        }

        if opcion == "0":
            guardar_coleccion(coleccion)
            limpiar_pantalla()
            print(c("\n  ✔ Colección guardada. ¡Hasta pronto!\n", "exito"))
            break
        elif opcion in acciones:
            acciones[opcion]()
        else:
            print(c("  ✗ Opción inválida. Intenta de nuevo.", "error"))
            pausar()


# ── PUNTO DE ENTRADA ─────────────────────────────────────────────────────────

if __name__ == "__main__":
    print(c("\n  Cargando colección...", "separador"))
    coleccion = cargar_coleccion()
    menu_principal(coleccion)