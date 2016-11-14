package as;

import java.util.ArrayList;

public class A10Converter {

    public static A10 convert(int nr) {
        A10 a10 = new A10();

        String nrString = Integer.toString(nr);
        for (int i = 0; i < nrString.length(); i++) {
            a10.addDigit(Integer.parseInt(nrString.substring(i, i + 1)));
        }

        return a10;
    }

    public static A10 convert(int[] digitAmounts) throws Exception {
        if (digitAmounts.length != 10) {
            throw new Exception("Not enough digits defined.");
        }

        A10 a10 = new A10();
        for (int i = 0; i < 10; i++) {
            a10.setDigitAmount(i, digitAmounts[i]);
        }
        a10.updateSize();

        return a10;
    }

    public static A10 convert(ArrayList<Integer> positions) {
        A10 a10 = new A10();

        positions.stream().forEach((position) -> {
            a10.addDigit(position);
        });

        return a10;
    }

}
