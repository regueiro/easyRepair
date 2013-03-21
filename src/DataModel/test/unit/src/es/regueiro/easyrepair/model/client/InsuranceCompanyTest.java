package es.regueiro.easyrepair.model.client;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class InsuranceCompanyTest {

    public InsuranceCompanyTest() {
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
    public void testInsuranceCompany() {
        System.out.println("Testing setting atributes of an insurance company");
        InsuranceCompany ins = new InsuranceCompany();
        InsuranceCompany dos = new InsuranceCompany();

        ins.setName("testname");
        ins.setNotes("testnotes");
        ins.setNif(new NIF("66877899V"));
        ins.setId(new Long("1"));
        ins.setVersion(2);
        ins.setEnabled(true);
        ins.setWeb("www.web.com");

        dos.setName("second");

        // Comprobamos que asigna correctamente
        assertEquals("testname", ins.getName());
        assertEquals("testnotes", ins.getNotes());
        assertEquals("66877899V", ins.getNif().getNumber());
        assertEquals("1", ins.getId().toString());
        assertEquals(2, ins.getVersion());
        assertEquals("http://www.web.com", ins.getWeb());
        assertTrue(ins.getEnabled());

        assertEquals("second", dos.getName());

        // Comprobamos el comportamiento con cadenas vacias y nulos

        ins.setNotes("");
        assertNull(ins.getNotes());
        ins.setNotes("    ");
        assertNull(ins.getNotes());
        ins.setNotes(null);
        assertNull(ins.getNotes());

        ins.setWeb("http://www.udc.es");
        assertEquals("http://www.udc.es", ins.getWeb());

        ins.setWeb("");
        assertNull(ins.getWeb());
        ins.setWeb(null);
        assertNull(ins.getWeb());
        try {
            ins.setWeb("a  ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        // El nombre no puede ser nulo
        try {
            ins.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ins.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", ins.getName());



        InsuranceCompany cuatro = new InsuranceCompany();

        // Sin nombre no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        cuatro.setName("cuatro");
        cuatro.validate();

     
        // Comprobamos el funcionamiento de equals y hashcode
        InsuranceCompany ins2 = new InsuranceCompany();
        InsuranceCompany ins3 = new InsuranceCompany();

        assertFalse(ins2.equals(null));
        assertFalse(ins2.equals(5));
        assertTrue(ins2.equals(ins2));
        assertEquals(ins2.hashCode(), ins2.hashCode());

        ins2.setName("test");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setName("aaa");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setName("test");
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));
        assertEquals(ins2.hashCode(), ins3.hashCode());

        ins2.setNotes("test");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setNotes("aaa");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setNotes("test");
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));
        assertEquals(ins2.hashCode(), ins3.hashCode());

        ins2.addEmail(new Email("test@tes.com", "lab"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.addEmail(new Email("test@1.com", "lab"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.removeEmail("lab");

        ins3.addEmail(new Email("test@tes.com", "lab"));
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));

        ins3.setEmail(new ArrayList<Email>());
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins2.setEmail(new ArrayList<Email>());

        ins2.addPhone(new Phone("689558899", "lab"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.addPhone(new Phone("689996655", "lab"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.removePhone("lab");

        ins3.addPhone(new Phone("689558899", "lab"));
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));

        ins3.setPhone(new ArrayList<Phone>());
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins2.setPhone(new ArrayList<Phone>());


        Address add4 = new Address();
        add4.setCity("test");
        add4.setLabel("label");
        ins2.addAddress(add4);
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        Address add5 = new Address();
        add5.setCity("test");
        add5.setLabel("lab");
        ins3.addAddress(add5);
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.removeAddress("lab");
        add5.setLabel("label");
        ins3.addAddress(add5);
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));

        ins3.setAddress(new ArrayList<Address>());
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins2.setAddress(new ArrayList<Address>());


        ins2.setNif(new NIF("66877899V"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setNif(new NIF("21564146J"));
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setNif(new NIF("66877899V"));
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));
        assertEquals(ins2.hashCode(), ins3.hashCode());
        
        
        ins2.setWeb("www.udc.es");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setWeb("www.udc.es/ptrp");
        assertFalse(ins2.equals(ins3));
        assertFalse(ins3.equals(ins2));

        ins3.setWeb("www.udc.es");
        assertTrue(ins2.equals(ins3));
        assertTrue(ins3.equals(ins2));
        assertEquals(ins2.hashCode(), ins3.hashCode());
    }
}