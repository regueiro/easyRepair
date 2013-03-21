/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
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
public class WarehouseTest {
    
    public WarehouseTest() {
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
     * Test of getId method, of class Warehouse.
     */
    @Test
    public void testWarehouse() {
        System.out.println("Testing Warehouse Components");
        Warehouse war = new Warehouse();
        Address add = new Address();
        Email em = new Email();
        Phone pho = new Phone();
        add.setLabel("a");
        em.setLabel("e");
        pho.setLabel("p");
        
        
        war.setName("testname");
        war.setAddress(add);
        war.setEmail(em);
        war.setPhone(pho);
        war.setNotes("notes");
        war.setEnabled(true);
        war.setVersion(10);
        war.setId(Long.parseLong("20"));
        
        
        // Comprobamos que asigna correctamente
        assertEquals("testname", war.getName());
        assertEquals(add, war.getAddress());
        assertEquals(em, war.getEmail());
        assertEquals(pho, war.getPhone());
        assertEquals("notes", war.getNotes());
        assertTrue(war.getEnabled());
        assertEquals(10, war.getVersion());
        assertTrue(Long.parseLong("20") == war.getId());
        

        
        // El nombre no puede ser nulo
        try {
            war.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            war.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        // Comprobamos el comportamiento con cadenas vacias y nulos
        war.setNotes("");
        assertNull(war.getNotes());
        war.setNotes("    ");
        assertNull(war.getNotes());
        war.setNotes(null);
        assertNull(war.getNotes());
        
        // Comprobamos el funcionamiento de equals y hashcode
        Warehouse war1 = new Warehouse();
        Warehouse war2 = new Warehouse();

        assertFalse(war1.equals(null));
        assertFalse(war1.equals(5));
        assertTrue(war1.equals(war1));
        assertEquals(war1.hashCode(), war1.hashCode());

        war1.setName("test");
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setName("aaa");
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setName("test");
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        assertEquals(war1.hashCode(), war2.hashCode());
        
        war1.setNotes("test");
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setNotes("aaa");
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setNotes("test");
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        assertEquals(war1.hashCode(), war2.hashCode());

        
        war1.setEmail(new Email("test@tes.com", "lab"));
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setEmail(new Email("test@1.com", "lab"));
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setEmail(new Email("test@tes.com", "lab"));
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        
        assertTrue(war1.hashCode() == war2.hashCode());
        
        war1.setEmail(null);
        war2.setEmail(null);

        
        
        war1.setPhone(new Phone("689558899", "lab"));
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setPhone(new Phone("689996655", "lab"));
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        war2.setPhone(new Phone("689558899", "lab"));
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        
        assertTrue(war1.hashCode() == war2.hashCode());

        war2.setPhone(null);
        war1.setPhone(null);
        

        Address add4 = new Address();
        add4.setCity("test");
        add4.setLabel("label");
        war1.setAddress(add4);
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        Address add5 = new Address();
        add5.setCity("test");
        add5.setLabel("lab");
        war2.setAddress(add5);
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));

        add5.setLabel("label");
        war2.setAddress(add5);
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        
        assertTrue(war1.hashCode() == war2.hashCode());

        war2.setAddress(null);
        war1.setAddress(null);
        
        
        war1.setEnabled(true);
        war2.setEnabled(false);
        assertFalse(war1.equals(war2));
        assertFalse(war2.equals(war1));
        assertFalse(war1.hashCode() == war2.hashCode());
        
        war1.setEnabled(false);
        war2.setEnabled(false);
        assertTrue(war1.equals(war2));
        assertTrue(war2.equals(war1));
        assertTrue(war1.hashCode() == war2.hashCode());
    }
}
