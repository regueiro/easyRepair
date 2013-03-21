package es.regueiro.easyrepair.model.employee;

import es.regueiro.easyrepair.model.shared.*;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;


public class EmployeeTest {

    public EmployeeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEmployee() {
        System.out.println("Testing setting atributes of an employee");
        Employee emp = new Employee();
        Employee dos = new Employee();

        emp.setName("testname");
        emp.setSurname("testsurname");
        emp.setNotes("testnotes");
        emp.setNif(new NIF("66877899V"));
        emp.setNss(new NSS("258865148973"));
        emp.setOccupation("Mechanic");
        emp.setId(new Long("1"));
        emp.setEmployeeId("1");
        emp.setVersion(2);
        emp.setEnabled(true);

        dos.setName("second");

        // Comprobamos que asigna correctamente
        assertEquals("testnotes", emp.getNotes());
        assertEquals("258865148973", emp.getNss().getNumber());
        assertEquals("Mechanic", emp.getOccupation());
        assertEquals("1", emp.getId().toString());
        assertEquals("00000001", emp.getEmployeeId());
        assertEquals(2, emp.getVersion());
        assertTrue(emp.getEnabled());

        assertEquals("second", dos.getName());

        // Comprobamos el comportamiento con cadenas vacias y nulos
        emp.setEmployeeId("");
        assertNull(emp.getEmployeeId());
        emp.setEmployeeId("    ");
        assertNull(emp.getEmployeeId());
        emp.setEmployeeId(null);
        assertNull(emp.getEmployeeId());

        emp.setOccupation("");
        assertNull(emp.getOccupation());
        emp.setOccupation("    ");
        assertNull(emp.getOccupation());
        emp.setOccupation(null);
        assertNull(emp.getOccupation());

        emp.setNotes("");
        assertNull(emp.getNotes());
        emp.setNotes("    ");
        assertNull(emp.getNotes());
        emp.setNotes(null);
        assertNull(emp.getNotes());

        


        Employee cuatro = new Employee();

        // Comprobamos que no asigna ids incorrectas
        try {
            cuatro.setEmployeeId("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            cuatro.setEmployeeId("123456789");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            cuatro.setEmployeeId("12345678s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        try {
            cuatro.setEmployeeId("-1234567");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
    
        
        


        // Comprobamos el funcionamiento de equals y hashcode
        Employee emp4 = new Employee();
        Employee emp5 = new Employee();

        assertFalse(emp4.equals(null));
        assertFalse(emp4.equals(5));
        assertTrue(emp4.equals(emp4));
        assertEquals(emp4.hashCode(), emp4.hashCode());

        emp4.setName("test");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setName("aaa");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setName("test");
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());

        emp4.setSurname("test");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setSurname("aaa");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setSurname("test");
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());

        emp4.setOccupation("test");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setOccupation("aaa");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setOccupation("test");
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());

        emp4.setNotes("test");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNotes("aaa");
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNotes("test");
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());

        emp4.addEmail(new Email("test@tes.com", "lab"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.addEmail(new Email("test@1.com", "lab"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.removeEmail("lab");

        emp5.addEmail(new Email("test@tes.com", "lab"));
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        
        assertTrue(emp4.hashCode() == emp5.hashCode());

        emp5.setEmail(new ArrayList<Email>());
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));
        
        emp4.setEmail(null);
        
        assertFalse(emp4.hashCode() == emp5.hashCode());

        emp4.setEmail(new ArrayList<Email>());

        
        
        emp4.addPhone(new Phone("689558899", "lab"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.addPhone(new Phone("689996655", "lab"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.removePhone("lab");

        emp5.addPhone(new Phone("689558899", "lab"));
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        
        assertTrue(emp4.hashCode() == emp5.hashCode());

        emp5.setPhone(new ArrayList<Phone>());
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));
        
        emp4.setPhone(null);
        
        assertFalse(emp4.hashCode() == emp5.hashCode());

        emp4.setPhone(new ArrayList<Phone>());


        Address add4 = new Address();
        add4.setCity("test");
        add4.setLabel("label");
        emp4.addAddress(add4);
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        Address add5 = new Address();
        add5.setCity("test");
        add5.setLabel("lab");
        emp5.addAddress(add5);
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.removeAddress("lab");
        add5.setLabel("label");
        emp5.addAddress(add5);
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        
        assertTrue(emp4.hashCode() == emp5.hashCode());

        emp5.setAddress(new ArrayList<Address>());
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));
        
        emp4.setAddress(null);
        
        assertFalse(emp4.hashCode() == emp5.hashCode());

        emp4.setAddress(new ArrayList<Address>());


        emp4.setNif(new NIF("66877899V"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNif(new NIF("21564146J"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNif(new NIF("66877899V"));
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());


        emp4.setNss(new NSS("258865148973"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNss(new NSS("550087115810"));
        assertFalse(emp4.equals(emp5));
        assertFalse(emp5.equals(emp4));

        emp5.setNss(new NSS("258865148973"));
        assertTrue(emp4.equals(emp5));
        assertTrue(emp5.equals(emp4));
        assertEquals(emp4.hashCode(), emp5.hashCode());
    }
    
   
    
}