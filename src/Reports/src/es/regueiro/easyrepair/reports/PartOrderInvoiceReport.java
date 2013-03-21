package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.Supplier;
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
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.openide.util.NbPreferences;

public class PartOrderInvoiceReport {

    private static BigDecimal partTotal = new BigDecimal("0");
    private static AggregationSubtotalBuilder<BigDecimal> totalSum;

    private PartOrderInvoiceReport() {
    }

    private static JRDataSource createPartOrderInvoiceDataSource(PartOrderInvoice invoice) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();

        if (invoice != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (invoice.getInvoiceNumber() != null) {
                map.put("invoiceNumber", invoice.getInvoiceNumber());
            } else {
                map.put("invoiceNumber", "");
            }
            if (invoice.getStatus() != null) {
                map.put("status", invoice.getStatus());
            } else {
                map.put("status", "");
            }
            if (invoice.getPaymentMethod() != null) {
                map.put("paymentMethod", invoice.getPaymentMethod());
            } else {
                map.put("paymentMethod", "");
            }
            if (invoice.getInvoiceDate() != null) {
                map.put("invoiceDate", invoice.getInvoiceDate().toString());
            } else {
                map.put("invoiceDate", "");
            }
            if (invoice.getAcceptedDate() != null) {
                map.put("acceptedDate", invoice.getAcceptedDate().toString());
            } else {
                map.put("acceptedDate", "");
            }
            if (invoice.getEstimatedPaymentDate() != null) {
                map.put("estimatedPaymentDate", invoice.getEstimatedPaymentDate().toString());
            } else {
                map.put("estimatedPaymentDate", "");
            }
            if (invoice.getPaymentDate() != null) {
                map.put("paymentDate", invoice.getPaymentDate().toString());
            } else {
                map.put("paymentDate", "");
            }
            if (invoice.getNotes() != null) {
                map.put("notes", invoice.getNotes());
            } else {
                map.put("notes", invoice.getNotes());
            }
            if (invoice.getOrder() != null) {
                if (invoice.getOrder().getDiscount() != null) {
                    map.put("discount", invoice.getOrder().getDiscount());
                } else {
                    map.put("discount", new BigDecimal("0"));
                }
            } else {
                map.put("discount", new BigDecimal("0"));
            }
            if (invoice.getResponsible() != null) {
                String resp = "";
                if (invoice.getResponsible().getName() != null) {
                    resp += invoice.getResponsible().getName();
                }
                if (invoice.getResponsible().getSurname() != null) {
                    resp += " " + invoice.getResponsible().getSurname();
                }
                map.put("responsible", resp);
            }
            if (invoice.getOrder() != null && invoice.getOrder().getSupplier() != null) {
                map.put("supplier", invoice.getOrder().getSupplier().getName());

                if (invoice.getOrder().getSupplier().getNif() != null) {
                    map.put("supplierNif", invoice.getOrder().getSupplier().getNif().getNumber());
                } else {
                    map.put("supplierNif", "");
                }

            } else {
                map.put("supplier", "");
                map.put("supplierNif", "");
            }


            myColl.add(map);
        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    private static JRDataSource createPartListDataSource(PartOrderInvoice invoice) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (invoice != null) {
            List<PartLine> partList = invoice.getOrder().getPartsList();

            if (partList != null && !partList.isEmpty()) {
                for (PartLine p : partList) {
                    Map<String, Object> mapPart = new HashMap<String, Object>();
                    if (p.getPart() != null) {
                        if (p.getPart().getMake() != null) {
                            mapPart.put("make", p.getPart().getMake());
                        } else {
                            mapPart.put("make", "");
                        }
                        if (p.getPart().getModel() != null) {
                            mapPart.put("model", p.getPart().getModel());
                        } else {
                            mapPart.put("model", "");
                        }
                        if (p.getPart().getPrice() != null) {
                            mapPart.put("unitPrice", p.getPart().getPrice());
                            BigDecimal price = p.getPart().getPrice().multiply(new BigDecimal(p.getQuantity()));
                            if (p.getDiscount() != null) {
                                partTotal = partTotal.add(price.subtract(price.multiply(p.getDiscount().divide(new BigDecimal("100")))));
                            } else {
                                partTotal = price;
                            }
                        } else {
                            mapPart.put("unitPrice", "");
                        }
                    } else {
                        mapPart.put("make", "");
                        mapPart.put("model", "");

                    }
                    mapPart.put("quantity", p.getQuantity());


                    if (p.getDiscount() != null) {
                        mapPart.put("discount", p.getDiscount());
                    } else {
                        mapPart.put("discount", new BigDecimal("0"));
                    }

                    myColl.add(mapPart);
                }
            }
        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    private static JRDataSource createPartOrderInvoiceListDataSource(List<PartOrderInvoice> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (PartOrderInvoice invoice : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (invoice.getInvoiceNumber() != null) {
                    map.put("invoiceNumber", invoice.getInvoiceNumber());
                } else {
                    map.put("invoiceNumber", "");
                }
                if (invoice.getStatus() != null) {
                    map.put("status", invoice.getStatus());
                } else {
                    map.put("status", "");
                }
                if (invoice.getPaymentMethod() != null) {
                    map.put("paymentMethod", invoice.getPaymentMethod());
                } else {
                    map.put("paymentMethod", "");
                }
                if (invoice.getInvoiceDate() != null) {
                    map.put("invoiceDate", invoice.getInvoiceDate().toString());
                } else {
                    map.put("invoiceDate", "");
                }
                if (invoice.getAcceptedDate() != null) {
                    map.put("acceptedDate", invoice.getAcceptedDate().toString());
                } else {
                    map.put("acceptedDate", "");
                }
                if (invoice.getEstimatedPaymentDate() != null) {
                    map.put("estimatedPaymentDate", invoice.getEstimatedPaymentDate().toString());
                } else {
                    map.put("estimatedPaymentDate", "");
                }
                if (invoice.getPaymentDate() != null) {
                    map.put("paymentDate", invoice.getPaymentDate().toString());
                } else {
                    map.put("paymentDate", "");
                }
                if (invoice.getNotes() != null) {
                    map.put("notes", invoice.getNotes());
                } else {
                    map.put("notes", invoice.getNotes());
                }
                if (invoice.getOrder() != null) {
                    if (invoice.getOrder().getDiscount() != null) {
                        map.put("discount", invoice.getOrder().getDiscount());
                    } else {
                        map.put("discount", new BigDecimal("0"));
                    }
                } else {
                    map.put("discount", new BigDecimal("0"));
                }
                if (invoice.getResponsible() != null) {
                    String resp = "";
                    if (invoice.getResponsible().getName() != null) {
                        resp += invoice.getResponsible().getName();
                    }
                    if (invoice.getResponsible().getSurname() != null) {
                        resp += " " + invoice.getResponsible().getSurname();
                    }
                    map.put("responsible", resp);
                }
                if (invoice.getOrder() != null && invoice.getOrder().getSupplier() != null) {
                    map.put("supplier", invoice.getOrder().getSupplier().getName());

                    if (invoice.getOrder().getSupplier().getNif() != null) {
                        map.put("supplierNif", invoice.getOrder().getSupplier().getNif().getNumber());
                    } else {
                        map.put("supplierNif", "");
                    }

                } else {
                    map.put("supplier", "");
                    map.put("supplierNif", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generatePartOrderInvoiceReport(PartOrderInvoice invoice) {
        partTotal = new BigDecimal("0");

        JasperReportBuilder report = report();
        if (invoice != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();

            SubreportBuilder partListSubreport = cmp.subreport(generatePartListReport(invoice).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder totalSubreport = cmp.subreport(generateTotalReport(invoice).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTORDERINVOICENO"), invoice.getInvoiceNumber()),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(createSupplierComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIER"), invoice.getOrder().getSupplier())).heightFixedOnTop(),
                    cmp.hListCell(createPartOrderInvoiceComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICE"), invoice)).heightFixedOnTop()),
                    cmp.verticalGap(10),
                    cmp.verticalList(
                    partListSubreport,
                    cmp.verticalGap(10),
                    totalSubreport))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(invoice));
        }
        return report;
    }

    private static JasperReportBuilder generatePartListReport(PartOrderInvoice invoice) {
        JasperReportBuilder report = report();
        if (invoice != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  
            TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn()
                    .setFixedColumns(2)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            TextColumnBuilder<String> makeColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MAKE"), "make", type.stringType())
                    .setFixedWidth(100);
            TextColumnBuilder<String> modelColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("MODEL"), "model", type.stringType())
                    .setFixedWidth(150);
            TextColumnBuilder<Integer> quantityColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("QUANTITY"), "quantity", type.integerType())
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            TextColumnBuilder<BigDecimal> unitPriceColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("UNITPRICE"), "unitPrice", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> discountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DISCOUNT"), "discount", type.bigDecimalType());
            TextColumnBuilder<String> taxColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("IVA"), exp.text(NbPreferences.root().get("iva", "21") + "%"))
                    .setFixedColumns(3)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            //price = unitPrice * quantity  
            TextColumnBuilder<BigDecimal> priceColumn = unitPriceColumn.multiply(quantityColumn).subtract(unitPriceColumn.multiply(quantityColumn).multiply(discountColumn.divide(2, new BigDecimal("100"))))
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType);
            //vat = price * tax  
            TextColumnBuilder<BigDecimal> vatColumn = priceColumn.multiply(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("IVA"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType);
            //total = price + vat  
            TextColumnBuilder<BigDecimal> totalColumn = priceColumn.add(vatColumn)
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TOTALPRICE"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType)
                    .setRows(2)
                    .setStyle(subtotalStyle);
            //init subtotals  
            totalSum = sbt.sum(totalColumn)
                    .setLabel(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TOTALLABEL"))
                    .setLabelStyle(es.regueiro.easyrepair.reports.Templates.boldStyle);

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle)
                    //columns  
                    .columns(
                    rowNumberColumn, makeColumn, modelColumn, quantityColumn, unitPriceColumn, discountColumn, totalColumn, priceColumn, taxColumn, vatColumn)
                    .columnGrid(
                    rowNumberColumn, makeColumn, modelColumn, quantityColumn, unitPriceColumn, discountColumn,
                    grid.horizontalColumnGridList()
                    .add(totalColumn).newRow()
                    .add(priceColumn, taxColumn, vatColumn))
                    //subtotals  
                    .subtotalsAtSummary(
                    totalSum, sbt.sum(priceColumn), sbt.sum(vatColumn))
                    //band components  
                    .setDataSource(createPartListDataSource(invoice));
        }
        return report;
    }

    private static JasperReportBuilder generateTotalReport(PartOrderInvoice invoice) {
        JasperReportBuilder report = report();
        if (invoice != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  
            TextColumnBuilder<BigDecimal> amountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("AMOUNT"), exp.value(partTotal))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType);
            TextColumnBuilder<BigDecimal> discountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DISCOUNT"), "discount", type.bigDecimalType())
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            TextColumnBuilder<String> taxColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("IVA"), exp.text(NbPreferences.root().get("iva", "21") + "%"))
                    .setFixedColumns(3)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            //price = unitPrice * quantity  
            TextColumnBuilder<BigDecimal> priceColumn = amountColumn.subtract(amountColumn.multiply(discountColumn.divide(2, new BigDecimal("100"))))
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PRICE"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType);
            //TextColumnBuilder<BigDecimal> priceColumn = col.column("Price", exp.number(partTotal.add(labourTotal))) //vat = price * tax  
            TextColumnBuilder<BigDecimal> vatColumn = priceColumn.multiply(new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100")))
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("IVA"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType);
            //total = price + vat  
            TextColumnBuilder<BigDecimal> totalColumn = priceColumn.add(vatColumn)
                    .setTitle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("TOTALPRICE"))
                    .setDataType(es.regueiro.easyrepair.reports.Templates.currencyType)
                    .setRows(2)
                    .setStyle(subtotalStyle);
            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle)
                    //columns  
                    .columns(
                    amountColumn, discountColumn, totalColumn, priceColumn, taxColumn, vatColumn)
                    .columnGrid(
                    amountColumn, discountColumn,
                    grid.horizontalColumnGridList()
                    .add(totalColumn).newRow()
                    .add(priceColumn, taxColumn, vatColumn))
                    //band components  
                    .setDataSource(createPartOrderInvoiceDataSource(invoice));
        }
        return report;
    }

    public static JasperReportBuilder generatePartOrderInvoiceListReport(List<PartOrderInvoice> list) {
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
            TextColumnBuilder<String> invoiceNumberColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICENUMBER"), "invoiceNumber", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> invoiceDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICEDATE"), "invoiceDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> acceptedDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), "acceptedDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> estimatedPaymentDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDPAYMENTDATE"), "estimatedPaymentDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> paymentDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTDATE"), "paymentDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> statusColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "status", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> paymentMethodColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), "paymentMethod", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> paymentResponsibleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTRESPONSIBLE"), "paymentResponsible", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> discountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DISCOUNT"), "discount", type.stringType())
                    .setFixedColumns(5)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> responsibleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), "invoiceResponsible", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> supplierColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIER"), "supplier", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> supplierNifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIERNIF"), "supplierNif", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns 
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTORDERINVOICELIST"), ""))
                    .columns(
                    rowNumberColumn, invoiceNumberColumn, invoiceDateColumn, acceptedDateColumn, estimatedPaymentDateColumn, paymentDateColumn, paymentMethodColumn, paymentResponsibleColumn, discountColumn, statusColumn, responsibleColumn, supplierColumn, supplierNifColumn)
                    .columnGrid(
                    rowNumberColumn, invoiceNumberColumn,
                    grid.horizontalColumnGridList()
                    .add(invoiceDateColumn).newRow()
                    .add(acceptedDateColumn),
                    grid.horizontalColumnGridList()
                    .add(estimatedPaymentDateColumn).newRow()
                    .add(paymentDateColumn),
                    grid.horizontalColumnGridList()
                    .add(paymentMethodColumn).newRow()
                    .add(paymentResponsibleColumn),
                    grid.horizontalColumnGridList()
                    .add(statusColumn).newRow()
                    .add(responsibleColumn),
                    grid.horizontalColumnGridList()
                    .add(supplierColumn).newRow()
                    .add(supplierNifColumn))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartOrderInvoiceListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createSupplierComponent(String label, Supplier supplier) {
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
        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createPartOrderInvoiceComponent(String label, PartOrderInvoice invoice) {
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (invoice != null) {
            if (invoice.getInvoiceDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICEDATE"), invoice.getInvoiceDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICEDATE"), "");
            }
            if (invoice.getAcceptedDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), invoice.getAcceptedDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), "");
            }
            if (invoice.getEstimatedPaymentDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDPAYMENTDATE"), invoice.getEstimatedPaymentDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDPAYMENTDATE"), "");
            }
            if (invoice.getPaymentDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTDATE"), invoice.getPaymentDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTDATE"), "");
            }
            if (invoice.getPaymentMethod() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), invoice.getPaymentMethod());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTMETHOD"), invoice.getPaymentMethod());
            }
            if (invoice.getStatus() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), invoice.getStatus());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), invoice.getStatus());
            }

            if (invoice.getResponsible() != null) {
                String resp = "";
                if (invoice.getResponsible().getName() != null) {
                    resp += invoice.getResponsible().getName();
                }
                if (invoice.getResponsible().getSurname() != null) {
                    resp += " " + invoice.getResponsible().getSurname();
                }
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), resp);
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(13).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
}
