/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Santi
 */
public class SupplierTest {

    public SupplierTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCategory method, of class Supplier.
     */
    @Test
    public void testSupplier() {
        System.out.println("Testing Supplier Components");
        Supplier sup = new Supplier();

        sup.setCategory("cat");
        sup.setWeb("http://google.es");
        sup.setPaymentMethod("pay");
        sup.setNotes("notes");
        sup.setShippingMethod("air");
        sup.setEnabled(true);
        sup.setVersion(10);
        sup.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals("cat", sup.getCategory());
        assertEquals("http://google.es", sup.getWeb());
        assertEquals("pay", sup.getPaymentMethod());
        assertEquals("notes", sup.getNotes());
        assertEquals("air", sup.getShippingMethod());
        assertTrue(sup.getEnabled());
        assertEquals(10, sup.getVersion());
        assertTrue(Long.parseLong("20") == sup.getId());


        // Comprobamos el comportamiento con cadenas vacias y nulos
        sup.setNotes("");
        assertNull(sup.getNotes());
        sup.setNotes("    ");
        assertNull(sup.getNotes());
        sup.setNotes(null);
        assertNull(sup.getNotes());

        sup.setCategory("");
        assertNull(sup.getCategory());
        sup.setCategory("    ");
        assertNull(sup.getCategory());
        sup.setCategory(null);
        assertNull(sup.getCategory());

        sup.setPaymentMethod("");
        assertNull(sup.getPaymentMethod());
        sup.setPaymentMethod("    ");
        assertNull(sup.getPaymentMethod());
        sup.setPaymentMethod(null);
        assertNull(sup.getPaymentMethod());

        sup.setShippingMethod("");
        assertNull(sup.getShippingMethod());
        sup.setShippingMethod("    ");
        assertNull(sup.getShippingMethod());
        sup.setShippingMethod(null);
        assertNull(sup.getShippingMethod());

        sup.setWeb(null);
        assertNull(sup.getWeb());

        //Comprobamos el funcionamiento de las webs
        sup.setWeb("google.de");
        assertEquals("http://google.de", sup.getWeb());

        try {
            sup.setWeb("goog le.de");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        

        // Comprobamos el funcionamiento de equals y hashcode
        Supplier sup1 = new Supplier();
        Supplier sup2 = new Supplier();

        assertFalse(sup1.equals(null));
        assertFalse(sup1.equals(2));
        assertTrue(sup1.equals(sup1));
        assertEquals(sup1.hashCode(), sup1.hashCode());

        sup1.setName("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setName("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setName("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());

        sup1.setCategory("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setCategory("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setCategory("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());

        sup1.setPaymentMethod("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setPaymentMethod("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setPaymentMethod("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());

        sup1.setShippingMethod("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setShippingMethod("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setShippingMethod("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());

        sup1.setNotes("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setNotes("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setNotes("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());
        
        sup1.setWeb("test");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setWeb("aaa");
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setWeb("test");
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());

        sup1.addEmail(new Email("test@tes.com", "lab"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.addEmail(new Email("test@1.com", "lab"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.removeEmail("lab");

        sup2.addEmail(new Email("test@tes.com", "lab"));
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));

        assertTrue(sup1.hashCode() == sup2.hashCode());

        sup2.setEmail(new ArrayList<Email>());
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup1.setEmail(null);

        assertFalse(sup1.hashCode() == sup2.hashCode());

        sup1.setEmail(new ArrayList<Email>());



        sup1.addPhone(new Phone("689228899", "lab"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.addPhone(new Phone("689996622", "lab"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.removePhone("lab");

        sup2.addPhone(new Phone("689228899", "lab"));
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));

        assertTrue(sup1.hashCode() == sup2.hashCode());

        sup2.setPhone(new ArrayList<Phone>());
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup1.setPhone(null);

        assertFalse(sup1.hashCode() == sup2.hashCode());

        sup1.setPhone(new ArrayList<Phone>());


        Address add1 = new Address();
        add1.setCity("test");
        add1.setLabel("label");
        sup1.addAddress(add1);
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        Address add2 = new Address();
        add2.setCity("test");
        add2.setLabel("lab");
        sup2.addAddress(add2);
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.removeAddress("lab");
        add2.setLabel("label");
        sup2.addAddress(add2);
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));

        assertTrue(sup1.hashCode() == sup2.hashCode());

        sup2.setAddress(new ArrayList<Address>());
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup1.setAddress(null);

        assertFalse(sup1.hashCode() == sup2.hashCode());

        sup1.setAddress(new ArrayList<Address>());


        sup1.setNif(new NIF("12345678Z"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setNif(new NIF("23456789D"));
        assertFalse(sup1.equals(sup2));
        assertFalse(sup2.equals(sup1));

        sup2.setNif(new NIF("12345678Z"));
        assertTrue(sup1.equals(sup2));
        assertTrue(sup2.equals(sup1));
        assertEquals(sup1.hashCode(), sup2.hashCode());
    }
}
