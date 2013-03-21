/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.shared;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Santi
 */
public class CompanyTest {

    public CompanyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setName method, of class Company.
     */
    @Test
    public void testCompanyComponents() {
        System.out.println("Testing Person Components ");
        Company comp = new InsuranceCompany();


        comp.setName("testname");


        // Comprobamos que asigna correctamente
        assertEquals("testname", comp.getName());


        // El nombre no puede ser nulo
        try {
            comp.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            comp.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", comp.getName());


        Company dos = new InsuranceCompany();

        // Sin nombre no valida
        try {
            dos.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        dos.setName("dos");
        dos.validate();
    }
}