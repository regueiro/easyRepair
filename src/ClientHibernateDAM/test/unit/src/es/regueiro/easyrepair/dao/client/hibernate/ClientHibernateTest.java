package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.controller.InsuranceCompanyController;
import es.regueiro.easyrepair.api.client.controller.VehicleController;
import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.Vehicle;
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

public class ClientHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private ClientHibernateController controller = new ClientHibernateController();
    private Vehicle veh;
    private InsuranceCompany ins;

    public ClientHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Vehicle.hbm.xml");
        
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
        Query q4 = s.createQuery("delete from Vehicle");
        q4.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q5 = s.createQuery("delete from Client");
        q5.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q6 = s.createQuery("delete from InsuranceCompany");
        q6.executeUpdate();
        t.commit();

        s.close();

        // Creamos un Vehicle de prueba
        VehicleController vehController = new VehicleHibernateController();
        InsuranceCompanyController insController = new InsuranceCompanyHibernateController();
        veh = new Vehicle();
        veh.setMake("make");
        veh.setRegistration("reg");
//        vehController.setVehicle(veh);
//        vehController.saveVehicle();

        ins = insController.newInsuranceCompany();
        ins.setName("insurance");
        insController.setInsuranceCompany(ins);
        insController.saveInsuranceCompany();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveClient() {

        System.out.println("Probando el guardado de Client");

        // Creamos un Client y le asignamos todas sus propiedades
        Client cli1 = controller.newClient();
        Client cli2 = controller.newClient();

        cli1.setName("testname");
        cli1.setSurname("testsurname");
        cli1.setNotes("testnotes");
        cli1.setNif(new NIF("66877899V"));

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        cli1.addAddress(add);
        cli1.addPhone(new Phone("987234132", "lab"));
        cli1.addEmail(new Email("address@a.com", "label"));
        
        cli1.addVehicle(veh);


        // Lo asignamos al controlador
        controller.setClient(cli1);

        // Comprobamos la asignacion
        assertEquals(cli1, controller.getClient());

        // Lo guardamos
        controller.saveClient();

        // Y comprobamos que se guarda correctamente.
        Client foundClient = controller.searchByName("testname").get(0);
        assertTrue(cli1.equals(foundClient));
        assertTrue(cli1.equals(controller.getClientById(cli1.getId())));
        assertFalse(cli1.equals(controller.getClientById(cli2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Client copiacli1 = controller.getClientById(cli1.getId());

        // Lo modificamos y guardamos
        copiacli1.setName("modificado");
        controller.setClient(copiacli1);
        controller.saveClient();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setClient(cli1);
        try {
            controller.saveClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        cli1.setName("modificado");
        controller.setClient(cli1);
        controller.overwriteClient();


        // y nos aseguramos que ha guardado todo correctamente
        Client copia2cli1 = controller.getClientById(cli1.getId());
        assertTrue(cli1.equals(copia2cli1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Client cli3 = controller.newClient();
        cli3.setName("ba");
        controller.setClient(cli3);
        controller.overwriteClient();

        Client copiacli3 = controller.getClientById(controller.getClient().getId());
        assertTrue(cli3.equals(copiacli3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        cli2.setName("dos");

        Client cli4 = controller.newClient();
        cli4.setName("cuatro");


        controller.setClient(cli2);
        controller.saveClient();

        cli4.setId(cli2.getId());
        controller.deleteClient();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setClient(cli4);
            controller.saveClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getClientById(cli3.getId()));



        Client cli5 = controller.newClient();
        controller.setClient(cli5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        cli5.setName("cinco");
          controller.saveClient();

        assertEquals(cli5, controller.getClientById(cli5.getId()));
    }

    @Test
    public void testEnableDisableClient() {

        System.out.println("Probando el activado y desactivado de Client");

        // Creamos un Employee y lo guardamos
        Client cli1 = controller.newClient();

        cli1.setName("nam");


        controller.setClient(cli1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableClient();
        assertFalse(cli1.getEnabled());
        controller.enableClient();
        assertTrue(cli1.getEnabled());



        // Comprobamos que se pueden activar y desactivar Client modificados
        Client foundcli1 = controller.getClientById(cli1.getId());
        foundcli1.setName("cambiado");
        controller.setClient(foundcli1);
        controller.saveClient();

        controller.setClient(cli1);
        controller.disableClient();
        assertFalse(cli1.getEnabled());


        foundcli1 = controller.getClientById(cli1.getId());
        foundcli1.setName("otravez");
        controller.setClient(foundcli1);
        controller.saveClient();

        controller.setClient(cli1);
        controller.enableClient();
        assertTrue(cli1.getEnabled());




        cli1.setId(Long.parseLong("123"));
        controller.setClient(cli1);
        // No se puede desactivar un Client que no existe
        try {
            controller.disableClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un Client que no existe
        try {
            controller.enableClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteClient() {

        System.out.println("Probando el borrado de Client");

        // Creamos un Client y lo guardamos
        Client cli1 = controller.newClient();

        cli1.setName("test");


        controller.setClient(cli1);
        controller.saveClient();


        // Comprobamos que lo borra correctamente
        controller.deleteClient();
        assertNull(controller.getClientById(cli1.getId()));




        Client cli2 = controller.newClient();
        cli2.setId(new Long("-1"));
        controller.setClient(cli2);


        // Si la id es invalida dara error
        try {
            controller.deleteClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Client aunque estén modificadas
        Client cli3 = controller.newClient();
        cli3.setName("tres");


        controller.setClient(cli3);
        controller.saveClient();

        Client cli4 = controller.getClientById(cli3.getId());
        cli4.setName("cua");
        controller.setClient(cli4);
        controller.saveClient();


        controller.setClient(cli3);
        controller.deleteClient();


        // Comprobamos que da error si intentamos borrar un Client que ya ha sido borrado
        try {
            controller.setClient(cli3);
            controller.deleteClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Client cli5 = controller.newClient();
        cli5.setName("ci");
        cli5.setId(cli3.getId());

        controller.setClient(cli5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getClientById(cli5.getId()));


        // No podemos borrar un Client que esté referenciado por un Vehicle
        Client cli6 = controller.newClient();
        cli6.setName("sa");
        controller.setClient(cli6);
        controller.saveClient();

        // Enlazamos el Vehicle
        cli6.addVehicle(veh);
        
        // Lo guardamos
        controller.saveClient();

        // Y comprobamos que no se puede borrar el Client
        try {
            controller.deleteClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }

    @Test
    public void testFindClient() {

        System.out.println("Probando la busqueda de Client");

        // Creamos un Client
        Client cli1 = controller.newClient();
        Client cli2 = controller.newClient();

        // Asignamos sus propiedades básicas y lo guardamos
        cli1.setName("testname");
        cli1.setSurname("testsurname");
        cli1.setNif(new NIF("66877899V"));
        cli1.setClientId("1");
        cli1.setNotes("testnotes");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        add.setCountry("country");
        add.setNotes("notes");
        add.setPostalCode("12342");
        add.setProvince("province");
        add.setStreet("street");
        cli1.addAddress(add);
        cli1.addPhone(new Phone("987234132", "lab"));
        cli1.addEmail(new Email("address@a.com", "label"));

        

        controller.setClient(cli1);
        assertEquals(cli1, controller.getClient());
        controller.saveClient();



        cli2.setName("dos");
        cli2.setSurname("apellido");
        cli2.setNif(new NIF("12345678Z"));
        cli2.setNotes("notas");
        cli2.setEnabled(false);

        Address add1 = new Address();
        add1.setLabel("bel");
        add1.setCity("ciudad");
        add1.setCountry("pais");
        add1.setNotes("not");
        add1.setPostalCode("65465");
        add1.setProvince("provincia");
        add1.setStreet("calle");
        cli2.addAddress(add1);
        cli2.addPhone(new Phone("988556699", "lab"));
        cli2.addEmail(new Email("test@email.es", "label"));

        controller.setClient(cli2);
        controller.saveClient();


        List<Client> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos Clientes y ninguno mas
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("testname");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("dos");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchBySurname("testsu");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchBySurname("apellido");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("123");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("668");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByClientId("00000000001");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("ciu");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("city");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("pai");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("cou");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("be");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("la");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("65");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("12");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("provinci");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("province");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("cal");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("stre");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("test");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("addre");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailLabel("label");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByPhoneNumber("988");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("987");
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneLabel("lab");
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.size() == 2);



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(cli1));
        assertFalse(foundList.contains(cli2));
        assertTrue(foundList.size() == 1);
        cli2.setEnabled(true);
        controller.setClient(cli2);
        controller.saveClient();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(cli1));
        assertTrue(foundList.contains(cli2));
        assertTrue(foundList.size() == 2);
    }

    @Test
    public void testClientCollisions() {

        System.out.println("Probando las colisiones de Client");

        // Creamos un Client y lo guardamos
        Client cli1 = controller.newClient();
        Client cli2 = controller.newClient();


        cli1.setName("testname");
        cli1.setNif(new NIF("12345678Z"));


        controller.setClient(cli1);
        controller.saveClient();


        cli2.setName("dos");
        cli2.setClientId(cli1.getClientId());
        controller.setClient(cli2);
        // No puede haber dos Client con el mismo clientId
        try {
            controller.saveClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        


        // No puede haber dos Client con el mismo nif
        Client cli3 = controller.newClient();
        cli3.setName("tres");
        cli3.setNif(cli1.getNif());
        controller.setClient(cli3);
        try {
            controller.saveClient();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }
}
