package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.model.repair.Labour;
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

public class LabourHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private LabourHibernateController controller = new LabourHibernateController();

    public LabourHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/repair/hibernate/Labour.hbm.xml");

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();


        t = s.beginTransaction();
        Query q1 = s.createQuery("delete from Labour");
        q1.executeUpdate();
        t.commit();

        s.close();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveLabour() {

        System.out.println("Probando el guardado de Labour");

        // Creamos un Labour y le asignamos todas sus propiedades
        Labour lab1 = controller.newLabour();
        Labour lab2 = controller.newLabour();

        lab1.setName("testname");
        lab1.setDescription("desc");
        lab1.setNotes("not");
        lab1.setPrice("123");


        // Lo asignamos al controlador
        controller.setLabour(lab1);

        // Comprobamos la asignacion
        assertEquals(lab1, controller.getLabour());

        // Lo guardamos
        controller.saveLabour();

        // Y comprobamos que se guarda correctamente.
        Labour foundLabour = controller.searchByName("testname").get(0);
        assertTrue(lab1.equals(foundLabour));
        assertTrue(lab1.equals(controller.getLabourById(lab1.getId())));
        assertFalse(lab1.equals(controller.getLabourById(lab2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Labour copialab1 = controller.getLabourById(lab1.getId());

        // Lo modificamos y guardamos
        copialab1.setName("modificado");
        controller.setLabour(copialab1);
        controller.saveLabour();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setLabour(lab1);
        try {
            controller.saveLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        lab1.setName("modificado");
        controller.setLabour(lab1);
        controller.overwriteLabour();


        // y nos aseguramos que ha guardado todo correctamente
        Labour copia2lab1 = controller.getLabourById(lab1.getId());
        assertTrue(lab1.equals(copia2lab1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        Labour lab3 = controller.newLabour();
        lab3.setName("test");
        lab3.setPrice("1");
        controller.setLabour(lab3);
        controller.overwriteLabour();

        Labour copialab3 = controller.getLabourById(controller.getLabour().getId());
        assertTrue(lab3.equals(copialab3));



        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        lab2.setName("do");
        lab2.setPrice("1");

        Labour lab4 = controller.newLabour();
        lab4.setName("cu");
        lab4.setPrice("1");


        controller.setLabour(lab2);
        controller.saveLabour();

        lab4.setId(lab2.getId());
        controller.deleteLabour();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setLabour(lab4);
            controller.saveLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getLabourById(lab3.getId()));



        Labour lab5 = controller.newLabour();
        controller.setLabour(lab5);

        // Si no tiene nombre no se puede guardar
        try {
            controller.saveLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        lab5.setName("cinco");
        // Si no tiene precio no se puede guardar
        try {
            controller.saveLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        lab5.setPrice("43");
        controller.saveLabour();

        assertEquals(lab5, controller.getLabourById(lab5.getId()));
    }

    @Test
    public void testEnableDisableLabour() {

        System.out.println("Probando el activado y desactivado de Labour");

        // Creamos un Employee y lo guardamos
        Labour lab1 = controller.newLabour();

        lab1.setName("nam");
        lab1.setPrice("1");


        controller.setLabour(lab1);

        // Comprobamos que desactiva y activa correctamente
        controller.disableLabour();
        assertFalse(lab1.getEnabled());
        controller.enableLabour();
        assertTrue(lab1.getEnabled());



        // Comprobamos que se pueden activar y desactivar Labour modificados
        Labour foundlab1 = controller.getLabourById(lab1.getId());
        foundlab1.setName("cambiado");
        controller.setLabour(foundlab1);
        controller.saveLabour();

        controller.setLabour(lab1);
        controller.disableLabour();
        assertFalse(lab1.getEnabled());


        foundlab1 = controller.getLabourById(lab1.getId());
        foundlab1.setName("otravez");
        controller.setLabour(foundlab1);
        controller.saveLabour();

        controller.setLabour(lab1);
        controller.enableLabour();
        assertTrue(lab1.getEnabled());




        lab1.setId(Long.parseLong("123"));
        controller.setLabour(lab1);
        // No se puede desactivar un Labour que no existe
        try {
            controller.disableLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        // No se puede activar un Labour que no existe
        try {
            controller.enableLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


    }

    @Test
    public void testDeleteLabour() {

        System.out.println("Probando el borrado de Labour");

        // Creamos un Labour y lo guardamos
        Labour lab1 = controller.newLabour();

        lab1.setName("test");
        lab1.setPrice("1");


        controller.setLabour(lab1);
        controller.saveLabour();


        // Comprobamos que lo borra correctamente
        controller.deleteLabour();
        assertNull(controller.getLabourById(lab1.getId()));




        Labour lab2 = controller.newLabour();
        lab2.setId(new Long("-1"));
        controller.setLabour(lab2);


        // Si la id es invalida dara error
        try {
            controller.deleteLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Labour aunque estén modificadas
        Labour lab3 = controller.newLabour();
        lab3.setName("tres");
        lab3.setPrice("1");


        controller.setLabour(lab3);
        controller.saveLabour();

        Labour lab4 = controller.getLabourById(lab3.getId());
        lab4.setName("cuatro");
        controller.setLabour(lab4);
        controller.saveLabour();


        controller.setLabour(lab3);
        controller.deleteLabour();


        // Comprobamos que da error si intentamos borrar un Labour que ya ha sido borrado
        try {
            controller.setLabour(lab3);
            controller.deleteLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Labour lab5 = controller.newLabour();
        lab5.setName("cinco");
        lab5.setPrice("1");
        lab5.setId(lab3.getId());

        controller.setLabour(lab5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getLabourById(lab5.getId()));


//        // No podemos borrar un Labour que esté referenciado por un $clasesubordinada$
//        Labour lab6 = controller.newLabour();
//        lab6.setName("seis");
//        controller.setLabour(lab6);
//        controller.saveLabour();
//
//        // Creamos el $clasesubordinada$
//        $clasesubordinada$ $varsub$ = new $clasesubordinada$();
//        $varsub$.setName("sub");
//        $varsub$.setLabour(lab6);
//
//        // Lo guardamos
//        $clasesubordinada$Controller $varsub$Controller = new $clasesubordinada$HibernateController();
//        $varsub$Controller.set$clasesubordinada$$clasesubordinada$($varsub$);
//        $varsub$Controller.save$clasesubordinada$();
//
//        // Y comprobamos que no se puede borrar el Labour
//        try {
//            controller.deleteLabour();
//            fail("Expected Runtime Exception");
//        } catch (RuntimeException expected) {
//        }
    }

    @Test
    public void testFindLabour() {

        System.out.println("Probando la busqueda de Labour");

        // Creamos un Labour
        Labour lab1 = controller.newLabour();
        Labour lab2 = controller.newLabour();

        // Asignamos sus propiedades básicas y lo guardamos
        lab1.setName("testname");
        lab1.setDescription("desc");
        lab1.setNotes("not");
        lab1.setPrice("123");

        controller.setLabour(lab1);
        assertEquals(lab1, controller.getLabour());
        controller.saveLabour();



        lab2.setName("second");
        lab2.setDescription("not");
        lab2.setNotes("pass");
        lab2.setPrice("888");
        lab2.setEnabled(false);

        controller.setLabour(lab2);
        controller.saveLabour();


        List<Labour> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos Laboures y ninguno mas
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("testname");
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("second");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByDescription("des");
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByDescription("no");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByDescription("des");
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMaxPrice("888");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByMaxPrice("700");
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByMinPrice("1");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 2);
        foundList = controller.searchByMinPrice("200");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPrice("888");
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByPrice("123");
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.size() == 1);
        



        // Comprobamos que encuentra correctamente los activados y desactivados
        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(lab1));
        assertFalse(foundList.contains(lab2));
        assertTrue(foundList.size() == 1);
        lab2.setEnabled(true);
        controller.setLabour(lab2);
        controller.saveLabour();

        foundList = controller.listAllEnabled();
        assertTrue(foundList.contains(lab1));
        assertTrue(foundList.contains(lab2));
        assertTrue(foundList.size() == 2);
    }

    @Test
    public void testLabourCollisions() {

        System.out.println("Probando las colisiones de Labour");

        // Creamos un Labour y lo guardamos
        Labour lab1 = controller.newLabour();
        Labour lab2 = controller.newLabour();


        lab1.setName("test");
        lab1.setPrice("1");


        controller.setLabour(lab1);
        controller.saveLabour();


        lab2.setName(lab1.getName());
        lab2.setPrice("2");
        controller.setLabour(lab2);
        
        // No puede haber dos Labour con el mismo nombre
        try {
            controller.saveLabour();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }
}
