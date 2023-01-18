import static org.junit.Assert.*;

import java.util.*;

import com.example.onlineclassregister.Student;
import com.example.onlineclassregister.regEntry;
import org.junit.Test;

public class StudentTest {

    @Test
    public void testGetStudents() {
        List<Student> students = Student.getStudents();
        assertNotNull(students);
        assertTrue(students.size() > 0);
    }

    @Test
    public void testStudentConstructor() {
        List<Integer> coursesAttended = new ArrayList<>();
        coursesAttended.add(1);
        coursesAttended.add(2);
        coursesAttended.add(3);
        coursesAttended.add(4);

        List<regEntry> regEntries = new ArrayList<>();
        regEntries.add(new regEntry(1, new Date()));
        regEntries.add(new regEntry(2, new Date()));
        regEntries.add(new regEntry(3, new Date()));
        regEntries.add(new regEntry(4, new Date()));

        Map<Integer, Double> averages = new HashMap<>();
        averages.put(1, 9.0);
        averages.put(2, 8.5);
        averages.put(3, 8.0);
        averages.put(4, 7.5);

        Student student = new Student("John", "Smith", 1, 3, "jsmith@school.com", "1234", new Date(), 1, 5, 2, 1, 2, 3, 4, 8.55, coursesAttended, 1, 1, regEntries, averages);

        assertEquals(student.getfName(), "John");
        assertEquals(student.getlName(), "Smith");
        assertEquals(student.getRole(), 3);
        assertEquals(student.getMail(), "jsmith@school.com");
        assertEquals(student.getPhone(), "1234");
        assertEquals(student.getBirthDate(), new Date());
        assertEquals(student.getIsActive(), 1);
        assertEquals(student.classId, 1);
        assertEquals(student.totalMissingAttendance, 5);
        assertEquals(student.totalMotivated, 2);
        assertEquals(student.userId, 1);
        assertEquals(student.parent1Id, 2);
        assertEquals(student.parent2Id, 3);
        assertEquals(student.coursesCount, 4);
        assertEquals(student.average, 8.55, 0.01);
        assertEquals(student.coursesAttended, coursesAttended);
        assertEquals(student.regEntries, regEntries);
        assertEquals(student.averages, averages);
    }



}
