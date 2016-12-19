package BlindSearch;

import java.io.*;

public abstract class Node {
    public static int countSteps;
    public static int countNodes;

    public final int depth;
    public final Node parent;
    public final int cost;
    public final char action;
    public final State state;

    public Node(int depth, Node parent, int cost, char action, State state) {
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.action = action;
        this.state = state;
        countNodes++;
    }

    public abstract void backtracking(PrintStream writer) throws IOException;

    public abstract boolean expand();
}

