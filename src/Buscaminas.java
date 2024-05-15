import java.util.Scanner;

public class Buscaminas {
    static char[][] tablero;
    static char[][] minas;
    static int[][] solucion;
    static Scanner cap;

    public static void main(String[] args) {
        cap = new Scanner(System.in);

        System.out.println("Seleccione el nivel:\n1. Principiante\n2. Intermedio\n3. Experto\n4. Maestro");
        int nivel = cap.nextInt();

        switch (nivel){
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

        generarSolucion();
        jugar();
    }

    public static void mostrarTablero() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                System.out.print(tablero[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void iniciarTablero(int filas, int columnas, int totalMinas) {
        tablero = new char[filas][columnas];
        minas = new char[filas][columnas];
        solucion = new int[filas][columnas];

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                tablero[i][j] = '-';
            }
        }

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

    public static void generarSolucion() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas[0].length; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if ((i + k >= 0) && (j + l >= 0) && (i + k < minas.length) && (j + l < minas[0].length)) {
                            if (minas[i][j] != 'X') {
                                if (minas[i + k][j + l] == 'X') {
                                    solucion[i][j]++;
                                }
                            } else {
                                solucion[i][j] = -1;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void jugar() {
        boolean completado = false;
        do {
            mostrarTablero();
            System.out.print("Ingrese fila (o -1 para marcar/desmarcar una mina): ");
            int x = cap.nextInt();
            if (x == -1) {
                System.out.print("Ingrese fila para marcar/desmarcar: ");
                int markX = cap.nextInt() - 1;
                System.out.print("Ingrese columna para marcar/desmarcar: ");
                int markY = cap.nextInt() - 1;

                if (validarEntrada(markX, markY)) {
                    if (tablero[markX][markY] == '-') {
                        tablero[markX][markY] = 'M';
                    } else if (tablero[markX][markY] == 'M') {
                        tablero[markX][markY] = '-';
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
                    tablero[x - 1][y] = (char) (solucion[x - 1][y] + '0');
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

    public static void revelarMinas() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas[0].length; j++) {
                if (minas[i][j] == 'X') {
                    tablero[i][j] = 'X';
                }
            }
        }
    }

    public static boolean validarEntrada(int x, int y) {
        return x >= 0 && y >= 0 && x < tablero.length && y < tablero[0].length;
    }
}
