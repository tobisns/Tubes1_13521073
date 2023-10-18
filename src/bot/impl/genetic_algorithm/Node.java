package bot.impl.genetic_algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

    private int value;
    private Node parent;
    private Map<Integer, Node> children;
    private List<Node> leafNodes; // only for tree root
    private int fitness;

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    private String chromosome;

    public Node() {
        this.children = new HashMap<>();
    }

    public Node(Node parent) {
        this.children = new HashMap<>();
        this.parent = parent;
    }

    public Node(int value) {
        this.children = new HashMap<>();
        this.value = value;
    }

    public Node(int value, Node parent) {
        this.children = new HashMap<>();
        this.value = value;
        this.parent = parent;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getFitness() {
        return this.fitness;
    }

    public List<Node> getLeafNodes() {
        return leafNodes;
    }

    public void addLeaf(Node node) {
        this.leafNodes.add(node);
    }

    public void initLeafNodes() {
        this.leafNodes = new ArrayList<>();
    }

    public void addChild(Integer key, Node node) {
        children.put(key, node);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Map<Integer, Node> getChildren() {
        return children;
    }

    public Node getChild(int index) {
        if (!children.containsKey(index)) {
            throw new RuntimeException("Error, key does not exist in children");
        }
        return children.get(index);
    }

    public boolean hasChild(int index) {
        return children.containsKey(index);
    }

    public void setChildren(Map<Integer, Node> children) {
        this.children = children;
    }
}
