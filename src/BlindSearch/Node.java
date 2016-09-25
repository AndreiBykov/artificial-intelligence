package BlindSearch;

import java.io.*;

public abstract class Node {
    static int countSteps;
    static int countNodes;

    protected final int depth;
    protected final Node parent;
    protected final int cost;
    protected final char action;
    protected final State state;

    Node(int depth, Node parent, int cost, char action, State state) {
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.action = action;
        this.state = state;
        countNodes++;
    }

    abstract void backtracking(Writer writer) throws IOException;

    abstract boolean expand();
}

