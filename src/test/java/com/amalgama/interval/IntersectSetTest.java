package com.amalgama.interval;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IntersectSetTest {

    private static List<RangeSet<Double>> listRangeSet = new ArrayList<>();

    static {
        RangeSet<Double> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.atMost(1d));
        rangeSet.add(Range.closed(2d, 10d));
        rangeSet.add(Range.atLeast(16d));
        listRangeSet.add(rangeSet);

        rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(-18d, -15d));
        rangeSet.add(Range.closed(-13d, 0d));
        rangeSet.add(Range.openClosed(3d, 20d));
        rangeSet.add(Range.closed(22d, 23d));
        listRangeSet.add(rangeSet);

        rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(-18d, -16d));
        rangeSet.add(Range.closed(-13d, -7d));
        rangeSet.add(Range.closed(-4d, -2d));
        rangeSet.add(Range.closed(1d, 2d));
        rangeSet.add(Range.closed(3d, 5d));
        rangeSet.add(Range.closed(8d, 11d));
        rangeSet.add(Range.closed(14d, 17d));
        rangeSet.add(Range.closed(19d, 23d));
        listRangeSet.add(rangeSet);
    }

    @Test
    void getSubSetsTest() {
        IntersectSet intersectSet = new IntersectSet(listRangeSet);
        System.out.println(intersectSet);
        List<Range<Double>> expectedList = Arrays.asList(Range.closed(-18d, -16d), Range.closed(-13d, -7d),
                Range.closed(-4d, -2d), Range.openClosed(3d, 5d), Range.closed(8d, 10d), Range.closed(16d, 17d),
                Range.closed(19d, 20d), Range.closed(22d, 23d));
        assertEquals(expectedList, intersectSet.getSubSets());
    }

    @Test
    void getClosestTest() {
        IntersectSet intersectSet = new IntersectSet(listRangeSet);
        double actual = intersectSet.findClosest(4.5d);
        assertEquals(4.5d, actual);

        actual = intersectSet.findClosest(3.08d);
        assertEquals(3.08d, actual);

        actual = intersectSet.findClosest(100d);
        assertEquals(23d, actual);

        actual = intersectSet.findClosest(-20d);
        assertEquals(-18d, actual);
    }

    @Test
    void getClosestIfSetIsEmpty() {
        List<RangeSet<Double>> list = new ArrayList<>();
        RangeSet<Double> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(-100d, 1d));
        rangeSet.add(Range.closed(5d, 10d));
        list.add(rangeSet);
        rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(2d, 3d));
        rangeSet.add(Range.closedOpen(4d, 5d));
        list.add(rangeSet);

        IntersectSet intersectSet = new IntersectSet(list);
        assertNull(intersectSet.findClosest(3d));
    }

    @Test
    void timeline() {

        IntersectSet intersectSet = new IntersectSet(listRangeSet);

        for (int i = 0; i < 5_000; i++) {
            Double v = new Random().nextDouble();
            intersectSet.add(Range.closed(v, v + 10));
        }

        double[] dd = new double[500];
        for (int i = 0; i < dd.length; i++) {
            dd[i] = new Random().nextDouble();
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (double aDd : dd) {
            intersectSet.findClosest(aDd);
        }
        stopwatch.stop();
        System.out.println("Time elapsed: " + stopwatch.elapsed(MILLISECONDS));

    }
}