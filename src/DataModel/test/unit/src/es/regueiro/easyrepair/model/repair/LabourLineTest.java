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
public class LabourLineTest {
    
    public LabourLineTest() {
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
     * Test of getId method, of class LabourLine.
     */
    @Test
    public void testLabourLine() {
        System.out.println("Testing LabourLine Components");
        LabourLine lli = new LabourLine();
        Labour lab = new Labour();
        lab.setName("name");

        lli.setLabour(lab);
        lli.setHours("12");
        lli.setDiscount("10.5");
        lli.setVersion(2);
        lli.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals(lab, lli.getLabour());
        assertTrue(new BigDecimal("12").equals(lli.getHours()));
        assertTrue(new BigDecimal("10.5").equals(lli.getDiscount()));
        assertEquals(2, lli.getVersion());
        assertTrue(Long.parseLong("20") == lli.getId());



        // La cantidad no pueden ser menor  a cero
        try {
            lli.setHours("-19");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            lli.setHours("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        lli.setHours(null);
        assertTrue(new BigDecimal("0").equals(lli.getHours()));
        
        // El descuento no pueden ser mayor de cien o menor de cero
        try {
            lli.setDiscount("120");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            lli.setDiscount("-12");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            lli.setDiscount("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        
        // Comprobamos el comportamiento con nulos
        lli.setDiscount(null);
        assertTrue(new BigDecimal("0").equals(lli.getDiscount()));

        // Comprobamos el funcionamiento de equals y hashcode
        LabourLine lli1 = new LabourLine();
        LabourLine lli2 = new LabourLine();
        Labour lab1 = new Labour();
        Labour lab2 = new Labour();
        Labour lab3 = new Labour();
        lab1.setName("1");
        lab2.setName("2");
        lab3.setName("1");

        assertFalse(lli1.equals(null));
        assertFalse(lli1.equals(5));
        assertTrue(lli1.equals(lli1));
        assertEquals(lli1.hashCode(), lli1.hashCode());

        lli1.setLabour(lab1);
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setLabour(lab2);
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setLabour(lab3);
        assertTrue(lli1.equals(lli2));
        assertTrue(lli2.equals(lli1));
        assertEquals(lli1.hashCode(), lli2.hashCode());
        
        lli2.setLabour(lab1);
        assertTrue(lli1.equals(lli2));
        assertTrue(lli2.equals(lli1));
        assertEquals(lli1.hashCode(), lli2.hashCode());


        lli1.setHours("1");
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setHours("12");
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setHours("1");
        assertTrue(lli1.equals(lli2));
        assertTrue(lli2.equals(lli1));
        assertEquals(lli1.hashCode(), lli2.hashCode());
        
        
        lli1.setDiscount("1");
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setDiscount("3");
        assertFalse(lli1.equals(lli2));
        assertFalse(lli2.equals(lli1));

        lli2.setDiscount("1");
        assertTrue(lli1.equals(lli2));
        assertTrue(lli2.equals(lli1));
        assertEquals(lli1.hashCode(), lli2.hashCode());
    }
}
