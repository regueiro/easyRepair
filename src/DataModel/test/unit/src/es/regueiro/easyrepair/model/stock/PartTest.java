/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

import java.math.BigDecimal;
import java.util.List;
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
public class PartTest {
    
    public PartTest() {
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
     * Test of setStock method, of class Part.
     */
    @Test
    public void testPart() {
        System.out.println("Testing Part Components");
        Part par = new Part();
        Stock sto = new Stock();

        par.setMake("make");
        par.setModel("model");
        par.setCategory("cat");
        par.setNotes("notes");
        par.setPrice("10.5");
        par.setVersion(2);
        par.setEnabled(true);
        par.addStock(sto);
        par.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals("make", par.getMake());
        assertEquals("model", par.getModel());
        assertEquals("cat", par.getCategory());
        assertEquals("notes", par.getNotes());
        assertTrue(new BigDecimal("10.5").equals(par.getPrice()));
        assertEquals(2, par.getVersion());
        assertTrue(Long.parseLong("20") == par.getId());
        assertTrue(par.getEnabled());
        assertTrue(par.getStock().contains(sto));
        
        
        // Comprobamos el comportamiento con cadenas vacias y nulos
        par.setMake("");
        assertNull(par.getMake());
        par.setMake("    ");
        assertNull(par.getMake());
        par.setMake(null);
        assertNull(par.getMake());
        
        par.setModel("");
        assertNull(par.getModel());
        par.setModel("    ");
        assertNull(par.getModel());
        par.setModel(null);
        assertNull(par.getModel());
        
        par.setCategory("");
        assertNull(par.getCategory());
        par.setCategory("    ");
        assertNull(par.getCategory());
        par.setCategory(null);
        assertNull(par.getCategory());
        
        par.setNotes("");
        assertNull(par.getNotes());
        par.setNotes("    ");
        assertNull(par.getNotes());
        par.setNotes(null);
        assertNull(par.getNotes());
        
        // Comprobamos el funcionamiento del stock
        Stock sto1 = new Stock();
        Warehouse war1 = new Warehouse();
        war1.setId(Long.parseLong("12"));
        sto1.setWarehouse(war1);
        par.addStock(sto1);
        

        assertTrue(par.hasStockInWarehouse(sto1.getWarehouse().getId()));
        assertFalse(par.hasStockInWarehouse(Long.parseLong("123")));
        assertTrue(par.getStock().contains(sto1));
        
        par.setStock(null);
        assertNull(par.getStock());
        par.addStock(sto1);
        assertTrue(par.getStock().contains(sto1));
        assertTrue(par.getStock().size() == 1);
        par.addStock(sto1);
        assertTrue(par.getStock().contains(sto1));
        assertTrue(par.getStock().size() == 1);
        par.removeStock(sto1.getWarehouse().getId());
        assertTrue(par.getStock().isEmpty());
        
        assertFalse(par.hasStockInWarehouse(sto1.getWarehouse().getId()));
        
        
        
        par.removeStock(sto1.getWarehouse().getId());
        assertTrue(par.getStock().isEmpty());
        
        
        par.setStock(null);
        par.removeStock(sto1.getWarehouse().getId());
        assertNull(par.getStock());
        
        assertFalse(par.hasStockInWarehouse(sto1.getWarehouse().getId()));
        
        par.clearStock();
        assertTrue(par.getStock().isEmpty());
        par.addStock(sto1);
        par.removeStock(Long.parseLong("123"));
        par.clearStock();
        assertTrue(par.getStock().isEmpty());
        par.setStock(null);
        par.clearStock();
        assertTrue(par.getStock().isEmpty());
        
        
        
        // El precio no pueden ser menor de cero
        try {
            par.setPrice("-13");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            par.setPrice("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
         // Comprobamos el comportamiento con nulos
        par.setPrice(null);
        assertTrue(new BigDecimal("0").equals(par.getPrice()));
        
        
        // Comprobamos el funcionamiento de equals y hashcode
        Part par1 = new Part();
        Part par2 = new Part();
        Stock sto2 = new Stock();
        Stock sto3 = new Stock();
        Warehouse war2 = new Warehouse();
        sto2.setWarehouse(war2);
        

        assertFalse(par1.equals(null));
        assertFalse(par1.equals(5));
        assertTrue(par1.equals(par1));
        assertEquals(par1.hashCode(), par1.hashCode());

        par1.setMake("1");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setMake("2");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setMake("1");
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());


        par1.setModel("1");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setModel("2");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setModel("1");
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
        
        
        par1.setCategory("1");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setCategory("2");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setCategory("1");
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
        
        par1.setNotes("1");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setNotes("2");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setNotes("1");
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
        
        par1.setPrice("1");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setPrice("2");
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.setPrice("1");
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
        
        par1.addStock(sto2);
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.addStock(sto3);
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));

        par2.clearStock();
        par2.addStock(sto2);
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
        
        par1.setEnabled(true);
        par2.setEnabled(false);
        assertFalse(par1.equals(par2));
        assertFalse(par2.equals(par1));
        assertFalse(par1.hashCode() == par2.hashCode());
        
        par1.setEnabled(false);
        par2.setEnabled(false);
        assertTrue(par1.equals(par2));
        assertTrue(par2.equals(par1));
        assertEquals(par1.hashCode(), par2.hashCode());
    }
}
