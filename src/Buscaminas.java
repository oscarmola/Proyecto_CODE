import java.util.Scanner;

public class Buscaminas {
    static char[][] tablero;  // Tablero de juego que el jugador ve
    static char[][] minas;    // Tablero que contiene las minas
    static int[][] solucion;  // Tablero con los conteos de minas adyacentes
    static Scanner cap;       // Objeto Scanner para capturar la entrada del usuario

    public static void main(String[] args) {
        cap = new Scanner(System.in);

        // Solicitar al usuario que seleccione el nivel de dificultad
        System.out.println("Seleccione el nivel:\n1. Principiante\n2. Intermedio\n3. Experto\n4. Maestro");
        int nivel = cap.nextInt();

        // Configurar el tablero según el nivel seleccionado
        switch (nivel) {
            case 1:
                iniciarTablero(3, 3, 1);
                break;
            case 2:
                iniciarTablero(6, 6, 10);
                break;
            case 3:
                iniciarTablero(10, 10, 30);
                break;
            case 4:
                iniciarTablero(15, 15, 50);
                break;
            default:
                System.out.println("Nivel no válido. Seleccionando nivel Principiante por defecto.");
                iniciarTablero(3, 3, 1);
                break;
        }

        // Generar la solución y empezar el juego
        generarSolucion();
        jugar();
    }

    // Método para mostrar el tablero en consola
    public static void mostrarTablero() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                System.out.print(tablero[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Método para inicializar el tablero con minas y configuraciones
    public static void iniciarTablero(int filas, int columnas, int totalMinas) {
        tablero = new char[filas][columnas];
        minas = new char[filas][columnas];
        solucion = new int[filas][columnas];

        // Inicializar el tablero con '-'
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                tablero[i][j] = '-';
            }
        }

        // Colocar minas aleatoriamente en el tablero
        int contadorMinas = 0;
        while (contadorMinas < totalMinas) {
            int x = (int) (Math.random() * filas);
            int y = (int) (Math.random() * columnas);
            if (minas[x][y] == '\u0000') {
                minas[x][y] = 'X';
                contadorMinas++;
            }
        }
    }

    // Método para generar la solución del tablero con conteo de minas adyacentes
    public static void generarSolucion() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas[0].length; j++) {
                if (minas[i][j] == 'X') {
                    incrementarContadores(i, j);
                }
            }
        }
    }

    // Método para incrementar los contadores alrededor de una mina
    public static void incrementarContadores(int fila, int columna) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = fila + i;
                int y = columna + j;
                if (x >= 0 && y >= 0 && x < solucion.length && y < solucion[0].length && minas[x][y] != 'X') {
                    solucion[x][y]++;
                }
            }
        }
    }

    // Método principal del juego
    public static void jugar() {
        boolean completado = false;
        do {
            mostrarTablero();

            System.out.println("Ingrese fila (o 0 para marcar): ");
            int x = cap.nextInt();
            if (x == 0) {
                System.out.println("Ingrese fila a marcar: ");
                int markX = cap.nextInt() - 1;
                System.out.println("Ingrese columna a marcar: ");
                int markY = cap.nextInt() - 1;
                if (validarEntrada(markX, markY)) {
                    if (tablero[markX][markY] == '-') {
                        tablero[markX][markY] = 'M';  // Marcar casilla como mina
                    } else if (tablero[markX][markY] == 'M') {
                        tablero[markX][markY] = '-';  // Desmarcar casilla
                    } else {
                        System.out.println("No se puede marcar esta casilla.");
                    }
                } else {
                    System.out.println("Entrada inválida. Intente de nuevo.");
                }
                continue;
            }

            System.out.print("Ingrese columna: ");
            int y = cap.nextInt() - 1;

            if (validarEntrada(x - 1, y)) {
                if (minas[x - 1][y] == 'X') {
                    System.out.println("Perdiste!");
                    revelarMinas();
                    mostrarTablero();
                    break;
                } else {
                    tablero[x - 1][y] = (char) (solucion[x - 1][y] + '0');  // Mostrar el número de minas adyacentes
                    if (verificarVictoria()) {
                        System.out.println("Ganaste!");
                        revelarMinas();
                        mostrarTablero();
                        break;
                    }
                }
            } else {
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        } while (!completado);
    }

    // Método para verificar si el jugador ha ganado
    public static boolean verificarVictoria() {
        int posiblesMinas = 0;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] == '-' || tablero[i][j] == 'M') {
                    posiblesMinas++;
                }
            }
        }
        return posiblesMinas == 0;
    }

    // Método para revelar todas las minas en el tablero
    public static void revelarMinas() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas[0].length; j++) {
                if (minas[i][j] == 'X') {
                    tablero[i][j] = 'X';
                }
            }
        }
    }

    // Método para validar si una entrada es válida (dentro de los límites del tablero)
    public static boolean validarEntrada(int x, int y) {
        return x >= 0 && y >= 0 && x < tablero.length && y < tablero[0].length;
    }
}
