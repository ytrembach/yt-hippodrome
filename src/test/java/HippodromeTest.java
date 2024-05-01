import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Math.random;
import static org.junit.jupiter.api.Assertions.*;

public class HippodromeTest {

    public static final String TEST_HORSE_NAME_PREFIX = "TEST_HORSE";
    public static final Double MIN_TEST_HORSE_SPEED = 1D;
    public static final Double MAX_TEST_HORSE_SPEED = 5D;

    public static final int GET_HORSES_COUNT = 30;
    public static final int MOVE_HORSES_COUNT = 50;

    List<Horse> testHorses = getTestHorses(GET_HORSES_COUNT, this::newHorse);
    Hippodrome testHippodrome = new Hippodrome(testHorses);

    // horse lists generation

    Horse newHorse() {
        return new Horse(TEST_HORSE_NAME_PREFIX,
                MIN_TEST_HORSE_SPEED + random() * (MAX_TEST_HORSE_SPEED - MIN_TEST_HORSE_SPEED));
    }

    Horse newMockedHorse() {
        return Mockito.mock(Horse.class);
    }

    List<Horse> getTestHorses(final int count, Supplier<Horse> horseFunction) {
        List<Horse> testHorses = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            testHorses.add(horseFunction.get());
        }
        return testHorses;
    }

    // a

    @Test
    public void NullHorsesList() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Hippodrome(null));
        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    public void EmptyHorsesList() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new Hippodrome(Collections.emptyList()));
        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    // b

    @Test
    public void getHorsesInOrder() {
        final List<Horse> actual = testHippodrome.getHorses();
        testHorses.forEach(h -> assertEquals(testHorses.indexOf(h), actual.indexOf(h)));
    }

    @Test
    public void moveCalled() {
        // use alternate hippodrome and horses list
        testHorses = getTestHorses(MOVE_HORSES_COUNT, this::newMockedHorse);
        testHippodrome = new Hippodrome(testHorses);

        testHippodrome.move();
        testHorses.forEach(h -> Mockito.verify(h).move());
    }

    @Test
    public void getWinnerCorrect() {
        for (int i = 0; i < 100; i++) {
            testHippodrome.move();
        }
        assertEquals(testHorses.stream()
                        .max(Comparator.comparingDouble(Horse::getDistance))
                        .orElseThrow(),
                testHippodrome.getWinner());

    }


}
