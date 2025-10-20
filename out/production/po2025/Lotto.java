import java.util.Random;
public class Lotto {
	public static void main(String[] args) {
		Random random = new Random();
		int[] lottoNumbers = new int[6];
		int count = 0;
		
		while (count < 6) {
			int number = random.nextInt(49) + 1;
			if (!contains(lottoNumbers, count, number)) {
                lottoNumbers[count] = number;
                count++;
            }
        }
		
		System.out.print("Wylosowane liczby lotto: ");
        for (int num : lottoNumbers) {
            System.out.print(num + " ");
		}
	}
	
	private static boolean contains(int[] array, int count, int value) {
        for (int i = 0; i < count; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
}