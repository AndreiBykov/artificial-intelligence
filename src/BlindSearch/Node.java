package BlindSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Node {
    static final LinkedList<Node> QUEUE = new LinkedList<>();

    static int countSteps;
    static int countNodes;

    private final int depth;
    private final Node parent;
    private int cost;
    private final char action;
    private final State state;

    Node(int depth, Node parent, char action, State state) {
        this.depth = depth;
        this.parent = parent;
        this.cost = 0;
        this.action = action;
        this.state = state;
        countNodes++;
    }

    boolean expand() {
        cost = countSteps;
        System.out.println("Текущая вершина:");
        System.out.println(state);
        System.out.println("Глубина: " + depth + "; Стоимость: " + cost + "; Действие: " + action + ";");
        if (state.isTarget()) {
            System.out.println("END");
            System.out.println("Количество созданных вершин: " + countNodes);
            System.out.println("Количество пройденных шагов:  " + countSteps);
            return true;
        } else {
            State tmp = state.moveLeft();
            if (tmp != null) {
                QUEUE.push(new Node(depth + 1, this, 'L', tmp));
                System.out.println("Создана вершина действием L:");
                System.out.println(tmp);
            }
            tmp = state.moveDown();
            if (tmp != null) {
                QUEUE.push(new Node(depth + 1, this, 'D', tmp));
                System.out.println("Создана вершина действием D:");
                System.out.println(tmp);
            }
            tmp = state.moveRight();
            if (tmp != null) {
                QUEUE.push(new Node(depth + 1, this, 'R', tmp));
                System.out.println("Создана вершина действием R:");
                System.out.println(tmp);
            }
            tmp = state.moveUp();
            if (tmp != null) {
                QUEUE.push(new Node(depth + 1, this, 'U', tmp));
                System.out.println("Создана вершина действием U:");
                System.out.println(tmp);
            }
            System.out.println("Всего в очереди вершин, ожидающих раскрытия: " + QUEUE.size());
            System.out.println("-----------------------------------------------");
            return false;
        }
    }

    static boolean nextStep() {
        if (!QUEUE.isEmpty()) {
            countSteps++;
            return QUEUE.pop().expand();
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        QUEUE.push(new Node(1, null, '0', State.createFirstState()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        while (true) {
            if (nextStep()) {
                break;
            }
            if (!flag) {
                flag = "run".equalsIgnoreCase(reader.readLine());
            }
        }
    }
}

class State {
    static final State TARGET = new State(new int[][]{{1, 2, 3}, {8, 0, 4}, {7, 6, 5}});
    static final State FIRST = new State(new int[][]{{6, 0, 8}, {5, 2, 1}, {4, 3, 7}});
    static final HashSet<State> STATES = new HashSet<>();
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

    static State createFirstState() {
        STATES.add(FIRST);
        return FIRST;
    }

    private boolean canMoveRight() {
        if (zeroJ > 0 && zeroJ < N) {
            return true;
        }
        return false;
    }

    State moveRight() {
        if (canMoveRight()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI, zeroJ - 1);
            State state = new State(result, zeroI, zeroJ - 1);
            if (STATES.add(state)) {
                return state;
            }
        }
        return null;
    }

    private boolean canMoveLeft() {
        if (zeroJ >= 0 && zeroJ < N - 1) {
            return true;
        }
        return false;
    }

    State moveLeft() {
        if (canMoveLeft()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI, zeroJ + 1);
            State state = new State(result, zeroI, zeroJ + 1);
            if (STATES.add(state)) {
                return state;
            }
        }
        return null;
    }

    private boolean canMoveUp() {
        if (zeroI >= 0 && zeroI < N - 1) {
            return true;
        }
        return false;
    }

    State moveUp() {
        if (canMoveUp()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI + 1, zeroJ);
            State state = new State(result, zeroI + 1, zeroJ);
            if (STATES.add(state)) {
                return state;
            }
        }
        return null;
    }

    private boolean canMoveDown() {
        if (zeroI > 0 && zeroI < N) {
            return true;
        }
        return false;
    }

    State moveDown() {
        if (canMoveDown()) {
            int[][] result = copyMatrix();
            swap(result, zeroI, zeroJ, zeroI - 1, zeroJ);
            State state = new State(result, zeroI - 1, zeroJ);
            if (STATES.add(state)) {
                return state;
            }
        }
        return null;
    }

    private void swap(int[][] matrix, int i1, int j1, int i2, int j2) {
        int temp = matrix[i1][j1];
        matrix[i1][j1] = matrix[i2][j2];
        matrix[i2][j2] = temp;
    }

    private int[][] copyMatrix() {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

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