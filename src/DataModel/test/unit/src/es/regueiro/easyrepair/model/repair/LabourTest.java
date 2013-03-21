/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.repair;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class LabourTest {
    
    public LabourTest() {
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
     * Test of getDescription method, of class Labour.
     */
    @Test
    public void testLabour() {
        System.out.println("Testing Labour Components");
        Labour lab = new Labour();

        lab.setName("name");
        lab.setDescription("desc");
        lab.setNotes("notes");
        lab.setPrice("10.5");
        lab.setVersion(2);
        lab.setEnabled(true);
        lab.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals("name", lab.getName());
        assertEquals("desc", lab.getDescription());
        assertEquals("notes", lab.getNotes());
        assertTrue(new BigDecimal("10.5").equals(lab.getPrice()));
        assertEquals(2, lab.getVersion());
        assertTrue(Long.parseLong("20") == lab.getId());
        assertTrue(lab.getEnabled());
        
        
        // Comprobamos el comportamiento con cadenas vacias y nulos
        lab.setDescription("");
        assertNull(lab.getDescription());
        lab.setDescription("    ");
        assertNull(lab.getDescription());
        lab.setDescription(null);
        assertNull(lab.getDescription());
        
        
        lab.setNotes("");
        assertNull(lab.getNotes());
        lab.setNotes("    ");
        assertNull(lab.getNotes());
        lab.setNotes(null);
        assertNull(lab.getNotes());
        
        
        // El nombre no puede ser nulo
        try {
            lab.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            lab.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        
        // El precio no pueden ser menor de cero
        try {
            lab.setPrice("-13");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            lab.setPrice("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
         // Comprobamos el comportamiento con nulos
        lab.setPrice(null);
        assertTrue(new BigDecimal("0").equals(lab.getPrice()));
        
        
        // Comprobamos el funcionamiento de equals y hashcode
        Labour lab1 = new Labour();
        Labour lab2 = new Labour();
        

        assertFalse(lab1.equals(null));
        assertFalse(lab1.equals(5));
        assertTrue(lab1.equals(lab1));
        assertEquals(lab1.hashCode(), lab1.hashCode());

        lab1.setName("1");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setName("2");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setName("1");
        assertTrue(lab1.equals(lab2));
        assertTrue(lab2.equals(lab1));
        assertEquals(lab1.hashCode(), lab2.hashCode());


        lab1.setDescription("1");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setDescription("2");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setDescription("1");
        assertTrue(lab1.equals(lab2));
        assertTrue(lab2.equals(lab1));
        assertEquals(lab1.hashCode(), lab2.hashCode());
        
                
        lab1.setNotes("1");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setNotes("2");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setNotes("1");
        assertTrue(lab1.equals(lab2));
        assertTrue(lab2.equals(lab1));
        assertEquals(lab1.hashCode(), lab2.hashCode());
        
        lab1.setPrice("1");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setPrice("2");
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));

        lab2.setPrice("1");
        assertTrue(lab1.equals(lab2));
        assertTrue(lab2.equals(lab1));
        assertEquals(lab1.hashCode(), lab2.hashCode());
        

        lab1.setEnabled(true);
        lab2.setEnabled(false);
        assertFalse(lab1.equals(lab2));
        assertFalse(lab2.equals(lab1));
        assertFalse(lab1.hashCode() == lab2.hashCode());
        
        lab1.setEnabled(false);
        lab2.setEnabled(false);
        assertTrue(lab1.equals(lab2));
        assertTrue(lab2.equals(lab1));
        assertEquals(lab1.hashCode(), lab2.hashCode());
    }
}
