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

        A10 a = A10Converter.convert(nr);

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
                String a10 = A10Converter.convert(k).toString();
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

    public static int combinatorics(int n, int k) {
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
            sum += combinatorics(10 - i + (n - 1), n - 1);
        }

        return sum;
    }

    public static void main(String[] args) {
        /**
         * See
         * http://math.stackexchange.com/questions/159210/non-decreasing-digits
         */


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

            A10 a = A10Converter.convert(da);
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

        



        System.exit(0);

        ArrayList<Integer> positions = new ArrayList<>();

        int[] happyNumbers = {1, 7, 10, 13, 19, 23, 28, 31, 32, 44, 49, 68, 70, 79, 82, 86, 91, 94, 97, 100, 103, 109, 129, 130, 133, 139, 167, 176, 188, 190, 192, 193, 203, 208, 219, 226, 230, 236, 239, 262, 263, 280, 291, 293, 301, 302, 310, 313, 319, 320, 326, 329, 331, 338, 356, 362, 365, 367, 368, 376, 379, 383, 386, 391, 392, 397, 404, 409, 440, 446, 464, 469, 478, 487, 490, 496, 536, 556, 563, 565, 566, 608, 617, 622, 623, 632, 635, 637, 638, 644, 649, 653, 655, 656, 665, 671, 673, 680, 683, 694, 700, 709, 716, 736, 739, 748, 761, 763, 784, 790, 793, 802, 806, 818, 820, 833, 836, 847, 860, 863, 874, 881, 888, 899, 901, 904, 907, 910, 912, 913, 921, 923, 931, 932, 937, 940, 946, 964, 970, 973, 989, 998};
        ArrayList<String> a10HappyNumbers = new ArrayList<>();
        for (int happyNumber : happyNumbers) {
            String a10 = A10Converter.convert(happyNumber).toString();
            if (!a10HappyNumbers.contains(a10)) {
                a10HappyNumbers.add(a10);
            }
        }

        positions.add(0);

        HashMap<Integer, Integer> happyCount = new HashMap<>();

        int idx = 0;
        int i = 0;
        while (i < 0) {
            A10 a10 = A10Converter.convert(positions);
            String a10String = a10.toString();
            if (a10HappyNumbers.contains(a10String)) {
                //System.out.println(getPositionsString(positions) + " => " + a10.toString() + " :)");
                happyCount.put(a10.getSize(), happyCount.getOrDefault(a10.getSize(), 0) + 1);
            } else {
                A10 a10H = A10Converter.convert(a10.h());
                if (a10HappyNumbers.contains(a10H.toString())) {
                    a10HappyNumbers.add(a10String);
                    //System.out.println(getPositionsString(positions) + " => " + a10.toString() + " ->:)");
                    happyCount.put(a10.getSize(), happyCount.getOrDefault(a10.getSize(), 0) + 1);
                }
            }

            positions.set(idx, positions.get(idx) + 1);
            if (positions.get(idx) == 10) {
                positions.set(idx, 0);
                if (idx > 0) {
                    int b = 1;
                    while (idx - b >= 0 && positions.get(idx - b) < 10) {
                        if (positions.get(idx - b) == 9) {
                            positions.set(idx - b, 0);
                            if (idx - b == 0) {
                                positions.set(idx - b, 1);
                                positions.add(0);
                                idx++;
                                break;
                            }
                            b++;
                        } else {
                            positions.set(idx - b, positions.get(idx - b) + 1);
                            break;
                        }
                    }
                } else {
                    positions.set(0, 1);
                    positions.add(0);
                    idx++;
                }
            }

            i++;
        }

        Iterator<Map.Entry<Integer, Integer>> iterator = happyCount.entrySet().iterator();
        int sum = 0;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            sum += entry.getValue();
            System.out.println("Numbers of digits up to length " + entry.getKey() + " contain " + sum + " happy numbers");

        }
    }

    
}
