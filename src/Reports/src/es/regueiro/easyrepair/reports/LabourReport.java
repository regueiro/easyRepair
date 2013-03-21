package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.repair.Labour;
import java.math.BigDecimal;
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
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.openide.util.NbPreferences;

public class LabourReport {

    private LabourReport() {
    }

    private static JRDataSource createLabourListDataSource(List<Labour> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Labour labour : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (labour.getName() != null) {
                    map.put("name", labour.getName());
                } else {
                    map.put("name", "");
                }
                if (labour.getDescription() != null) {
                    map.put("description", labour.getDescription());
                } else {
                    map.put("description", "");
                }

                if (labour.getPrice() != null) {
                    map.put("price", labour.getPrice());
                    BigDecimal priceWithIVA = labour.getPrice().multiply(new BigDecimal("1").add(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))).setScale(2);
                    map.put("priceWithIva", priceWithIVA);
                } else {
                    map.put("price", new BigDecimal("0"));
                    map.put("priceWithIva", new BigDecimal("0"));
                }
                if (labour.getNotes() != null) {
                    map.put("notes", labour.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateLabourReport(Labour labour) {

        JasperReportBuilder report = report();
        if (labour != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABOURRECORD"), ""),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(createLabourComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABOUR"), labour)).heightFixedOnTop()), cmp.verticalGap(10))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generateLabourListReport(List<Labour> list) {
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
            TextColumnBuilder<String> descriptionColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DESCRIPTION"), "description", type.stringType());
            TextColumnBuilder<BigDecimal> priceColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), "price", type.bigDecimalType()).setDataType(es.regueiro.easyrepair.reports.Templates.currencyType).setFixedWidth(100);
            TextColumnBuilder<BigDecimal> priceWithIvaColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), "priceWithIva", type.bigDecimalType()).setDataType(es.regueiro.easyrepair.reports.Templates.currencyType).setFixedWidth(100);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("LABOURLIST"), ""))
                    .columns(
                    rowNumberColumn, nameColumn, descriptionColumn, priceColumn, priceWithIvaColumn)
                    .columnGrid(
                    rowNumberColumn, nameColumn, descriptionColumn, priceColumn, priceWithIvaColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createLabourListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createLabourComponent(String label, Labour labour) {


        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (labour != null) {
            if (labour.getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), labour.getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "");
            }
            if (labour.getDescription() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DESCRIPTION"), labour.getDescription());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DESCRIPTION"), "");
            }
            if (labour.getPrice() != null) {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), labour.getPrice());
                BigDecimal priceWithIVA = labour.getPrice().multiply(new BigDecimal("1").add(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))).setScale(2);
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), priceWithIVA);
            } else {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), new BigDecimal("0"));
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), new BigDecimal("0"));
            }
            if (labour.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), labour.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }

        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(15).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private static void addCurrency(HorizontalListBuilder list, String label, BigDecimal value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(15).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value).setPattern(getPattern())).newRow();
        }
    }

    private static String getPattern() {
        if (NbPreferences.root().getBoolean("inFront", false)) {
            return NbPreferences.root().get("currency", " €") + " #,###.00";
        } else {
            return "#,###.00 " + NbPreferences.root().get("currency", " €");
        }
    }
}
