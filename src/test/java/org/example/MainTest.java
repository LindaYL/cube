import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    @Test
    public void testHelloWorld() {
        assertEquals("Hello, World!", helloWorld());
    }

    private String helloWorld() {
        return "Hello, World!";
    }
}