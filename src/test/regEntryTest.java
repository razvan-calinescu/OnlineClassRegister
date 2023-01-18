import static org.junit.Assert.*;
import java.util.Date;

import com.example.onlineclassregister.regEntry;
import org.junit.Test;

public class regEntryTest {

    @Test
    public void testConstructor() {
        Date date = new Date();
        regEntry entry = new regEntry(1, date);
        assertEquals(1, entry.getSubjectId());
        assertEquals(date, entry.getDate());
    }

    @Test
    public void testSetMotivated() {
        regEntry entry = new regEntry(1, new Date());
        entry.setMotivated(true);
        assertTrue(entry.isMotivated());
    }

    @Test
    public void testGetMotivated() {
        regEntry entry = new regEntry(1, new Date());
        entry.setMotivated(true);
        assertTrue(entry.getMotivated());
    }

    @Test
    public void testSetValue() {
        regEntry entry = new regEntry(1, new Date());
        entry.setValue(100);
        assertEquals(100, entry.getValue(), 0);
    }

    @Test
    public void testGetValue() {
        regEntry entry = new regEntry(1, new Date());
        entry.setValue(100);
        assertEquals(100, entry.getValue(), 0);
    }
}
