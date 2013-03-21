/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.shared;

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
public class InvoiceTest {

    public InvoiceTest() {
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
     * Test of getPaymentMethod method, of class Invoice.
     */
    @Test
    public void testInvoiceComponents() {
        System.out.println("Testing Invoice Components ");
        Invoice inv = new Invoice();

        inv.setInvoiceNumber("57948484964");
        inv.setPaymentMethod("testpay");
        inv.setStatus("payed");
        inv.setNotes("notes");

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
        assertEquals("notes", inv.getNotes());
        assertEquals(emp, inv.getResponsible());


        // Comprobamos el comportamiento con cadenas vacias y nulos
        inv.setInvoiceNumber("");
        assertNull(inv.getInvoiceNumber());
        inv.setInvoiceNumber("    ");
        assertNull(inv.getInvoiceNumber());
        inv.setInvoiceNumber(null);
        assertNull(inv.getInvoiceNumber());

        inv.setPaymentMethod("");
        assertNull(inv.getPaymentMethod());
        inv.setPaymentMethod("    ");
        assertNull(inv.getPaymentMethod());
        inv.setPaymentMethod(null);
        assertNull(inv.getPaymentMethod());

        inv.setNotes("");
        assertNull(inv.getNotes());
        inv.setNotes("    ");
        assertNull(inv.getNotes());
        inv.setNotes(null);
        assertNull(inv.getNotes());

        inv.setStatus("");
        assertNull(inv.getStatus());
        inv.setStatus("    ");
        assertNull(inv.getStatus());
        inv.setStatus(null);
        assertNull(inv.getStatus());


        // Comprobamos que no asigna ids incorrectas
        try {
            inv.setInvoiceNumber("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            inv.setInvoiceNumber("123456789123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            inv.setInvoiceNumber("12341215678s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            inv.setInvoiceNumber("-1234567891");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        // Comprobación de los rangos de las fechas
        LocalDate i2012 = new LocalDate(2012, 1, 2);
        LocalDate i2011 = new LocalDate(2011, 1, 2);
        LocalDate i2010 = new LocalDate(2010, 1, 2);
        LocalDate i2009 = new LocalDate(2009, 1, 2);

        inv.setInvoiceDate(null);
        inv.setAcceptedDate(null);
        inv.setEstimatedPaymentDate(null);
        inv.setPaymentDate(null);

        inv.setInvoiceDate(i2009);
        inv.setAcceptedDate(i2010);
        inv.setEstimatedPaymentDate(i2011);
        inv.setPaymentDate(i2012);

        inv.setAcceptedDate(null);
        inv.setEstimatedPaymentDate(null);
        inv.setPaymentDate(null);
        inv.setInvoiceDate(i2012);


        try {
            // La fecha de aceptación no puede ser antes de la fecha de la factura
            inv.setAcceptedDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        try {
            // La fecha de pago estimado no puede ser antes de la fecha de la factura
            inv.setEstimatedPaymentDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            // La fecha de pago no puede ser antes de la fecha de la factura
            inv.setPaymentDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        inv.setInvoiceDate(null);
        inv.setAcceptedDate(i2011);
        inv.setAcceptedDate(null);
        inv.setPaymentDate(i2011);
        try {
            // La fecha de aceptación no puede ser despues de la fecha de pago
            inv.setAcceptedDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        inv.setAcceptedDate(i2010);
        inv.setAcceptedDate(null);

        inv.setPaymentDate(null);
        inv.setInvoiceDate(null);
        inv.setAcceptedDate(i2012);
        inv.setEstimatedPaymentDate(null);
        try {
            // La fecha de pago no puede ser antes de la fecha de aceptación
            inv.setPaymentDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        inv.setPaymentDate(null);
        inv.setInvoiceDate(null);
        inv.setAcceptedDate(null);
        inv.setEstimatedPaymentDate(null);


        inv.setPaymentDate(i2011);
        try {
            // La fecha de la factura no puede ser despues de la fecha de pago
            inv.setInvoiceDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        inv.setInvoiceDate(i2010);
        inv.setPaymentDate(null);
        inv.setInvoiceDate(null);
        inv.setAcceptedDate(i2011);
        try {
            // La fecha de la factura no puede ser despues de la fecha de aceptacion
            inv.setInvoiceDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        inv.setInvoiceDate(i2010);
        inv.setAcceptedDate(null);
        inv.setInvoiceDate(null);
        inv.setEstimatedPaymentDate(i2011);
        try {
            // La fecha de la factura no puede ser despues de la fecha de pago estimado
            inv.setInvoiceDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        inv.setInvoiceDate(i2010);
    }
}
