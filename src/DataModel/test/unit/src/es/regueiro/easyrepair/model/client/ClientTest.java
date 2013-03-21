package es.regueiro.easyrepair.model.client;

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
public class ClientTest {

    public ClientTest() {
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
    public void testClient() {
        System.out.println("Testing setting atributes of a client");
        Client client = new Client();
        Client dos = new Client();

        client.setName("testname");
        client.setSurname("testsurname");
        client.setNotes("testnotes");
        client.setNif(new NIF("66877899V"));
        client.setId(new Long("1"));
        client.setClientId("1");
        client.setVersion(2);
        client.setEnabled(true);

        dos.setName("second");

        // Comprobamos que asigna correctamente
        assertEquals("testname", client.getName());
        assertEquals("testsurname", client.getSurname());
        assertEquals("testnotes", client.getNotes());
        assertEquals("66877899V", client.getNif().getNumber());
        assertEquals("1", client.getId().toString());
        assertEquals("00000000001", client.getClientId());
        assertEquals(2, client.getVersion());
        assertTrue(client.getEnabled());

        assertEquals("second", dos.getName());

        // Comprobamos el comportamiento con cadenas vacias y nulos
        client.setClientId("");
        assertNull(client.getClientId());
        client.setClientId("    ");
        assertNull(client.getClientId());
        client.setClientId(null);
        assertNull(client.getClientId());

        client.setNotes("");
        assertNull(client.getNotes());
        client.setNotes("    ");
        assertNull(client.getNotes());
        client.setNotes(null);
        assertNull(client.getNotes());



        // El nombre no puede ser nulo
        try {
            client.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            client.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", client.getName());

        // El apellido se guarda como nulo si está vacío
        client.setSurname("");
        assertNull(client.getSurname());
        client.setSurname("    ");
        assertNull(client.getSurname());
        client.setSurname(null);
        assertNull(client.getSurname());


        Client cuatro = new Client();

        // Sin nombre no valida
        try {
            cuatro.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        cuatro.setName("cuatro");
        cuatro.validate();

        // Comprobamos que no asigna ids incorrectas
        try {
            cuatro.setClientId("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        try {
            cuatro.setClientId("0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        
        try {
            cuatro.setClientId("00000000000");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            cuatro.setClientId("123456789101");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            cuatro.setClientId("12345678s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }


        // Comprobamos que asigna vehiculos correctamente
        Client client4 = new Client();
        Client client5 = new Client();
        Vehicle veh = new Vehicle();
        Vehicle veh1 = new Vehicle();

        veh.setRegistration("22123");
        veh1.setRegistration("32132");

        assertTrue(client4.getVehicles().isEmpty());
        client4.removeVehicle(null);
        assertTrue(client4.getVehicles().isEmpty());
        assertFalse(client4.hasVehicle("aaa"));

        assertTrue(client5.getVehicles().isEmpty());
        client5.clearVehicles();
        assertTrue(client5.getVehicles().isEmpty());

        client4.addVehicle(veh);
        assertTrue(client4.getVehicles().contains(veh));
        assertTrue(client4.hasVehicle(veh.getRegistration()));
        assertFalse(client4.getVehicles().contains(veh1));
        assertFalse(client4.hasVehicle(veh1.getRegistration()));

        client4.addVehicle(veh);
        assertTrue(client4.getVehicles().contains(veh));
        assertFalse(client4.getVehicles().contains(veh1));


        client4.addVehicle(veh1);
        assertTrue(client4.getVehicles().contains(veh));
        assertTrue(client4.getVehicles().contains(veh1));

        client4.clearVehicles();
        assertFalse(client4.getVehicles().contains(veh));
        assertFalse(client4.getVehicles().contains(veh1));
        assertTrue(client4.getVehicles().isEmpty());

        client4.addVehicle(veh);
        client4.addVehicle(veh1);
        client4.removeVehicle(veh.getRegistration());
        assertFalse(client4.getVehicles().contains(veh));
        assertTrue(client4.getVehicles().contains(veh1));

        client4.removeVehicle(veh1.getRegistration());
        assertFalse(client4.getVehicles().contains(veh));
        assertFalse(client4.getVehicles().contains(veh1));
        assertTrue(client4.getVehicles().isEmpty());

        client4.removeVehicle("false");
        assertFalse(client4.getVehicles().contains(veh));
        assertFalse(client4.getVehicles().contains(veh1));
        assertTrue(client4.getVehicles().isEmpty());
        assertFalse(client4.hasVehicle("aaa"));

        List<Vehicle> listVehicles = new ArrayList<Vehicle>();
        client4.setVehicles(listVehicles);

        assertTrue(client4.getVehicles().isEmpty());

        listVehicles.add(veh);

        assertFalse(client4.getVehicles().isEmpty());
        assertTrue(client4.getVehicles().contains(veh));


        // Comprobamos el funcionamiento de equals y hashcode
        Client client2 = new Client();
        Client client3 = new Client();

        assertFalse(client2.equals(null));
        assertFalse(client2.equals(5));
        assertTrue(client2.equals(client2));
        assertEquals(client2.hashCode(), client2.hashCode());

        client2.setName("test");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setName("aaa");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setName("test");
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client2.setSurname("test");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setSurname("aaa");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setSurname("test");
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client2.setNotes("test");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setNotes("aaa");
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setNotes("test");
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client2.addEmail(new Email("test@tes.com", "lab"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.addEmail(new Email("test@1.com", "lab"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.removeEmail("lab");

        client3.addEmail(new Email("test@tes.com", "lab"));
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client3.setEmail(new ArrayList<Email>());
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client2.setEmail(new ArrayList<Email>());

        client2.addPhone(new Phone("689558899", "lab"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.addPhone(new Phone("689996655", "lab"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.removePhone("lab");

        client3.addPhone(new Phone("689558899", "lab"));
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client3.setPhone(new ArrayList<Phone>());
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client2.setPhone(new ArrayList<Phone>());


        Address add4 = new Address();
        add4.setCity("test");
        add4.setLabel("label");
        client2.addAddress(add4);
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        Address add5 = new Address();
        add5.setCity("test");
        add5.setLabel("lab");
        client3.addAddress(add5);
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.removeAddress("lab");
        add5.setLabel("label");
        client3.addAddress(add5);
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client3.setAddress(new ArrayList<Address>());
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client2.setAddress(new ArrayList<Address>());


        Vehicle veh3 = new Vehicle();
        veh3.setRegistration("222");
        client2.addVehicle(veh3);
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        Vehicle veh4 = new Vehicle();
        veh4.setRegistration("333");
        client3.addVehicle(veh4);
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.removeVehicle("333");
        veh4.setRegistration("222");
        client3.addVehicle(veh4);
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());

        client3.setVehicles(new ArrayList<Vehicle>());
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client2.setVehicles(new ArrayList<Vehicle>());
        
        
        client2.setNif(new NIF("66877899V"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setNif(new NIF("21564146J"));
        assertFalse(client2.equals(client3));
        assertFalse(client3.equals(client2));

        client3.setNif(new NIF("66877899V"));
        assertTrue(client2.equals(client3));
        assertTrue(client3.equals(client2));
        assertEquals(client2.hashCode(), client3.hashCode());


        
        
    }
}