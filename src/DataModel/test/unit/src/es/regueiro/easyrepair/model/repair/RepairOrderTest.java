/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.repair;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.PartLine;
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
public class RepairOrderTest {

    public RepairOrderTest() {
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
     * Test of getInvoice method, of class RepairOrder.
     */
    @Test
    public void testRepairOrder() {
        System.out.println("Testing Repair Order Components");

        RepairOrder ord = new RepairOrder();
        Vehicle veh = new Vehicle();
        veh.setMake("aa");
        Employee emp = new Employee();
        emp.setName("emp");
        Part par = new Part();
        par.setMake("make");
        PartLine pli = new PartLine();
        pli.setPart(par);
        Labour lab = new Labour();
        par.setMake("make");
        LabourLine lli = new LabourLine();
        lli.setLabour(lab);

        ord.setOrderNumber("57948484964");
        ord.setGasTankLevel("full");
        ord.setDescription("des");
        ord.setStatus("status");
        ord.setNotes("notes");
        ord.setKilometres("12");
        ord.setVehicle(veh);
        ord.setResponsible(emp);
        ord.addPart(pli);
        ord.addLabour(lli);
        ord.setId(Long.parseLong("20"));
        ord.setVersion(2);

        LocalDate ordDate = new LocalDate(2012, 1, 1);
        LocalDate estDate = new LocalDate(2012, 1, 2);
        LocalDate finDate = new LocalDate(2012, 1, 3);
        LocalDate delDate = new LocalDate(2012, 1, 4);

        ord.setOrderDate(ordDate);
        ord.setEstimatedDate(estDate);
        ord.setFinishDate(finDate);
        ord.setDeliveryDate(delDate);

        // Comprobamos que asigna correctamente
        assertEquals("57948484964", ord.getOrderNumber());
        assertEquals("full", ord.getGasTankLevel());
        assertEquals("des", ord.getDescription());
        assertEquals("status", ord.getStatus());
        assertEquals("notes", ord.getNotes());
        assertEquals("12", ord.getKilometres());
        assertEquals(emp, ord.getResponsible());
        assertEquals(veh, ord.getVehicle());
        assertTrue(ord.getPartsList().contains(pli));
        assertTrue(ord.getLabourList().contains(lli));
        assertEquals(ordDate, ord.getOrderDate());
        assertEquals(estDate, ord.getEstimatedDate());
        assertEquals(finDate, ord.getFinishDate());
        assertEquals(delDate, ord.getDeliveryDate());
        assertTrue(Long.parseLong("20") == ord.getId());
        assertEquals(2, ord.getVersion());


        RepairOrder p2 = new RepairOrder();

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

        p2.setDescription("");
        assertNull(p2.getDescription());
        p2.setDescription("    ");
        assertNull(p2.getDescription());
        p2.setDescription(null);
        assertNull(p2.getDescription());

        p2.setGasTankLevel("");
        assertNull(p2.getGasTankLevel());
        p2.setGasTankLevel("    ");
        assertNull(p2.getGasTankLevel());
        p2.setGasTankLevel(null);
        assertNull(p2.getGasTankLevel());


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


        // El numero de kilometros no pueden ser menor de cero
        try {
            p2.setKilometres("-23120");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            p2.setKilometres("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        p2.setKilometres(null);
        
        assertTrue(p2.getKilometres() == null);


        // Comprobación de los rangos de las fechas
        LocalDate i2012 = new LocalDate(2012, 1, 2);
        LocalDate i2011 = new LocalDate(2011, 1, 2);
        LocalDate i2010 = new LocalDate(2010, 1, 2);
        LocalDate i2009 = new LocalDate(2009, 1, 2);

        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setFinishDate(null);
        ord.setDeliveryDate(null);

        ord.setOrderDate(i2009);
        ord.setEstimatedDate(i2010);
        ord.setFinishDate(i2011);
        ord.setDeliveryDate(i2012);

        ord.setEstimatedDate(null);
        ord.setFinishDate(null);
        ord.setDeliveryDate(null);
        ord.setOrderDate(i2012);


        try {
            // La fecha estimada no puede ser antes de la fecha de la orden
            ord.setEstimatedDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            // La fecha de finalizacion no puede ser antes de la fecha de la orden
            ord.setFinishDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            // La fecha de entrega no puede ser antes de la fecha de la orden
            ord.setDeliveryDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(null);
        ord.setDeliveryDate(i2010);


        try {
            // La fecha de la orden no puede ser despues de la fecha de entrega
            ord.setOrderDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(i2009);

        ord.setDeliveryDate(null);
        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setEstimatedDate(i2010);
        try {
            // La fecha de la orden no puede ser despues de la fecha estimada
            ord.setOrderDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(i2009);


        ord.setDeliveryDate(null);
        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setFinishDate(i2010);
        try {
            // La fecha de la orden no puede ser despues de la fecha de finalizacion
            ord.setOrderDate(i2011);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setOrderDate(i2009);


        ord.setDeliveryDate(null);
        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setFinishDate(i2010);
        try {
            // La fecha de entrega no puede ser antes de la fecha de finalizacion
            ord.setDeliveryDate(i2009);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setDeliveryDate(i2012);


        ord.setFinishDate(null);
        ord.setOrderDate(null);
        ord.setEstimatedDate(null);
        ord.setDeliveryDate(i2011);
        try {
            // La fecha de finalizion no puede ser despues de la fecha de entrega
            ord.setFinishDate(i2012);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        ord.setFinishDate(i2010);




        // Comprobamos el funcionamiento de las partes
        RepairOrder ord1 = new RepairOrder();
        Part par2 = new Part();
        par2.setMake("make");
        par2.setId(Long.parseLong("14"));

        PartLine pli2 = new PartLine();
        pli2.setPart(par2);
        pli2.setId(Long.parseLong("12"));

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

        // Comprobamos el funcionamiento de la mano de obra
        RepairOrder ord2 = new RepairOrder();
        Labour lab2 = new Labour();
        lab2.setName("name");
        lab2.setId(Long.parseLong("14"));

        LabourLine lli2 = new LabourLine();
        lli2.setLabour(lab2);
        lli2.setId(Long.parseLong("12"));

        ord2.addLabour(lli2);



        assertTrue(ord2.hasLabour(lab2.getId()));
        assertFalse(ord2.hasLabour(Long.parseLong("123")));
        assertTrue(ord2.getLabourList().contains(lli2));

        ord2.setLabourList(null);
        assertNull(ord2.getLabourList());
        ord2.addLabour(lli2);
        assertTrue(ord2.getLabourList().contains(lli2));
        assertTrue(ord2.getLabourList().size() == 1);
        ord2.addLabour(lli2);
        assertTrue(ord2.getLabourList().contains(lli2));
        assertTrue(ord2.getLabourList().size() == 1);
        ord2.removeLabour(lab2.getId());
        assertTrue(ord2.getLabourList().isEmpty());

        assertFalse(ord2.hasLabour(lab2.getId()));



        ord2.setLabourList(null);
        ord2.removeLabour(lab2.getId());
        assertNull(ord2.getLabourList());

        assertFalse(ord2.hasLabour(lab2.getId()));

        ord2.clearLabourList();
        assertTrue(ord2.getLabourList().isEmpty());
        ord2.addLabour(lli2);
        ord2.removeLabour(Long.parseLong("123"));
        ord2.clearLabourList();
        assertTrue(ord2.getLabourList().isEmpty());
        ord2.setLabourList(null);
        ord2.clearLabourList();
        assertTrue(ord2.getLabourList().isEmpty());


        Labour lab3 = new Labour();
        par3.setMake("m");
        LabourLine lli3 = new LabourLine();
        lli3.setLabour(lab3);

        ord2.addLabour(lli2);
        assertTrue(ord2.getLabourList().contains(lli2));
        ord2.removeLabourLine(lli3);
        assertTrue(ord2.getLabourList().contains(lli2));
        ord2.removeLabourLine(lli2);
        assertFalse(ord2.getLabourList().contains(lli2));

        ord2.setLabourList(null);
        ord2.removeLabourLine(lli3);


        // Comprobamos el funcionamiento de equals y hashcode
        RepairOrder ord5 = new RepairOrder();
        RepairOrder ord6 = new RepairOrder();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        emp1.setName("tio");
        Vehicle veh1 = new Vehicle();
        Vehicle veh2 = new Vehicle();
        veh1.setMake("r");

        Part par4 = new Part();
        Part par5 = new Part();
        par4.setMake("make");
        par5.setMake("no");
        PartLine pli4 = new PartLine();
        PartLine pli5 = new PartLine();
        pli4.setPart(par4);
        pli5.setPart(par5);
        Labour lab4 = new Labour();
        Labour lab5 = new Labour();
        lab4.setName("name");
        lab5.setName("no");
        LabourLine lli4 = new LabourLine();
        LabourLine lli5 = new LabourLine();
        lli4.setLabour(lab4);
        lli5.setLabour(lab5);




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



        ord5.setOrderDate(new LocalDate(2012, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderDate(new LocalDate(2011, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setOrderDate(new LocalDate(2012, 2, 2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setOrderDate(null);
        ord6.setOrderDate(null);




        ord5.setEstimatedDate(new LocalDate(2012, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setEstimatedDate(new LocalDate(2011, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setEstimatedDate(new LocalDate(2012, 2, 2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setEstimatedDate(null);
        ord6.setEstimatedDate(null);


        ord5.setFinishDate(new LocalDate(2012, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setFinishDate(new LocalDate(2011, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setFinishDate(new LocalDate(2012, 2, 2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setFinishDate(null);
        ord6.setFinishDate(null);


        ord5.setDeliveryDate(new LocalDate(2012, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDeliveryDate(new LocalDate(2011, 2, 2));
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDeliveryDate(new LocalDate(2012, 2, 2));
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());
        ord5.setDeliveryDate(null);
        ord6.setDeliveryDate(null);



        ord5.setVehicle(veh1);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setVehicle(veh2);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        veh2.setMake("r");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());

        ord6.setVehicle(veh1);
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());


        ord5.setKilometres("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setKilometres("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setKilometres("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());


        ord5.setGasTankLevel("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setGasTankLevel("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setGasTankLevel("1");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());



        ord5.setDescription("1");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDescription("2");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.setDescription("1");
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



        ord5.addLabour(lli4);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.addLabour(lli5);
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        ord6.clearLabourList();

        ord6.addLabour(lli4);
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


        Estimate est5 = ord5.getEstimate();
        Estimate est6 = ord6.getEstimate();
        
        est5.setNotes("aa");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        est6.setNotes("bb");
        assertFalse(ord5.equals(ord6));
        assertFalse(ord6.equals(ord5));

        est6.setNotes("aa");
        assertTrue(ord5.equals(ord6));
        assertTrue(ord6.equals(ord5));
        assertEquals(ord5.hashCode(), ord6.hashCode());

        
        RepairInvoice inv5 = ord5.getInvoice();
        RepairInvoice inv6 = ord6.getInvoice();
        
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
