package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.controller.UserController;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.model.user.Role;
import es.regueiro.easyrepair.model.user.User;
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


public class RoleHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private RoleHibernateController controller = new RoleHibernateController();

    public RoleHibernateTest() {
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

        cfg.addResource("es/regueiro/easyrepair/dao/user/hibernate/Role.hbm.xml");
        cfg.addResource("es/regueiro/easyrepair/dao/user/hibernate/User.hbm.xml");

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        s = sessionFactory.openSession();


        t = s.beginTransaction();
        Query q1 = s.createSQLQuery("truncate table role_privilege");
        q1.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q2 = s.createSQLQuery("delete from User");
        q2.executeUpdate();
        t.commit();

        t = s.beginTransaction();
        Query q3 = s.createSQLQuery("delete from Role");
        q3.executeUpdate();
        t.commit();

        s.close();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of listAll method, of class RoleHibernateController.
     */
    @Test
    public void testSaveRole() {

        System.out.println("Probando el guardado de Role");

        // Creamos un Role y le asignamos todas sus propiedades
        Role role1 = controller.newRole();
        Role role2 = controller.newRole();

        role1.setName("role");
        role1.setDescription("desc");
        role1.addPrivilege(Privilege.PART);


        // Lo asignamos al controlador
        controller.setRole(role1);

        // Comprobamos la asignacion
        assertEquals(role1, controller.getRole());

        // Lo guardamos
        controller.saveRole();

        // Y comprobamos que se guarda correctamente.
        Role foundRole = controller.searchByName("role").get(0);
        assertTrue(role1.equals(foundRole));
        assertTrue(role1.equals(controller.getRoleById(role1.getId())));
        assertFalse(role1.equals(controller.getRoleById(role2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        Role copiarole1 = controller.getRoleById(role1.getId());

        // Lo modificamos y guardamos
        copiarole1.setName("modificado");
        controller.setRole(copiarole1);
        controller.saveRole();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setRole(role1);
        try {
            controller.saveRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Pero si podemos sobreescribir
        role1.setName("namemod");
        role1.setDescription("descmod");
        role1.addPrivilege(Privilege.VEHICLE);
        controller.setRole(role1);
        controller.overwriteRole();

        // y nos aseguramos que ha guardado todo correctamente
        Role copia2role1 = controller.getRoleById(role1.getId());
        assertTrue(role1.equals(copia2role1));


        // Tambien podemos sobreescribir aunque no exista el objeto
        Role role3 = controller.newRole();
        role3.setName("noexiste");
        role3.setDescription("noexiste");
        role3.addPrivilege(Privilege.ADMIN);
        controller.setRole(role3);
        controller.overwriteRole();

        
        Role copiarole3 = controller.getRoleById(controller.getRole().getId());
        assertTrue(role3.equals(copiarole3));

        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        role2.setName("name2");

        Role role4 = controller.newRole();
        role4.setName("name3");


        controller.setRole(role2);
        controller.saveRole();

        role4.setId(role2.getId());
        controller.deleteRole();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setRole(role3);
            controller.saveRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getRoleById(role3.getId()));
        
        
        Role role5 = controller.newRole();
        controller.setRole(role5);
        
        // Si no tiene nombre no se puede guardar
        try {
            controller.saveRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        
        role5.setDescription("cinco");
        // Si no tiene nombre no se puede guardar
        try {
            controller.saveRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        
        role5.setName("cinco");
        controller.saveRole();
        
        
        assertEquals(role5, controller.getRoleById(role5.getId()));
    }

    @Test
    public void testDeleteRole() {

        System.out.println("Probando el borrado de Role");

        // Creamos un Role y lo guardamos
        Role role1 = controller.newRole();

        role1.setName("role");
        role1.setDescription("desc");
        role1.addPrivilege(Privilege.PART);

        controller.setRole(role1);
        controller.saveRole();


        // Comprobamos que lo borra correctamente
        controller.deleteRole();
        assertNull(controller.getRoleById(role1.getId()));




        Role role2 = controller.newRole();
        role2.setId(new Long("-1"));
        controller.setRole(role2);


        // Si la id es invalida dara error
        try {
            controller.deleteRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente Role aunque estén modificadas
        Role role3 = controller.newRole();
        role3.setName("test");


        controller.setRole(role3);
        controller.saveRole();

        Role role4 = controller.getRoleById(role3.getId());
        role4.setName("cuatro");
        controller.setRole(role4);
        controller.saveRole();


        controller.setRole(role3);
        controller.deleteRole();


        // Comprobamos que da error si intentamos borrar un Role que ya ha sido borrado
        try {
            controller.setRole(role3);
            controller.deleteRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        Role role5 = controller.newRole();
        role5.setName("cinco");
        role5.setId(role3.getId());

        controller.setRole(role5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getRoleById(role5.getId()));

        
        
        // No podemos borrar un rol que esté referenciado por un usuario
        Role role6 = controller.newRole();
        role6.setName("seis");
        role6.addPrivilege(Privilege.ADMIN);
        controller.setRole(role6);
        controller.saveRole();
        
        // Creamos el usuario
        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        user.setRole(role6);
        
        // Lo guardamos
        UserController userController = new UserHibernateController();
        userController.setUser(user);
        userController.saveUser();
        
        // Y comprobamos que no se puede borrar el rol
        try {
            controller.deleteRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }

    @Test
    public void testFindRole() {

        System.out.println("Probando la busqueda de Role");

        // Creamos un Role
        Role role1 = controller.newRole();
        Role role2 = controller.newRole();

        // Asignamos sus propiedades básicas y lo guardamos
        role1.setName("testname");
        role1.setDescription("desc");
        role1.addPrivilege(Privilege.ADMIN);

        controller.setRole(role1);
        assertEquals(role1, controller.getRole());
        controller.saveRole();

        role2.setName("nombre");
        role2.setDescription("prici");
        role2.addPrivilege(Privilege.EMPLOYEE);

        controller.setRole(role2);
        controller.saveRole();


        List<Role> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos roles y ninguno mas
        assertTrue(foundList.contains(role1));
        assertTrue(foundList.contains(role2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("test");
        assertTrue(foundList.contains(role1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("omb");
        assertTrue(foundList.contains(role2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByDescription("des");
        assertTrue(foundList.contains(role1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByDescription("pr");
        assertTrue(foundList.contains(role2));
        assertTrue(foundList.size() == 1);

    }

    @Test
    public void testRoleCollisions() {

        System.out.println("Probando las colisiones de Role");

        // Creamos un Role y lo guardamos
        Role role1 = controller.newRole();
        Role role2 = controller.newRole();


        role1.setName("name");
        role1.setDescription("desc");
        role1.addPrivilege(Privilege.ADMIN);


        controller.setRole(role1);
        controller.saveRole();


        role2.setName(role1.getName());
        controller.setRole(role2);
        // No puede haber dos Role con el mismo nombre
        try {
            controller.saveRole();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }
}
