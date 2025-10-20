public
class CodingBat {

    // Zadanie z CodingBat: nearHundred
    public boolean nearHundred(int n) {
        // zwraca true, jeśli n jest w odległości 10 od 100 lub 200
        return (Math.abs(100 - n) <= 10 || Math.abs(200 - n) <= 10);
    }

    // Możesz dodawać kolejne metody z CodingBat tutaj
    // np. public boolean sleepIn(boolean weekday, boolean vacation) { ... }

    public static void main(String[] args) {
        CodingBat cb = new CodingBat();

        // Testowanie nearHundred
        System.out.println("Test nearHundred(93): " + cb.nearHundred(93)); // true
        System.out.println("Test nearHundred(89): " + cb.nearHundred(89)); // false
        System.out.println("Test nearHundred(210): " + cb.nearHundred(210)); // true
        System.out.println("Test nearHundred(150): " + cb.nearHundred(150)); // false

        // Tutaj możesz dodać wywołania testowe dla innych metod
    }
}