package BlindSearch;

import java.util.Arrays;


class State {
    static final State TARGET = new State(new int[][]{{1, 2, 3}, {8, 0, 4}, {7, 6, 5}});
    static final State FIRST = new State(new int[][]{{6, 0, 8}, {5, 2, 1}, {4, 3, 7}});
    private static final int N = 3;

    private final int[][] matrix;
    private final int zeroI;
    private final int zeroJ;

    private State(int[][] matrix) {
        this.matrix = matrix;
        zeroI = searchZeroI();
        zeroJ = searchZeroJ();
    }

    private State(int[][] matrix, int zeroI, int zeroJ) {
        this.matrix = matrix;
        this.zeroI = zeroI;
        this.zeroJ = zeroJ;
    }

    /**
     * Проверяем, является ли состояние целевым
     *
     * @return {@code true} если состояние является целевым
     */
    boolean isTarget() {
        return equals(TARGET);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof State) {
            State state = (State) obj;
            for (int i = 0; i < N; i++) {
                if (!Arrays.equals(state.matrix[i], matrix[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (int i = 0; i < N; i++) {
            h *= Arrays.hashCode(matrix[i]);
        }
        return h;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int[] ints : matrix) {
            string.append(Arrays.toString(ints));
            if (ints != matrix[matrix.length - 1])
                string.append("\r\n");
        }
        return string.toString();
    }

    /**
     * Проверяем возможность движения вправо
     *
     * @return {@code true} если возможно
     */
    boolean canMoveRight() {
        return zeroJ > 0 && zeroJ < N;
    }

    /**
     * Получаем новое состояние движением вправо
     *
     * @return полученное состояние
     */
    State moveRight() {
        if (canMoveRight()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI, zeroJ - 1);
            return new State(result, zeroI, zeroJ - 1);
        }
        return null;
    }

    /**
     * Проверяем возможность движения влево
     *
     * @return {@code true} если возможно
     */
    boolean canMoveLeft() {
        return zeroJ >= 0 && zeroJ < N - 1;
    }

    /**
     * Получаем новое состояние движением влево
     *
     * @return полученное состояние
     */
    State moveLeft() {
        if (canMoveLeft()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI, zeroJ + 1);
            return new State(result, zeroI, zeroJ + 1);
        }
        return null;
    }

    /**
     * Проверяем возможность движения вверх
     *
     * @return {@code true} если возможно
     */
    boolean canMoveUp() {
        return zeroI >= 0 && zeroI < N - 1;
    }

    /**
     * Получаем новое состояние движением вверх
     *
     * @return полученное состояние
     */
    State moveUp() {
        if (canMoveUp()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI + 1, zeroJ);
            return new State(result, zeroI + 1, zeroJ);
        }
        return null;
    }

    /**
     * Проверяем возможность движения вниз
     *
     * @return {@code true} если возможно
     */
    boolean canMoveDown() {
        return zeroI > 0 && zeroI < N;
    }

    /**
     * Получаем новое состояние движением вниз
     *
     * @return полученное состояние
     */
    State moveDown() {
        if (canMoveDown()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI - 1, zeroJ);
            return new State(result, zeroI - 1, zeroJ);
        }
        return null;
    }

    /**
     * Меняем местами два элемента в матрице состояния (имитируем передвижение)
     *
     * @param matrix матрица состояния
     * @param i1     строка 1-го элемента
     * @param j1     столбец 1-го элемента
     * @param i2     строка 2-го элемента
     * @param j2     столбец 2-го элемента
     */
    private void swap(int[][] matrix, int i1, int j1, int i2, int j2) {
        int temp = matrix[i1][j1];
        matrix[i1][j1] = matrix[i2][j2];
        matrix[i2][j2] = temp;
    }

    /**
     * Создаём копию матрицы текущего состояния
     *
     * @return новую матрицу
     */
    private int[][] copyMatrix() {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++) {
            result[i] = Arrays.copyOf(matrix[i], N);
        }
        return result;
    }

    /**
     * Поиск строки с нулевым элементом (пустой клеткой)
     *
     * @return индекс строки
     */
    private int searchZeroI() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Поиск столбца с нулевым элементом (пустой клеткой)
     *
     * @return индекс столбца
     */
    private int searchZeroJ() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == 0) {
                    return j;
                }
            }
        }
        return -1;
    }
}