import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Lotto2 {
    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Musi być 6 cyfr");
            return;
        }

        ArrayList<Integer> typy = new ArrayList<>();
        for (String arg : args) {
            try {
                int liczba = Integer.parseInt(arg);

                // sprawdzamy, czy liczba mieści się w zakresie 1–49
                if (liczba < 1 || liczba > 49) {
                    System.out.println("Błąd: liczby muszą być w zakresie 1–49.");
                    return;
                }

                if (typy.contains(liczba)) {
                    System.out.println("Błąd: liczby nie mogą się powtarzać.");
                    return;
                }

                typy.add(liczba);
            } catch (NumberFormatException e) {
                System.out.println("Błąd: podaj tylko liczby całkowite.");
                return;
            }
        }

        ArrayList<Integer> wyniki = new ArrayList<>();
        Random losuj = new Random();

        while (wyniki.size() < 6) {
            int liczba = losuj.nextInt(49) + 1;
            if (!wyniki.contains(liczba)) {
                wyniki.add(liczba);
            }
        }

        Collections.sort(typy);
        Collections.sort(wyniki);

        int trafienia = 0;
        for (int liczba : typy) {
            if (wyniki.contains(liczba)) {
                trafienia++;
            }
        }

        System.out.println("Twoje typy:        " + typy);
        System.out.println("Wylosowane liczby: " + wyniki);
        System.out.println("Liczba trafień:    " + trafienia);
    }
}
