import com.example.onlineclassregister.Class;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testClass {

    @Test
    public void testConstructor() {
        List<Integer> subjectsTaught = new ArrayList<>();
        subjectsTaught.add(1);
        subjectsTaught.add(2);
        Class testClass = new Class(1, 2, 30, 2022, "IX A", "11", 2, subjectsTaught);
        // test that the class attributes are set correctly
        Assert.assertEquals(1, testClass.id);
        Assert.assertEquals(2, testClass.classTeacherId);
        Assert.assertEquals(30, testClass.studentsCount);
        Assert.assertEquals(2022, testClass.year);
        Assert.assertEquals("IX A", testClass.name);
        Assert.assertEquals("11", testClass.room);
        Assert.assertEquals(2, testClass.subjectsCount);
        Assert.assertEquals(subjectsTaught, testClass.subjectsTaught);
    }

}
