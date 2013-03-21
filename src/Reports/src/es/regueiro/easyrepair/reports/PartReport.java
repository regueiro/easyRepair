package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Stock;
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
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.openide.util.NbPreferences;

public class PartReport {

    private PartReport() {
    }

    private static JRDataSource createPartListDataSource(List<Part> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Part part : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (part.getMake() != null) {
                    map.put("make", part.getMake());
                } else {
                    map.put("make", "");
                }
                if (part.getModel() != null) {
                    map.put("model", part.getModel());
                } else {
                    map.put("model", "");
                }
                if (part.getCategory() != null) {
                    map.put("category", part.getCategory());
                } else {
                    map.put("category", "");
                }
                if (part.getPrice() != null) {
                    map.put("price", part.getPrice());
                    BigDecimal priceWithIVA = part.getPrice().multiply(new BigDecimal("1").add(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))).setScale(2);
                    map.put("priceWithIva", priceWithIVA);
                } else {
                    map.put("price", new BigDecimal("0"));
                    map.put("priceWithIva", new BigDecimal("0"));
                }
                if (part.getNotes() != null) {
                    map.put("notes", part.getNotes());
                } else {
                    map.put("notes", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generatePartReport(Part part) {

        JasperReportBuilder report = report();
        if (part != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());

            SubreportBuilder stockListSubreport = cmp.subreport(generateStockListReport(part).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTRECORD"), ""),
                    cmp.multiPageList().setStyle(stl.style(10)).add(
                    createPartComponent(part),
                    cmp.verticalGap(10),
                    stockListSubreport,
                    cmp.verticalGap(10)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent);
        }
        return report;

    }

    public static JasperReportBuilder generatePartListReport(List<Part> list) {
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
            TextColumnBuilder<String> makeColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "make", type.stringType());
            TextColumnBuilder<String> modelColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "model", type.stringType());
            TextColumnBuilder<String> categoryColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), "category", type.stringType());
            TextColumnBuilder<BigDecimal> priceColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), "price", type.bigDecimalType()).setDataType(es.regueiro.easyrepair.reports.Templates.currencyType).setFixedWidth(100);
            TextColumnBuilder<BigDecimal> priceWithIvaColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), "priceWithIva", type.bigDecimalType()).setDataType(es.regueiro.easyrepair.reports.Templates.currencyType).setFixedWidth(100);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTLIST"), ""))
                    .columns(
                    rowNumberColumn, makeColumn, modelColumn, categoryColumn, priceColumn, priceWithIvaColumn)
                    .columnGrid(
                    rowNumberColumn, makeColumn, modelColumn, categoryColumn, priceColumn, priceWithIvaColumn)
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createPartComponent(Part part) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (part != null) {
            if (part.getMake() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), part.getMake());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "");
            }
            if (part.getModel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), part.getModel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "");
            }
            if (part.getCategory() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), part.getCategory());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CATEGORY"), "");
            }
            if (part.getPrice() != null) {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), part.getPrice());
                BigDecimal priceWithIVA = part.getPrice().multiply(new BigDecimal("1").add(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))).setScale(2);
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), priceWithIVA);
            } else {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"), new BigDecimal("0"));
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICEWITHIVA"), new BigDecimal("0"));
            }
            if (part.getNotes() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), part.getNotes());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTES"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PART")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static JRDataSource createStockListDataSource(Part part) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (part != null) {
            List<Stock> stockList = part.getStock();

            if (stockList != null && !stockList.isEmpty()) {
                for (Stock s : stockList) {
                    Map<String, Object> mapStock = new HashMap<String, Object>();
                    if (s.getWarehouse() != null && s.getWarehouse().getName() != null) {
                        mapStock.put("warehouse", s.getWarehouse().getName());
                    } else {
                        mapStock.put("warehouse", "");
                    }
                    mapStock.put("units", s.getUnits());

                    myColl.add(mapStock);
                }

            }
        }

        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    private static JasperReportBuilder generateStockListReport(Part part) {
        JasperReportBuilder report = report();
        if (part != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  
            TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn()
                    .setFixedColumns(2)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            TextColumnBuilder<String> warehouseColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WAREHOUSE"), "warehouse", type.stringType());
            TextColumnBuilder<Integer> unitsColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("UNITS"), "units", type.integerType())
                    .setFixedWidth(50)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle)
                    //columns  
                    .columns(
                    rowNumberColumn, warehouseColumn, unitsColumn)
                    .columnGrid(
                    rowNumberColumn, warehouseColumn, unitsColumn)
                    //band components  
                    .setDataSource(createStockListDataSource(part));
        }
        return report;
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
