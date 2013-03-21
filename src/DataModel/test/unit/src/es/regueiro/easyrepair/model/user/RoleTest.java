package es.regueiro.easyrepair.model.user;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class RoleTest {

    public RoleTest() {
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
    public void testRole() {
        System.out.println("Testing Roles");
        Role role = new Role();
        Role dos = new Role();

        role.setName("testname");
        role.setDescription("testnotes");
        role.setId(new Long("1"));
        role.setVersion(2);

        dos.setName("second");

        // Comprobamos que asigna correctamente
        assertEquals("testname", role.getName());
        assertEquals("testnotes", role.getDescription());
        assertEquals("1", role.getId().toString());
        assertEquals(2, role.getVersion());

        assertEquals("second", dos.getName());

        // Comprobamos el comportamiento con cadenas vacias y nulos

        role.setDescription("");
        assertNull(role.getDescription());
        role.setDescription("    ");
        assertNull(role.getDescription());
        role.setDescription(null);
        assertNull(role.getDescription());



        // El nombre no puede ser nulo
        try {
            role.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            role.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", role.getName());


        Role cuatro = new Role();

        // Sin nombre no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        cuatro.setName("cuatro");
        // Sin privilegios no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        // Sin privilegios no valida
        cuatro.setPrivileges(new ArrayList<Privilege>());
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        

        cuatro.addPrivilege(Privilege.LABOUR);
        cuatro.validate();
        
        

        // Comprobamos que asigna privilegios correctamente
        Role role4 = new Role();
        Role role5 = new Role();
     
        assertFalse(role4.hasPrivilege(Privilege.ADMIN));
         
         
        assertNull(role4.getPrivileges());
        role4.addPrivilege(Privilege.ADMIN);
        assertNotNull(role4.getPrivileges());
        assertTrue(role4.hasPrivilege(Privilege.ADMIN));
        assertTrue(role4.getPrivileges().size() == 1);
        
        assertFalse(role4.hasPrivilege(Privilege.EMPLOYEE));
        
        role4.addPrivilege(Privilege.ADMIN);
        assertTrue(role4.hasPrivilege(Privilege.ADMIN));
        assertTrue(role4.getPrivileges().size() == 1);
        
        role4.removePrivilege(Privilege.USER);
        assertTrue(role4.hasPrivilege(Privilege.ADMIN));
        assertTrue(role4.getPrivileges().size() == 1);
        
        role4.removePrivilege(null);
        assertTrue(role4.hasPrivilege(Privilege.ADMIN));
        assertTrue(role4.getPrivileges().size() == 1);
        
        role4.removePrivilege(Privilege.ADMIN);
        assertFalse(role4.hasPrivilege(Privilege.ADMIN));
        assertTrue(role4.getPrivileges().isEmpty());
        
        
//        assertFalse(role4.equals(role5));
//        assertFalse(role5.hashCode() == role4.hashCode());
        
        
        role5.addPrivilege(Privilege.USER);
        role5.removePrivilege(Privilege.USER);
        assertTrue(role4.equals(role5));
        
        assertTrue(role5.hashCode() == role4.hashCode());
        
        role4.setPrivileges(null);
        role5.setPrivileges(null);
        role4.clearPrivileges();
        role5.clearPrivileges();
        assertTrue(role4.getPrivileges().isEmpty());
        assertTrue(role5.getPrivileges().isEmpty());
        
        role4.addPrivilege(Privilege.REPAIR_ESTIMATE);
        role5.addPrivilege(Privilege.USER);
        role4.clearPrivileges();
        role5.clearPrivileges();
        assertTrue(role4.getPrivileges().isEmpty());
        assertTrue(role5.getPrivileges().isEmpty());

        // Comprobamos el funcionamiento de equals y hashcode
        Role role2 = new Role();
        Role role3 = new Role();

        assertFalse(role2.equals(null));
        assertFalse(role2.equals(5));
        assertTrue(role2.equals(role2));
        assertEquals(role2.hashCode(), role2.hashCode());

        role2.setName("test");
        assertFalse(role2.equals(role3));
        assertFalse(role3.equals(role2));

        role3.setName("aaa");
        assertFalse(role2.equals(role3));
        assertFalse(role3.equals(role2));

        role3.setName("test");
        assertTrue(role2.equals(role3));
        assertTrue(role3.equals(role2));
        assertEquals(role2.hashCode(), role3.hashCode());

     

        role2.setDescription("test");
        assertFalse(role2.equals(role3));
        assertFalse(role3.equals(role2));

        role3.setDescription("aaa");
        assertFalse(role2.equals(role3));
        assertFalse(role3.equals(role2));

        role3.setDescription("test");
        assertTrue(role2.equals(role3));
        assertTrue(role3.equals(role2));
        assertEquals(role2.hashCode(), role3.hashCode());

        
        
        
     
    }
}