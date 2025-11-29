package org.firstinspires.ftc.teamcode.util.structs;

import java.util.*;

public class ComparableCircularList<T extends Comparable<T>> {
    private final List<T> list;

    private final T def;
    private int curr = 0;

    public ComparableCircularList(int size,T def) {
        if(def==null) {
            throw new IllegalArgumentException("Default value cannot be null");
        }
        this.def = def;
        list = new ArrayList<T>();
        for(int i = 0; i<size; i++) {
            list.add(def);
        }
    }

    public void set(T item) {
        list.set(curr, item);
        curr++;
        if(curr==list.size()) {
            curr = 0;
        }
    }

    public void set(T item, int index) {
        list.set(index,item);
    }

    public T get() {
        T ans = list.get(curr);
        curr++;
        if(curr==list.size()) {
            curr = 0;
        }
        return ans;
    }

    public T get(int index) {
        return list.get(index);
    }

    public void reset() {
        for(int i = 0; i<list.size(); i++) {
            list.set(i,def);
        }
    }

    public boolean getTotalComparison() {
        double tot = 0;
        double change = 0;
        for(int i = 0; i<list.size(); i++) {
            T check = list.get(i);
            if(check==null || check.compareTo(def)==0) {
                change++;
            } else if(check.compareTo(def)>0) {
                tot++;
            }
        }
        return tot>(list.size()-change)/2;
    }
}