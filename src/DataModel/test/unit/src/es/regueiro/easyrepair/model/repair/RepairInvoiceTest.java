/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.repair;

import es.regueiro.easyrepair.model.employee.Employee;
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
public class RepairInvoiceTest {
    
    public RepairInvoiceTest() {
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
     * Test of getEnabled method, of class RepairInvoice.
     */
    @Test
    public void testRepairInvoice() {
        System.out.println("Testing Repair Order Invoice Components");
        
        RepairOrder ord = new RepairOrder();
        RepairInvoice inv = ord.getInvoice();

        inv.setInvoiceNumber("57948484964");
        inv.setPaymentMethod("testpay");
        inv.setStatus("payed");
        inv.setPaymentResponsible("resp");
        inv.setNotes("notes");
        inv.setId(Long.parseLong("20"));
        inv.setVersion(2);

        Employee emp = new Employee();
        emp.setName("tom");
        inv.setResponsible(emp);

        LocalDate invDate = new LocalDate(2012, 1, 2);
        LocalDate accDate = new LocalDate(2012, 1, 3);
        LocalDate estDate = new LocalDate(2012, 1, 4);
        LocalDate payDate = new LocalDate(2012, 1, 5);

        inv.setInvoiceDate(invDate);
        inv.setAcceptedDate(accDate);
        inv.setEstimatedPaymentDate(estDate);
        inv.setPaymentDate(payDate);

        // Comprobamos que asigna correctamente
        assertEquals("57948484964", inv.getInvoiceNumber());
        assertEquals("testpay", inv.getPaymentMethod());
        assertEquals(invDate, inv.getInvoiceDate());
        assertEquals(accDate, inv.getAcceptedDate());
        assertEquals(estDate, inv.getEstimatedPaymentDate());
        assertEquals(payDate, inv.getPaymentDate());
        assertEquals("payed", inv.getStatus());
        assertEquals("resp", inv.getPaymentResponsible());
        assertEquals("notes", inv.getNotes());
        assertEquals(emp, inv.getResponsible());
        assertEquals(ord, inv.getOrder());
        assertTrue(Long.parseLong("20") == inv.getId());
        assertEquals(2, inv.getVersion());

        
        // Comprobamos el comportamiento con cadenas vacias y nulos
        inv.setPaymentResponsible("");
        assertNull(inv.getPaymentResponsible());
        inv.setPaymentResponsible("    ");
        assertNull(inv.getPaymentResponsible());
        inv.setPaymentResponsible(null);
        assertNull(inv.getPaymentResponsible());

        // Comprobamos el funcionamiento de equals y hashcode
        RepairOrder ord1 = new RepairOrder();
        RepairOrder ord2 = new RepairOrder();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        emp1.setName("tio");

        RepairInvoice inv1 = ord1.getInvoice();
        RepairInvoice inv2 = ord2.getInvoice();
        ord1.setNotes("not");
        

        assertFalse(inv1.equals(null));
        assertFalse(inv1.equals(5));
        assertTrue(inv1.equals(inv1));
        assertEquals(inv1.hashCode(), inv1.hashCode());

        inv1.setInvoiceNumber("1");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setInvoiceNumber("2");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setInvoiceNumber("1");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        
        
        inv1.setInvoiceDate(new LocalDate(2012,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setInvoiceDate(new LocalDate(2011,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setInvoiceDate(new LocalDate(2012,2,2));
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        inv1.setInvoiceDate(null);
        inv2.setInvoiceDate(null);
        
        
        inv1.setAcceptedDate(new LocalDate(2012,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setAcceptedDate(new LocalDate(2011,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setAcceptedDate(new LocalDate(2012,2,2));
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        inv1.setAcceptedDate(null);
        inv2.setAcceptedDate(null);
        
        
        inv1.setEstimatedPaymentDate(new LocalDate(2012,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setEstimatedPaymentDate(new LocalDate(2011,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setEstimatedPaymentDate(new LocalDate(2012,2,2));
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        inv1.setEstimatedPaymentDate(null);
        inv2.setEstimatedPaymentDate(null);
        
        
        inv1.setPaymentDate(new LocalDate(2012,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentDate(new LocalDate(2011,2,2));
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentDate(new LocalDate(2012,2,2));
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        inv1.setPaymentDate(null);
        inv2.setPaymentDate(null);
        
        
        
        inv1.setPaymentMethod("1");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentMethod("2");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentMethod("1");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
                
                
                
        inv1.setStatus("1");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setStatus("2");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setStatus("1");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        
        inv1.setPaymentResponsible("1");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentResponsible("2");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setPaymentResponsible("1");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        
        inv1.setNotes("1");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setNotes("2");
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setNotes("1");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        
        
        inv1.setResponsible(emp1);
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        inv2.setResponsible(emp2);
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));

        emp2.setName("tio");
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        inv2.setResponsible(emp1);
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
        
        
        ord1.setEnabled(true);
        ord2.setEnabled(false);
        assertFalse(inv1.equals(inv2));
        assertFalse(inv2.equals(inv1));
        assertFalse(inv1.hashCode() == inv2.hashCode());
        
        ord1.setEnabled(false);
        ord2.setEnabled(false);
        assertTrue(inv1.equals(inv2));
        assertTrue(inv2.equals(inv1));
        assertEquals(inv1.hashCode(), inv2.hashCode());
    }
}
