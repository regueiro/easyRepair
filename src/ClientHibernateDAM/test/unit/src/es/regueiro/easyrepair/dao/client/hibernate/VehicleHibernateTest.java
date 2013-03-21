package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.Vehicle;
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

public class VehicleHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private VehicleHibernateController controller = new VehicleHibernateController();
    private ClientHibernateController clientController = new ClientHibernateController();

    public VehicleHibernateTest() {
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


    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFindVehicle() {

        System.out.println("Probando la busqueda de Vehicle");

        Client cli1 = clientController.newClient();

        // Creamos un Vehicle
        Vehicle veh1 = new Vehicle();
        veh1.setMake("make");
        veh1.setRegistration("reg");
        veh1.setColour("red");
        veh1.setFuel("gas");
        veh1.setNotes("notes");
        veh1.setModel("model");
        veh1.setType("sedan");
        veh1.setVin("111");
        veh1.setYear("1999");


        cli1.setName("testname");
        cli1.setSurname("testsurname");
        cli1.setNotes("testnotes");
        cli1.setNif(new NIF("66877899V"));
        cli1.addVehicle(veh1);




        clientController.setClient(cli1);
        clientController.saveClient();



        Client cli2 = clientController.newClient();

        // Creamos un Vehicle
        Vehicle veh2 = new Vehicle();
        veh2.setMake("marca");
        veh2.setRegistration("mat");
        veh2.setColour("color");
        veh2.setFuel("diesel");
        veh2.setNotes("notas");
        veh2.setModel("laal");
        veh2.setType("hatch");
        veh2.setVin("55552");
        veh2.setYear("2003");
        veh2.setEnabled(false);

        cli2.setName("nombre");
        cli2.setSurname("apellid");
        cli2.setNif(new NIF("12345678Z"));
        cli2.addVehicle(veh2);

        clientController.setClient(cli2);
        clientController.saveClient();

        List<Vehicle> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos roles y ninguno mas
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 2);

     
        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByMake("make");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMake("marca");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByRegistration("reg");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByRegistration("mat");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByColour("red");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByColour("color");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByFuel("gas");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByFuel("diesel");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByModel("model");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByModel("laal");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByType("sedan");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByType("hatch");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByVin("11");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByVin("5");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByYear("1999");
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByYear("2003");
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        

        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(veh1));
        assertFalse(foundList.contains(veh2));
        assertTrue(foundList.size() == 1);
        veh2.setEnabled(true);
        clientController.setClient(cli2);
        clientController.saveClient();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(veh1));
        assertTrue(foundList.contains(veh2));
        assertTrue(foundList.size() == 2);
    }

  
}
