import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Lotto3 {
    public static void main(String[] args) {
        ArrayList<Integer> typy = new ArrayList<>();
        Collections.addAll(typy, 1, 37, 3, 15, 5, 26);
        Collections.sort(typy);

        long liczbaLosowan = 0;

        long start = System.currentTimeMillis();

        Random losuj = new Random();
        ArrayList<Integer> wyniki = new ArrayList<>();

        while (true) {
            wyniki.clear();

            while (wyniki.size() < 6) {
                int liczba = losuj.nextInt(49) + 1;
                if (!wyniki.contains(liczba)) {
                    wyniki.add(liczba);
                }
            }

            Collections.sort(wyniki);
            liczbaLosowan++;

            if (wyniki.equals(typy)) {
                break;
            }

        }

        long koniec = System.currentTimeMillis();
        double sekundy = (koniec - start) / 1000.0;

        System.out.println("Twoje typy:        " + typy);
        System.out.println("Wylosowane liczby: " + wyniki);
        System.out.println("Trafienie 6/6 po:  " + liczbaLosowan + " losowaniach!");
        System.out.println("Czas działania:    " + sekundy + " sekund");
    }
}
