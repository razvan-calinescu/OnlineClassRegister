import com.example.onlineclassregister.SchoolPerson;
import org.junit.Test;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class SchoolPersonTest {

    ///testing constructor
    @Test
    public void testConstructor() {
        String fName = "John";
        String lName = "Smith";
        int id = 1;
        int role = 2;
        String mail = "jsmith@school.com";
        String phone = "12345";
        Date birthDate = new Date();
        boolean isAdmin = true;
        int isActive = 1;

        SchoolPerson person = new SchoolPerson(fName, lName, id, role, mail, phone, birthDate, isAdmin, isActive);

        assertEquals(fName, person.getfName());
        assertEquals(lName, person.getlName());
        assertEquals(id, person.getUserId());
        assertEquals(role, person.getRole());
        assertEquals(mail, person.getMail());
        assertEquals(phone, person.getPhone());
        assertEquals(birthDate, person.getBirthDate());
        assertEquals(isAdmin, person.isAdmin());
        assertEquals(isActive, person.getIsActive());
    }

    //Checking list not empty
    @Test
    public void testGetUsers() {
        List<SchoolPerson> users = SchoolPerson.getUsers();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }


}
