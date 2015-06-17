package test

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by kinz on 13/6/15.
 * Copywrite - Kinz
 */
class SortingTest extends Specification {

    @Unroll
    def "Sorting by #methodName"() {
        when:
        Sorting sorter = new Sorting()
        sorter.size=100
        sorter.prepare()

        println "Inputs: $sorter.inputs"
        sorter."$methodName"()
        println "Outputs: $sorter.inputs"

        then:
        verifySorted(sorter.inputs)

        where:
        methodName          | dummy
        "bubbleSort"        | ""
        "selectionSort"     | ""
        "insertionSort"     | ""
        "mergeSort"         | ""
        "quickSort"         | ""
        "timSort"           | ""
        "radixSort"         | ""
    }



    private boolean verifySorted(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1])
                return false;
        }
        return true;
    }
}
