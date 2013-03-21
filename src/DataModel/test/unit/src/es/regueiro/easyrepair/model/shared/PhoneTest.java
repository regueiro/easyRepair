package es.regueiro.easyrepair.model.shared;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class PhoneTest {
    
    public PhoneTest() {
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
    public void TestPhone() {
        System.out.println("Testing Phones");

        Phone ph1 = new Phone();
        // Sin datos no valida
        try {
            ph1.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        assertNull(ph1.getNumber());
        assertNull(ph1.getLabel());
        assertNull(ph1.getNotes());
        assertNull(ph1.getId());

        Phone ph2 = new Phone("986858585", "label");

        ph2.setNotes("  ");
        assertNull(ph2.getNotes());
        
        ph2.setNotes("notes");

        ph2.validate();

        assertEquals("986858585", ph2.getNumber());
        assertEquals("label", ph2.getLabel());
        assertEquals("notes", ph2.getNotes());


        ph1.setNumber("+1-202(4561414)");
        assertEquals("+12024561414", ph1.getNumber());

        ph1.setLabel("aaa");
        assertEquals("aaa", ph1.getLabel());


        ph1.setId(new Long("12312"));

        assertEquals(new Long("12312"), ph1.getId());

        assertEquals("986858585", ph2.toString());


        Phone ph6 = new Phone("+1-202(4561414)", "l");
        assertEquals("+12024561414", ph6.getNumber());



        Phone ph3 = new Phone();


        try {
            ph3.setNumber(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ph3.setNumber("    ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        try {
            ph3.setNumber("aaaaaa");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ph3.setNumber("9861234567");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            Phone ph7 = new Phone("9861234567", "la");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ph3.setNumber("+120245614144");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ph3.setLabel(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ph3.setLabel("  ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ph3.setNumber("981447789");

        try {
            ph3.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        Phone ph7 = new Phone();
        ph7.setLabel("la");
        try {
            ph7.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        Phone ph4 = new Phone();
        Phone ph5 = new Phone();

        assertFalse(ph4.equals(null));
        assertFalse(ph4.equals(5));
        assertTrue(ph4.equals(ph4));
        assertTrue(ph4.equals(ph5));
        assertTrue(ph5.equals(ph4));
        assertEquals(ph4.hashCode(), ph5.hashCode());

        ph4.setNumber("986123456");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));



        ph5.setNumber("986123457");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));

        ph5.setNumber("986123456");

        assertTrue(ph4.equals(ph5));
        assertEquals(ph4.hashCode(), ph5.hashCode());




        ph4.setLabel("label");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));


        ph5.setLabel("lab");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));

        ph5.setLabel("label");

        assertTrue(ph4.equals(ph5));
        assertEquals(ph4.hashCode(), ph5.hashCode());


        ph4.setNotes("notes");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));


        ph5.setNotes("lab");

        assertFalse(ph4.equals(ph5));
        assertFalse(ph5.equals(ph4));

        ph5.setNotes("notes");

        assertTrue(ph4.equals(ph5));
        assertEquals(ph4.hashCode(), ph5.hashCode());
    }
}
