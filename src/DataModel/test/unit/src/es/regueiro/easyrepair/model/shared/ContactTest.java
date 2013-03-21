/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.shared;

import es.regueiro.easyrepair.model.employee.Employee;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class ContactTest {

    public ContactTest() {
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
    public void testContactComponents() {
        // Comprobamos que añade y borra correctamente
        System.out.println("Testing Contact Components");
        Email em = new Email("add@res.com", "lab");

        Contact emp = new Employee();

        emp.addEmail(em);

        assertTrue(emp.containsEmailLabel("lab"));
        assertTrue(emp.getEmail().contains(em));

        Phone ph = new Phone("986667788", "label");

        emp.addPhone(ph);

        assertTrue(emp.containsPhoneLabel("label"));
        assertTrue(emp.getPhone().contains(ph));

        Address add = new Address();
        add.setLabel("lab");
        add.setCity("aa");

        emp.addAddress(add);


        assertTrue(emp.containsAddressLabel("lab"));
        assertTrue(emp.getAddress().contains(add));


        emp.setAddress(null);
        assertNull(emp.getAddress());

        emp.setPhone(null);
        assertNull(emp.getPhone());

        emp.setEmail(null);
        assertNull(emp.getEmail());


        Address add2 = new Address();
        add2.setLabel("lab");
        add2.setCity("c");

        emp.addAddress(add);
        try {
            emp.addAddress(add2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        emp.setAddress(null);

        assertNull(emp.getAddress());

        assertFalse(emp.containsAddressLabel("aa"));

        emp.clearAdddress();

        assertTrue(emp.getAddress().isEmpty());

        emp.setAddress(new ArrayList<Address>());

        assertFalse(emp.containsAddressLabel("aa"));

        emp.addAddress(add2);

        add.setLabel("a");
        emp.addAddress(add);

        assertTrue(emp.containsAddressLabel("a"));

        emp.removeAddress("a");
        assertFalse(emp.containsAddressLabel("a"));
        emp.removeAddress("lab");
        assertFalse(emp.containsAddressLabel("lab"));

        emp.setAddress(null);
        emp.removeAddress(null);

        assertNull(emp.getAddress());



        Email em2 = new Email("a@a.com", "lab");
        emp.addEmail(em);
        try {
            emp.addEmail(em2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        emp.setEmail(null);

        assertNull(emp.getEmail());

        assertFalse(emp.containsEmailLabel("aa"));


        emp.clearEmail();
        assertTrue(emp.getEmail().isEmpty());


        emp.setEmail(new ArrayList<Email>());

        assertFalse(emp.containsEmailLabel("aa"));

        emp.addEmail(em2);

        em.setLabel("a");
        emp.addEmail(em);
        assertTrue(emp.containsEmailLabel("a"));

        emp.removeEmail("a");
        assertFalse(emp.containsEmailLabel("aa"));
        emp.removeEmail("lab");
        assertFalse(emp.containsEmailLabel("aa"));

        emp.setEmail(null);

        emp.removeEmail(null);
        assertNull(emp.getEmail());


        Phone ph2 = new Phone("986558899", "label");

        emp.addPhone(ph2);
        try {
            emp.addPhone(ph);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        emp.setPhone(null);

        assertNull(emp.getPhone());
        assertFalse(emp.containsPhoneLabel("aa"));

        emp.clearPhone();
        assertFalse(emp.containsPhoneLabel("aa"));

        emp.setPhone(new ArrayList<Phone>());

        assertFalse(emp.containsPhoneLabel("aa"));

        emp.addPhone(ph);

        ph2.setLabel("a");
        emp.addPhone(ph2);
        assertTrue(emp.containsPhoneLabel("a"));

        emp.removePhone("a");
        assertFalse(emp.containsPhoneLabel("a"));
        emp.removePhone("label");
        assertFalse(emp.containsPhoneLabel("label"));

        emp.setPhone(null);
        emp.removePhone(null);
        assertNull(emp.getPhone());

        emp.addPhone(ph2);
        assertTrue(emp.getPhone().contains(ph2));
        emp.clearPhone();
        assertTrue(emp.getPhone().isEmpty());

        emp.addEmail(em);
        assertTrue(emp.getEmail().contains(em));
        emp.clearEmail();
        assertTrue(emp.getEmail().isEmpty());

        emp.addAddress(add);
        assertTrue(emp.getAddress().contains(add));
        emp.clearAdddress();
        assertTrue(emp.getAddress().isEmpty());

        
        emp.setNif(new NIF("66877899V"));
        assertEquals("66877899V", emp.getNif().getNumber());
    }
}
