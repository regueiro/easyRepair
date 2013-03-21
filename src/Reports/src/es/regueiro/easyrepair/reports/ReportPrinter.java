package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.*;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.repair.*;
import es.regueiro.easyrepair.model.stock.*;
import java.util.*;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

public class ReportPrinter {
    
    private void print(JasperReportBuilder report) {
        if (report != null) {
            try {
                JasperViewer.viewReport(report.toJasperPrint(), false);
            } catch (DRException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void printClient(Client client) {
        print(ClientReport.generateClientReport(client));
    }
    
    public void printClientList(List<Client> list) {
        print(ClientReport.generateClientListReport(list));
    }
    
    public void printInsuranceCompany(InsuranceCompany insuranceCompany) {
        print(InsuranceCompanyReport.generateInsuranceCompanyReport(insuranceCompany));
    }
    
    public void printInsuranceCompanyList(List<InsuranceCompany> list) {
        print(InsuranceCompanyReport.generateInsuranceCompanyListReport(list));
    }
    
    public void printVehicle(Vehicle vehicle) {
        print(VehicleReport.generateVehicleReport(vehicle));
    }
    
    public void printVehicleList(List<Vehicle> list) {
        print(VehicleReport.generateVehicleListReport(list));
    }
    
    public void printEmployee(Employee employee) {
        print(EmployeeReport.generateEmployeeReport(employee));
    }
    
    public void printEmployeeList(List<Employee> list) {
        print(EmployeeReport.generateEmployeeListReport(list));
    }
    
    public void printWarehouse(Warehouse warehouse) {
        print(WarehouseReport.generateWarehouseReport(warehouse));
    }
    
    public void printWarehouseList(List<Warehouse> list) {
        print(WarehouseReport.generateWarehouseListReport(list));
    }
    
    public void printSupplier(Supplier supplier) {
        print(SupplierReport.generateSupplierReport(supplier));
    }
    
    public void printSupplierList(List<Supplier> list) {
        print(SupplierReport.generateSupplierListReport(list));
    }
    
    public void printPart(Part part) {
        print(PartReport.generatePartReport(part));
    }
    
    public void printPartList(List<Part> list) {
        print(PartReport.generatePartListReport(list));
    }
    
    public void printLabour(Labour labour) {
        print(LabourReport.generateLabourReport(labour));
    }
    
    public void printLabourList(List<Labour> list) {
        print(LabourReport.generateLabourListReport(list));
    }
    
    public void printEstimate(Estimate estimate) {
        print(EstimateReport.generateEstimateReport(estimate));
    }
    
    public void printEstimateList(List<Estimate> list) {
        print(EstimateReport.generateEstimateListReport(list));
    }
    
    public void printRepairInvoice(RepairInvoice invoice) {
        print(RepairInvoiceReport.generateRepairInvoiceReport(invoice));
    }
    
    public void printRepairInvoiceList(List<RepairInvoice> list) {
        print(RepairInvoiceReport.generateRepairInvoiceListReport(list));
    }
    
    public void printRepairOrder(RepairOrder order) {
        print(RepairOrderReport.generateRepairOrderReport(order));
    }
    
    public void printRepairOrderList(List<RepairOrder> list) {
        print(RepairOrderReport.generateRepairOrderListReport(list));
    }
    
    public void printPartOrderInvoice(PartOrderInvoice partOrderInvoice) {
        print(PartOrderInvoiceReport.generatePartOrderInvoiceReport(partOrderInvoice));
    }
    
    public void printPartOrderInvoiceList(List<PartOrderInvoice> list) {
        print(PartOrderInvoiceReport.generatePartOrderInvoiceListReport(list));
    }
    
    public void printPartOrder(PartOrder partOrder) {
        print(PartOrderReport.generatePartOrderReport(partOrder));
    }
    
    public void printPartOrderList(List<PartOrder> list) {
        print(PartOrderReport.generatePartOrderListReport(list));
    }
}
