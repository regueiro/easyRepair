package es.regueiro.easyrepair.model.shared;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class NIFTest {
    
    public NIFTest() {
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
    public void TestNIF() {
        System.out.println("Testing NIFs");

        NIF nif1 = new NIF();

        assertNull(nif1.getNumber());

        NIF nif2 = new NIF("66877899V");

        assertEquals("66877899V", nif2.getNumber());


        NIF nif3 = new NIF();


        nif3.setNumber("66877899V");
        try {
            nif3.setNumber(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            nif3.setNumber("    ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        try {
            nif3.setNumber("3414");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NIF test = new NIF("849");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        NIF nif4 = new NIF();
        NIF nif5 = new NIF();

        assertFalse(nif4.equals(null));
        assertFalse(nif4.equals(5));
        assertTrue(nif4.equals(nif4));
        assertTrue(nif4.equals(nif5));
        assertTrue(nif5.equals(nif4));
        assertEquals(nif4.hashCode(), nif5.hashCode());


        nif4.setNumber("66877899V");

        assertFalse(nif5.equals(nif4));
        assertFalse(nif4.equals(nif5));

        nif5.setNumber("21564146J");

        assertFalse(nif5.equals(nif4));
        assertFalse(nif4.equals(nif5));


        nif5.setNumber("66877899V");
        assertTrue(nif4.equals(nif5));
        assertTrue(nif5.equals(nif4));
        assertEquals(nif4.hashCode(), nif5.hashCode());
    }
}
