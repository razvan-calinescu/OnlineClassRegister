import static org.junit.Assert.assertEquals;

import com.example.onlineclassregister.Subject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectTest {

    @Test
    public void testConstructor() {
        Subject subject = new Subject(1, "Math", 7.5);
        assertEquals(1, subject.getId());
        assertEquals("Math", subject.getName());
    }

    @Test
    public void testGetId() {
        Subject subject = new Subject(1, "Math", 75);
        assertEquals(1, subject.getId());
    }

    @Test
    public void testGetName() {
        Subject subject = new Subject(1, "Math", 75);
        assertEquals("Math", subject.getName());
    }


}
