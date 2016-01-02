/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.generator.java.time;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.pholser.junit.quickcheck.internal.Reflection.defaultValueOf;

/**
 * Produces values of type {@link LocalDateTime}.
 */
public class LocalDateTimeGenerator extends Generator<LocalDateTime> {
    private LocalDateTime min = LocalDateTime.MIN;
    private LocalDateTime max = LocalDateTime.MAX;

    public LocalDateTimeGenerator() {
        super(LocalDateTime.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified
     * {@linkplain InRange#min() minimum} and/or {@linkplain InRange#max()
     * maximum}, inclusive, with uniform distribution, down to the nanosecond.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use
     * dates with values of either {@link LocalDateTime#MIN} or {@link LocalDateTime#MAX}
     * as appropriate.</p>
     *
     * <p>{@link InRange#format()} describes
     * {@linkplain DateTimeFormatter#ofPattern(String) how the generator is to
     * interpret the range's endpoints}.</p>
     *
     * @param range annotation that gives the range's constraints
     * @throws IllegalArgumentException if the range's values cannot be
     * converted to {@code LocalDateTime}
     */
    public void configure(InRange range) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(range.format());

        try {
            if (!defaultValueOf(InRange.class, "min").equals(range.min()))
                min = LocalDateTime.parse(range.min(), formatter);
            if (!defaultValueOf(InRange.class, "max").equals(range.max()))
                max = LocalDateTime.parse(range.max(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e);
        }

        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException(String.format("bad range, %s > %s", range.min(), range.max()));
    }

    @Override
    public LocalDateTime generate(SourceOfRandomness random, GenerationStatus status) {
        long epochDay = random.nextLong(min.toLocalDate().toEpochDay(), max.toLocalDate().toEpochDay());
        long nanoOfDay = random.nextLong(min.toLocalTime().toNanoOfDay(), max.toLocalTime().toNanoOfDay());

        return LocalDateTime.of(LocalDate.ofEpochDay(epochDay), LocalTime.ofNanoOfDay(nanoOfDay));
    }
}