/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.repair;

import es.regueiro.easyrepair.model.employee.Employee;
import java.math.BigDecimal;
import org.joda.time.LocalDate;
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
public class EstimateTest {
    
    public EstimateTest() {
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
     * Test of getOrder method, of class Estimate.
     */
    @Test
    public void testEstimate() {
        System.out.println("Testing Estimate Components");
        
        RepairOrder ord = new RepairOrder();
        Estimate est = ord.getEstimate();

        est.setEstimateNumber("57948484964");
        est.setStatus("payed");
        est.setNotes("notes");
        est.setId(Long.parseLong("20"));
        est.setVersion(2);

        Employee emp = new Employee();
        emp.setName("tom");
        est.setResponsible(emp);

        LocalDate estDate = new LocalDate(2012, 1, 2);
        LocalDate accDate = new LocalDate(2012, 1, 3);

        est.setEstimateDate(estDate);
        est.setAcceptedDate(accDate);

        // Comprobamos que asigna correctamente
        assertEquals("57948484964", est.getEstimateNumber());
        assertEquals(estDate, est.getEstimateDate());
        assertEquals(accDate, est.getAcceptedDate());
        assertEquals("payed", est.getStatus());
        assertEquals("notes", est.getNotes());
        assertEquals(emp, est.getResponsible());
        assertEquals(ord, est.getOrder());
        assertTrue(Long.parseLong("20") == est.getId());
        assertEquals(2, est.getVersion());

        
        // Comprobamos el comportamiento con cadenas vacias y nulos
        est.setEstimateNumber("");
        assertNull(est.getEstimateNumber());
        est.setEstimateNumber("    ");
        assertNull(est.getEstimateNumber());
        est.setEstimateNumber(null);
        assertNull(est.getEstimateNumber());
        
        
        est.setNotes("");
        assertNull(est.getNotes());
        est.setNotes("    ");
        assertNull(est.getNotes());
        est.setNotes(null);
        assertNull(est.getNotes());
        
        est.setStatus("");
        assertNull(est.getStatus());
        est.setStatus("    ");
        assertNull(est.getStatus());
        est.setStatus(null);
        assertNull(est.getStatus());

        // Comprobamos que no asigna ids incorrectas
        try {
            est.setEstimateNumber("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            est.setEstimateNumber("123456789123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            est.setEstimateNumber("12341215678s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            est.setEstimateNumber("-1234567891");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        
        
        // El descuento no pueden ser mayor de cien o menor de cero
        try {
            est.setDiscount("120");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            est.setDiscount("-12");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            est.setDiscount("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        est.setDiscount(null);
        assertTrue(new BigDecimal("0").equals(est.getDiscount()));
        
        
        
        // Comprobación de los rangos de las fechas
        LocalDate i2012 = new LocalDate(2012, 1, 2);
        LocalDate i2011 = new LocalDate(2011, 1, 2);
        LocalDate i2010 = new LocalDate(2010, 1, 2);
        LocalDate i2009 = new LocalDate(2009, 1, 2);

        est.setEstimateDate(null);
        est.setAcceptedDate(null);

        est.setEstimateDate(i2009);
        est.setAcceptedDate(i2010);

        est.setAcceptedDate(null);
        est.setEstimateDate(i2012);


        try {
            // La fecha de aceptación no puede ser antes de la fecha del presupuesto
            est.setAcceptedDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
         est.setAcceptedDate(i2012);


        est.setEstimateDate(null);
        est.setAcceptedDate(i2011);
        est.setAcceptedDate(null);

        
        
        est.setAcceptedDate(i2010);
        try {
            // La fecha del presupuesto no puede ser despues de la fecha de aceptacion
            est.setEstimateDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        est.setEstimateDate(i2009);
        
        
        
        // Comprobamos el funcionamiento de equals y hashcode
        RepairOrder ord1 = new RepairOrder();
        RepairOrder ord2 = new RepairOrder();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        emp1.setName("tio");

        Estimate est1 = ord1.getEstimate();
        Estimate est2 = ord2.getEstimate();
        ord1.setNotes("not");
        

        assertFalse(est1.equals(null));
        assertFalse(est1.equals(5));
        assertTrue(est1.equals(est1));
        assertEquals(est1.hashCode(), est1.hashCode());

        est1.setEstimateNumber("1");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setEstimateNumber("2");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setEstimateNumber("1");
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        
        
        est1.setEstimateDate(new LocalDate(2012,2,2));
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setEstimateDate(new LocalDate(2011,2,2));
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setEstimateDate(new LocalDate(2012,2,2));
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        est1.setEstimateDate(null);
        est2.setEstimateDate(null);
        
        
        est1.setAcceptedDate(new LocalDate(2012,2,2));
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setAcceptedDate(new LocalDate(2011,2,2));
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setAcceptedDate(new LocalDate(2012,2,2));
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        est1.setAcceptedDate(null);
        est2.setAcceptedDate(null);
        
                
        est1.setStatus("1");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setStatus("2");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setStatus("1");
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        
        
        est1.setNotes("1");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setNotes("2");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setNotes("1");
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        
        
        est1.setDiscount("1");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setDiscount("2");
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setDiscount("1");
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        
        
        est1.setResponsible(emp1);
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        est2.setResponsible(emp2);
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));

        emp2.setName("tio");
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        est2.setResponsible(emp1);
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
        
        
        ord1.setEnabled(true);
        ord2.setEnabled(false);
        assertFalse(est1.equals(est2));
        assertFalse(est2.equals(est1));
        assertFalse(est1.hashCode() == est2.hashCode());
        
        ord1.setEnabled(false);
        ord2.setEnabled(false);
        assertTrue(est1.equals(est2));
        assertTrue(est2.equals(est1));
        assertEquals(est1.hashCode(), est2.hashCode());
    }
}
