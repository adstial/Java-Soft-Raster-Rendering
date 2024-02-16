package code.dependence.utils;

import java.util.Random;

public class RandomUniqueNumber {
    private long[] numbers;
    private int index;
    private int size;
    public RandomUniqueNumber(int size) {
        build(size);
    }

    private void build(int size) {
        this.size = size;
        numbers = new long[size];
        index = 0;

        var random = new Random();
        var _index = 0;
        while (_index < size) {
            var number = random.nextLong();
            if (hasContain(number)) {
                numbers[_index] = number;
                _index++;
            }
        }
    }

    public long getInt() {
        if (index < numbers.length) {
            var res = numbers[index];
            index++;
            return res;
        } else {
            build(size);
            var res = numbers[index];
            index++;
            return res;
        }
    }

    private boolean hasContain(long number) {
        for (int i = 0; i < index; i++) {
            if (numbers[i] == number) return false;
        }
        return true;
    }

}
