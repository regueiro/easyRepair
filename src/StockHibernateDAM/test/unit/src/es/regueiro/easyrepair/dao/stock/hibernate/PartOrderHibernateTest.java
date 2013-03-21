package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.PartOrder;
import es.regueiro.easyrepair.model.stock.Stock;
import es.regueiro.easyrepair.model.stock.Supplier;
import es.regueiro.easyrepair.model.stock.Warehouse;
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

public class PartOrderHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private PartOrderHibernateController controller = new PartOrderHibernateController();
    private Part part;
    private Stock stock;
    private Employee employee;
    private Supplier supplier;
    private Warehouse warehouse;

    public PartOrderHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Warehouse.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/PartOrder.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/PartOrderInvoice.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Employee.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Employee_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Employee_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Employee_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/PartLine.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Stock.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Part.hbm.xml");

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
        Query q3 = s.createQuery("delete from PartOrderInvoice");
        q3.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q4 = s.createQuery("delete from PartOrder");
        q4.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q5 = s.createQuery("delete from Email");
        q5.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q6 = s.createQuery("delete from Address");
        q6.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q7 = s.createQuery("delete from Phone");
        q7.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q8 = s.createQuery("delete from Supplier");
        q8.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q9 = s.createQuery("delete from Warehouse");
        q9.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q10 = s.createQuery("delete from Part");
        q10.executeUpdate();
        t.commit();
        
        t = s.beginTransaction();
        Query q11 = s.createQuery("delete from Employee");
        q11.executeUpdate();
        t.commit();

        s.close();

        // Creamos un Part de prueba
        PartHibernateController partController = new PartHibernateController();
        part = partController.newPart();
        part.setMake("make");
        part.setModel("model");
        partController.setPart(part);
        partController.savePart();

        SupplierHibernateController supplierController = new SupplierHibernateController();
        supplier = supplierController.newSupplier();
        supplier.setName("name");
        supplierController.setSupplier(supplier);
        supplierController.saveSupplier();


        WarehouseHibernateController warehouseController = new WarehouseHibernateController();
        warehouse = warehouseController.newWarehouse();
        warehouse.setName("name");
        warehouseController.setWarehouse(warehouse);
        warehouseController.saveWarehouse();

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
    public void testSavePartOrder() {

        System.out.println("Probando el guardado de PartOrder");

        // Creamos un PartOrder y le asignamos todas sus propiedades
        PartOrder ord1 = controller.newPartOrder();
        PartOrder ord2 = controller.newPartOrder();

        PartLine pli = new PartLine();
        pli.setPart(part);
        pli.setQuantity(2);

        ord1.setOrderNumber("57948484964");
        ord1.setShippingWarehouse(warehouse);
        ord1.setShippingCosts("11");
        ord1.setOtherCosts("111");
        ord1.setDiscount("14");
        ord1.setStatus("status");
        ord1.setNotes("notes");
        ord1.setSupplier(supplier);
        ord1.setResponsible(employee);
        ord1.addPart(pli);

        LocalDate ordDate = new LocalDate(2012, 1, 2);
        LocalDate estDate = new LocalDate(2012, 1, 4);
        LocalDate recDate = new LocalDate(2012, 1, 5);

        ord1.setOrderDate(ordDate);
        ord1.setEstimatedDate(estDate);
        ord1.setReceiptDate(recDate);


        // Lo asignamos al controlador
        controller.setPartOrder(ord1);

        // Comprobamos la asignacion
        assertEquals(ord1, controller.getPartOrder());

        // Lo guardamos
        controller.savePartOrder();

        // Y comprobamos que se guarda correctamente.
        PartOrder foundPartOrder = controller.searchByOrderNumber("57948484964").get(0);
        assertTrue(ord1.equals(foundPartOrder));
        assertTrue(ord1.equals(controller.getPartOrderById(ord1.getId())));
        assertFalse(ord1.equals(controller.getPartOrderById(ord2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        PartOrder copiaord1 = controller.getPartOrderById(ord1.getId());

        // Lo modificamos y guardamos
        copiaord1.setStatus("modificado");
        controller.setPartOrder(copiaord1);
        controller.savePartOrder();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setPartOrder(ord1);
        try {
            controller.savePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        ord1.setStatus("modificado");
        controller.setPartOrder(ord1);
        controller.overwritePartOrder();


        // y nos aseguramos que ha guardado todo correctamente
        PartOrder copia2ord1 = controller.getPartOrderById(ord1.getId());
        assertTrue(ord1.equals(copia2ord1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        PartOrder ord3 = controller.newPartOrder();
        ord3.setOrderNumber("12345678910");
        ord3.setSupplier(supplier);
        controller.setPartOrder(ord3);
        controller.overwritePartOrder();

        PartOrder copiaord3 = controller.getPartOrderById(controller.getPartOrder().getId());
        assertTrue(ord3.getOrderNumber().equals(copiaord3.getOrderNumber()));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        ord2.setStatus("do");
        ord2.setSupplier(supplier);

        PartOrder ord4 = controller.newPartOrder();
        ord4.setStatus("cu");
        ord4.setSupplier(supplier);


        controller.setPartOrder(ord2);
        controller.savePartOrder();

        ord4.setId(ord2.getId());
        controller.deletePartOrder();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setPartOrder(ord4);
            controller.savePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getPartOrderById(ord3.getId()));



        PartOrder ord5 = controller.newPartOrder();
        controller.setPartOrder(ord5);

        // Si no tiene supplier no se puede guardar
        try {
            controller.savePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        ord5.setSupplier(supplier);
//        // Si no tiene password no se puede guardar
//        try {
//            controller.savePartOrder();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//
//        ord5.setPassword("pass");
//        // Si no tiene PartOrder no se puede guardar
//        try {
//            controller.savePartOrder();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//
//        ord5.setPart(part);
        controller.savePartOrder();

        assertEquals(ord5, controller.getPartOrderById(ord5.getId()));
    }

    @Test
    public void testEnableDisablePartOrder() {

        System.out.println("Probando el activado y desactivado de PartOrder");

        // Creamos un Employee y lo guardamos
        PartOrder ord1 = controller.newPartOrder();

        ord1.setStatus("nam");
        ord1.setSupplier(supplier);


        controller.setPartOrder(ord1);

        // Comprobamos que desactiva y activa correctamente
        controller.disablePartOrder();
        assertFalse(ord1.getEnabled());
        controller.enablePartOrder();
        assertTrue(ord1.getEnabled());



        // Comprobamos que se pueden activar y desactivar PartOrder modificados
        PartOrder foundord1 = controller.getPartOrderById(ord1.getId());
        foundord1.setStatus("cambiado");
        controller.setPartOrder(foundord1);
        controller.savePartOrder();

        controller.setPartOrder(ord1);
        controller.disablePartOrder();
        assertFalse(ord1.getEnabled());


        foundord1 = controller.getPartOrderById(ord1.getId());
        foundord1.setStatus("otravez");
        controller.setPartOrder(foundord1);
        controller.savePartOrder();

        controller.setPartOrder(ord1);
        controller.enablePartOrder();
        assertTrue(ord1.getEnabled());




        ord1.setId(Long.parseLong("123"));
        controller.setPartOrder(ord1);
        // No se puede desactivar un PartOrder que no existe
        try {
            controller.disablePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un PartOrder que no existe
        try {
            controller.enablePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeletePartOrder() {

        System.out.println("Probando el borrado de PartOrder");

        // Creamos un PartOrder y lo guardamos
        PartOrder ord1 = controller.newPartOrder();

        ord1.setStatus("test");
        ord1.setSupplier(supplier);


        controller.setPartOrder(ord1);
        controller.savePartOrder();


        // Comprobamos que lo borra correctamente
        controller.deletePartOrder();
        assertNull(controller.getPartOrderById(ord1.getId()));




        PartOrder ord2 = controller.newPartOrder();
        ord2.setId(new Long("-1"));
        controller.setPartOrder(ord2);


        // Si la id es invalida dara error
        try {
            controller.deletePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente PartOrder aunque estén modificadas
        PartOrder ord3 = controller.newPartOrder();
        ord3.setStatus("tres");
        ord3.setSupplier(supplier);

        controller.setPartOrder(ord3);
        controller.savePartOrder();

        PartOrder ord4 = controller.getPartOrderById(ord3.getId());
        ord4.setStatus("cuatro");
        ord4.setSupplier(supplier);
        controller.setPartOrder(ord4);
        controller.savePartOrder();


        controller.setPartOrder(ord3);
        controller.deletePartOrder();


        // Comprobamos que da error si intentamos borrar un PartOrder que ya ha sido borrado
        try {
            controller.setPartOrder(ord3);
            controller.deletePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        PartOrder ord5 = controller.newPartOrder();
        ord5.setStatus("cinco");
        ord5.setSupplier(supplier);
        ord5.setId(ord3.getId());

        controller.setPartOrder(ord5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deletePartOrder();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getPartOrderById(ord5.getId()));



    }

//    @Test
//    public void testFindPartOrder() {
//
//        System.out.println("Probando la busqueda de PartOrder");
//
//        // Creamos un PartOrder
//        PartOrder ord1 = controller.newPartOrder();
//        PartOrder ord2 = controller.newPartOrder();
//
//        // Asignamos sus propiedades básicas y lo guardamos
//        Employee emp = new Employee();
//        emp.setName("emp");
//        PartLine pli = new PartLine();
//        pli.setPart(part);
//        pli.setQuantity(2);
//
//        ord1.setOrderNumber("57948484964");
//        ord1.setShippingWarehouse(warehouse);
//        ord1.setShippingCosts("11");
//        ord1.setOtherCosts("111");
//        ord1.setDiscount("14");
//        ord1.setStatus("status");
//        ord1.setNotes("notes");
//        ord1.setSupplier(supplier);
////        ord1.setResponsible(emp);
//        ord1.addPart(pli);
//
//        LocalDate ordDate = new LocalDate(2012, 1, 2);
//        LocalDate estDate = new LocalDate(2012, 1, 4);
//        LocalDate recDate = new LocalDate(2012, 1, 5);
//
//        ord1.setOrderDate(ordDate);
//        ord1.setEstimatedDate(estDate);
//        ord1.setReceiptDate(recDate);
//
//        controller.setPartOrder(ord1);
//        assertEquals(ord1, controller.getPartOrder());
//        controller.savePartOrder();
//
//
//
//        ord2.setOrderNumber("235414");
//        ord2.setShippingWarehouse(warehouse);
//        ord2.setShippingCosts("59");
//        ord2.setOtherCosts("1");
//        ord2.setDiscount("0");
//        ord2.setStatus("nnn");
//        ord2.setNotes("at");
//        ord2.setSupplier(supplier);
////        ord1.setResponsible(emp);
//        ord2.addPart(pli);
//        ord2.setEnabled(false);
//
//        LocalDate ordDate2 = new LocalDate(2011, 1, 2);
//        LocalDate estDate2 = new LocalDate(2011, 1, 4);
//        LocalDate recDate2 = new LocalDate(2011, 1, 5);
//
//        ord2.setOrderDate(ordDate2);
//        ord2.setEstimatedDate(estDate2);
//        ord2.setReceiptDate(recDate2);
//
//        controller.setPartOrder(ord2);
//        controller.savePartOrder();
//
//
//        List<PartOrder> foundList = controller.listAll();
//        // Nos aseguramos que encuentra a los dos PartOrderes y ninguno mas
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.contains(ord2));
//        assertTrue(foundList.size() == 2);
//
//
//        // Probamos si encuentra correctamente por cada propiedad
//        foundList = controller.searchByDiscount("14");
//        assertTrue(foundList.contains(ord1));
//        assertTrue(foundList.size() == 1);
//        foundList = controller.searchByDiscount("0");
//        assertTrue(foundList.contains(ord2));
//        assertTrue(foundList.size() == 1);
//        foundList = controller.searchByEstimatedDate("2010/01/01","2013/01/01");
//        assertTrue(foundList.contains(ord1));
//        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("pass");
////        assertTrue(foundList.contains(ord1));
////        assertTrue(foundList.size() == 1);
////        foundList = controller.searchByDescription("asdasd");
////        assertTrue(foundList.contains(ord2));
////        assertTrue(foundList.size() == 1);
//        
//
//
//
//        // Comprobamos que encuentra correctamente los activados y desactivados
//        foundList = controller.listAllEnabled();
//        assertTrue(foundList.contains(ord1));
//        assertFalse(foundList.contains(ord2));
//        assertTrue(foundList.size() == 1);
//        ord2.setEnabled(true);
//        controller.setPartOrder(ord2);
//        controller.savePartOrder();
//
//        foundList = controller.listAllEnabled();
//        assertTrue(foundList.contains(ord1));
//        assertTrue(foundList.contains(ord2));
//        assertTrue(foundList.size() == 2);
//    }

//    @Test
//    public void testPartOrderCollisions() {
//
//        System.out.println("Probando las colisiones de PartOrder");
//
//        // Creamos un PartOrder y lo guardamos
//        PartOrder ord1 = controller.newPartOrder();
//        PartOrder ord2 = controller.newPartOrder();
//
//
//        ord1.set$propiedad1$("test");
//
//
//        controller.setPartOrder(ord1);
//        controller.savePartOrder();
//
//
//        ord2.setName(ord1.getName());
//        controller.setPartOrder(ord2);
//        // No puede haber dos PartOrder con el mismo nombre
//        try {
//            controller.savePartOrder();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//    }
}
