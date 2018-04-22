package com.amalgama.interval;

import com.google.common.collect.Range;

import java.util.Comparator;

class RangeComparator<T extends Comparable<T>> implements Comparator<Range<T>> {

    @Override
    public int compare(Range<T> first, Range<T> second) {
        int comparatorResult = first.lowerEndpoint().compareTo(second.lowerEndpoint());
        return comparatorResult == 0 ? first.upperEndpoint().compareTo(second.upperEndpoint()) : comparatorResult;
    }
}

