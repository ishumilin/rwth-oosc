package oosc.assignment02.inheritance;

public class InheritanceDemo {
    public static void main(String[] args) {
        GeoObject circle = new Circle(10);
        System.out.println("Circle diameter: " + ((Circle) circle).getDiameter());
        System.out.println("Circle color: " + circle.getColor());
    }

    public static class GeoObject {
        private String color = "black";

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static class Ellipse extends GeoObject {
        private int conjugateDiameter;

        public Ellipse(int conjugateDiameter) {
            this.conjugateDiameter = conjugateDiameter;
        }

        public int getConjugateDiameter() {
            return conjugateDiameter;
        }
    }

    public static class Circle extends Ellipse {
        private int diameter;

        public Circle(int diameter) {
            super(diameter);
            this.diameter = diameter;
        }

        public int getDiameter() {
            return diameter;
        }
    }
}