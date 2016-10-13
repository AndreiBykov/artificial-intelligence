package BlindSearch;

import java.io.*;
import java.util.*;


class NodeBidirectional extends Node {

    private static final LinkedList<NodeBidirectional> QUEUE_FROM_START = new LinkedList<>();
    private static final LinkedList<NodeBidirectional> QUEUE_FROM_TARGET = new LinkedList<>();
    private static final HashSet<State> STATES_FROM_START = new HashSet<>();
    private static final HashSet<State> STATES_FROM_TARGET = new HashSet<>();
    private static final Map<Character, Character> ACTIONS = new HashMap<Character, Character>() {
        {
            put('U', 'D');
            put('R', 'L');
            put('D', 'U');
            put('L', 'R');
        }
    };

    private NodeBidirectional(int depth, Node parent, int cost, char action, State state) {
        super(depth, parent, cost, action, state);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        createFirstNode();
        createTargetNode();
        System.out.println("-----------------------------------------------");
        while (nextStep()) {
            if (!flag) {
                flag = "run".equalsIgnoreCase(reader.readLine());
            }
        }
    }

    /**
     * Создание начальной вершины
     */
    private static void createFirstNode() {
        NodeBidirectional node = new NodeBidirectional(0, null, 0, '0', State.FIRST);
        STATES_FROM_START.add(State.FIRST);
        QUEUE_FROM_START.push(node);
        System.out.println("Создана начальная вершина:");
        System.out.println(node.state);
    }

    /**
     * Создание целевой вершины
     */
    private static void createTargetNode() {
        NodeBidirectional node = new NodeBidirectional(0, null, 0, '0', State.TARGET);
        STATES_FROM_TARGET.add(State.TARGET);
        QUEUE_FROM_TARGET.push(node);
        System.out.println("Создана целевая вершина:");
        System.out.println(node.state);
    }

    /**
     * Поиск вершины с нужным состоянием в очереди
     *
     * @param queue очередь, в которой осуществляется поиск
     * @param state состояние, которое должно быть у нужной вершины
     * @return нужную вершину
     */
    private static NodeBidirectional findNodeOnState(LinkedList<NodeBidirectional> queue, State state) {
        for (NodeBidirectional node : queue) {
            if (node.state.equals(state)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Поиск пути решения и запись в поток
     *
     * @param writer поток для записи пути
     * @throws IOException
     */
    @Override
    public void backtracking(Writer writer) throws IOException {
        NodeBidirectional nodeFromStart;
        NodeBidirectional nodeFromTarget;
        if (countSteps % 2 == 1) {
            nodeFromStart = this;
            nodeFromTarget = findNodeOnState(QUEUE_FROM_TARGET, state);
        } else {
            nodeFromTarget = this;
            nodeFromStart = findNodeOnState(QUEUE_FROM_START, state);
        }
        LinkedList<Node> listFromStart = new LinkedList<>();
        listFromStart.add(nodeFromStart);
        while (listFromStart.getFirst().parent != null) {
            listFromStart.addFirst(listFromStart.getFirst().parent);
        }
        LinkedList<Node> listToTarget = new LinkedList<>();
        listToTarget.add(nodeFromTarget);
        while (listToTarget.getLast().parent != null) {
            listToTarget.addLast(listToTarget.getLast().parent);
        }
        if (nodeFromStart != null && nodeFromTarget != null) {
            writer.write("Длина пути: " + (nodeFromStart.depth + nodeFromTarget.depth) + "\r\n");
        }
        writer.write("Решиение путём двунаправленного поиска:\r\n");
        for (Node node : listFromStart) {
            writer.write(node.action);
        }
        for (Node node : listToTarget) {
            if (node.action != '0') {
                writer.write(ACTIONS.get(node.action));
            }
        }
        writer.close();
    }

    /**
     * Проверяет вершину на достижение конечного состояния
     * и затем раскрывает её
     *
     * @return {@code true} если мы достигли конца (нашли решение или закончили, не найдя его)
     */
    @Override
    public boolean expand() {
        System.out.println("Текущая вершина:");
        System.out.println(state);
        System.out.println("Глубина: " + depth + "; Стоимость: " + cost + "; Действие: " + action + ";");
        LinkedList<NodeBidirectional> queue;
        LinkedList<NodeBidirectional> queueOther;
        HashSet<State> states;
        HashSet<State> statesOther;
        if (countSteps % 2 == 1) {
            System.out.println("Принадлежит дереву от начальной вершины");
            queue = QUEUE_FROM_START;
            states = STATES_FROM_START;
            queueOther = QUEUE_FROM_TARGET;
            statesOther = STATES_FROM_TARGET;

        } else {
            System.out.println("Принадлежит дереву от целевой вершины");
            queue = QUEUE_FROM_TARGET;
            states = STATES_FROM_TARGET;
            queueOther = QUEUE_FROM_START;
            statesOther = STATES_FROM_START;
        }
        if (statesOther.contains(state)) {
            System.out.println("Поиск закончен");
            System.out.println("Количество созданных вершин: " + countNodes);
            System.out.println("Количество пройденных шагов:  " + countSteps);

            try {
                backtracking(new PrintWriter(System.out));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } else {
            State tmp;

            if (state.canMoveUp()) {
                tmp = state.moveUp();
                if (states.add(tmp)) {
                    queue.addLast(new NodeBidirectional(depth + 1, this, cost + 1, 'U', tmp));
                    System.out.println("Создана вершина действием U:");
                    System.out.println(tmp);
                }
            }

            if (state.canMoveRight()) {
                tmp = state.moveRight();
                if (states.add(tmp)) {
                    queue.addLast(new NodeBidirectional(depth + 1, this, cost + 1, 'R', tmp));
                    System.out.println("Создана вершина действием R:");
                    System.out.println(tmp);
                }
            }

            if (state.canMoveDown()) {
                tmp = state.moveDown();
                if (states.add(tmp)) {
                    queue.addLast(new NodeBidirectional(depth + 1, this, cost + 1, 'D', tmp));
                    System.out.println("Создана вершина действием D:");
                    System.out.println(tmp);
                }
            }

            if (state.canMoveLeft()) {
                tmp = state.moveLeft();
                if (states.add(tmp)) {
                    queue.addLast(new NodeBidirectional(depth + 1, this, cost + 1, 'L', tmp));
                    System.out.println("Создана вершина действием L:");
                    System.out.println(tmp);
                }
            }

            System.out.println("Всего в очереди вершин, ожидающих раскрытия: " + (queue.size() + queueOther.size()));
            System.out.println("-----------------------------------------------");
            return false;
        }
    }

    /**
     * Делаем следующий шаг
     *
     * @return {@code true} если ещё есть шаги для поиска решения
     */
    static boolean nextStep() {
        if (countSteps % 2 == 0) {
            if (!QUEUE_FROM_START.isEmpty()) {
                countSteps++;
                return !QUEUE_FROM_START.pop().expand();
            }
        } else {
            if (!QUEUE_FROM_TARGET.isEmpty()) {
                countSteps++;
                return !QUEUE_FROM_TARGET.pop().expand();
            }
        }
        System.out.println("Решение не найдено.");
        return false;
    }
}