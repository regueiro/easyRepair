package es.regueiro.easyrepair.dao.employee.hibernate;

import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.NSS;
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private EmployeeHibernateController controller = new EmployeeHibernateController();

    public EmployeeHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/employee/hibernate/Phone.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/employee/hibernate/Email.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/employee/hibernate/Address.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/employee/hibernate/Employee.hbm.xml");

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
        Query q4 = s.createQuery("delete from Employee");
        q4.executeUpdate();
        t.commit();



        s.close();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveEmployee() {

        System.out.println("Probando el guardado de Employee");

        // Creamos un Employee y le asignamos todas sus propiedades
        Employee emp1 = controller.newEmployee();
        Employee emp2 = controller.newEmployee();

        emp1.setName("testname");
        emp1.setSurname("testsurname");
        emp1.setNif(new NIF("66877899V"));
        emp1.setNss(new NSS("258865148973"));
        emp1.setEmployeeId("1");
        emp1.setNotes("testnotes");
        emp1.setOccupation("Mechanic");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        emp1.addAddress(add);
        emp1.addPhone(new Phone("987234132", "lab"));
        emp1.addEmail(new Email("address@a.com", "label"));


        // Lo asignamos al controlador
        controller.setEmployee(emp1);

        // Comprobamos la asignacion
        assertEquals(emp1, controller.getEmployee());

        // Lo guardamos
        controller.saveEmployee();

        // Y comprobamos que se guarda correctamente.
        Employee foundEmployee = controller.searchByName("testname").get(0);
        assertTrue(emp1.equals(foundEmployee));
        assertTrue(emp1.equals(controller.getEmployeeById(emp1.getId())));
        assertFalse(emp1.equals(controller.getEmployeeById(emp2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Employee copiaemp1 = controller.getEmployeeById(emp1.getId());

        // Lo modificamos y guardamos
        copiaemp1.setSurname("modificado");
        controller.setEmployee(copiaemp1);
        controller.saveEmployee();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setEmployee(emp1);
        try {
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        emp1.setNotes("modificado");
        controller.setEmployee(emp1);
        controller.overwriteEmployee();


        // y nos aseguramos que ha guardado todo correctamente
        Employee copia2emp1 = controller.getEmployeeById(emp1.getId());
        assertTrue(emp1.equals(copia2emp1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Employee emp3 = controller.newEmployee();
        emp3.setName("na");
        controller.setEmployee(emp3);
        controller.overwriteEmployee();

        Employee copiaemp3 = controller.getEmployeeById(controller.getEmployee().getId());
        assertTrue(emp3.equals(copiaemp3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        emp2.setName("lala");

        Employee emp4 = controller.newEmployee();
        emp4.setName("naaa");


        controller.setEmployee(emp2);
        controller.saveEmployee();

        emp4.setId(emp2.getId());
        controller.deleteEmployee();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setEmployee(emp4);
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getEmployeeById(emp3.getId()));



        Employee emp5 = controller.newEmployee();
        controller.setEmployee(emp5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        emp5.setName("cinco");
        controller.saveEmployee();

    }

    @Test
    public void testEnableDisableEmployee() {

        System.out.println("Probando el activado y desactivado de Employee");

        // Creamos un Employee y lo guardamos
        Employee emp1 = controller.newEmployee();

        emp1.setName("nam");


        controller.setEmployee(emp1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableEmployee();
        assertFalse(emp1.getEnabled());
        controller.enableEmployee();
        assertTrue(emp1.getEnabled());

        

        // Comprobamos que se pueden activar y desactivar empleados modificados
        Employee foundemp1 = controller.getEmployeeById(emp1.getId());
        foundemp1.setName("cambiado");
        controller.setEmployee(foundemp1);
        controller.saveEmployee();
        
        controller.setEmployee(emp1);
        controller.disableEmployee();
        assertFalse(emp1.getEnabled());
        
        
        foundemp1 = controller.getEmployeeById(emp1.getId());
        foundemp1.setName("otravez");
        controller.setEmployee(foundemp1);
        controller.saveEmployee();
        
        controller.setEmployee(emp1);
        controller.enableEmployee();
        assertTrue(emp1.getEnabled());
        
        
        
        

        // No se puede desactivar un empleado que no existe
        emp1.setId(Long.parseLong("123"));
        controller.setEmployee(emp1);
        try {
            controller.disableEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un empleado que no existe
        try {
            controller.enableEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

    }

    @Test
    public void testDeleteEmployee() {

        System.out.println("Probando el borrado de Employee");

        // Creamos un Employee y lo guardamos
        Employee emp1 = controller.newEmployee();

        emp1.setName("nam");


        controller.setEmployee(emp1);
        controller.saveEmployee();


        // Comprobamos que lo borra correctamente
        controller.deleteEmployee();
        assertNull(controller.getEmployeeById(emp1.getId()));




        Employee emp2 = controller.newEmployee();
        emp2.setId(new Long("-1"));
        controller.setEmployee(emp2);


        // Si la id es invalida dara error
        try {
            controller.deleteEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Employee aunque estén modificadas
        Employee emp3 = controller.newEmployee();
        emp3.setName("3");


        controller.setEmployee(emp3);
        controller.saveEmployee();

        Employee emp4 = controller.getEmployeeById(emp3.getId());
        emp4.setName("4");
        controller.setEmployee(emp4);
        controller.saveEmployee();


        controller.setEmployee(emp3);
        controller.deleteEmployee();


        // Comprobamos que da error si intentamos borrar un Employee que ya ha sido borrado
        try {
            controller.setEmployee(emp3);
            controller.deleteEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Employee emp5 = controller.newEmployee();
        emp5.setName("56");
        emp5.setId(emp3.getId());

        controller.setEmployee(emp5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getEmployeeById(emp5.getId()));

    }

    @Test
    public void testFindEmployee() {

        System.out.println("Probando la busqueda de Employee");

        // Creamos un Employee
        Employee emp1 = controller.newEmployee();
        Employee emp2 = controller.newEmployee();

        // Asignamos sus propiedades básicas y lo guardamos
        emp1.setName("testname");
        emp1.setSurname("testsurname");
        emp1.setNif(new NIF("66877899V"));
        emp1.setNss(new NSS("258865148973"));
        emp1.setEmployeeId("1");
        emp1.setNotes("testnotes");
        emp1.setOccupation("Mechanic");

        Address add = new Address();
        add.setLabel("lav");
        add.setCity("city");
        add.setCountry("country");
        add.setNotes("notes");
        add.setPostalCode("12342");
        add.setProvince("province");
        add.setStreet("street");
        emp1.addAddress(add);
        emp1.addPhone(new Phone("987234132", "lab"));
        emp1.addEmail(new Email("address@a.com", "label"));

        controller.setEmployee(emp1);
        assertEquals(emp1, controller.getEmployee());
        controller.saveEmployee();



        emp2.setName("dos");
        emp2.setSurname("apellido");
        emp2.setNif(new NIF("12345678Z"));
        emp2.setNss(new NSS("123456789002"));
        emp2.setNotes("notas");
        emp2.setOccupation("recepcion");
        emp2.setEnabled(false);

        Address add1 = new Address();
        add1.setLabel("bel");
        add1.setCity("ciudad");
        add1.setCountry("pais");
        add1.setNotes("not");
        add1.setPostalCode("65465");
        add1.setProvince("provincia");
        add1.setStreet("calle");
        emp2.addAddress(add1);
        emp2.addPhone(new Phone("988556699", "lab"));
        emp2.addEmail(new Email("test@email.es", "label"));

        controller.setEmployee(emp2);
        controller.saveEmployee();


        List<Employee> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos roles y ninguno mas
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("testname");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("dos");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchBySurname("testsu");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchBySurname("apellido");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("123");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNif("668");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNss("258865148973");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByNss("12345");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmployeeId("00000001");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByOccupation("recep");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByOccupation("mecha");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("ciu");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCity("city");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("pai");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressCountry("cou");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("be");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressLabel("la");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("65");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressPostalCode("12");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("provinci");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressProvince("province");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("cal");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByAddressStreet("stre");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("test");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailAddress("addre");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByEmailLabel("label");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByPhoneNumber("988");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneNumber("987");
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPhoneLabel("lab");
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.size() == 2);


        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(emp1));
        assertFalse(foundList.contains(emp2));
        assertTrue(foundList.size() == 1);
        emp2.setEnabled(true);
        controller.setEmployee(emp2);
        controller.saveEmployee();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(emp1));
        assertTrue(foundList.contains(emp2));
        assertTrue(foundList.size() == 2);

    }

    @Test
    public void testEmployeeCollisions() {

        System.out.println("Probando las colisiones de Employee");

        // Creamos un Employee y lo guardamos
        Employee emp1 = controller.newEmployee();
        Employee emp2 = controller.newEmployee();


        emp1.setName("testname");
        emp1.setNif(new NIF("12345678Z"));
        emp1.setNss(new NSS("123456789002"));


        controller.setEmployee(emp1);
        controller.saveEmployee();


        emp2.setName("dos");
        emp2.setEmployeeId(emp1.getEmployeeId());
        controller.setEmployee(emp2);
        // No puede haber dos Employee con el mismo employeeId
        try {
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // No puede haber dos Employee con el mismo nif
        Employee emp3 = controller.newEmployee();
        emp3.setName("tres");
        emp3.setNif(emp1.getNif());
        controller.setEmployee(emp3);
        try {
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // No puede haber dos Employee con el mismo nss
        Employee emp4 = controller.newEmployee();
        emp4.setName("cuatro");
        emp4.setNss(emp1.getNss());
        controller.setEmployee(emp4);
        try {
            controller.saveEmployee();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



    }
}
