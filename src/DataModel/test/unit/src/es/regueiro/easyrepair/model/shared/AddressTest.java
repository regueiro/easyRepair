package es.regueiro.easyrepair.model.shared;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class AddressTest {
    
    public AddressTest() {
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
    public void TestAddress() {
        System.out.println("Testing Addresses");

        Address ad1 = new Address();

        assertNull(ad1.getCity());
        assertNull(ad1.getCountry());
        assertNull(ad1.getPostalCode());
        assertNull(ad1.getProvince());
        assertNull(ad1.getStreet());
        assertNull(ad1.getLabel());
        assertNull(ad1.getNotes());
        assertNull(ad1.getId());

        Address ad2 = new Address();

        ad2.setCity("testcity");
        ad2.setCountry("testcountry");
        ad2.setPostalCode("123456");
        ad2.setProvince("testprovince");
        ad2.setStreet("testreet");
        ad2.setLabel("label");
        ad2.setNotes("notes");

        assertEquals("testcity", ad2.getCity());
        assertEquals("testcountry", ad2.getCountry());
        assertEquals("123456", ad2.getPostalCode());
        assertEquals("testprovince", ad2.getProvince());
        assertEquals("testreet", ad2.getStreet());
        assertEquals("label", ad2.getLabel());
        assertEquals("notes", ad2.getNotes());


        ad1.setCity("address");
        assertEquals("address", ad1.getCity());

        ad1.setLabel("aaa");
        assertEquals("aaa", ad1.getLabel());


        ad2.setCity("");
        ad2.setCountry("");
        ad2.setPostalCode("");
        ad2.setProvince("");
        ad2.setStreet("");
        ad2.setNotes("");

        assertNull(ad2.getCity());
        assertNull(ad2.getCountry());
        assertNull(ad2.getPostalCode());
        assertNull(ad2.getProvince());
        assertNull(ad2.getStreet());
        assertNull(ad2.getNotes());

        ad2.setCity(null);
        ad2.setCountry(null);
        ad2.setPostalCode(null);
        ad2.setProvince(null);
        ad2.setStreet(null);
        ad2.setNotes(null);

        assertNull(ad2.getCity());
        assertNull(ad2.getCountry());
        assertNull(ad2.getPostalCode());
        assertNull(ad2.getProvince());
        assertNull(ad2.getStreet());
        assertNull(ad2.getNotes());

        ad1.setId(new Long("12312"));

        assertEquals(new Long("12312"), ad1.getId());


        Address ad3 = new Address();
        Address ad4 = new Address();
        Address ad5 = new Address();


        try {
            ad2.setLabel(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ad2.setLabel("   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }



        try {
            ad3.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ad3.setCity("city");

        try {
            ad3.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ad3.setLabel("aa");
        ad3.validate();

        ad3.setCity(null);

        ad3.setCountry("a");
        ad3.validate();

        ad3.setCountry(null);
        ad3.setPostalCode("2");
        ad3.validate();
        ad3.setPostalCode(null);
        ad3.setProvince("aaa");
        ad3.validate();
        ad3.setProvince(null);
        ad3.setStreet("aa");
        ad3.validate();
        ad3.setStreet(null);
        ad3.setNotes("not");
        ad3.validate();



        assertFalse(ad4.equals(null));
        assertFalse(ad4.equals(5));
        assertTrue(ad4.equals(ad5));
        assertTrue(ad4.equals(ad5));
        assertTrue(ad5.equals(ad4));
        assertEquals(ad4.hashCode(), ad5.hashCode());


        ad4.setLabel("label");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setLabel("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setLabel("label");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());


        ad4.setNotes("notes");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));


        ad5.setNotes("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setNotes("notes");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());


        ad4.setStreet("Street");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setStreet("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setStreet("Street");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());


        ad4.setCity("City");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setCity("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setCity("City");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());



        ad4.setCountry("Country");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setCountry("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setCountry("Country");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());



        ad4.setProvince("Province");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setProvince("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setProvince("Province");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());


        ad4.setPostalCode("PostalCode");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setPostalCode("lab");

        assertFalse(ad4.equals(ad5));
        assertFalse(ad5.equals(ad4));

        ad5.setPostalCode("PostalCode");

        assertTrue(ad4.equals(ad5));
        assertEquals(ad4.hashCode(), ad5.hashCode());
    }
}
