package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Warehouse;
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

public class WarehouseHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private WarehouseHibernateController controller = new WarehouseHibernateController();

    public WarehouseHibernateTest() {
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

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();


        t = s.beginTransaction();
        Query q1 = s.createQuery("delete from Warehouse");
        q1.executeUpdate();
        t.commit();

        s.close();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveWarehouse() {

        System.out.println("Probando el guardado de Warehouse");

        // Creamos un Warehouse y le asignamos todas sus propiedades
        Warehouse war1 = controller.newWarehouse();
        Warehouse war2 = controller.newWarehouse();

        war1.setName("testname");
        war1.setNotes("testnotes");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        war1.setAddress(add);
        war1.setPhone(new Phone("987234132", "lab"));
        war1.setEmail(new Email("address@a.com", "label"));


        // Lo asignamos al controlador
        controller.setWarehouse(war1);

        // Comprobamos la asignacion
        assertEquals(war1, controller.getWarehouse());

        // Lo guardamos
        controller.saveWarehouse();

        // Y comprobamos que se guarda correctamente.
        Warehouse foundWarehouse = controller.searchByName("testname").get(0);
        assertTrue(war1.equals(foundWarehouse));
        assertTrue(war1.equals(controller.getWarehouseById(war1.getId())));
        assertFalse(war1.equals(controller.getWarehouseById(war2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Warehouse copiawar1 = controller.getWarehouseById(war1.getId());

        // Lo modificamos y guardamos
        copiawar1.setName("modificado");
        controller.setWarehouse(copiawar1);
        controller.saveWarehouse();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setWarehouse(war1);
        try {
            controller.saveWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        war1.setName("modificado");
        controller.setWarehouse(war1);
        controller.overwriteWarehouse();


        // y nos aseguramos que ha guardado todo correctamente
        Warehouse copia2war1 = controller.getWarehouseById(war1.getId());
        assertTrue(war1.equals(copia2war1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Warehouse war3 = controller.newWarehouse();
        war3.setName("tres");
        controller.setWarehouse(war3);
        controller.overwriteWarehouse();

        Warehouse copiawar3 = controller.getWarehouseById(controller.getWarehouse().getId());
        assertTrue(war3.equals(copiawar3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        war2.setName("no");

        Warehouse war4 = controller.newWarehouse();
        war4.setName("ca");


        controller.setWarehouse(war2);
        controller.saveWarehouse();

        war4.setId(war2.getId());
        controller.deleteWarehouse();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setWarehouse(war4);
            controller.saveWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getWarehouseById(war3.getId()));



        Warehouse war5 = controller.newWarehouse();
        controller.setWarehouse(war5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        war5.setName("cinco");
        controller.saveWarehouse();

        assertEquals(war5, controller.getWarehouseById(war5.getId()));
    }

    @Test
    public void testEnableDisableWarehouse() {

        System.out.println("Probando el activado y desactivado de Warehouse");

        // Creamos un Employee y lo guardamos
        Warehouse war1 = controller.newWarehouse();

        war1.setName("nam");


        controller.setWarehouse(war1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableWarehouse();
        assertFalse(war1.getEnabled());
        controller.enableWarehouse();
        assertTrue(war1.getEnabled());



        // Comprobamos que se pueden activar y desactivar Warehouse modificados
        Warehouse foundwar1 = controller.getWarehouseById(war1.getId());
        foundwar1.setName("cambiado");
        controller.setWarehouse(foundwar1);
        controller.saveWarehouse();

        controller.setWarehouse(war1);
        controller.disableWarehouse();
        assertFalse(war1.getEnabled());


        foundwar1 = controller.getWarehouseById(war1.getId());
        foundwar1.setName("otravez");
        controller.setWarehouse(foundwar1);
        controller.saveWarehouse();

        controller.setWarehouse(war1);
        controller.enableWarehouse();
        assertTrue(war1.getEnabled());




        war1.setId(Long.parseLong("123"));
        controller.setWarehouse(war1);
        // No se puede desactivar un Warehouse que no existe
        try {
            controller.disableWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un Warehouse que no existe
        try {
            controller.enableWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteWarehouse() {

        System.out.println("Probando el borrado de Warehouse");

        // Creamos un Warehouse y lo guardamos
        Warehouse war1 = controller.newWarehouse();

        war1.setName("name");


        controller.setWarehouse(war1);
        controller.saveWarehouse();


        // Comprobamos que lo borra correctamente
        controller.deleteWarehouse();
        assertNull(controller.getWarehouseById(war1.getId()));




        Warehouse war2 = controller.newWarehouse();
        war2.setId(new Long("-1"));
        controller.setWarehouse(war2);


        // Si la id es invalida dara error
        try {
            controller.deleteWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Warehouse aunque estén modificadas
        Warehouse war3 = controller.newWarehouse();
        war3.setName("tr");


        controller.setWarehouse(war3);
        controller.saveWarehouse();

        Warehouse war4 = controller.getWarehouseById(war3.getId());
        war4.setName("cua");
        controller.setWarehouse(war4);
        controller.saveWarehouse();


        controller.setWarehouse(war3);
        controller.deleteWarehouse();


        // Comprobamos que da error si intentamos borrar un Warehouse que ya ha sido borrado
        try {
            controller.setWarehouse(war3);
            controller.deleteWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Warehouse war5 = controller.newWarehouse();
        war5.setName("cin");
        war5.setId(war3.getId());

        controller.setWarehouse(war5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteWarehouse();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getWarehouseById(war5.getId()));


    }

    @Test
    public void testFindWarehouse() {

        System.out.println("Probando la busqueda de Warehouse");

        // Creamos un Warehouse
        Warehouse war1 = controller.newWarehouse();
        Warehouse war2 = controller.newWarehouse();

        // Asignamos sus propiedades básicas y lo guardamos
        war1.setName("testname");
        war1.setNotes("testnotes");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        add.setCountry("country");
        add.setNotes("notes");
        add.setPostalCode("12342");
        add.setProvince("province");
        add.setStreet("street");
        war1.setAddress(add);
        war1.setPhone(new Phone("987234132", "lab"));
        war1.setEmail(new Email("address@a.com", "label"));

        controller.setWarehouse(war1);
        assertEquals(war1, controller.getWarehouse());
        controller.saveWarehouse();



        war2.setName("second");
        war2.setNotes("notas");
        war2.setEnabled(false);

        Address add1 = new Address();
        add1.setLabel("bel");
        add1.setCity("ciudad");
        add1.setCountry("pais");
        add1.setNotes("not");
        add1.setPostalCode("65465");
        add1.setProvince("provincia");
        add1.setStreet("calle");
        war2.setAddress(add1);
        war2.setPhone(new Phone("988556699", "lab"));
        war2.setEmail(new Email("test@email.es", "label"));

        controller.setWarehouse(war2);
        controller.saveWarehouse();


        List<Warehouse> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos Warehousees y ninguno mas
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("testname");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("second");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("ciu");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("city");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("pai");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("cou");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("65");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("12");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("provinci");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("province");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("cal");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("stre");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("test");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("addre");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("988");
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("987");
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.size() == 1);



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(war1));
        assertFalse(foundList.contains(war2));
        assertTrue(foundList.size() == 1);
        war2.setEnabled(true);
        controller.setWarehouse(war2);
        controller.saveWarehouse();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(war1));
        assertTrue(foundList.contains(war2));
        assertTrue(foundList.size() == 2);
    }

//    @Test
//    public void testWarehouseCollisions() {
//
//        System.out.println("Probando las colisiones de Warehouse");
//
//        // Creamos un Warehouse y lo guardamos
//        Warehouse war1 = controller.newWarehouse();
//        Warehouse war2 = controller.newWarehouse();
//
//
//        war1.setName("nam");
//
//
//        controller.setWarehouse(war1);
//        controller.saveWarehouse();
//
//
//        war2.setName(war1.getName());
//        controller.setWarehouse(war2);
//        // No puede haber dos Warehouse con el mismo nombre
//        try {
//            controller.saveWarehouse();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//    }
}
