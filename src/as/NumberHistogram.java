package as;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public final class NumberHistogram {

    private final int[] _amount;
    private int _size = 0;

    public NumberHistogram() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public NumberHistogram(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9) {
        _amount = new int[]{a0, a1, a2, a3, a4, a5, a6, a7, a8, a9};
        updateSize();
    }

    private BigInteger _factorial(int nr) {
        BigInteger sum = new BigInteger("1");
        for (int i = nr; i > 1; i--) {
            sum = sum.multiply(new BigInteger(Integer.toString(i)));
        }

        return sum;
    }

    // Get the count of numbers with this a10-signature.
    public BigInteger numberCount() {
        BigDecimal count = new BigDecimal(_factorial(_size));

        for (int i = 0; i < 10; i++) {
            if (_amount[i] > 1) {
                count = count.divide(new BigDecimal(_factorial(_amount[i])), MathContext.DECIMAL32);
            }
        }

        // Remove all possible numbers that would start by a zero.
        if (_amount[0] > 0) {
            double t = (1.0 - (double) _amount[0] / (double) _size);
            count = count.multiply(new BigDecimal(t), MathContext.DECIMAL32);
        }

        return count.round(MathContext.DECIMAL32).toBigInteger();
    }

    public void updateSize() {
        _size = 0;

        for (int i = 0; i < 10; i++) {
            _size += _amount[i];
        }
    }

    public void setDigitAmount(int digit, int amount) {
        _amount[digit] = amount;
    }

    public void addDigit(int digit) {
        _amount[digit]++;
        _size++;
    }

    public int getSize() {
        return _size;
    }

    public int h() {
        int h = 0;
        for (int i = 1; i < 10; i++) {
            h += _amount[i] * DigitSquares.squares[i];
        }

        return h;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < _amount.length; i++) {
            sb.append(_amount[i]).append("|");
        }

        return sb.toString();
    }

    public boolean hasNext() {
        return (_amount[9] != _size);
    }

    public void next() {
        if (_amount[0] == _size - 1) {
            // The number consists of zeros and only one non-zero number.
            _amount[0]--;
            for (int i = 1; i < 10; i++) {
                if (_amount[i] > 0) {
                    _amount[i]++;
                    break;
                }
            }
            return;
        }

        if (_amount[9] == _size - 1) {
            // The number consists of nines and only one non-zero number.
            if (_amount[0] == 1) {
                // So we have a number like 9099..99. That would become 9999..99, which means all digits are 9.
                _amount[0]--;
                _amount[9]++;
                return;
            }

            _amount[9] = 0;
            _amount[0] = _size - 1;
            for (int i = 1; i < 9; i++) {
                if (_amount[i] > 0) {
                    _amount[i] = 0;
                    _amount[i + 1] = 1;
                    break;
                }
            }
            return;
        }

        if (_amount[9] > 0) {
            int t = _amount[9];
            _amount[9] = 0;
            int f1 = _firstNonZeroFromRight(8);
            if (f1 == 0) {
                _amount[f1]--;
                _amount[9] = t + 1;
                return;
            }
            if (_amount[f1] > 1) {
                _amount[f1]--;
                _amount[f1 + 1] += t + 1;
                return;
            }
            int f2 = _firstNonZeroFromRight(f1 - 1);
            if (f2 > 0) {
                _amount[f1]--;
                _amount[f1 + 1] += t + 1;
            } else {
                _amount[0]--;
                _amount[f1] += t + 1;
            }
            return;
        }

        ////////////////////////
        // Scan from the right for the first non-zero.
        int rightNonZero = 0;
        for (int i = 8; i > 0; i--) {
            if (_amount[i] > 0) {
                rightNonZero = i;
                break;
            }
        }

        if (_amount[rightNonZero] == 1) {
            _amount[rightNonZero]--;
            _amount[rightNonZero + 1]++;
            return;
        }
        _amount[rightNonZero]--;
        _amount[rightNonZero + 1]++;

    }

    private int _firstNonZeroFromRight(int start) {
        for (int i = start; i >= 0; i--) {
            if (_amount[i] > 0) {
                return i;
            }
        }

        return -1;
    }
}
