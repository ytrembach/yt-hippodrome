import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class HorseTest {

    final public static String TEST_HORSE_NAME = "TestHorse";
    final public static double TEST_SPEED = 2.5;
    final public static double TEST_DISTANCE = 10;

    final public static double RANDOM_MIN = 0.2;
    final public static double RANDOM_MAX = 0.9;

    Horse testHorse;

    @BeforeEach
    public void initHorse() {
        testHorse = new Horse(TEST_HORSE_NAME, TEST_SPEED, TEST_DISTANCE);
    }

    // a
    @Test
    public void nullHorseName() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(null, TEST_SPEED));
        assertEquals("Name cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t", "  \n  \t"})
    public void blankHorseName(final String name) {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(name, TEST_SPEED));
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    public void negativeSpeed() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(TEST_HORSE_NAME, -TEST_SPEED));
        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    public void negativeDistance() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Horse(TEST_HORSE_NAME, TEST_SPEED, -TEST_DISTANCE));
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }

    // b
    @Test
    public void getName() {
        assertEquals(TEST_HORSE_NAME, testHorse.getName());
    }

    // c
    @Test
    public void getSpeed() {
        assertEquals(TEST_SPEED, testHorse.getSpeed());
    }

    // d
    @Test
    public void getDistanceSet() { assertEquals(TEST_DISTANCE, testHorse.getDistance());}

    @Test
    public void getDistanceUnset() {
        testHorse = new Horse(TEST_HORSE_NAME, TEST_SPEED);
        assertEquals(0, testHorse.getDistance());
    }

    // e
    @Test
    public void moveGetRandom() {
        try (MockedStatic<Horse> mockedHorse = Mockito.mockStatic(Horse.class)) {
            testHorse.move();
            mockedHorse.verify(() -> Horse.getRandomDouble(RANDOM_MIN, RANDOM_MAX));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.2, 0.5, 0.8, 0.9 })
    public void moveFormula(final Double random) {
        try (MockedStatic<Horse> mockedHorse = Mockito.mockStatic(Horse.class)) {
            mockedHorse.when(() -> Horse.getRandomDouble(any(Double.class), any(Double.class))).thenReturn(random);
            double origDistance = testHorse.getDistance();
            testHorse.move();
            assertEquals(origDistance + testHorse.getSpeed() * random, testHorse.getDistance());
        }
    }

}
