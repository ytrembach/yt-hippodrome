import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.Collections;

public class MainTest {

    @Test
    @Timeout(22)
    @Disabled
    public void mainTimeout() {
        String[] params = new String[0];
        try {
            Main.main(params);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
