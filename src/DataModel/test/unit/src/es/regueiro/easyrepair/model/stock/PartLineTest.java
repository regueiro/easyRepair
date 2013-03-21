/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

import java.math.BigDecimal;
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
public class PartLineTest {

    public PartLineTest() {
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
     * Test of getId method, of class PartLine.
     */
    @Test
    public void testPartLine() {
        System.out.println("Testing PartLine Components");
        PartLine pli = new PartLine();
        Part par = new Part();
        par.setMake("name");

        pli.setPart(par);
        pli.setQuantity(12);
        pli.setDiscount("10.5");
        pli.setVersion(2);
        pli.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals(par, pli.getPart());
        assertEquals(12, pli.getQuantity());
        assertTrue(new BigDecimal("10.5").equals(pli.getDiscount()));
        assertEquals(2, pli.getVersion());
        assertTrue(Long.parseLong("20") == pli.getId());



        // La cantidad no pueden ser menor o igual a cero
        try {
            pli.setQuantity(-19);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            pli.setQuantity(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        // El descuento no pueden ser mayor de cien o menor de cero
        try {
            pli.setDiscount("120");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            pli.setDiscount("-12");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            pli.setDiscount("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        
        // Comprobamos el comportamiento con nulos
        pli.setDiscount(null);
        assertTrue(new BigDecimal("0").equals(pli.getDiscount()));

        // Comprobamos el funcionamiento de equals y hashcode
        PartLine pli1 = new PartLine();
        PartLine pli2 = new PartLine();
        Part par1 = new Part();
        Part par2 = new Part();
        Part par3 = new Part();
        par1.setMake("1");
        par2.setMake("2");
        par3.setMake("1");

        assertFalse(pli1.equals(null));
        assertFalse(pli1.equals(5));
        assertTrue(pli1.equals(pli1));
        assertEquals(pli1.hashCode(), pli1.hashCode());

        pli1.setPart(par1);
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setPart(par2);
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setPart(par3);
        assertTrue(pli1.equals(pli2));
        assertTrue(pli2.equals(pli1));
        assertEquals(pli1.hashCode(), pli2.hashCode());
        
        pli2.setPart(par1);
        assertTrue(pli1.equals(pli2));
        assertTrue(pli2.equals(pli1));
        assertEquals(pli1.hashCode(), pli2.hashCode());


        pli1.setQuantity(1);
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setQuantity(3);
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setQuantity(1);
        assertTrue(pli1.equals(pli2));
        assertTrue(pli2.equals(pli1));
        assertEquals(pli1.hashCode(), pli2.hashCode());
        
        
        pli1.setDiscount("1");
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setDiscount("3");
        assertFalse(pli1.equals(pli2));
        assertFalse(pli2.equals(pli1));

        pli2.setDiscount("1");
        assertTrue(pli1.equals(pli2));
        assertTrue(pli2.equals(pli1));
        assertEquals(pli1.hashCode(), pli2.hashCode());
    }
}
