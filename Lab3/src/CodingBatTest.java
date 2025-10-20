import static org.junit.Assert.*;

public class CodingBatTest {

    @org.junit.Test
    public void nearHundred() {
        CodingBat cb = new CodingBat();
        CodingBat bb = new CodingBat();
        assertEquals(true, cb.nearHundred(93));
        assertEquals(true, bb.nearHundred(90));
    }

}