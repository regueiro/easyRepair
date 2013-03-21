/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

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
public class StockTest {
    
    public StockTest() {
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
     * Test of getId method, of class Stock.
     */
    @Test
    public void testStock() {
        System.out.println("Testing Stock Components");
        Stock sto = new Stock();
        Warehouse war = new Warehouse();
        war.setName("name");
        
        sto.setWarehouse(war);
        sto.setUnits(12);
        sto.setId(Long.parseLong("20"));


        // Comprobamos que asigna correctamente
        assertEquals(war, sto.getWarehouse());
        assertEquals(12, sto.getUnits());
        assertTrue(Long.parseLong("20") == sto.getId());
        
        
        
        // Las unidades no pueden ser menor de cero
        try {
            sto.setUnits(-19);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        // Comprobamos el funcionamiento de equals y hashcode
        Stock sto1 = new Stock();
        Stock sto2 = new Stock();
        Warehouse war1 = new Warehouse();
        Warehouse war2 = new Warehouse();
        Warehouse war3 = new Warehouse();
        war1.setName("1");
        war2.setName("2");
        war3.setName("1");

        assertFalse(sto1.equals(null));
        assertFalse(sto1.equals(5));
        assertTrue(sto1.equals(sto1));
        assertEquals(sto1.hashCode(), sto1.hashCode());
        
        assertTrue(sto1.equals(sto2));

        sto1.setWarehouse(war1);
        assertFalse(sto1.equals(sto2));
        assertFalse(sto2.equals(sto1));

        sto2.setWarehouse(war2);
        assertFalse(sto1.equals(sto2));
        assertFalse(sto2.equals(sto1));
        
        sto2.setWarehouse(war3);
        assertTrue(sto1.equals(sto2));
        assertTrue(sto2.equals(sto1));
        assertEquals(sto1.hashCode(), sto2.hashCode());
        
        sto2.setWarehouse(war1);
        assertTrue(sto1.equals(sto2));
        assertTrue(sto2.equals(sto1));
        assertEquals(sto1.hashCode(), sto2.hashCode());
                
        sto1.setUnits(1);
        assertFalse(sto1.equals(sto2));
        assertFalse(sto2.equals(sto1));

        sto2.setUnits(3);
        assertFalse(sto1.equals(sto2));
        assertFalse(sto2.equals(sto1));

        sto2.setUnits(1);
        assertTrue(sto1.equals(sto2));
        assertTrue(sto2.equals(sto1));
        assertEquals(sto1.hashCode(), sto2.hashCode());
    }
}
