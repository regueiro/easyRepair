package es.regueiro.easyrepair.model.shared;

import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class NSSTest {

    public NSSTest() {
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

    /**
     * Test of setNumber method, of class NSS.
     */
    @Test
    public void TestNSS() {
        System.out.println("Testing NSSs");

        NSS nss1 = new NSS();

        assertNull(nss1.getNumber());

        NSS nss2 = new NSS("258865148973");

        assertEquals("258865148973", nss2.getNumber());


        NSS nss3 = new NSS();


        nss3.setNumber("25/88651489/73");

        assertEquals("258865148973", nss3.getNumber());
        try {
            nss3.setNumber(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            nss3.setNumber("    ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        try {
            nss3.setNumber("3414");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NSS test = new NSS("849");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NSS test = new NSS("-258865148973");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NSS test = new NSS("25886!148973");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            NSS test = new NSS("258865148976");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NSS test = new NSS("25-88651489-73");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            NSS test = new NSS("2581186!148973");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            NSS test = new NSS("258865148976");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        NSS nss4 = new NSS();
        NSS nss5 = new NSS();

        assertFalse(nss4.equals(null));
        assertFalse(nss4.equals(5));
        assertTrue(nss4.equals(nss4));
        assertTrue(nss4.equals(nss5));
        assertTrue(nss5.equals(nss4));
        assertEquals(nss4.hashCode(), nss5.hashCode());


        nss4.setNumber("258865148973");

        assertFalse(nss5.equals(nss4));
        assertFalse(nss4.equals(nss5));

        nss5.setNumber("55/00871158/10");

        assertFalse(nss5.equals(nss4));
        assertFalse(nss4.equals(nss5));


        nss4.setNumber("550087115810");
        assertTrue(nss4.equals(nss5));
        assertTrue(nss5.equals(nss4));
        assertEquals(nss4.hashCode(), nss5.hashCode());
    }
}
