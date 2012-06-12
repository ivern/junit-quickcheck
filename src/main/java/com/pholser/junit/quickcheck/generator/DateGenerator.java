package com.pholser.junit.quickcheck.generator;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class DateGenerator extends Generator<Date> {
    private final LongGenerator longGenerator = new LongGenerator();

    public DateGenerator() {
        super(Date.class);
    }

    @Override
    public Date generate(SourceOfRandomness random, int size) {
        return new Date(longGenerator.generate(random, size));
    }
}
