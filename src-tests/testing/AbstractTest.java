package testing;

import static org.junit.Assert.*;
import org.junit.Ignore;

@Ignore
public abstract class AbstractTest {
    public void failException(Throwable throwable) {
        fail("Nieoczekiwany wyjatek: " + throwable.getClass());
    }
}
