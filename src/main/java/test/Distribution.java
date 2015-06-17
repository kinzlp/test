package test;

import java.util.Random;

/**
 * Created by kinz on 12/6/15.
 * Copywrite - Kinz
 */
public enum Distribution {

    SAWTOOTH {
        @Override
        int[] create(int length) {
            int[] result = new int[length];
            for (int i = 0; i < length; i += 5) {
                result[i] = 0;
                result[i + 1] = 1;
                result[i + 2] = 2;
                result[i + 3] = 3;
                result[i + 4] = 4;
            }
            return result;
        }
    },
    INCREASING {
        @Override
        int[] create(int length) {
            int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = i;
            }
            return result;
        }
    },
    DECREASING {
        @Override
        int[] create(int length) {
            int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = length - i;
            }
            return result;
        }
    },
    RANDOM {
        @Override
        int[] create(int length) {
            Random random = new Random();
            int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = Math.abs(random.nextInt());
            }
            return result;
        }
    };

    abstract int[] create(int length);

}
