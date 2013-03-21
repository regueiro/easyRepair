package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class VehicleReport {

    private VehicleReport() {
    }

    private static JRDataSource createVehicleListDataSource(List<Vehicle> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Vehicle vehicle : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (vehicle.getRegistration() != null) {
                    map.put("registration", vehicle.getRegistration());
                } else {
                    map.put("registration", "");
                }
                if (vehicle.getMake() != null) {
                    map.put("make", vehicle.getMake());
                } else {
                    map.put("make", "");
                }
                if (vehicle.getModel() != null) {
                    map.put("model", vehicle.getModel());
                } else {
                    map.put("model", "");
                }
                if (vehicle.getYear() != null) {
                    map.put("year", vehicle.getYear());
                } else {
                    map.put("year", "");
                }
                if (vehicle.getVin() != null) {
                    map.put("vin", vehicle.getVin());
                } else {
                    map.put("vin", "");
                }
                if (vehicle.getColour() != null) {
                    map.put("colour", vehicle.getColour());
                } else {
                    map.put("colour", "");
                }
                if (vehicle.getType() != null) {
                    map.put("type", vehicle.getType());
                } else {
                    map.put("type", "");
                }
                if (vehicle.getFuel() != null) {
                    map.put("fuel", vehicle.getFuel());
                } else {
                    map.put("fuel", "");
                }
                if (vehicle.getInsuranceCompany() != null && vehicle.getInsuranceCompany().getName() != null) {
                    map.put("insuranceCompany", vehicle.getInsuranceCompany().getName());
                } else {
                    map.put("insuranceCompany", "");
                }
                if (vehicle.getInsuranceNumber() != null) {
                    map.put("insuranceNumber", vehicle.getInsuranceNumber());
                } else {
                    map.put("insuranceNumber", "");
                }
                if (vehicle.getNotes() != null) {
                    map.put("notes", vehicle.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateVehicleReport(Vehicle vehicle) {

        JasperReportBuilder report = report();
        if (vehicle != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLERECORD"), ""),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createVehicleComponent(vehicle)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateVehicleListReport(List<Vehicle> list) {
        JasperReportBuilder report = report();
        if (list != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  

            TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn()
                    .setFixedColumns(2)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            TextColumnBuilder<String> registrationColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), "registration", type.stringType());
            TextColumnBuilder<String> makeColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "make", type.stringType());
            TextColumnBuilder<String> modelColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "model", type.stringType());
            TextColumnBuilder<String> yearColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("YEAR"), "year", type.stringType());
            TextColumnBuilder<String> vinColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VIN"), "vin", type.stringType());
            TextColumnBuilder<String> colourColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COLOUR"), "colour", type.stringType());
            TextColumnBuilder<String> typeColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TYPE"), "type", type.stringType());
            TextColumnBuilder<String> fuelColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FUEL"), "fuel", type.stringType());
            TextColumnBuilder<String> insuranceCompanyColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY"), "insuranceCompany", type.stringType());
            TextColumnBuilder<String> insuranceNumberColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCENUMBER"), "insuranceNumber", type.stringType());


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLELIST"), ""))
                    .columns(
                    rowNumberColumn, registrationColumn, makeColumn, modelColumn, yearColumn, vinColumn, colourColumn, typeColumn, fuelColumn, insuranceCompanyColumn, insuranceNumberColumn)
                    .columnGrid(
                    rowNumberColumn, 
                    grid.horizontalColumnGridList()
                    .add(registrationColumn).newRow()
                    .add(vinColumn),
                    grid.horizontalColumnGridList()
                    .add(makeColumn).newRow()
                    .add(modelColumn),
                    grid.horizontalColumnGridList()
                    .add(yearColumn).newRow()
                    .add(colourColumn),
                    grid.horizontalColumnGridList()
                    .add(typeColumn).newRow()
                    .add(fuelColumn),
                    grid.horizontalColumnGridList()
                    .add(insuranceCompanyColumn).newRow()
                    .add(insuranceNumberColumn))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createVehicleListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createVehicleComponent(Vehicle vehicle) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (vehicle != null) {
            if (vehicle.getRegistration() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), vehicle.getRegistration());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), "");
            }
            if (vehicle.getMake() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), vehicle.getMake());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "");
            }
            if (vehicle.getModel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), vehicle.getModel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "");
            }
            if (vehicle.getYear() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("YEAR"), vehicle.getYear());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("YEAR"), "");
            }
            if (vehicle.getVin() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VIN"), vehicle.getVin());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VIN"), "");
            }
            if (vehicle.getColour() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COLOUR"), vehicle.getColour());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COLOUR"), "");
            }
            if (vehicle.getType() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TYPE"), vehicle.getType());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TYPE"), "");
            }

            if (vehicle.getFuel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FUEL"), vehicle.getFuel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FUEL"), "");
            }
            if (vehicle.getInsuranceCompany() != null && vehicle.getInsuranceCompany().getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY"), vehicle.getInsuranceCompany().getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY"), "");
            }
            if (vehicle.getInsuranceNumber() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCENUMBER"), vehicle.getInsuranceNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCENUMBER"), "");
            }
            if (vehicle.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), vehicle.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLE")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }


    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(15).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
}
