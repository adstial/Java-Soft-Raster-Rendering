package code.dependence.json.ast;

import java.util.ArrayList;

public class Node {
    private NodeType type;
    private Object value;
    private ArrayList<Node> children;
    private Node parent;

    public Node(NodeType type, Object value, Node parent) {
        this.type = type;
        this.value = value;
        this.parent = parent;
    }

    public NodeType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Node getParent() {
        return parent;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void insert(Node child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }







}
