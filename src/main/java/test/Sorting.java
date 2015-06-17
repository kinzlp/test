package test;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kinz on 12/6/15.
 * Copywrite - Kinz
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10)
public class Sorting {

    @Param({"1000"})
    int size;


    int[] inputs;

    @Setup(Level.Invocation)
    public void prepare() {
        inputs = Distribution.RANDOM.create(size);
    }

    public static void moveRight(int[] arrays, int pos) {
        arrays[pos + 1] = arrays[pos];
    }

    public static void swap(int[] arrays, int x, int y) {
        if (x == y) return;
        arrays[x] = arrays[x] ^ arrays[y];
        arrays[y] = arrays[x] ^ arrays[y];
        arrays[x] = arrays[x] ^ arrays[y];
    }

    @Benchmark
    public void bubbleSort() {
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < inputs.length - 1; i++) {
                if (inputs[i] > inputs[i + 1]) {
                    swap(inputs, i, i + 1);
                    swapped = true;
                }
            }
        } while (swapped);
    }


    @Benchmark
    public void selectionSort() {
        int minIndex;

        for (int i = 0; i < inputs.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < inputs.length; j++) {
                if (inputs[j] < inputs[minIndex])
                    minIndex = j;
            }
            swap(inputs, i, minIndex);
        }

    }

    @Benchmark
    public void insertionSort() {
        for (int i = 1; i < inputs.length; i++) {
            int currentElement = inputs[i];
            for (int j = i - 1; j >= 0; j--) {
                if (inputs[j] > currentElement) {
                    moveRight(inputs, j);
                    if (j == 0)
                        inputs[j] = currentElement;
                } else {
                    inputs[j] = currentElement;
                    break;
                }
            }
        }
    }


    @Benchmark
    public void timSort() {
        // Java7 default is Tim sort
        Arrays.sort(inputs);
    }

    /**
     * **************
     * Merge sort
     *
     * @return ***************
     */
    @Benchmark
    public int[] mergeSort() {
        int[] tmp = new int[inputs.length];
        mergeSort(inputs, tmp, 0, inputs.length - 1);
        inputs = tmp;
        return tmp;
    }

    private void mergeSort(int[] a, int[] tmp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, tmp, left, center);
            mergeSort(a, tmp, center + 1, right);
            merge(a, tmp, left, center + 1, right);
        }
    }

    private static void merge(int[] a, int[] tmp, int left, int right, int rightEnd) {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while (left <= leftEnd && right <= rightEnd)
            tmp[k++] = a[left] < a[right] ? a[left++] : a[right++];

        while (left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while (right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for (int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
    }

    /***********************
     * Merge sort end
     ***********************/


    /**
     * **************************
     * Quick sort start
     * ***************************
     */
    @Benchmark
    public int[] quickSort() {
        quickSort(inputs, 0, inputs.length - 1);
        return inputs;
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low >= high)
            return;

        // pick the pivot
        int middle = low + (high - low) / 2;
        int pivot = arr[middle];

        // make left < pivot and right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }

            while (arr[j] > pivot) {
                j--;
            }

            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        // recursively sort two sub parts
        if (low < j)
            quickSort(arr, low, j);

        if (high > i)
            quickSort(arr, i, high);
    }

    /**
     * ***********************************
     * Radix sort
     * ************************************
     */
    @Benchmark
    public void radixSort() {
        final int RADIX = 10;
        // declare and initialize bucket[]
        List<Integer>[] bucket = new ArrayList[RADIX];
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList<Integer>();
        }

        // sort
        boolean maxLength = false;
        int tmp = -1;
        long placement = 1;
        while (!maxLength) {
            maxLength = true;
            // split input between lists
            for (Integer i : inputs) {
                tmp = (int)(i / placement);
                //System.out.println(placement);
                bucket[tmp % RADIX].add(i);
                if (maxLength && tmp > 0) {
                    maxLength = false;

                }
            }
            // empty lists into input array
            int a = 0;
            for (int b = 0; b < RADIX; b++) {
                for (Integer i : bucket[b]) {
                    inputs[a++] = i;
                }
                bucket[b].clear();
            }
            //move to next digit
            placement *= RADIX;
        }
    }

    public static void main(String[] args) {
        test.Sorting sorter = new test.Sorting();
        sorter.size = 1000;
        sorter.prepare();
        long time = System.nanoTime();
        sorter.bubbleSort();
        time = System.nanoTime() - time;
        System.out.println("Time spec " + time + "ns");


    }
}
