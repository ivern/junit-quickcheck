package com.pholser.junit.parameters.internal;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GeneratingRandomValuesFromJDKTest {
    private Random random;
    private JDKSourceOfRandomness source;

    @Before
    public void setUp() {
        random = mock(Random.class);
        source = new JDKSourceOfRandomness(random);
    }

    @Test
    public void fetchesNextIntFromAJavaUtilRandom() {
        source.nextInt();

        verify(random).nextInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithBackwardsRange() {
        source.nextInt(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntWithIdenticalMinAndMax() {
        source.nextInt(-2, -2);
    }

    @Test
    public void nextIntInRange() {
        int value = source.nextInt(3, 5);

        verify(random).nextInt(5 - 3 + 1);
        assertThat(value, lessThanOrEqualTo(5));
        assertThat(value, greaterThanOrEqualTo(3));
    }
}