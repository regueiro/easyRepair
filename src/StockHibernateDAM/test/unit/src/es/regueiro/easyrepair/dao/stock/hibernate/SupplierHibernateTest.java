package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Supplier;
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

public class SupplierHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private SupplierHibernateController controller = new SupplierHibernateController();

    public SupplierHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/stock/hibernate/Supplier.hbm.xml");

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();

        t = s.beginTransaction();
        Query q1 = s.createQuery("delete from Email");
        q1.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q2 = s.createQuery("delete from Address");
        q2.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q3 = s.createQuery("delete from Phone");
        q3.executeUpdate();
        t.commit();
        
        t = s.beginTransaction();
        Query q4 = s.createQuery("delete from Supplier");
        q4.executeUpdate();
        t.commit();

        s.close();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveSupplier() {

        System.out.println("Probando el guardado de Supplier");

        // Creamos un Supplier y le asignamos todas sus propiedades
        Supplier sup1 = controller.newSupplier();
        Supplier sup2 = controller.newSupplier();

        sup1.setName("testname");
        sup1.setNotes("testnotes");
        sup1.setNif(new NIF("66877899V"));
        sup1.setCategory("cate");
        sup1.setPaymentMethod("cash");
        sup1.setShippingMethod("air");
        sup1.setWeb("google.es");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        sup1.addAddress(add);
        sup1.addPhone(new Phone("987234132", "lab"));
        sup1.addEmail(new Email("address@a.com", "label"));


        // Lo asignamos al controlador
        controller.setSupplier(sup1);

        // Comprobamos la asignacion
        assertEquals(sup1, controller.getSupplier());

        // Lo guardamos
        controller.saveSupplier();

        // Y comprobamos que se guarda correctamente.
        Supplier foundSupplier = controller.searchByName("testname").get(0);
        assertTrue(sup1.equals(foundSupplier));
        assertTrue(sup1.equals(controller.getSupplierById(sup1.getId())));
        assertFalse(sup1.equals(controller.getSupplierById(sup2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Supplier copiasup1 = controller.getSupplierById(sup1.getId());

        // Lo modificamos y guardamos
        copiasup1.setName("modificado");
        controller.setSupplier(copiasup1);
        controller.saveSupplier();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setSupplier(sup1);
        try {
            controller.saveSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        sup1.setName("modificado");
        controller.setSupplier(sup1);
        controller.overwriteSupplier();


        // y nos aseguramos que ha guardado todo correctamente
        Supplier copia2sup1 = controller.getSupplierById(sup1.getId());
        assertTrue(sup1.equals(copia2sup1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Supplier sup3 = controller.newSupplier();
        sup3.setName("tre");
        controller.setSupplier(sup3);
        controller.overwriteSupplier();

        Supplier copiasup3 = controller.getSupplierById(controller.getSupplier().getId());
        assertTrue(sup3.equals(copiasup3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        sup2.setName("do");

        Supplier sup4 = controller.newSupplier();
        sup4.setName("cua");


        controller.setSupplier(sup2);
        controller.saveSupplier();

        sup4.setId(sup2.getId());
        controller.deleteSupplier();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setSupplier(sup4);
            controller.saveSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getSupplierById(sup3.getId()));



        Supplier sup5 = controller.newSupplier();
        controller.setSupplier(sup5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        sup5.setName("cinco");
        controller.saveSupplier();

        assertEquals(sup5, controller.getSupplierById(sup5.getId()));
    }

    @Test
    public void testEnableDisableSupplier() {

        System.out.println("Probando el activado y desactivado de Supplier");

        // Creamos un Employee y lo guardamos
        Supplier sup1 = controller.newSupplier();

        sup1.setName("nam");


        controller.setSupplier(sup1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableSupplier();
        assertFalse(sup1.getEnabled());
        controller.enableSupplier();
        assertTrue(sup1.getEnabled());



        // Comprobamos que se pueden activar y desactivar Supplier modificados
        Supplier foundsup1 = controller.getSupplierById(sup1.getId());
        foundsup1.setName("cambiado");
        controller.setSupplier(foundsup1);
        controller.saveSupplier();

        controller.setSupplier(sup1);
        controller.disableSupplier();
        assertFalse(sup1.getEnabled());


        foundsup1 = controller.getSupplierById(sup1.getId());
        foundsup1.setName("otravez");
        controller.setSupplier(foundsup1);
        controller.saveSupplier();

        controller.setSupplier(sup1);
        controller.enableSupplier();
        assertTrue(sup1.getEnabled());




        sup1.setId(Long.parseLong("123"));
        controller.setSupplier(sup1);
        // No se puede desactivar un Supplier que no existe
        try {
            controller.disableSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un Supplier que no existe
        try {
            controller.enableSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteSupplier() {

        System.out.println("Probando el borrado de Supplier");

        // Creamos un Supplier y lo guardamos
        Supplier sup1 = controller.newSupplier();

        sup1.setName("test");


        controller.setSupplier(sup1);
        controller.saveSupplier();


        // Comprobamos que lo borra correctamente
        controller.deleteSupplier();
        assertNull(controller.getSupplierById(sup1.getId()));




        Supplier sup2 = controller.newSupplier();
        sup2.setId(new Long("-1"));
        controller.setSupplier(sup2);


        // Si la id es invalida dara error
        try {
            controller.deleteSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Supplier aunque estén modificadas
        Supplier sup3 = controller.newSupplier();
        sup3.setName("tre");


        controller.setSupplier(sup3);
        controller.saveSupplier();

        Supplier sup4 = controller.getSupplierById(sup3.getId());
        sup4.setName("cua");
        controller.setSupplier(sup4);
        controller.saveSupplier();


        controller.setSupplier(sup3);
        controller.deleteSupplier();


        // Comprobamos que da error si intentamos borrar un Supplier que ya ha sido borrado
        try {
            controller.setSupplier(sup3);
            controller.deleteSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Supplier sup5 = controller.newSupplier();
        sup5.setName("cin");
        sup5.setId(sup3.getId());

        controller.setSupplier(sup5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteSupplier();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getSupplierById(sup5.getId()));


        // No podemos borrar un Supplier que esté referenciado por un $clasesubordinada$
        Supplier sup6 = controller.newSupplier();
        sup6.setName("se");
        controller.setSupplier(sup6);
        controller.saveSupplier();

    }

    @Test
    public void testFindSupplier() {

        System.out.println("Probando la busqueda de Supplier");

        // Creamos un Supplier
        Supplier sup1 = controller.newSupplier();
        Supplier sup2 = controller.newSupplier();

        // Asignamos sus propiedades básicas y lo guardamos
        sup1.setName("testname");
        sup1.setNif(new NIF("66877899V"));
        sup1.setNotes("testnotes");
        sup1.setCategory("cate");
        sup1.setShippingMethod("air");
        sup1.setPaymentMethod("cash");
        sup1.setWeb("google.com");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        add.setCountry("country");
        add.setNotes("notes");
        add.setPostalCode("12342");
        add.setProvince("province");
        add.setStreet("street");
        sup1.addAddress(add);
        sup1.addPhone(new Phone("987234132", "lab"));
        sup1.addEmail(new Email("address@a.com", "label"));


        controller.setSupplier(sup1);
        assertEquals(sup1, controller.getSupplier());
        controller.saveSupplier();



        sup2.setName("second");
        sup2.setNif(new NIF("12345678Z"));
        sup2.setNotes("notas");
        sup2.setCategory("other");
        sup2.setShippingMethod("urgent");
        sup2.setPaymentMethod("cheque");
        sup2.setEnabled(false);
        sup2.setWeb("udc.es");

        Address add1 = new Address();
        add1.setLabel("bel");
        add1.setCity("ciudad");
        add1.setCountry("pais");
        add1.setNotes("not");
        add1.setPostalCode("65465");
        add1.setProvince("provincia");
        add1.setStreet("calle");
        sup2.addAddress(add1);
        sup2.addPhone(new Phone("988556699", "lab"));
        sup2.addEmail(new Email("test@email.es", "label"));

        controller.setSupplier(sup2);
        controller.saveSupplier();


        List<Supplier> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos Supplieres y ninguno mas
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("testname");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("second");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("123");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("668");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByCategory("cat");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByCategory("ot");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByShippingMethod("air");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByShippingMethod("gen");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPaymentMethod("ca");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPaymentMethod("ch");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByWeb("google");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByWeb("es");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("ciu");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("city");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("pai");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("cou");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("be");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("la");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("65");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("12");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("provinci");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("province");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("cal");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("stre");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("test");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("addre");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailLabel("label");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByPhoneNumber("988");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("987");
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneLabel("lab");
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.size() == 2);



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(sup1));
        assertFalse(foundList.contains(sup2));
        assertTrue(foundList.size() == 1);
        sup2.setEnabled(true);
        controller.setSupplier(sup2);
        controller.saveSupplier();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(sup1));
        assertTrue(foundList.contains(sup2));
        assertTrue(foundList.size() == 2);
    }

//    @Test
//    public void testSupplierCollisions() {
//
//        System.out.println("Probando las colisiones de Supplier");
//
//        // Creamos un Supplier y lo guardamos
//        Supplier sup1 = controller.newSupplier();
//        Supplier sup2 = controller.newSupplier();
//
//
//        sup1.setName("nam");
//
//
//        controller.setSupplier(sup1);
//        controller.saveSupplier();
//
//
//        sup2.setName(sup1.getName());
//        controller.setSupplier(sup2);
//        // No puede haber dos Supplier con el mismo nombre
//        try {
//            controller.saveSupplier();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
//    }
}
