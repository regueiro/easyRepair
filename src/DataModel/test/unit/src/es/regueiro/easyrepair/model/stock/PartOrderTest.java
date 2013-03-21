/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.employee.Employee;
import java.math.BigDecimal;
import java.util.List;
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
public class PartOrderTest {

    public PartOrderTest() {
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
     * Test of getOrderNumber method, of class PartOrder.
     */
    @Test
    public void testPartOrder() {
        System.out.println("Testing Part Order Components");

        PartOrder ord = new PartOrder();
        Warehouse war = new Warehouse();
        war.setName("aa");
        Supplier sup = new Supplier();
        sup.setName("sup");
        Employee emp = new Employee();
        emp.setName("emp");
        Part par = new Part();
        par.setMake("make");
        PartLine pli = new PartLine();
        pli.setPart(par);

        ord.setOrderNumber("57948484964");
        ord.setShippingWarehouse(war);
        ord.setShippingCosts("11.5");
        ord.setOtherCosts("111.5");
        ord.setDiscount("14");
        ord.setStatus("status");
        ord.setNotes("notes");
        ord.setSupplier(sup);
        ord.setResponsible(emp);
        ord.addPart(pli);
        ord.setId(Long.parseLong("20"));
        ord.setVersion(2);

        LocalDate ordDate = new LocalDate(2012, 1, 2);
        LocalDate estDate = new LocalDate(2012, 1, 4);
        LocalDate recDate = new LocalDate(2012, 1, 5);

        ord.setOrderDate(ordDate);
        ord.setEstimatedDate(estDate);
        ord.setReceiptDate(recDate);

        // Comprobamos que asigna correctamente
        assertEquals("57948484964", ord.getOrderNumber());
        assertEquals(war, ord.getShippingWarehouse());
        assertTrue(new BigDecimal("11.5").equals(ord.getShippingCosts()));
        assertTrue(new BigDecimal("111.5").equals(ord.getOtherCosts()));
        assertTrue(new BigDecimal("14").equals(ord.getDiscount()));
        assertEquals("status", ord.getStatus());
        assertEquals("notes", ord.getNotes());
        assertEquals(sup, ord.getSupplier());
        assertEquals(emp, ord.getResponsible());
        assertTrue(ord.getPartsList().contains(pli));
        assertEquals(ordDate, ord.getOrderDate());
        assertEquals(estDate, ord.getEstimatedDate());
        assertEquals(recDate, ord.getReceiptDate());
        assertTrue(Long.parseLong("20") == ord.getId());
        assertEquals(2, ord.getVersion());


        PartOrder p2 = new PartOrder();

        // Comprobamos el comportamiento con cadenas vacias y nulos
        p2.setOrderNumber("");
        assertNull(p2.getOrderNumber());
        p2.setOrderNumber("    ");
        assertNull(p2.getOrderNumber());
        p2.setOrderNumber(null);
        assertNull(p2.getOrderNumber());

        p2.setNotes("");
        assertNull(p2.getNotes());
        p2.setNotes("    ");
        assertNull(p2.getNotes());
        p2.setNotes(null);
        assertNull(p2.getNotes());

        p2.setStatus("");
        assertNull(p2.getStatus());
        p2.setStatus("    ");
        assertNull(p2.getStatus());
        p2.setStatus(null);
        assertNull(p2.getStatus());


        // Comprobamos que no asigna ids incorrectas
        try {
            p2.setOrderNumber("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            p2.setOrderNumber("123456789112");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            p2.setOrderNumber("1234567813s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            p2.setOrderNumber("-1234567123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        // El descuento no pueden ser mayor de cien o menor de cero
        try {
            p2.setDiscount("120");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            p2.setDiscount("-12");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            p2.setDiscount("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        p2.setDiscount(null);
        assertTrue(new BigDecimal("0").equals(p2.getDiscount()));


        // Otros costes ha de ser un número
        try {
            p2.setOtherCosts("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        p2.setOtherCosts(null);
        assertTrue(new BigDecimal("0").equals(p2.getOtherCosts()));


        // Costes de envío ha de ser un número
        try {
            p2.setShippingCosts("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        p2.setShippingCosts(null);
        assertTrue(new BigDecimal("0").equals(p2.getShippingCosts()));


        // Comprobación de los rangos de las fechas
        LocalDate i2012 = new LocalDate(2012, 1, 2);
        LocalDate i2011 = new LocalDate(2011, 1, 2);
        LocalDate i2010 = new LocalDate(2010, 1, 2);
        LocalDate i2009 = new LocalDate(2009, 1, 2);

        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setReceiptDate(null);

        ord.setOrderDate(i2009);
        ord.setEstimatedDate(i2011);
        ord.setReceiptDate(i2012);

        ord.setEstimatedDate(null);
        ord.setReceiptDate(null);
        ord.setOrderDate(i2012);


        try {
            // La fecha estimada no puede ser antes de la fecha de la orden
            ord.setEstimatedDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            // La fecha de recibo no puede ser antes de la fecha de la orden
            ord.setReceiptDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(null);
        ord.setReceiptDate(i2010);


        try {
            // La fecha de la orden no puede ser despues de la fecha de recibo
            ord.setOrderDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(i2009);

        ord.setReceiptDate(null);
        ord.setOrderDate(null);
        ord.setEstimatedDate(i2010);
        try {
            // La fecha de la orden no puede ser despues de la fecha estimada
            ord.setOrderDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(i2009);



        
        // Comprobamos el funcionamiento de las partes
        PartOrder ord1 = new PartOrder();
        Part par2 = new Part();
        par2.setMake("make");
        par2.setId(Long.parseLong("14"));
        
        PartLine pli2 = new PartLine();
        pli2.setPart(par2);
        pli2.setId(Long.parseLong("12"));
//        Warehouse war1 = new Warehouse();
//        war1.setId(Long.parseLong("12"));
//        pli2.setS(war1);
//        par.addStock(sto1);

        ord1.addPart(pli2);


        
        assertTrue(ord1.hasPart(par2.getId()));
        assertFalse(ord1.hasPart(Long.parseLong("123")));
        assertTrue(ord1.getPartsList().contains(pli2));

        ord1.setPartsList(null);
        assertNull(ord1.getPartsList());
        ord1.addPart(pli2);
        assertTrue(ord1.getPartsList().contains(pli2));
        assertTrue(ord1.getPartsList().size() == 1);
        ord1.addPart(pli2);
        assertTrue(ord1.getPartsList().contains(pli2));
        assertTrue(ord1.getPartsList().size() == 1);
        ord1.removePart(par2.getId());
        assertTrue(ord1.getPartsList().isEmpty());

        assertFalse(ord1.hasPart(par2.getId()));



        ord1.setPartsList(null);
        ord1.removePart(par2.getId());
        assertNull(ord1.getPartsList());

        assertFalse(ord1.hasPart(par2.getId()));

        ord1.clearPartsList();
        assertTrue(ord1.getPartsList().isEmpty());
        ord1.addPart(pli2);
        ord1.removePart(Long.parseLong("123"));
        ord1.clearPartsList();
        assertTrue(ord1.getPartsList().isEmpty());
        ord1.setPartsList(null);
        ord1.clearPartsList();
        assertTrue(ord1.getPartsList().isEmpty());


        Part par3 = new Part();
        par3.setMake("m");
        PartLine pli3 = new PartLine();
        pli3.setPart(par3);
        
        ord1.addPart(pli2);
        assertTrue(ord1.getPartsList().contains(pli2));
        ord1.removePartLine(pli3);
        assertTrue(ord1.getPartsList().contains(pli2));
        ord1.removePartLine(pli2);
        assertFalse(ord1.getPartsList().contains(pli2));
        
        ord1.setPartsList(null);
        ord1.removePartLine(pli3);
        


        // Comprobamos el funcionamiento de equals y hashcode
        PartOrder ord5 = new PartOrder();
        PartOrder ord6 = new PartOrder();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        emp1.setName("tio");
        Warehouse war1 = new Warehouse();
        Warehouse war2 = new Warehouse();
        war1.setName("r");
        Supplier sup1 = new Supplier();
        Supplier sup2 = new Supplier();
        sup1.setName("sup");
        Part par4 = new Part();
        Part par5 = new Part();
        par4.setMake("make");
        par5.setMake("no");
        PartLine pli4 = new PartLine();
        PartLine pli5 = new PartLine();
        
        pli4.setPart(par4);
        pli5.setPart(par5);
        
        

        assertFalse(ord5.equals(null));
        assertFalse(ord5.equals(5));
        assertTrue(ord5.equals(ord5));
        assertEquals(ord5.hashCode(), ord5.hashCode());

        ord5.setOrderNumber("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderNumber("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderNumber("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.setOrderDate(new LocalDate(2012,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderDate(new LocalDate(2011,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderDate(new LocalDate(2012,2,2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setOrderDate(null);
        ord6.setOrderDate(null);
        
        
        
        
        ord5.setEstimatedDate(new LocalDate(2012,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setEstimatedDate(new LocalDate(2011,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setEstimatedDate(new LocalDate(2012,2,2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setEstimatedDate(null);
        ord6.setEstimatedDate(null);
        
        
        ord5.setReceiptDate(new LocalDate(2012,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setReceiptDate(new LocalDate(2011,2,2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setReceiptDate(new LocalDate(2012,2,2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setReceiptDate(null);
        ord6.setReceiptDate(null);
        
        
        
        ord5.setShippingWarehouse(war1);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setShippingWarehouse(war2);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));
 
        war2.setName("r");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        ord6.setShippingWarehouse(war1);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
                
        
        ord5.setShippingCosts("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setShippingCosts("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setShippingCosts("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        ord5.setOtherCosts("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOtherCosts("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOtherCosts("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
                
        
        
        ord5.setDiscount("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDiscount("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDiscount("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.setStatus("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setStatus("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setStatus("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.setNotes("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setNotes("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setNotes("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.setSupplier(sup1);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setSupplier(sup2);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        sup2.setName("sup");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        ord6.setSupplier(sup1);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.addPart(pli4);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.addPart(pli5);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.clearPartsList();
        
        ord6.addPart(pli4);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        ord5.setResponsible(emp1);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setResponsible(emp2);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        emp2.setName("tio");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        ord6.setResponsible(emp1);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        PartOrderInvoice inv5 = ord5.getInvoice();
        PartOrderInvoice inv6 = ord6.getInvoice();
        
        inv5.setNotes("aa");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        inv6.setNotes("bb");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        inv6.setNotes("aa");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        
        
        
        ord5.setEnabled(true);
        ord6.setEnabled(false);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));
        assertFalse(ord5.hashCode() == ord6.hashCode());
        
        ord5.setEnabled(false);
        ord6.setEnabled(false);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
    }
}
