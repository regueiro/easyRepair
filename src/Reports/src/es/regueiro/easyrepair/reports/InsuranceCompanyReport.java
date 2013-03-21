package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
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

public class InsuranceCompanyReport {

    private InsuranceCompanyReport() {
    }

    private static JRDataSource createInsuranceCompanyListDataSource(List<InsuranceCompany> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (InsuranceCompany insuranceCompany : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (insuranceCompany.getName() != null) {
                    map.put("name", insuranceCompany.getName());
                } else {
                    map.put("name", "");
                }
                if (insuranceCompany.getWeb() != null) {
                    map.put("web", insuranceCompany.getWeb());
                } else {
                    map.put("web", "");
                }
                if (insuranceCompany.getNif() != null) {
                    map.put("nif", insuranceCompany.getNif().getNumber());
                } else {
                    map.put("nif", "");
                }
                if (insuranceCompany.getNotes() != null) {
                    map.put("notes", insuranceCompany.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateInsuranceCompanyReport(InsuranceCompany insuranceCompany) {

        JasperReportBuilder report = report();
        if (insuranceCompany != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANYRECORD"), ""),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createInsuranceCompanyComponent(insuranceCompany),
                    createAddressComponent(insuranceCompany),
                    createEmailComponent(insuranceCompany),
                    createPhoneComponent(insuranceCompany)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateInsuranceCompanyListReport(List<InsuranceCompany> list) {
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
            TextColumnBuilder<String> nifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "nif", type.stringType());
            TextColumnBuilder<String> webColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), "web", type.stringType());


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANYLIST"), ""))
                    .columns(
                    rowNumberColumn,  nameColumn,  nifColumn, webColumn)
                    .columnGrid(
                    rowNumberColumn,  nameColumn,  nifColumn, webColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createInsuranceCompanyListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createInsuranceCompanyComponent(InsuranceCompany insuranceCompany) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (insuranceCompany != null) {
            if (insuranceCompany.getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), insuranceCompany.getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "");
            }
            if (insuranceCompany.getNif() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), insuranceCompany.getNif().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "");
            }
            if (insuranceCompany.getWeb() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), insuranceCompany.getWeb());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), "");
            }
            if (insuranceCompany.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), insuranceCompany.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INSURANCECOMPANY")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createAddressComponent(InsuranceCompany insuranceCompany) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Address a : insuranceCompany.getAddress()) {
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
        if (insuranceCompany.getAddress() != null && !insuranceCompany.getAddress().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ADDRESS")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }

    }

    private static ComponentBuilder<?, ?> createEmailComponent(InsuranceCompany insuranceCompany) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Email e : insuranceCompany.getEmail()) {
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
        if (insuranceCompany.getEmail() != null && !insuranceCompany.getEmail().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAIL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }

    private static ComponentBuilder<?, ?> createPhoneComponent(InsuranceCompany insuranceCompany) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Phone p : insuranceCompany.getPhone()) {
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
        if (insuranceCompany.getPhone() != null && !insuranceCompany.getPhone().isEmpty()) {
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
