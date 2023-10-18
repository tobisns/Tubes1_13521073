package bot.impl.genetic_algorithm;

import java.util.Random;

public class GeneticOperator {

    public Node formTree(String chromosome) {
        Node parent = new Node(), current = parent;
        for (int i = 0; i < chromosome.length(); ++i) {
            Node next = new Node();
            current.addChild(Character.getNumericValue(chromosome.charAt(i)), next);
            next.setParent(current);
            current = next;
        }
        parent.initLeafNodes();
        current.setChromosome(chromosome);
        parent.addLeaf(current);
        return parent;
    }

    public Node mergeTree(String chromosome, Node treeRoot) {
        Node current = treeRoot;
        for (int i = 0; i < chromosome.length(); ++i) {
            int index = Character.getNumericValue(chromosome.charAt(i));
            if (!current.hasChild(index)) {
                Node child = new Node();
                child.setParent(current);
                current.addChild(index, child);
            }
            current = current.getChild(index);
        }
        current.setChromosome(chromosome);
        treeRoot.addLeaf(current);
        return treeRoot;
    }

    public int getElevationCount(Node leaf) {
        Node current = leaf;
        int peak = 0;
        while (current.getParent() != null && leaf.getValue() == current.getValue()) {
            peak += 1;
            current = current.getParent();
        }
        return peak;
    }

    public String[] crossover(String parent1, String parent2, int point) {
        if (parent1.length() != parent2.length()) {
            throw new RuntimeException("Error, crossover point is larger than parent length");
        }
        if (point > parent1.length()) {
            throw new RuntimeException("Error, crossover point is larger than parent length");
        }
        String[] arr = new String[4];
        StringBuilder child1 = new StringBuilder(), child2 = new StringBuilder();
        for (int i = 0; i < point; ++i) {
            child1.append(parent1.charAt(i));
            child2.append(parent2.charAt(i));
        }
        for (int i = point; i < parent1.length(); ++i) {
            child1.append(parent2.charAt(i));
            child2.append(parent1.charAt(i));
        }
        arr[0] = child1.toString();
        arr[1] = child2.toString();

        // mutate
        if (mutate()) {
            int mutationPoint = getRandomPoint(parent1.length());
            child1.setCharAt(mutationPoint, child2.charAt(mutationPoint));
            arr[2] = child1.toString();

            mutationPoint = getRandomPoint(parent1.length());
            child2.setCharAt(mutationPoint, child1.charAt(mutationPoint));
            arr[3] = child2.toString();
        }

        return arr;
    }

    public int getRandomPoint(int length) {
        Random random = new Random();
        return random.nextInt(length);
    }

    private boolean mutate() {
        Random random = new Random();
        double probability = random.nextDouble();

        return probability <= 0.03; // 3% mutation probability
    }

    public String generateRandomChromosome(int length) {
        StringBuilder res = new StringBuilder();
        for (int i = length; i > 1; --i) {
            res.append((char) ('0' + getRandomPoint(i)));
        }
        return res.toString();
    }

}
