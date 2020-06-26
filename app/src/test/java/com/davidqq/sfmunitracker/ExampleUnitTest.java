package com.davidqq.sfmunitracker;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}

/*
@RunWith(MockitoJUnitRunner.class)
public class MapsViewModelTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    //Allows for tests to livedata to be run on not only the primary thread
    @Rule
    public InstantTaskExecutorRule a = new InstantTaskExecutorRule();
}
 */