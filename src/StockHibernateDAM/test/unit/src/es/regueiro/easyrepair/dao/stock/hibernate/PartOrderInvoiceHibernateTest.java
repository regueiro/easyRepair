///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package es.regueiro.easyrepair.dao.stock.hibernate;
//
//import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
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
//public class PartOrderInvoiceHibernateTest {
//    
//    public PartOrderInvoiceHibernateTest() {
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
//     * Test of listAll method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testListAll() {
//        System.out.println("listAll");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.listAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of listAllEnabled method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testListAllEnabled() {
//        System.out.println("listAllEnabled");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.listAllEnabled();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchById method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchById() {
//        System.out.println("searchById");
//        String id = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByAcceptedDate method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByAcceptedDate() {
//        System.out.println("searchByAcceptedDate");
//        String dateAfter = "";
//        String dateBefore = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByAcceptedDate(dateAfter, dateBefore);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByEstimatedPaymentDate method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByEstimatedPaymentDate() {
//        System.out.println("searchByEstimatedPaymentDate");
//        String dateAfter = "";
//        String dateBefore = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByEstimatedPaymentDate(dateAfter, dateBefore);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByPaymentDate method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByPaymentDate() {
//        System.out.println("searchByPaymentDate");
//        String dateAfter = "";
//        String dateBefore = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByPaymentDate(dateAfter, dateBefore);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByStatus method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByStatus() {
//        System.out.println("searchByStatus");
//        String status = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByStatus(status);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByResponsible method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByResponsible() {
//        System.out.println("searchByResponsible");
//        String name = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByResponsible(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByPartOrderNumber method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByPartOrderNumber() {
//        System.out.println("searchByPartOrderNumber");
//        String partOrder = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByPartOrderNumber(partOrder);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByInvoiceNumber method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByInvoiceNumber() {
//        System.out.println("searchByInvoiceNumber");
//        String invoiceNumber = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByInvoiceNumber(invoiceNumber);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchByPaymentMenthod method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSearchByPaymentMenthod() {
//        System.out.println("searchByPaymentMenthod");
//        String paymentMethod = "";
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        List expResult = null;
//        List result = instance.searchByPaymentMenthod(paymentMethod);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSetOrderInvoice() {
//        System.out.println("setOrderInvoice");
//        PartOrderInvoice orderInvoice = null;
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        instance.setOrderInvoice(orderInvoice);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testGetOrderInvoice() {
//        System.out.println("getOrderInvoice");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        PartOrderInvoice expResult = null;
//        PartOrderInvoice result = instance.getOrderInvoice();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of saveOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testSaveOrderInvoice() {
//        System.out.println("saveOrderInvoice");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        instance.saveOrderInvoice();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of reloadOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testReloadOrderInvoice() {
//        System.out.println("reloadOrderInvoice");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        PartOrderInvoice expResult = null;
//        PartOrderInvoice result = instance.reloadOrderInvoice();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of overwriteOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testOverwriteOrderInvoice() {
//        System.out.println("overwriteOrderInvoice");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        instance.overwriteOrderInvoice();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testNewOrderInvoice() {
//        System.out.println("newOrderInvoice");
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        PartOrderInvoice expResult = null;
//        PartOrderInvoice result = instance.newOrderInvoice();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteOrderInvoice method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testDeleteOrderInvoice() {
//        System.out.println("deleteOrderInvoice");
//        PartOrderInvoice orderInvoice = null;
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        instance.deleteOrderInvoice(orderInvoice);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getOrderInvoiceById method, of class PartOrderInvoiceHibernateController.
//     */
//    @Test
//    public void testGetOrderInvoiceById() {
//        System.out.println("getOrderInvoiceById");
//        Long id = null;
//        PartOrderInvoiceHibernateController instance = new PartOrderInvoiceHibernateController();
//        PartOrderInvoice expResult = null;
//        PartOrderInvoice result = instance.getOrderInvoiceById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//}
