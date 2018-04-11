package com.backend.challenge.statistic.aggregation;

import com.backend.challenge.statistic.dto.Statistics;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collector implementation to aggregate statistics for the last 60 seconds.
 */
public class StatisticsCollector
        implements Collector<StatisticAggregation, StatisticsCollector.StatAccumulator, Statistics> {

    @Override
    public Supplier<StatAccumulator> supplier() {
        return StatAccumulator::new;
    }

    @Override
    public BiConsumer<StatAccumulator, StatisticAggregation> accumulator() {
        return StatAccumulator::add;
    }

    @Override
    public BinaryOperator<StatAccumulator> combiner() {
        return (acc1, acc2) -> {
            acc1.add(acc2);
            return acc1;
        };
    }

    @Override
    public Function<StatAccumulator, Statistics> finisher() {
        return StatAccumulator::toStatistics ;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    /**
     * Accumulator to hold intermediate results during statistics calculations.
     */
    public static class StatAccumulator implements StatisticsProvider {
        private double sum;
        private double max;
        private double min;
        private long count;

        /**
         * Create {@ling StatAccumulator} instance.
         */
        public StatAccumulator() {
            sum = 0;
            max = Double.MIN_VALUE;
            min = Double.MAX_VALUE;
            count = 0;
        }

        /**
         * Add statistics provider to accumulator.
         *
         * @param provider statistics provider
         */
        public void add(StatisticsProvider provider) {
            sum += provider.getSum();
            max = Math.max(max, provider.getMax());
            min = Math.min(min, provider.getMin());
            count += provider.getCount();
        }

        /**
         * Create final statistics.
         *
         * @return final statistics
         */
        public Statistics toStatistics() {
            if (count == 0) {
                // Empty statistics
                return new Statistics(0, 0, 0,0, 0);
            }
            return new Statistics(sum, sum / count, max, min, count);
        }


        @Override
        public double getSum() {
            return sum;
        }

        @Override
        public double getMax() {
            return max;
        }

        @Override
        public double getMin() {
            return min;
        }

        @Override
        public long getCount() {
            return count;
        }
    }
}
