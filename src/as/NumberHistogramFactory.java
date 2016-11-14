package as;

import java.util.ArrayList;

public class NumberHistogramFactory {

    public static NumberHistogram fromInteger(int nr) {
        NumberHistogram a10 = new NumberHistogram();

        String nrString = Integer.toString(nr);
        for (int i = 0; i < nrString.length(); i++) {
            a10.addDigit(Integer.parseInt(nrString.substring(i, i + 1)));
        }

        return a10;
    }

    public static NumberHistogram fromHistogramArray(int[] digitAmounts) throws Exception {
        if (digitAmounts.length != 10) {
            throw new Exception("Not enough digits defined.");
        }

        NumberHistogram a10 = new NumberHistogram();
        for (int i = 0; i < 10; i++) {
            a10.setDigitAmount(i, digitAmounts[i]);
        }
        a10.updateSize();

        return a10;
    }

    public static NumberHistogram fromDigits(ArrayList<Integer> positions) {
        NumberHistogram a10 = new NumberHistogram();

        positions.stream().forEach((position) -> {
            a10.addDigit(position);
        });

        return a10;
    }

}
