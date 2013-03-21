package es.regueiro.easyrepair.model.client;

import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Santi
 */
public class VehicleTest {

    public VehicleTest() {
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
    public void testVehicle() {
        System.out.println("Testing setting atributes of a vehicle");
        Vehicle vehicle = new Vehicle();
        Vehicle dos = new Vehicle();


        vehicle.setColour("red");
        vehicle.setFuel("gas");
        InsuranceCompany ins = new InsuranceCompany();
        vehicle.setInsuranceCompany(ins);
        vehicle.setInsuranceNumber("24213123");
        vehicle.setMake("ford");
        vehicle.setModel("90");
        vehicle.setNotes("test");
        Client cli = new Client();
        vehicle.setOwner(cli);
        vehicle.setRegistration("9986ass");
        vehicle.setType("coupe");
        vehicle.setVin("vf33crfnc12345678");
        vehicle.setYear("1986");
        vehicle.setId(new Long("1"));
//        vehicle.setVersion(2);
        vehicle.setEnabled(true);

        dos.setModel("second");

        // Comprobamos que asigna correctamente
        assertEquals("red", vehicle.getColour());
        assertEquals("gas", vehicle.getFuel());
        assertEquals("24213123", vehicle.getInsuranceNumber());
        assertEquals("ford", vehicle.getMake());
        assertEquals("90", vehicle.getModel());
        assertEquals("test", vehicle.getNotes());
        assertEquals("9986ass", vehicle.getRegistration());
        assertEquals("coupe", vehicle.getType());
        assertEquals("test", vehicle.getNotes());
        assertEquals("vf33crfnc12345678", vehicle.getVin());
        assertEquals("1986", vehicle.getYear());
        assertEquals(ins, vehicle.getInsuranceCompany());
        assertEquals(cli, vehicle.getOwner());
        assertEquals("1", vehicle.getId().toString());
//        assertEquals(2, vehicle.getVersion());
        assertTrue(vehicle.getEnabled());

        assertEquals("second", dos.getModel());

        // Comprobamos el comportamiento con cadenas vacias y nulos

        vehicle.setOwner(null);
        assertNull(vehicle.getOwner());
        
        
        vehicle.setOwner(cli);
        
        vehicle.setNotes("");
        assertNull(vehicle.getNotes());
        vehicle.setNotes("    ");
        assertNull(vehicle.getNotes());
        vehicle.setNotes(null);
        assertNull(vehicle.getNotes());
        
        vehicle.setColour("");
        assertNull(vehicle.getColour());
        vehicle.setColour("    ");
        assertNull(vehicle.getColour());
        vehicle.setColour(null);
        assertNull(vehicle.getColour());
        
        vehicle.setFuel("");
        assertNull(vehicle.getFuel());
        vehicle.setFuel("    ");
        assertNull(vehicle.getFuel());
        vehicle.setFuel(null);
        assertNull(vehicle.getFuel());
        
        vehicle.setInsuranceNumber("");
        assertNull(vehicle.getInsuranceNumber());
        vehicle.setInsuranceNumber("    ");
        assertNull(vehicle.getInsuranceNumber());
        vehicle.setInsuranceNumber(null);
        assertNull(vehicle.getInsuranceNumber());
        
        vehicle.setMake("");
        assertNull(vehicle.getMake());
        vehicle.setMake("    ");
        assertNull(vehicle.getMake());
        vehicle.setMake(null);
        assertNull(vehicle.getMake());
        
        vehicle.setModel("");
        assertNull(vehicle.getModel());
        vehicle.setModel("    ");
        assertNull(vehicle.getModel());
        vehicle.setModel(null);
        assertNull(vehicle.getModel());
        
        vehicle.setRegistration("");
        assertNull(vehicle.getRegistration());
        vehicle.setRegistration("    ");
        assertNull(vehicle.getRegistration());
        vehicle.setRegistration(null);
        assertNull(vehicle.getRegistration());
        vehicle.setRegistration("assssas");
        
        vehicle.setType("");
        assertNull(vehicle.getType());
        vehicle.setType("    ");
        assertNull(vehicle.getType());
        vehicle.setType(null);
        assertNull(vehicle.getType());

        try {
            vehicle.setYear("89");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        
        vehicle.setYear("1893");
        assertEquals("1893",vehicle.getYear());
        vehicle.setYear("2013");
        assertEquals("2013",vehicle.getYear());
        vehicle.setYear("1989");
        assertEquals("1989",vehicle.getYear());
        vehicle.setYear(null);
        assertNull(vehicle.getYear());
        
        
        vehicle.validate();
        vehicle.setRegistration(null);
        try {
            vehicle.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        vehicle.setRegistration("wsadas");
        vehicle.setOwner(null);
        try {
            vehicle.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        

        // Comprobamos el funcionamiento de equals y hashcode
        Vehicle veh2 = new Vehicle();
        Vehicle veh3 = new Vehicle();
        
        assertFalse(veh2.equals(null));
        assertFalse(veh2.equals(5));
        assertTrue(veh2.equals(veh2));
        assertEquals(veh2.hashCode(), veh2.hashCode());
        
        veh2.setColour("red");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setColour("red");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setFuel("gas");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setFuel("gas");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        
        InsuranceCompany ins2 = new InsuranceCompany();
        veh2.setInsuranceCompany(ins);
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setInsuranceCompany(ins);
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        
        veh2.setInsuranceNumber("24213123");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setInsuranceNumber("24213123");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setMake("ford");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setMake("ford");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setModel("90");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setModel("90");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setNotes("test");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setNotes("test");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
//        Client cli2 = new Client();
//        veh2.setOwner(cli);
//        assertFalse(veh2.equals(veh3));
//        assertFalse(veh3.equals(veh2));
//        
//        veh3.setOwner(cli);
//        assertTrue(veh2.equals(veh3));
//        assertTrue(veh3.equals(veh2));
//        assertEquals(veh2.hashCode(), veh3.hashCode());
                
        veh2.setRegistration("9986ass");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setRegistration("9986ass");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setType("coupe");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setType("coupe");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setVin("vf33crfnc12345678");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setVin("vf33crfnc12345678");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
        veh2.setYear("1986");
        assertFalse(veh2.equals(veh3));
        assertFalse(veh3.equals(veh2));
        
        veh3.setYear("1986");
        assertTrue(veh2.equals(veh3));
        assertTrue(veh3.equals(veh2));
        assertEquals(veh2.hashCode(), veh3.hashCode());
        
    }
}