package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Supplier;
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
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class SupplierReport {

    private SupplierReport() {
    }

    private static JRDataSource createSupplierListDataSource(List<Supplier> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Supplier supplier : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (supplier.getName() != null) {
                    map.put("name", supplier.getName());
                } else {
                    map.put("name", "");
                }
                if (supplier.getCategory() != null) {
                    map.put("category", supplier.getCategory());
                } else {
                    map.put("category", "");
                }
                if (supplier.getNif() != null) {
                    map.put("nif", supplier.getNif().getNumber());
                } else {
                    map.put("nif", "");
                }
                if (supplier.getWeb() != null) {
                    map.put("web", supplier.getWeb());
                } else {
                    map.put("web", "");
                }
                if (supplier.getShippingMethod() != null) {
                    map.put("shippingMethod", supplier.getShippingMethod());
                } else {
                    map.put("shippingMethod", "");
                }
                if (supplier.getPaymentMethod() != null) {
                    map.put("paymentMethod", supplier.getPaymentMethod());
                } else {
                    map.put("paymentMethod", "");
                }
                if (supplier.getNotes() != null) {
                    map.put("notes", supplier.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateSupplierReport(Supplier supplier) {

        JasperReportBuilder report = report();
        if (supplier != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIERRECORD"), ""),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createSupplierComponent(supplier),
                    createAddressComponent(supplier),
                    createEmailComponent(supplier),
                    createPhoneComponent(supplier)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateSupplierListReport(List<Supplier> list) {
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
            TextColumnBuilder<String> categoryColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), "category", type.stringType());
            TextColumnBuilder<String> webColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), "web", type.stringType());
            TextColumnBuilder<String> shippingMethodColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGMETHOD"), "shippingMethod", type.stringType());
            TextColumnBuilder<String> nifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "nif", type.stringType());
            TextColumnBuilder<String> paymentMethodColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), "paymentMethod", type.stringType());


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIERLIST"), ""))
                    .columns(
                    rowNumberColumn, nameColumn, nifColumn, categoryColumn, webColumn, shippingMethodColumn, paymentMethodColumn)
                    .columnGrid(
                    rowNumberColumn,  nameColumn, nifColumn, categoryColumn, webColumn, shippingMethodColumn, paymentMethodColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createSupplierListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createSupplierComponent(Supplier supplier) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (supplier != null) {
            if (supplier.getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), supplier.getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "");
            }
            if (supplier.getNif() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), supplier.getNif().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "");
            }
            if (supplier.getCategory() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), supplier.getCategory());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), "");
            }
            if (supplier.getWeb() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), supplier.getWeb());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WEB"), "");
            }
            if (supplier.getShippingMethod() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGMETHOD"), supplier.getShippingMethod());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGMETHOD"), "");
            }
            if (supplier.getPaymentMethod() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), supplier.getPaymentMethod());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), "");
            }
            if (supplier.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), supplier.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIER")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createAddressComponent(Supplier supplier) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Address a : supplier.getAddress()) {
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
        if (supplier.getAddress() != null && !supplier.getAddress().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ADDRESS")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }

    }

    private static ComponentBuilder<?, ?> createEmailComponent(Supplier supplier) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Email e : supplier.getEmail()) {
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
        if (supplier.getEmail() != null && !supplier.getEmail().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("EMAIL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    mainList);
        } else {
            return cmp.filler();
        }
    }

    private static ComponentBuilder<?, ?> createPhoneComponent(Supplier supplier) {
        MultiPageListBuilder mainList = cmp.multiPageList();

        for (Phone p : supplier.getPhone()) {
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
        if (supplier.getPhone() != null && !supplier.getPhone().isEmpty()) {
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
