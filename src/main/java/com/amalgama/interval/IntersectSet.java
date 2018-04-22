package com.amalgama.interval;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.util.*;

/**
 * Creates a tree of intersecting intervals from the list of received intervals.
 * </br> 1. [-INFINITY; 1] U [2; +INFINITY)
 * </br> 2. [0; 3] U [5; +INFINITY]
 * <pre>
 * {@code
 *         List<RangeSet<Double>> listRS = new ArrayList<>();
 *         RangeSet<Double> rs = TreeRangeSet.create();
 *         rs.add(Range.atMost(1d));
 *         rs.add(Range.atLeast(2d));
 *         listRS.add(rs);
 *         rs = TreeRangeSet.create();
 *         rs.add(Range.closed(0d, 3d));
 *         rs.add(Range.atLeast(5d));
 *         listRS.add(rs);
 *         IntersectSet intersectSet2 = new IntersectSet(listRS);
 *         System.out.println(intersectSet2);
 *
 *         output..
 *         * IntersectSet{[[0.0..1.0], [2.0..3.0], [5.0..+∞)]
 *  }
 *  </pre>
 *
 */
public class IntersectSet {

    /**
     * Tree of Intervals
     */
    private TreeSet<Range<Double>> treeSetRange;

    /**
     * Create, initialization and filling of the interval tree
     * @param listRangeSet List of RangeSet
     *
     */
    public IntersectSet(List<RangeSet<Double>> listRangeSet) {
        treeSetRange = new TreeSet<>(new RangeComparator<>());
        unionAllSets(listRangeSet);
    }

    /**
     * This method returns list of the intersection range.
     *
     * @return List of the intersection range.
     */
    public List<Range<Double>> getSubSets() {
        return new ArrayList<>(treeSetRange);
    }

    /**
     * This method returns value of the nearest interval point.
     *
     * @param point Point value for search.
     * @return value of the nearest interval point, null if there are no intersections of sets.
     */
    public Double findClosest(Double point) {
        return containsPoint(point);
    }

    private Double containsPoint(Double p) {
        if (treeSetRange.isEmpty()) {
            return null;
        }
        Range<Double> pp = Range.singleton(p);
        Range<Double> higher = treeSetRange.ceiling(pp);
        Range<Double> lower = treeSetRange.floor(pp);
        if (higher == null) {
            if (lower != null) {
                return lower.upperEndpoint();
            }
        } else if (lower == null) {
            return higher.lowerEndpoint();

        } else if (higher.contains(p) || lower.contains(p)) {
            return p;
        }
        double lowerP = Math.abs(Objects.requireNonNull(lower).upperEndpoint() - p);
        double higherP = Math.abs(Objects.requireNonNull(higher).lowerEndpoint() - p);
        return lowerP < higherP ? lower.upperEndpoint() : higher.lowerEndpoint();
    }

    private void unionAllSets(List<RangeSet<Double>> list) {
        RangeSet<Double> tmp = TreeRangeSet.create();
        RangeSet<Double> listRang = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            Set<Range<Double>> ranges = list.get(i).asRanges();

            for (Range<Double> r : ranges) {
                RangeSet<Double> intersR = listRang.subRangeSet(r);
                tmp.addAll(intersR);
            }
            listRang = tmp;
            tmp = TreeRangeSet.create();
        }
        treeSetRange.addAll(listRang.asRanges());
    }

    /**
     * This method just for tests.
     */
    void add(Range<Double> range) {
        treeSetRange.add(range);
    }

    @Override
    public String toString() {
        return "IntersectSet{" +
                treeSetRange +
                '}';
    }

}
