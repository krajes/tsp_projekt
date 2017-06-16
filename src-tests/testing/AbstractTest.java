package testing;

import static org.junit.Assert.*;

public abstract class AbstractTest {
    public void failException(Throwable throwable) {
        fail("Nieoczekiwany wyjatek: " + throwable.getClass());
    }
}
