package es.regueiro.easyrepair.model.shared;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class EmailTest {
    
    public EmailTest() {
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
    public void TestEmail() {
        System.out.println("Testing Emails");

        Email em1 = new Email();

        try {
            em1.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        // Los nuevos emails no tienen campos por defecto
        assertNull(em1.getAddress());
        assertNull(em1.getLabel());
        assertNull(em1.getNotes());
        assertNull(em1.getId());

        Email em2 = new Email("santi@regueiro.es", "label");

        em2.setNotes("  ");
        assertNull(em2.getNotes());
        
        em2.setNotes("notes");
        // Comprobamos que valida y guarda todo correctamente
        em2.validate();

        assertEquals("santi@regueiro.es", em2.getAddress());
        assertEquals("label", em2.getLabel());
        assertEquals("notes", em2.getNotes());


        em1.setAddress("add@ress.com");
        assertEquals("add@ress.com", em1.getAddress());

        em1.setLabel("aaa");
        assertEquals("aaa", em1.getLabel());


        em1.setId(new Long("12312"));

        assertEquals(new Long("12312"), em1.getId());


        Email em3 = new Email();

        em3.setAddress("ad@res.com");
        // Sin etiqueta no valida
        try {
            em3.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        // La direccion no puede ser nula, vacia o incorrecta
        try {
            em3.setAddress(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            em3.setAddress("    ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        try {
            em3.setAddress("aaaaaa");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        // La etiqueta no puede ser nula
        try {
            em3.setLabel(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            em3.setLabel("  ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        Email em6 = new Email();

        em6.setLabel("ad@res.com");
        // Sin direccion no valida
        try {
            em6.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        // Comprobamos equals y hashcode
        Email em4 = new Email();
        Email em5 = new Email();

        assertFalse(em4.equals(null));
        assertFalse(em4.equals(5));
        assertTrue(em4.equals(em4));
        assertTrue(em4.equals(em5));
        assertTrue(em5.equals(em4));
        assertEquals(em4.hashCode(), em5.hashCode());

        em4.setAddress("te@st.com");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));



        em5.setAddress("te@st.co");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));

        em5.setAddress("te@st.com");

        assertTrue(em4.equals(em5));
        assertEquals(em4.hashCode(), em5.hashCode());




        em4.setLabel("label");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));


        em5.setLabel("lab");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));

        em5.setLabel("label");

        assertTrue(em4.equals(em5));
        assertEquals(em4.hashCode(), em5.hashCode());


        em4.setNotes("notes");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));


        em5.setNotes("lab");

        assertFalse(em4.equals(em5));
        assertFalse(em5.equals(em4));

        em5.setNotes("notes");

        assertTrue(em4.equals(em5));
        assertEquals(em4.hashCode(), em5.hashCode());
    }
}
