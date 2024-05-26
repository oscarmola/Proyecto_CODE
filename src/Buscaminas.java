import java.util.Scanner;

public class Main {
    static char[][] tablero;
    static char[][] minas;
    static int[][] solucion;
    static Scanner cap;

    public static void main(String[] args) {
        cap = new Scanner(System.in);

        System.out.println("Seleccione el nivel:\n1. Principiante\n2. Intermedio\n3. Experto");
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
        }

        generarSolucion();
        jugar(1);
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

    public static void jugar(int cantidadMinas) {
        boolean completado = false;
        do {
            System.out.print("Ingrese fila: ");
            int x = cap.nextInt() - 1;
            System.out.print("Ingrese columna: ");
            int y = cap.nextInt() - 1;

            if (minas[x][y] == 'X') {
                System.out.println("Perdiste pendejo!");
                for (int i = 0; i < minas.length; i++) {
                    for (int j = 0; j < minas[0].length; j++) {
                        if (minas[i][j] == 'X') {
                            tablero[i][j] = 'X';
                        }
                    }
                }
                mostrarTablero();
                break;
            } else {
                tablero[x][y] = (solucion[x][y] + "").charAt(0);
                mostrarTablero();
            }

            boolean victoria = verficarVictoria(cantidadMinas);

            if (victoria) {
                System.out.println("Ganaste!");
                for (int i = 0; i < minas.length; i++) {
                    for (int j = 0; j < minas[0].length; j++) {
                        if (minas[i][j] == 'X') {
                            tablero[i][j] = '*';
                        }
                    }
                }
                mostrarTablero();
                break;
            }
        } while (!completado);
    }

    public static boolean verficarVictoria(int cantidadMinas) {
        int posiblesMinas = 0;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] == '-') {
                    posiblesMinas++;
                }
            }
        }
        if (posiblesMinas == cantidadMinas) {
            return true;
        }
        return false;
    }
}
