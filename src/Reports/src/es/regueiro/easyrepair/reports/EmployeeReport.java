package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.employee.Employee;
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

public class EmployeeReport {

    private EmployeeReport() {
    }

    private static JRDataSource createEmployeeListDataSource(List<Employee> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Employee employee : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (employee.getName() != null) {
                    map.put("name", employee.getName());
                } else {
                    map.put("name", "");
                }
                if (employee.getSurname() != null) {
                    map.put("surname", employee.getSurname());
                } else {
                    map.put("surname", "");
                }
                if (employee.getEmployeeId() != null) {
                    map.put("employeeID", employee.getEmployeeId());
                } else {
                    map.put("employeeID", "");
                }
                if (employee.getNif() != null) {
                    map.put("nif", employee.getNif().getNumber());
                } else {
                    map.put("nif", "");
                }
                if (employee.getNss() != null) {
                    map.put("nss", employee.getNss().getNumber());
                } else {
                    map.put("nss", "");
                }
                if (employee.getOccupation() != null) {
                    map.put("occupation", employee.getOccupation());
                } else {
                    map.put("occupation", "");
                }
                if (employee.getNotes() != null) {
                    map.put("notes", employee.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateEmployeeReport(Employee employee) {

        JasperReportBuilder report = report();
        if (employee != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEENO"), employee.getEmployeeId()),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createEmployeeComponent(employee),
                    createAddressComponent(employee),
                    createEmailComponent(employee),
                    createPhoneComponent(employee)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateEmployeeListReport(List<Employee> list) {
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
            TextColumnBuilder<String> nameColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "name", type.stringType());
            TextColumnBuilder<String> surnameColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SURNAME"), "surname", type.stringType());
            TextColumnBuilder<String> occupationColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OCCUPATION"), "occupation", type.stringType());
            TextColumnBuilder<String> employeeIDColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEEID"), "employeeID", type.stringType());
            TextColumnBuilder<String> nifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "nif", type.stringType());
            TextColumnBuilder<String> nssColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NSS"), "nss", type.stringType());


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEELIST"), ""))
                    .columns(
                    rowNumberColumn, employeeIDColumn, nameColumn, surnameColumn, nifColumn, nssColumn, occupationColumn)
                    .columnGrid(
                    rowNumberColumn, employeeIDColumn, nameColumn, surnameColumn, nifColumn, nssColumn, occupationColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createEmployeeListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createEmployeeComponent(Employee employee) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (employee != null) {
            if (employee.getEmployeeId() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEEID"), employee.getEmployeeId());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEEID"), "");
            }
            if (employee.getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), employee.getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "");
            }
            if (employee.getSurname() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SURNAME"), employee.getSurname());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SURNAME"), "");
            }
            if (employee.getNif() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), employee.getNif().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "");
            }
            if (employee.getNss() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NSS"), employee.getNss().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NSS"), "");
            }
            if (employee.getOccupation() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OCCUPATION"), employee.getOccupation());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OCCUPATION"), "");
            }
            if (employee.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), employee.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMPLOYEE")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createAddressComponent(Employee employee) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Address a : employee.getAddress()) {
            HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
            if (a.getLabel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), a.getLabel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), "");
            }
            if (a.getStreet() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STREET"), a.getStreet());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STREET"), "");
            }
            if (a.getCity() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CITY"), a.getCity());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CITY"), "");
            }
            if (a.getProvince() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PROVINCE"), a.getProvince());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PROVINCE"), "");
            }
            if (a.getPostalCode() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("POSTALCODE"), a.getPostalCode());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("POSTALCODE"), "");
            }
            if (a.getCountry() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COUNTRY"), a.getCountry());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COUNTRY"), "");
            }
            if (a.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), a.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
            mainList.add(list, cmp.verticalGap(5));
        }
        if (employee.getAddress() != null && !employee.getAddress().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ADDRESS")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }

    }

    private static ComponentBuilder<?, ?> createEmailComponent(Employee employee) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Email e : employee.getEmail()) {
            HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
            if (e.getLabel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), e.getLabel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), "");
            }
            if (e.getAddress() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAILADDRESS"), e.getAddress());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAILADDRESS"), "");
            }

            if (e.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), e.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
            mainList.add(list, cmp.verticalGap(5));
        }
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAIL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }

    private static ComponentBuilder<?, ?> createPhoneComponent(Employee employee) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Phone p : employee.getPhone()) {
            HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
            if (p.getLabel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), p.getLabel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABEL"), "");
            }
            if (p.getNumber() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NUMBER"), p.getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NUMBER"), "");
            }
            if (p.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), p.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
            mainList.add(list, cmp.verticalGap(5));
        }
        if (employee.getPhone() != null && !employee.getPhone().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PHONE")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }



    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(15).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
}
