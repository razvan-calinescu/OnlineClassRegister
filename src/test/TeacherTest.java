import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.onlineclassregister.Teacher;
import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    void testConstructor() {
        // Test the constructor and getters
        List<Integer> classes = new ArrayList<>();
        classes.add(1);
        classes.add(2);
        classes.add(3);
        Map<Integer, String> classesNames = new HashMap<>();
        classesNames.put(1, "Math");
        classesNames.put(2, "Programming");
        classesNames.put(3, "English");

        Teacher teacher = new Teacher("John", "Smith", 1, 2, "jsmoth@school.com", "1234", new Date(), 1, 2, 3, 4, 5, classes, classesNames, 1);

        assertEquals(1, teacher.getSubjectId());
        assertEquals(2, teacher.getWeeklyClasses());
        assertEquals(3, teacher.getClassTeacherId());
        assertEquals(4, teacher.getTeacherId());
        assertEquals(5, teacher.getClassesCount());
        assertEquals(classes, teacher.getClassesId());
        assertEquals(classesNames, teacher.getClassesName());
        assertEquals(1, teacher.getIsActive());
    }

    @Test
    void testGetTeachers() {
        // Test the getTeachers method
        List<Teacher> teachers = Teacher.getTeachers();
        assertNotNull(teachers);
        assertTrue(teachers.size() > 0);
    }
}
