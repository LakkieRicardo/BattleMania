package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.List;

public abstract class SwearSearchAlgorithm {
    private final String name;
    public static List<SwearSearchAlgorithm> algs = new ArrayList<>();

    public SwearSearchAlgorithm(String name) {
        this.name = name;
        algs.add(this);
    }

    public abstract boolean isSwearWord(String param1String);

    public String getName() {
        return this.name;
    }

    public static SwearSearchAlgorithm getSSA(String name) {
        for (SwearSearchAlgorithm alg : algs) {
            if (alg.getName().equalsIgnoreCase(name)) {
                return alg;
            }
        }
        return null;
    }

    public static void init() {
        new BasicSwearAlgorithm();
    }
}