///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package es.regueiro.easyrepair.dao.repair.hibernate;
//
//import es.regueiro.easyrepair.model.repair.Estimate;
//import java.util.List;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author Santi
// */
//public class EstimateHibernateTest {
//    
//    public EstimateHibernateTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of listAll method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testListAll() {
//        System.out.println("listAll");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.listAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of listAllEnabled method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testListAllEnabled() {
//        System.out.println("listAllEnabled");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.listAllEnabled();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByEstimateNumber method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByEstimateNumber() {
//        System.out.println("searchByEstimateNumber");
//        String estimateNumber = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByEstimateNumber(estimateNumber);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByEstimateDate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByEstimateDate() {
//        System.out.println("searchByEstimateDate");
//        String dateAfter = "";
//        String dateBefore = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByEstimateDate(dateAfter, dateBefore);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByAcceptedDate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByAcceptedDate() {
//        System.out.println("searchByAcceptedDate");
//        String dateAfter = "";
//        String dateBefore = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByAcceptedDate(dateAfter, dateBefore);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByStatus method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByStatus() {
//        System.out.println("searchByStatus");
//        String status = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByStatus(status);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByDiscount method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByDiscount() {
//        System.out.println("searchByDiscount");
//        String discount = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByDiscount(discount);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByMinDiscount method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByMinDiscount() {
//        System.out.println("searchByMinDiscount");
//        String discount = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByMinDiscount(discount);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByMaxDiscount method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByMaxDiscount() {
//        System.out.println("searchByMaxDiscount");
//        String discount = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByMaxDiscount(discount);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByResponsible method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSearchByResponsible() {
//        System.out.println("searchByResponsible");
//        String name = "";
//        EstimateHibernateController instance = new EstimateHibernateController();
//        List expResult = null;
//        List result = instance.searchByResponsible(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSetEstimate() {
//        System.out.println("setEstimate");
//        Estimate estimate = null;
//        EstimateHibernateController instance = new EstimateHibernateController();
//        instance.setEstimate(estimate);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testGetEstimate() {
//        System.out.println("getEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        Estimate expResult = null;
//        Estimate result = instance.getEstimate();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of saveEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testSaveEstimate() {
//        System.out.println("saveEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        instance.saveEstimate();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of reloadEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testReloadEstimate() {
//        System.out.println("reloadEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        Estimate expResult = null;
//        Estimate result = instance.reloadEstimate();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of overwriteEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testOverwriteEstimate() {
//        System.out.println("overwriteEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        instance.overwriteEstimate();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testNewEstimate() {
//        System.out.println("newEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        Estimate expResult = null;
//        Estimate result = instance.newEstimate();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteEstimate method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testDeleteEstimate() {
//        System.out.println("deleteEstimate");
//        EstimateHibernateController instance = new EstimateHibernateController();
//        instance.deleteEstimate();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEstimateById method, of class EstimateHibernateController.
//     */
//    @Test
//    public void testGetEstimateById() {
//        System.out.println("getEstimateById");
//        Long id = null;
//        EstimateHibernateController instance = new EstimateHibernateController();
//        Estimate expResult = null;
//        Estimate result = instance.getEstimateById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//}
