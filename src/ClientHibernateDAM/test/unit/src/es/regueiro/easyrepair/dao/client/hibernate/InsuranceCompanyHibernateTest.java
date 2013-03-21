package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
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

public class InsuranceCompanyHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private InsuranceCompanyHibernateController controller = new InsuranceCompanyHibernateController();

    public InsuranceCompanyHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company.hbm.xml");

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();


        t = s.beginTransaction();
        Query q1 = s.createQuery("delete from InsuranceCompany.email");
        q1.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q2 = s.createQuery("delete from InsuranceCompany.address");
        q2.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q3 = s.createQuery("delete from InsuranceCompany.phone");
        q3.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q4 = s.createQuery("delete from InsuranceCompany");
        q4.executeUpdate();
        t.commit();

        s.close();


    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveInsuranceCompany() {

        System.out.println("Probando el guardado de InsuranceCompany");

        // Creamos un InsuranceCompany y le asignamos todas sus propiedades
        InsuranceCompany ins1 = controller.newInsuranceCompany();
        InsuranceCompany ins2 = controller.newInsuranceCompany();

    	ins1.setName("testname");
        ins1.setNotes("testnotes");
        ins1.setNif(new NIF("66877899V"));
        ins1.setWeb("google.es");
        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        ins1.addAddress(add);
        ins1.addPhone(new Phone("987234132", "lab"));
        ins1.addEmail(new Email("address@a.com", "label"));


        // Lo asignamos al controlador
        controller.setInsuranceCompany(ins1);

        // Comprobamos la asignacion
        assertEquals(ins1, controller.getInsuranceCompany());

        // Lo guardamos
        controller.saveInsuranceCompany();

        // Y comprobamos que se guarda correctamente.
        InsuranceCompany foundInsuranceCompany = controller.searchByName("testname").get(0);
        assertTrue(ins1.equals(foundInsuranceCompany));
        assertTrue(ins1.equals(controller.getInsuranceCompanyById(ins1.getId())));
        assertFalse(ins1.equals(controller.getInsuranceCompanyById(ins2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        InsuranceCompany copiains1 = controller.getInsuranceCompanyById(ins1.getId());

        // Lo modificamos y guardamos
        copiains1.setName("modificado");
        controller.setInsuranceCompany(copiains1);
        controller.saveInsuranceCompany();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setInsuranceCompany(ins1);
        try {
            controller.saveInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        ins1.setName("modificado");
        controller.setInsuranceCompany(ins1);
        controller.overwriteInsuranceCompany();


        // y nos aseguramos que ha guardado todo correctamente
        InsuranceCompany copia2ins1 = controller.getInsuranceCompanyById(ins1.getId());
        assertTrue(ins1.equals(copia2ins1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        InsuranceCompany ins3 = controller.newInsuranceCompany();
        ins3.setName("tres");
        controller.setInsuranceCompany(ins3);
        controller.overwriteInsuranceCompany();

        InsuranceCompany copiains3 = controller.getInsuranceCompanyById(controller.getInsuranceCompany().getId());
        assertTrue(ins3.equals(copiains3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        ins2.setName("dos");

        InsuranceCompany ins4 = controller.newInsuranceCompany();
        ins4.setName("cuatro");


        controller.setInsuranceCompany(ins2);
        controller.saveInsuranceCompany();

        ins4.setId(ins2.getId());
        controller.deleteInsuranceCompany();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setInsuranceCompany(ins4);
            controller.saveInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getInsuranceCompanyById(ins3.getId()));



        InsuranceCompany ins5 = controller.newInsuranceCompany();
        controller.setInsuranceCompany(ins5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        ins5.setName("cinco");
        controller.saveInsuranceCompany();
        assertEquals(ins5, controller.getInsuranceCompanyById(ins5.getId()));

    }

    @Test
    public void testEnableDisableInsuranceCompany() {

        System.out.println("Probando el activado y desactivado de InsuranceCompany");

        // Creamos un Employee y lo guardamos
        InsuranceCompany ins1 = controller.newInsuranceCompany();

        ins1.setName("nam");


        controller.setInsuranceCompany(ins1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableInsuranceCompany();
        assertFalse(ins1.getEnabled());
        controller.enableInsuranceCompany();
        assertTrue(ins1.getEnabled());



        // Comprobamos que se pueden activar y desactivar InsuranceCompany modificados
        InsuranceCompany foundins1 = controller.getInsuranceCompanyById(ins1.getId());
        foundins1.setName("cambiado");
        controller.setInsuranceCompany(foundins1);
        controller.saveInsuranceCompany();

        controller.setInsuranceCompany(ins1);
        controller.disableInsuranceCompany();
        assertFalse(ins1.getEnabled());


        foundins1 = controller.getInsuranceCompanyById(ins1.getId());
        foundins1.setName("otravez");
        controller.setInsuranceCompany(foundins1);
        controller.saveInsuranceCompany();

        controller.setInsuranceCompany(ins1);
        controller.enableInsuranceCompany();
        assertTrue(ins1.getEnabled());




        ins1.setId(Long.parseLong("123"));
        controller.setInsuranceCompany(ins1);
        // No se puede desactivar un InsuranceCompany que no existe
        try {
            controller.disableInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un InsuranceCompany que no existe
        try {
            controller.enableInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteInsuranceCompany() {

        System.out.println("Probando el borrado de InsuranceCompany");

        // Creamos un InsuranceCompany y lo guardamos
        InsuranceCompany ins1 = controller.newInsuranceCompany();

        ins1.setName("name");


        controller.setInsuranceCompany(ins1);
        controller.saveInsuranceCompany();


        // Comprobamos que lo borra correctamente
        controller.deleteInsuranceCompany();
        assertNull(controller.getInsuranceCompanyById(ins1.getId()));




        InsuranceCompany ins2 = controller.newInsuranceCompany();
        ins2.setId(new Long("-1"));
        controller.setInsuranceCompany(ins2);


        // Si la id es invalida dara error
        try {
            controller.deleteInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente InsuranceCompany aunque estén modificadas
        InsuranceCompany ins3 = controller.newInsuranceCompany();
        ins3.setName("not");


        controller.setInsuranceCompany(ins3);
        controller.saveInsuranceCompany();

        InsuranceCompany ins4 = controller.getInsuranceCompanyById(ins3.getId());
        ins4.setName("aaaa");
        controller.setInsuranceCompany(ins4);
        controller.saveInsuranceCompany();


        controller.setInsuranceCompany(ins3);
        controller.deleteInsuranceCompany();


        // Comprobamos que da error si intentamos borrar un InsuranceCompany que ya ha sido borrado
        try {
            controller.setInsuranceCompany(ins3);
            controller.deleteInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        InsuranceCompany ins5 = controller.newInsuranceCompany();
        ins5.setName("aaaa");
        ins5.setId(ins3.getId());

        controller.setInsuranceCompany(ins5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getInsuranceCompanyById(ins5.getId()));


    }

    @Test
    public void testFindInsuranceCompany() {

        System.out.println("Probando la busqueda de InsuranceCompany");

        // Creamos un InsuranceCompany
        InsuranceCompany ins1 = controller.newInsuranceCompany();
        InsuranceCompany ins2 = controller.newInsuranceCompany();

        // Asignamos sus propiedades básicas y lo guardamos
        ins1.setName("testname");
        ins1.setNotes("testnotes");
        ins1.setNif(new NIF("66877899V"));
        ins1.setWeb("google.es");
        
        
        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        add.setCountry("country");
        add.setNotes("notes");
        add.setPostalCode("12342");
        add.setProvince("province");
        add.setStreet("street");
        ins1.addAddress(add);
        ins1.addPhone(new Phone("987234132", "lab"));
        ins1.addEmail(new Email("address@a.com", "label"));

        controller.setInsuranceCompany(ins1);
        assertEquals(ins1, controller.getInsuranceCompany());
        controller.saveInsuranceCompany();



        ins2.setName("dos");
        ins2.setNotes("notas");
        ins2.setNif(new NIF("12345678Z"));
        ins2.setWeb("banco.com");
        ins2.setEnabled(false);
        
        Address add1 = new Address();
        add1.setLabel("bel");
        add1.setCity("ciudad");
        add1.setCountry("pais");
        add1.setNotes("not");
        add1.setPostalCode("65465");
        add1.setProvince("provincia");
        add1.setStreet("calle");
        ins2.addAddress(add1);
        ins2.addPhone(new Phone("988556699", "lab"));
        ins2.addEmail(new Email("test@email.es", "label"));

        controller.setInsuranceCompany(ins2);
        controller.saveInsuranceCompany();


        List<InsuranceCompany> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos roles y ninguno mas
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("test");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("dos");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("66");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("123");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByWeb("goo");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByWeb("com");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("ciu");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("city");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("pai");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("cou");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("be");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("la");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("65");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("12");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("provinci");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("province");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("cal");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("stre");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("test");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("addre");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailLabel("label");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByPhoneNumber("988");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("987");
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneLabel("lab");
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.size() == 2);



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(ins1));
        assertFalse(foundList.contains(ins2));
        assertTrue(foundList.size() == 1);
        ins2.setEnabled(true);
        controller.setInsuranceCompany(ins2);
        controller.saveInsuranceCompany();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(ins1));
        assertTrue(foundList.contains(ins2));
        assertTrue(foundList.size() == 2);
    }

    @Test
    public void testInsuranceCompanyCollisions() {

        System.out.println("Probando las colisiones de InsuranceCompany");

        // Creamos un InsuranceCompany y lo guardamos
        InsuranceCompany ins1 = controller.newInsuranceCompany();
        InsuranceCompany ins2 = controller.newInsuranceCompany();


        ins1.setName("name");
        ins1.setNif(new NIF("12345678Z"));


        controller.setInsuranceCompany(ins1);
        controller.saveInsuranceCompany();


        ins2.setName("dos");
        ins2.setNif(ins1.getNif());
        
        controller.setInsuranceCompany(ins2);
        // No puede haber dos InsuranceCompany con el mismo nif
        try {
            controller.saveInsuranceCompany();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }
}
