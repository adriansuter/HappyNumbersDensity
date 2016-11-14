package as;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class HappyNumbersDensity {

    public static void checkSection(int nr, TreeMap<Integer, String> uniques) {
        BigInteger numbersCount = new BigInteger("0");

        NumberHistogram a = NumberHistogramFactory.fromInteger(nr);

        String lastLine = "";
        Iterator<Map.Entry<Integer, String>> uniqueIterator = uniques.entrySet().iterator();
        while (uniqueIterator.hasNext()) {
            Map.Entry<Integer, String> unique = uniqueIterator.next();
            if (!unique.getValue().equalsIgnoreCase(a.toString())) {
                System.out.println(lastLine);
                System.out.println(unique.getKey() + " " + unique.getValue() + " " + a.toString() + " " + (unique.getValue().equalsIgnoreCase(a.toString()) ? "==" : "!!"));
            }
            numbersCount = numbersCount.add(a.numberCount());

            lastLine = unique.getKey() + " " + unique.getValue() + " " + a.toString() + " " + (unique.getValue().equalsIgnoreCase(a.toString()) ? "==" : "!!");

            if (a.hasNext()) {
                a.next();
            }
        }

        System.out.println("Numbers Count: " + numbersCount);
    }

    public static void saveUniques(int digits) {
        try {
            TreeMap<Integer, String> uniques = new TreeMap<>();
            int nrStart = (int) Math.pow(10, digits - 1);
            int nrStop = nrStart * 10;
            int reportSeconds = 5;

            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.SECOND, reportSeconds);
            for (int k = nrStart; k < nrStop; k++) {
                String a10 = NumberHistogramFactory.fromInteger(k).toString();
                if (!uniques.containsValue(a10)) {
                    uniques.put(k, a10);
                }

                if (Calendar.getInstance().after(calendar)) {
                    calendar.add(Calendar.SECOND, reportSeconds);
                    System.out.println("Report: " + (k - nrStart + 1) + "/" + (nrStop - nrStart) + " (" + (int) Math.round(100 * (k - nrStart + 1) / (nrStop - nrStart)) + "%) >> " + uniques.size());
                }
            }

            System.out.println("DONE for length " + digits + ": " + uniques.size());

            File file = new File("uniques_" + digits + ".dat");
            file.createNewFile();

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    objectOutputStream.writeObject(uniques);
                    objectOutputStream.flush();
                }
                fileOutputStream.flush();
            }
        } catch (IOException ex) {
        }
    }

    public static TreeMap<Integer, String> loadUniques(int digits) {
        try {
            File file = new File("uniques_" + digits + ".dat");

            TreeMap<Integer, String> uniques;
            try (FileInputStream fileInputStream = new FileInputStream(file); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                uniques = (TreeMap<Integer, String>) objectInputStream.readObject();
            }
            return uniques;
        } catch (FileNotFoundException ex) {
        } catch (IOException | ClassNotFoundException ex) {
        }

        return null;
    }

    public static int combinations(int n, int k) {
        double g = 1.0;

        for (double i = k; i > 0; i--) {
            g *= ((double) n - i + 1.0) / i;
        }

        return (int) Math.round(g);
    }

    public static int countUniques(int n) {
        int sum = 0;

        System.out.println("n: " + n);
        for (int i = 1; i < 10; i++) {
            sum += combinations(10 - i + (n - 1), n - 1);
        }

        return sum;
    }

    public static void main(String[] args) {
        /**
         * See
         * http://math.stackexchange.com/questions/159210/non-decreasing-digits
         */

        HappyNumbersCollection happyNumbersCollection = new HappyNumbersCollection();
        happyNumbersCollection.init();

        System.out.println(happyNumbersCollection.getCount() + " -- " + happyNumbersCollection.getSignaturesCount());

        for (int i = 4; i < 50; i++) {
            NumberHistogram numberHistogram = new NumberHistogram(i - 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
            happyNumbersCollection.addHappy(numberHistogram.toString(), numberHistogram.numberCount());
            while (numberHistogram.hasNext()) {
                numberHistogram.next();

                int mapNumber = numberHistogram.h();
                String mapSignature = NumberHistogramFactory.fromInteger(mapNumber).toString();
                if (happyNumbersCollection.isHappy(mapSignature)) {
                    happyNumbersCollection.addHappy(numberHistogram.toString(), numberHistogram.numberCount());
                }
            }

            System.out.println(i + ": " + happyNumbersCollection.getCount() + " -- " + happyNumbersCollection.getSignaturesCount());
        }

        System.exit(0);

        /*
        saveUniques(4);
        saveUniques(5);
        saveUniques(6);
        saveUniques(7);
         */
        TreeMap<Integer, String> uniques;
        int length = 4;

        uniques = loadUniques(length);
        System.out.println("Length: " + length);
        checkSection((int) Math.pow(10, length - 1), uniques);
        System.out.println("Uniques-Size: " + uniques.size() + " // CountUniques: " + countUniques(length));
        System.out.println("---");

        length = 5;
        uniques = loadUniques(length);
        System.out.println("Length: " + length);
        checkSection((int) Math.pow(10, length - 1), uniques);
        System.out.println("Uniques-Size: " + uniques.size() + " // CountUniques: " + countUniques(length));
        System.out.println("---");

        length = 6;
        uniques = loadUniques(length);
        System.out.println("Length: " + length);
        checkSection((int) Math.pow(10, length - 1), uniques);
        System.out.println("Uniques-Size: " + uniques.size() + " // CountUniques: " + countUniques(length));
        System.out.println("---");

        length = 7;
        uniques = loadUniques(length);
        System.out.println("Length: " + length);
        checkSection((int) Math.pow(10, length - 1), uniques);
        System.out.println("Uniques-Size: " + uniques.size() + " // CountUniques: " + countUniques(length));
        System.out.println("---");

        int[] da = {7, 1, 0, 0, 0, 0, 0, 0, 0, 0};
        try {
            int count = 1;
            BigInteger numbersCount = new BigInteger("0");

            NumberHistogram a = NumberHistogramFactory.fromHistogramArray(da);
            // System.out.println(a.toString() + " => " + a.numberCount());
            numbersCount = numbersCount.add(a.numberCount());

            while (a.hasNext()) {
                a.next();
                //    System.out.println(a.toString() + " => " + a.numberCount());
                numbersCount = numbersCount.add(a.numberCount());

                count++;
            }
            System.out.println("Count: " + count + " // CountUniques: " + countUniques(8));
            System.out.println("Numbers Count: " + numbersCount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
