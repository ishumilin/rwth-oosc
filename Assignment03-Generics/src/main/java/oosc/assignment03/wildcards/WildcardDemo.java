package oosc.assignment03.wildcards;

import java.util.ArrayList;
import java.util.List;

public class WildcardDemo {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<Integer>();
        ints.add(1);
        ints.add(2);
        System.out.println("Sum: " + sumOfList(ints));
    }

    public static double sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }
}