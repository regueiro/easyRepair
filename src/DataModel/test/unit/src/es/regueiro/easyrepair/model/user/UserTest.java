package es.regueiro.easyrepair.model.user;

import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class UserTest {

    public UserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUser() {
        System.out.println("Testing setting atributes of a user");
        User user = new User();
        User dos = new User();

        user.setName("testname");
        user.setPassword("pass");
        System.out.println(user.getPassword());
        user.setId(new Long("1"));
        user.setVersion(2);


        dos.setName("second");

        String passHash = BCrypt.hashpw("pass", BCrypt.gensalt());
        // Comprobamos que asigna correctamente
        assertEquals("testname", user.getName());
        assertTrue(BCrypt.checkpw("pass", user.getPassword()));
        assertEquals("1", user.getId().toString());
        assertEquals(2, user.getVersion());

        assertEquals("second", dos.getName());



        // El nombre no puede ser nulo
        try {
            user.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            user.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", user.getName());

        // El password no puede ser nulo
        try {
            user.setPassword("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            user.setPassword(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
//        assertTrue(BCrypt.checkpw("pass", user.getPassword()));

        User cuatro = new User();

        // Sin nombre no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        cuatro.setName("cuatro");

        // Sin password no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        cuatro.setPassword("cinco");
        cuatro.validate();


        // Comprobamos que asigna roles correctamente
        User user4 = new User();
        User user5 = new User();
        Role role = new Role();
        Role role1 = new Role();

        role.setName("22123");
        role1.setName("32132");


        assertNull(user4.getRole());
        assertNull(user5.getRole());

        user4.setRole(role);
        assertEquals(user4.getRole(), role);
        assertFalse(user4.getRole().equals(role1));


        // Comprobamos el funcionamiento de equals y hashcode
        User user2 = new User();
        User user3 = new User();

        assertFalse(user2.equals(null));
        assertFalse(user2.equals(5));
        assertTrue(user2.equals(user2));
        assertEquals(user2.hashCode(), user2.hashCode());

        user2.setName("test");
        assertFalse(user2.equals(user3));
        assertFalse(user3.equals(user2));

        user3.setName("aaa");
        assertFalse(user2.equals(user3));
        assertFalse(user3.equals(user2));

        user3.setName("test");
        assertTrue(user2.equals(user3));
        assertTrue(user3.equals(user2));
        assertEquals(user2.hashCode(), user3.hashCode());

   

        Role role3 = new Role();
        role3.setName("222");
        user2.setRole(role3);
        assertTrue(user2.equals(user3));
        assertTrue(user3.equals(user2));

        Role role4 = new Role();
        role4.setName("333");
        user3.setRole(role4);
        assertTrue(user2.equals(user3));
        assertTrue(user3.equals(user2));


        role4.setName("222");
        user3.setRole(role4);
        assertTrue(user2.equals(user3));
        assertTrue(user3.equals(user2));
        assertEquals(user2.hashCode(), user3.hashCode());


    }
}