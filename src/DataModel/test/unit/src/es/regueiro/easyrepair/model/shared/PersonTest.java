/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.model.shared;

import es.regueiro.easyrepair.model.employee.Employee;
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
public class PersonTest {
    
    public PersonTest() {
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
     * Test of setName method, of class Person.
     */
    @Test
    public void testPersonComponents() {
        System.out.println("Testing Person Components ");
        Person emp = new Employee();
        
        
        emp.setName("testname");
        emp.setSurname("testsurname");
        
        
        // Comprobamos que asigna correctamente
        assertEquals("testname", emp.getName());
        assertEquals("testsurname", emp.getSurname());

        
        // El nombre no puede ser nulo
        try {
            emp.setName("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            emp.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals("testname", emp.getName());

        // El apellido si
        emp.setSurname("");
        assertNull(emp.getSurname());
        emp.setSurname("    ");
        assertNull(emp.getSurname());
        emp.setSurname(null);
        assertNull(emp.getSurname());
        
        Person per = new Employee();

        // Sin nombre no valida
        try {
            per.validate();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        per.setName("per");
        per.validate();
    }

    
}
