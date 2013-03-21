package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.repair.RepairInvoice;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RepairOrderHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private RepairOrderHibernateController controller = new RepairOrderHibernateController();
//    private $claseEnlazada$ $enlazada$;
    private Employee employee;
    private Client client;
    private Vehicle vehicle;

    public RepairOrderHibernateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Configuration cfg = new Configuration();
        cfg.configure();

        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/RepairOrder.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/RepairInvoice.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/RepairEstimate.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Part.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/PartLine.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Stock.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Labour.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/LabourLine.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Employee.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Employee_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Employee_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Employee_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Client.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Vehicle.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Client_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Client_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Client_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Insurance_Company.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Insurance_Company_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Insurance_Company_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Insurance_Company_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Warehouse.hbm.xml");

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();


        t = s.beginTransaction();
        Query q1 = s.createQuery("delete from Stock");
        q1.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q2 = s.createQuery("delete from PartLine");
        q2.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q3 = s.createQuery("delete from RepairInvoice");
        q3.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q4 = s.createQuery("delete from Estimate");
        q4.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q5 = s.createQuery("delete from RepairOrder");
        q5.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q6 = s.createQuery("delete from Email");
        q6.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q7 = s.createQuery("delete from Address");
        q7.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q8 = s.createQuery("delete from Phone");
        q8.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q9 = s.createQuery("delete from Employee");
        q9.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q10 = s.createQuery("delete from Warehouse");
        q10.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q11 = s.createQuery("delete from Part");
        q11.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q12 = s.createQuery("delete from Vehicle");
        q12.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q13 = s.createQuery("delete from InsuranceCompany");
        q13.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q14 = s.createQuery("delete from Client");
        q14.executeUpdate();
        t.commit();


        s.close();

//        // Creamos un $claseEnlazada$ de prueba
//        $claseEnlazada$Controller $enlazada$Controller = new $claseEnlazada$HibernateController();
//        $enlazada$ = $enlazada$Controller.new$claseEnlazada$();
//        $enlazada$.setName("name");
//        $enlazada$Controller.set$claseEnlazada$($enlazada$);
//        $enlazada$Controller.save$claseEnlazada$();


      
        
        client = new Client();
        client.setName("clie");
        vehicle = new Vehicle();
        vehicle.setRegistration("1235");
        client.addVehicle(vehicle);
        
        MockClientSaver saver = new MockClientSaver();
        saver.setClient(client);
        saver.saveClient();
        
        
        MockEmployeeSaver employeeSaver = new MockEmployeeSaver();
        employee = new Employee();
        employee.setName("emp");
        employeeSaver.setEmployee(employee);
        employeeSaver.saveEmployee();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveRepairOrder() {

        System.out.println("Probando el guardado de RepairOrder");
//
        // Creamos un RepairOrder y le asignamos todas sus propiedades
        RepairOrder rep1 = controller.newRepairOrder();
        RepairOrder rep2 = controller.newRepairOrder();
//        $claseEnlazada$Controller $enlazada$Controller = new $claseEnlazada$HibernateController();
//        $claseEnlazada$ $enlazada$ = $enlazada$Controller.new$claseEnlazada$();

        
        rep1.setKilometres("21243123");
        rep1.setGasTankLevel("full");
        rep1.setDescription("desc");
        rep1.setNotes("not");
        rep1.setStatus("stat");
        rep1.setVehicle(vehicle);
        rep1.setResponsible(employee);


        LocalDate ordDate = new LocalDate(2012, 1, 2);
        LocalDate estDate = new LocalDate(2012, 1, 4);
        LocalDate finDate = new LocalDate(2012, 1, 5);
        LocalDate delDate = new LocalDate(2012, 1, 6);

        rep1.setOrderDate(ordDate);
        rep1.setEstimatedDate(estDate);
        rep1.setFinishDate(finDate);
        rep1.setDeliveryDate(delDate);
        
        LocalDate invDate = new LocalDate(2012, 1, 2);
        LocalDate accDate = new LocalDate(2012, 1, 4);
        LocalDate espDate = new LocalDate(2012, 1, 5);
        LocalDate payDate = new LocalDate(2012, 1, 6);
        
        RepairInvoice inv = rep1.getInvoice();
        inv.setInvoiceDate(invDate);
        inv.setAcceptedDate(accDate);
        inv.setEstimatedPaymentDate(espDate);
        inv.setPaymentDate(payDate);
        inv.setNotes("tes");
        inv.setPaymentMethod("cash");
        inv.setPaymentResponsible("client");
        inv.setResponsible(employee);
        inv.setStatus("pending");

        // Lo asignamos al controlador
        controller.setRepairOrder(rep1);

        // Comprobamos la asignacion
        assertEquals(rep1, controller.getRepairOrder());

        // Lo guardamos
        controller.saveRepairOrder();

        // Y comprobamos que se guarda correctamente.
        RepairOrder foundRepairOrder = controller.searchByVehicle(vehicle.getRegistration()).get(0);
        assertTrue(rep1.equals(foundRepairOrder));
        assertTrue(rep1.equals(controller.getRepairOrderById(rep1.getId())));
        assertFalse(rep1.equals(controller.getRepairOrderById(rep2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        RepairOrder copiarep1 = controller.getRepairOrderById(rep1.getId());

        // Lo modificamos y guardamos
        copiarep1.setStatus("modificado");
        controller.setRepairOrder(copiarep1);
        controller.saveRepairOrder();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setRepairOrder(rep1);
        try {
            controller.saveRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        rep1.setStatus("modificado");
        controller.setRepairOrder(rep1);
        controller.overwriteRepairOrder();


        // y nos aseguramos que ha guardado todo correctamente
        RepairOrder copia2rep1 = controller.getRepairOrderById(rep1.getId());
        assertTrue(rep1.equals(copia2rep1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        RepairOrder rep3 = controller.newRepairOrder();
        rep3.setOrderNumber("12345678910");
        rep3.setVehicle(vehicle);
        controller.setRepairOrder(rep3);
        controller.overwriteRepairOrder();

        RepairOrder copiarep3 = controller.getRepairOrderById(controller.getRepairOrder().getId());
        assertTrue(rep3.getOrderNumber().equals(copiarep3.getOrderNumber()));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        rep2.setVehicle(vehicle);

        RepairOrder rep4 = controller.newRepairOrder();
        rep4.setVehicle(vehicle);


        controller.setRepairOrder(rep2);
        controller.saveRepairOrder();

        rep4.setId(rep2.getId());
        controller.deleteRepairOrder();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setRepairOrder(rep4);
            controller.saveRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getRepairOrderById(rep3.getId()));



        RepairOrder rep5 = controller.newRepairOrder();
        controller.setRepairOrder(rep5);

        // Si no tiene vehiculo no se puede guardar
        try {
            controller.saveRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        rep5.setVehicle(vehicle);
        controller.saveRepairOrder();

        assertEquals(rep5, controller.getRepairOrderById(rep5.getId()));
    }
//
    @Test
    public void testEnableDisableRepairOrder() {

        System.out.println("Probando el activado y desactivado de RepairOrder");

        // Creamos un Employee y lo guardamos
        RepairOrder rep1 = controller.newRepairOrder();

        rep1.setVehicle(vehicle);


        controller.setRepairOrder(rep1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableRepairOrder();
        assertFalse(rep1.getEnabled());
        controller.enableRepairOrder();
        assertTrue(rep1.getEnabled());



        // Comprobamos que se pueden activar y desactivar RepairOrder modificados
        RepairOrder foundrep1 = controller.getRepairOrderById(rep1.getId());
        foundrep1.setStatus("cambiado");
        controller.setRepairOrder(foundrep1);
        controller.saveRepairOrder();

        controller.setRepairOrder(rep1);
        controller.disableRepairOrder();
        assertFalse(rep1.getEnabled());


        foundrep1 = controller.getRepairOrderById(rep1.getId());
        foundrep1.setStatus("otravez");
        controller.setRepairOrder(foundrep1);
        controller.saveRepairOrder();

        controller.setRepairOrder(rep1);
        controller.enableRepairOrder();
        assertTrue(rep1.getEnabled());




        rep1.setId(Long.parseLong("123"));
        controller.setRepairOrder(rep1);
        // No se puede desactivar un RepairOrder que no existe
        try {
            controller.disableRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un RepairOrder que no existe
        try {
            controller.enableRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteRepairOrder() {

        System.out.println("Probando el borrado de RepairOrder");

        // Creamos un RepairOrder y lo guardamos
        RepairOrder rep1 = controller.newRepairOrder();

        rep1.setVehicle(vehicle);


        controller.setRepairOrder(rep1);
        controller.saveRepairOrder();


        // Comprobamos que lo borra correctamente
        controller.deleteRepairOrder();
        assertNull(controller.getRepairOrderById(rep1.getId()));




        RepairOrder rep2 = controller.newRepairOrder();
        rep2.setId(new Long("-1"));
        controller.setRepairOrder(rep2);


        // Si la id es invalida dara error
        try {
            controller.deleteRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente RepairOrder aunque estén modificadas
        RepairOrder rep3 = controller.newRepairOrder();
        rep3.setVehicle(vehicle);


        controller.setRepairOrder(rep3);
        controller.saveRepairOrder();

        RepairOrder rep4 = controller.getRepairOrderById(rep3.getId());
        rep4.setStatus("aa");
        controller.setRepairOrder(rep4);
        controller.saveRepairOrder();


        controller.setRepairOrder(rep3);
        controller.deleteRepairOrder();


        // Comprobamos que da error si intentamos borrar un RepairOrder que ya ha sido borrado
        try {
            controller.setRepairOrder(rep3);
            controller.deleteRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        RepairOrder rep5 = controller.newRepairOrder();
        rep5.setVehicle(vehicle);
        rep5.setId(rep3.getId());

        controller.setRepairOrder(rep5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteRepairOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getRepairOrderById(rep5.getId()));


    }
//
//    @Test
//    public void testFindRepairOrder() {
//
//        System.out.println("Probando la busqueda de RepairOrder");
//
//        // Creamos un RepairOrder
//        RepairOrder rep1 = controller.newRepairOrder();
//        RepairOrder rep2 = controller.newRepairOrder();
//
//        // Asignamos sus propiedades básicas y lo guardamos
//        rep1.set$propiedad1$("testname");
//
//        controller.setRepairOrder(rep1);
//        assertEquals(rep1, controller.getRepairOrder());
//        controller.saveRepairOrder();
//
//
//
//        rep2.set$propiedad1$("second");
//
//        controller.setRepairOrder(rep2);
//        controller.saveRepairOrder();
//
//
//        List<RepairOrder> foundList = controller.listAll();
//        // Nos aseguramos que encuentra a los dos RepairOrderes y ninguno mas
//        assertTrue(foundList.contains(rep1));
//        assertTrue(foundList.contains(rep2));
//        assertTrue(foundList.size() == 2);
//
//
//        // Probamos si encuentra correctamente por cada propiedad
//        foundList = controller.searchByName("testname");
//        assertTrue(foundList.contains(rep1));
//        assertTrue(foundList.size() == 1);
//        foundList = controller.searchByName("second");
//        assertTrue(foundList.contains(rep2));
//        assertTrue(foundList.size() == 1);
//        foundList = controller.searchByDescription("pass");
//        assertTrue(foundList.contains(rep1));
//        assertTrue(foundList.size() == 1);
//        foundList = controller.searchByDescription("asdasd");
//        assertTrue(foundList.contains(rep2));
//        assertTrue(foundList.size() == 1);
//
//
//
//        // Comprobamos que encuentra correctamente los activados y desactivados
//        foundList = controller.listAllEnabled();
//        assertTrue(foundList.contains(rep1));
//        assertFalse(foundList.contains(rep2));
//        assertTrue(foundList.size() == 1);
//        rep2.setEnabled(true);
//        controller.setRepairOrder(rep2);
//        controller.saveRepairOrder();
//
//        foundList = controller.listAllEnabled();
//        assertTrue(foundList.contains(rep1));
//        assertTrue(foundList.contains(rep2));
//        assertTrue(foundList.size() == 2);
//    }
//
//    @Test
//    public void testRepairOrderCollisions() {
//
//        System.out.println("Probando las colisiones de RepairOrder");
//
//        // Creamos un RepairOrder y lo guardamos
//        RepairOrder rep1 = controller.newRepairOrder();
//        RepairOrder rep2 = controller.newRepairOrder();
//
//
//        rep1.set$propiedad1$("test");
//
//
//        controller.setRepairOrder(rep1);
//        controller.saveRepairOrder();
//
//
//        rep2.setName(rep1.getName());
//        controller.setRepairOrder(rep2);
//        // No puede haber dos RepairOrder con el mismo nombre
//        try {
//            controller.saveRepairOrder();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//    }
    
    @Test
    public void TestVehicles() {
        Client cli = new Client();
        cli.setName("clie");
        Vehicle veh = new Vehicle();
        veh.setRegistration("1235");
        cli.addVehicle(veh);
        
        MockClientSaver saver = new MockClientSaver();
        saver.setClient(cli);
        saver.saveClient();
        
        MockVehicleFinder finder = new MockVehicleFinder();
        
        Vehicle dos = finder.getVehicle(veh.getId());
        assertTrue(veh.equals(dos));
    }
}
