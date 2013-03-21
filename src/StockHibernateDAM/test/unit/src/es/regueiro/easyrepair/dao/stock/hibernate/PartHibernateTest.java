package es.regueiro.easyrepair.dao.stock.hibernate;

import com.sun.org.omg.CORBA.ParDescriptionSeqHelper;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.PartOrder;
import es.regueiro.easyrepair.model.stock.Stock;
import es.regueiro.easyrepair.model.stock.Supplier;
import es.regueiro.easyrepair.model.stock.Warehouse;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private PartHibernateController controller = new PartHibernateController();
    private Stock stock;
    private Stock stock1;
    private Warehouse warehouse;

    public PartHibernateTest() {
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

        s.close();

        // Creamos un Stock de prueba
        stock = new Stock();
        stock1 = new Stock();
        warehouse = new Warehouse();
        stock.setWarehouse(warehouse);
        stock1.setWarehouse(warehouse);
        warehouse.setName("name");
        WarehouseHibernateController warehouseController = new WarehouseHibernateController();
        warehouseController.setWarehouse(warehouse);
        warehouseController.saveWarehouse();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSavePart() {

        System.out.println("Probando el guardado de Part");

        // Creamos un Part y le asignamos todas sus propiedades
        Part par1 = controller.newPart();
        Part par2 = controller.newPart();


        par1.setMake("testname");
        par1.setModel("model");
        par1.setCategory("cat");
        par1.setNotes("no");
        par1.setPrice("12");
        par1.addStock(stock);


        // Lo asignamos al controlador
        controller.setPart(par1);

        // Comprobamos la asignacion
        assertEquals(par1, controller.getPart());

        // Lo guardamos
        controller.savePart();

        // Y comprobamos que se guarda correctamente.
        Part foundPart = controller.searchByMake("testname").get(0);
        assertTrue(par1.equals(foundPart));
        assertTrue(par1.equals(controller.getPartById(par1.getId())));
        assertFalse(par1.equals(controller.getPartById(par2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Part copiapar1 = controller.getPartById(par1.getId());

        // Lo modificamos y guardamos
        copiapar1.setMake("modificado");
        controller.setPart(copiapar1);
        controller.savePart();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setPart(par1);
        try {
            controller.savePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        par1.setMake("modificado");
        controller.setPart(par1);
        controller.overwritePart();


        // y nos aseguramos que ha guardado todo correctamente
        Part copia2par1 = controller.getPartById(par1.getId());
        assertTrue(par1.equals(copia2par1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Part par3 = controller.newPart();
        par3.setMake("test");
        par3.setModel("model");
        controller.setPart(par3);
        controller.overwritePart();

        Part copiapar3 = controller.getPartById(controller.getPart().getId());
        assertTrue(par3.equals(copiapar3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        par2.setMake("do");
        par2.setModel("model");

        Part par4 = controller.newPart();
        par4.setMake("cu");
        par4.setModel("model");


        controller.setPart(par2);
        controller.savePart();

        par4.setId(par2.getId());
        controller.deletePart();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setPart(par4);
            controller.savePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getPartById(par3.getId()));



        Part par5 = controller.newPart();
        controller.setPart(par5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.savePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        par5.setMake("cinco");
        // Si no tiene modelo no se puede guardar
        try {
            controller.savePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        par5.setModel("pass");
//        // Si no tiene Part no se puede guardar
//        try {
//            controller.savePart();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//
//        par5.addStock(stock);
        controller.savePart();

        assertEquals(par5, controller.getPartById(par5.getId()));
    }

    @Test
    public void testEnableDisablePart() {

        System.out.println("Probando el activado y desactivado de Part");

        // Creamos un Employee y lo guardamos
        Part par1 = controller.newPart();

        par1.setMake("nam");
        par1.setModel("model");


        controller.setPart(par1);

        // Comprobamos que desactiva y activa correctamente
        controller.disablePart();
        assertFalse(par1.getEnabled());
        controller.enablePart();
        assertTrue(par1.getEnabled());



        // Comprobamos que se pueden activar y desactivar Part modificados
        Part foundpar1 = controller.getPartById(par1.getId());
        foundpar1.setMake("cambiado");
        controller.setPart(foundpar1);
        controller.savePart();

        controller.setPart(par1);
        controller.disablePart();
        assertFalse(par1.getEnabled());


        foundpar1 = controller.getPartById(par1.getId());
        foundpar1.setMake("otravez");
        controller.setPart(foundpar1);
        controller.savePart();

        controller.setPart(par1);
        controller.enablePart();
        assertTrue(par1.getEnabled());




        par1.setId(Long.parseLong("123"));
        controller.setPart(par1);
        // No se puede desactivar un Part que no existe
        try {
            controller.disablePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un Part que no existe
        try {
            controller.enablePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeletePart() {

        System.out.println("Probando el borrado de Part");

        // Creamos un Part y lo guardamos
        Part par1 = controller.newPart();

        par1.setMake("test");
        par1.setModel("model");


        controller.setPart(par1);
        controller.savePart();


        // Comprobamos que lo borra correctamente
        controller.deletePart();
        assertNull(controller.getPartById(par1.getId()));




        Part par2 = controller.newPart();
        par2.setId(new Long("-1"));
        controller.setPart(par2);


        // Si la id es invalida dara error
        try {
            controller.deletePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Part aunque estén modificadas
        Part par3 = controller.newPart();
        par3.setMake("tres");
        par3.setModel("model");


        controller.setPart(par3);
        controller.savePart();

        Part par4 = controller.getPartById(par3.getId());
        par4.setMake("cuatro");
        par4.setModel("model");
        controller.setPart(par4);
        controller.savePart();


        controller.setPart(par3);
        controller.deletePart();


        // Comprobamos que da error si intentamos borrar un Part que ya ha sido borrado
        try {
            controller.setPart(par3);
            controller.deletePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Part par5 = controller.newPart();
        par5.setMake("cinco");
        par5.setModel("model");
        par5.setId(par3.getId());

        controller.setPart(par5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deletePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getPartById(par5.getId()));


        // No podemos borrar un Part que esté referenciado por un PartOrder
        Part par6 = controller.newPart();
        par6.setMake("seis");
        par6.setModel("model");
        controller.setPart(par6);
        controller.savePart();

        // Creamos el PartOrder
        PartOrder partOrder = new PartOrder();
        Supplier sup = new Supplier();
        sup.setName("name");
        partOrder.setSupplier(sup);
        partOrder.setStatus("sub");
        
        PartLine pli = new PartLine();
        pli.setPart(par6);
        partOrder.addPart(pli);

        // Lo guardamos
        SupplierHibernateController supplierController = new SupplierHibernateController();
        supplierController.setSupplier(sup);
        supplierController.saveSupplier();
        PartOrderHibernateController partOrderController = new PartOrderHibernateController();
        partOrderController.setPartOrder(partOrder);
        partOrderController.savePartOrder();

        // Y comprobamos que no se puede borrar el Part
        try {
            controller.deletePart();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }

    @Test
    public void testFindPart() {

        System.out.println("Probando la busqueda de Part");

        // Creamos un Part
        Part par1 = controller.newPart();
        Part par2 = controller.newPart();

        // Asignamos sus propiedades básicas y lo guardamos
        par1.setMake("testname");
        par1.setModel("model");
        par1.setCategory("cat");
        par1.setNotes("no");
        par1.setPrice("12");
        par1.addStock(stock);

        controller.setPart(par1);
        assertEquals(par1, controller.getPart());
        controller.savePart();



        par2.setMake("second");
        par2.setModel("two");
        par2.setCategory("nose");
        par2.setNotes("tes");
        par2.setPrice("40");
        par2.addStock(stock1);
        par2.setEnabled(false);

        controller.setPart(par2);
        controller.savePart();


        List<Part> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos Partes y ninguno mas
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByMake("testname");
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMake("second");
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByModel("mod");
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByModel("tw");
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMaxPrice("15");
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMaxPrice("100");
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByMinPrice("10");
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByMinPrice("30");
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPrice("12");
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPrice("40");
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 1);



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(par1));
        assertFalse(foundList.contains(par2));
        assertTrue(foundList.size() == 1);
        par2.setEnabled(true);
        controller.setPart(par2);
        controller.savePart();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(par1));
        assertTrue(foundList.contains(par2));
        assertTrue(foundList.size() == 2);
    }

//    @Test
//    public void testPartCollisions() {
//
//        System.out.println("Probando las colisiones de Part");
//
//        // Creamos un Part y lo guardamos
//        Part par1 = controller.newPart();
//        Part par2 = controller.newPart();
//
//
//        par1.set$propiedad1$("test");
//
//
//        controller.setPart(par1);
//        controller.savePart();
//
//
//        par2.setName(par1.getName());
//        controller.setPart(par2);
//        // No puede haber dos Part con el mismo nombre
//        try {
//            controller.savePart();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//    }
}
