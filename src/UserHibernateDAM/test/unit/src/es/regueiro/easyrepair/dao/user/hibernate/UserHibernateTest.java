package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.controller.RoleController;
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


public class UserHibernateTest {

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session s;
    private Transaction t;
    private UserController controller = new UserHibernateController();
    private RoleController roleController = new RoleHibernateController();
    private Role role1, role2;
    
    public UserHibernateTest() {
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
        
        
        // Creamos un rol de prueba
        role1 = roleController.newRole();
        role1.setName("roleName");
        role1.addPrivilege(Privilege.ADMIN);
        roleController.setRole(role1);
        roleController.saveRole();
        
        role2 = roleController.newRole();
        role2.setName("dos");
        role2.addPrivilege(Privilege.USER);
        role2.addPrivilege(Privilege.USER_EDIT);
        roleController.setRole(role2);
        roleController.saveRole();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveUser() {

        System.out.println("Probando el guardado de User");

        
        
        
        // Creamos un User y le asignamos todas sus propiedades
        User user1 = controller.newUser();
        User user2 = controller.newUser();

        user1.setName("testname");
        user1.setPassword("testpass");
        user1.setRole(role1);


        // Lo asignamos al controlador
        controller.setUser(user1);

        // Comprobamos la asignacion
        assertEquals(user1, controller.getUser());

        // Lo guardamos
        controller.saveUser();

        // Y comprobamos que se guarda correctamente.
        User foundUser = controller.searchByName("testname").get(0);
        assertTrue(user1.equals(foundUser));
        assertTrue(user1.equals(controller.getUserById(user1.getId())));
        assertFalse(user1.equals(controller.getUserById(user2.getId())));


        // Cargamos el objeto de nuevo de la base de datos
        User copiauser1 = controller.getUserById(user1.getId());

        // Lo modificamos y guardamos
        copiauser1.setPassword("modificado");
        controller.setUser(copiauser1);
        controller.saveUser();

        // Y comprobamos que da error si intentamos guardar datos antiguos
        controller.setUser(user1);
        try {
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Pero si podemos sobreescribir
        user1.setPassword("mod");
        controller.setUser(user1);
        controller.overwriteUser();


        // y nos aseguramos que ha guardado todo correctamente
        User copia2user1 = controller.getUserById(user1.getId());
        assertTrue(user1.equals(copia2user1));



        // Tambien podemos sobreescribir aunque no exista el objeto
        User user3 = controller.newUser();
        user3.setName("noex");
        user3.setPassword("noex");
        user3.setRole(role1);
        controller.setUser(user3);
        controller.overwriteUser();

        User copiauser3 = controller.getUserById(controller.getUser().getId());
        assertTrue(user3.equals(copiauser3));


        // Comprobamos que si un objeto tiene ID y no existe en la base de datos, no se puede guardar
        user2.setName("a");
        user2.setPassword("a");
        user2.setRole(role1);

        User user4 = controller.newUser();
        user4.setName("cuatro");
        user4.setPassword("cuatro");
        user4.setRole(role1);

        controller.setUser(user2);
        controller.saveUser();

        user4.setId(user2.getId());
        controller.deleteUser();




        // Si la id no existe en la base de datos, da error
        try {
            controller.setUser(user3);
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // Nos aseguramos que no existe
        assertNull(controller.getUserById(user3.getId()));
        
        
        
        User user5 = controller.newUser();
        controller.setUser(user5);
        
        // Si no tiene nombre no se puede guardar
        try {
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        
        user5.setName("cinco");
        // Si no tiene password no se puede guardar
        try {
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        
        user5.setPassword("pass");
        // Si no tiene rol no se puede guardar
        try {
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
        
        user5.setRole(role1);
        controller.saveUser();
        
        assertEquals(user5, controller.getUserById(user5.getId()));
    }

    @Test
    public void testDeleteUser() {

        System.out.println("Probando el borrado de User");

        // Creamos un User y lo guardamos
        User user1 = controller.newUser();

        user1.setName("test");
        user1.setPassword("test");
        user1.setRole(role1);


        controller.setUser(user1);
        controller.saveUser();


        // Comprobamos que lo borra correctamente
        controller.deleteUser();
        assertNull(controller.getUserById(user1.getId()));




        User user2 = controller.newUser();
        user2.setId(new Long("-1"));
        controller.setUser(user2);


        // Si la id es invalida dara error
        try {
            controller.deleteUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }



        // Comprobamos que borra correctamente User aunque estén modificadas
        User user3 = controller.newUser();
        user3.setName("test1");
        user3.setPassword("test2");
        user3.setRole(role1);


        controller.setUser(user3);
        controller.saveUser();

        User user4 = controller.getUserById(user3.getId());
        user4.setName("testMod");
        controller.setUser(user4);
        controller.saveUser();


        controller.setUser(user3);
        controller.deleteUser();


        // Comprobamos que da error si intentamos borrar un User que ya ha sido borrado
        try {
            controller.setUser(user3);
            controller.deleteUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }


        // o que no existe en la bd
        User user5 = controller.newUser();
        user5.setName("cinco");
        user5.setPassword("aaaa");
        user5.setRole(role1);
        user5.setId(user3.getId());

        controller.setUser(user5);

        // Si la id no existe en la base de datos, da error
        try {
            controller.deleteUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }

        // Nos aseguramos que no existe
        assertNull(controller.getUserById(user5.getId()));



    }

    @Test
    public void testFindUser() {

        System.out.println("Probando la busqueda de User");

        // Creamos un User
        User user1 = controller.newUser();
        User user2 = controller.newUser();

        // Asignamos sus propiedades básicas y lo guardamos
        user1.setName("uno");
        user1.setPassword("uno");
        user1.setRole(role1);

        controller.setUser(user1);
        assertEquals(user1, controller.getUser());
        controller.saveUser();



        user2.setName("dos");
        user2.setPassword("bbbb");
        user2.setRole(role1);

        controller.setUser(user2);
        controller.saveUser();


        List<User> foundList = controller.listAll();
        // Nos aseguramos que encuentra a los dos roles y ninguno mas
        assertTrue(foundList.contains(user1));
        assertTrue(foundList.contains(user2));
        assertTrue(foundList.size() == 2);


        // Probamos si encuentra correctamente por cada propiedad
        foundList = controller.searchByName("uno");
        assertTrue(foundList.contains(user1));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByName("dos");
        assertTrue(foundList.contains(user2));
        assertTrue(foundList.size() == 1);
        foundList = controller.searchByRole("roleName");
        assertTrue(foundList.contains(user1));
        assertTrue(foundList.contains(user2));
        assertTrue(foundList.size() == 2);


        User foundUser = controller.getUserByName("uno");
        assertEquals(foundUser, user1);
        foundUser = controller.getUserByName("dos");
        assertEquals(foundUser, user2);
    }

    @Test
    public void testUserCollisions() {

        System.out.println("Probando las colisiones de User");

        // Creamos un User y lo guardamos
        User user1 = controller.newUser();
        User user2 = controller.newUser();


        user1.setName("uni");
        user1.setPassword("bbbb");
        user1.setRole(role1);


        controller.setUser(user1);
        controller.saveUser();


        user2.setName(user1.getName());
        controller.setUser(user2);
        // No puede haber dos User con el mismo nombre
        try {
            controller.saveUser();
            fail("Expected Runtime Exception");
        } catch (RuntimeException expected) {
        }
    }
    
    
    @Test
    public void testUserLoginAndPrivileges() {
        
        System.out.println("Probando el login");
        
        // Creamos un usuario
        User user1 = new User();
        user1.setName("log");
        user1.setPassword("pass");
        user1.setRole(role1);

        controller.setUser(user1);
        controller.saveUser();

        // Comprobamos que valida correctamente el login
        assertTrue(controller.isValidLogin("log", "pass"));
        assertFalse(controller.isValidLogin("log", "pasS"));
        assertFalse(controller.isValidLogin("l1g", "pass"));

        
        // Y que encuentra correctamente los privilegios
        assertTrue(controller.getUserPrivileges("log").contains(Privilege.ADMIN));
        assertFalse(controller.getUserPrivileges("log").contains(Privilege.USER));
        
        user1.setRole(role2);
        controller.saveUser();
        assertFalse(controller.getUserPrivileges("log").contains(Privilege.ADMIN));
        assertTrue(controller.getUserPrivileges("log").contains(Privilege.USER));
        assertTrue(controller.getUserPrivileges("log").contains(Privilege.USER_EDIT));
    }
}
