package tasks;
import animals.*;
import java.util.Random;

public class Zoo {
    private Animal[] animals = new Animal[100];
    private Random rand = new Random();

    public Zoo() {
        fillZoo();
    }

    private void fillZoo() {
        for (int i = 0; i < animals.length; i++) {
            int type = rand.nextInt(3); // 0, 1 lub 2
            switch (type) {
                case 0 -> animals[i] = new Dog("Dog_" + i);
                case 1 -> animals[i] = new Parrot("Parrot_" + i);
                case 2 -> animals[i] = new Snake("Snake_" + i);
            }
        }
    }

    public int getTotalLegs() {
        int sum = 0;
        for (Animal a : animals) {
            sum += a.getLegs();
        }
        return sum;
    }

    public void printZoo() {
        for (Animal a : animals) {
            System.out.println(a.getDescription());
        }
    }

    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.printZoo();
        System.out.println("\nŁączna liczba nóg: " + zoo.getTotalLegs());
    }
}
