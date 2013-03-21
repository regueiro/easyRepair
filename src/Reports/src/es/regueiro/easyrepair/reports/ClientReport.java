package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Client;
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

public class ClientReport {

    private ClientReport() {
    }

    private static JRDataSource createClientListDataSource(List<Client> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Client client : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (client.getName() != null) {
                    map.put("name", client.getName());
                } else {
                    map.put("name", "");
                }
                if (client.getSurname() != null) {
                    map.put("surname", client.getSurname());
                } else {
                    map.put("surname", "");
                }
                if (client.getClientId() != null) {
                    map.put("clientID", client.getClientId());
                } else {
                    map.put("clientID", "");
                }
                if (client.getNif() != null) {
                    map.put("nif", client.getNif().getNumber());
                } else {
                    map.put("nif", "");
                }
                if (client.getNotes() != null) {
                    map.put("notes", client.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateClientReport(Client client) {

        JasperReportBuilder report = report();
        if (client != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTNO"), client.getClientId()),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createClientComponent(client),
                    createAddressComponent(client),
                    createEmailComponent(client),
                    createPhoneComponent(client),
                    createVehicleComponent(client)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateClientListReport(List<Client> list) {
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
            TextColumnBuilder<String> clientIDColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTID"), "clientID", type.stringType());
            TextColumnBuilder<String> nifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "nif", type.stringType());


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTLIST"), ""))
                    .columns(
                    rowNumberColumn, clientIDColumn, nameColumn, surnameColumn, nifColumn)
                    .columnGrid(
                    rowNumberColumn, clientIDColumn, nameColumn, surnameColumn, nifColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createClientListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createClientComponent(Client client) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (client != null) {
            if (client.getClientId() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTID"), client.getClientId());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTID"), "");
            }
            if (client.getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), client.getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "");
            }
            if (client.getSurname() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SURNAME"), client.getSurname());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SURNAME"), "");
            }
            if (client.getNif() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), client.getNif().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "");
            }
            if (client.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), client.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENT")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createAddressComponent(Client client) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Address a : client.getAddress()) {
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
        if (client.getAddress() != null && !client.getAddress().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ADDRESS")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }

    }

    private static ComponentBuilder<?, ?> createEmailComponent(Client client) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Email e : client.getEmail()) {
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
        if (client.getEmail() != null && !client.getEmail().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAIL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }

    private static ComponentBuilder<?, ?> createPhoneComponent(Client client) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Phone p : client.getPhone()) {
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
        if (client.getPhone() != null && !client.getPhone().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PHONE")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }

    private static ComponentBuilder<?, ?> createVehicleComponent(Client client) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Vehicle v : client.getVehicles()) {
            HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
            if (v.getRegistration() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), v.getRegistration());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), "");
            }
            if (v.getMake() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), v.getMake());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "");
            }
            if (v.getModel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), v.getModel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "");
            }
            if (v.getYear() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("YEAR"), v.getYear());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("YEAR"), "");
            }
            if (v.getVin() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VIN"), v.getVin());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VIN"), "");
            }
            if (v.getColour() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COLOUR"), v.getColour());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("COLOUR"), "");
            }
            if (v.getType() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TYPE"), v.getType());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TYPE"), "");
            }

            if (v.getFuel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FUEL"), v.getFuel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FUEL"), "");
            }
            if (v.getInsuranceCompany() != null && v.getInsuranceCompany().getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY"), v.getInsuranceCompany().getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY"), "");
            }
            if (v.getInsuranceNumber() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCENUMBER"), v.getInsuranceNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCENUMBER"), "");
            }
            if (v.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), v.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
            mainList.add(list, cmp.verticalGap(5));
        }
        if (client.getVehicles() != null && !client.getVehicles().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLE")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
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
