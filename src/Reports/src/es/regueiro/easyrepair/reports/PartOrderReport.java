package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.stock.PartOrder;
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

public class PartOrderReport {

    private static BigDecimal partTotal = new BigDecimal("0");
    private static BigDecimal shippingCosts = new BigDecimal("0");
    private static BigDecimal otherCosts = new BigDecimal("0");
    private static AggregationSubtotalBuilder<BigDecimal> totalSum;

    private PartOrderReport() {
    }

    private static JRDataSource createPartOrderDataSource(PartOrder partOrder) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();

        if (partOrder != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (partOrder.getOrderNumber() != null) {
                map.put("orderNumber", partOrder.getOrderNumber());
            } else {
                map.put("orderNumber", "");
            }
            if (partOrder.getShippingCosts() != null) {
                map.put("shippingCosts", partOrder.getShippingCosts());
                shippingCosts = partOrder.getShippingCosts();
            } else {
                map.put("shippingCosts", new BigDecimal("0"));
            }
            if (partOrder.getOtherCosts() != null) {
                map.put("otherCosts", partOrder.getOtherCosts());
                otherCosts = partOrder.getOtherCosts();
            } else {
                map.put("otherCosts", new BigDecimal("0"));
            }
            if (partOrder.getDiscount() != null) {
                map.put("discount", partOrder.getDiscount());
            } else {
                map.put("discount", new BigDecimal("0"));
            }
            if (partOrder.getStatus() != null) {
                map.put("status", partOrder.getStatus());
            } else {
                map.put("status", partOrder.getStatus());
            }
            if (partOrder.getNotes() != null) {
                map.put("notes", partOrder.getNotes());
            } else {
                map.put("notes", partOrder.getNotes());
            }
            if (partOrder.getOrderDate() != null) {
                map.put("orderDate", partOrder.getOrderDate().toString());
            } else {
                map.put("orderDate", "");
            }
            if (partOrder.getEstimatedDate() != null) {
                map.put("estimatedDate", partOrder.getEstimatedDate().toString());
            } else {
                map.put("estimatedDate", "");
            }
            if (partOrder.getReceiptDate() != null) {
                map.put("receiptDate", partOrder.getReceiptDate().toString());
            } else {
                map.put("receiptDate", "");
            }
            if (partOrder.getShippingWarehouse() != null && partOrder.getShippingWarehouse().getName() != null) {
                map.put("warehouse", partOrder.getShippingWarehouse().getName());
            } else {
                map.put("warehouse", "");
            }
            if (partOrder.getResponsible() != null) {
                String resp = "";
                if (partOrder.getResponsible().getName() != null) {
                    resp += partOrder.getResponsible().getName();
                }
                if (partOrder.getResponsible().getSurname() != null) {
                    resp += " " + partOrder.getResponsible().getSurname();
                }
                map.put("responsible", resp);
            }
            if (partOrder.getSupplier() != null) {
                map.put("supplier", partOrder.getSupplier().getName());

                if (partOrder.getSupplier().getNif() != null) {
                    map.put("supplierNif", partOrder.getSupplier().getNif().getNumber());
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

    private static JRDataSource createPartListDataSource(PartOrder partOrder) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (partOrder != null) {
            List<PartLine> partList = partOrder.getPartsList();

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

    private static JRDataSource createPartOrderListDataSource(List<PartOrder> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (PartOrder partOrder : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (partOrder.getOrderNumber() != null) {
                    map.put("orderNumber", partOrder.getOrderNumber());
                } else {
                    map.put("orderNumber", "");
                }
                if (partOrder.getShippingCosts() != null) {
                    map.put("shippingCosts", partOrder.getShippingCosts());
                } else {
                    map.put("shippingCosts", new BigDecimal("0"));
                }
                if (partOrder.getOtherCosts() != null) {
                    map.put("otherCosts", partOrder.getOtherCosts());
                } else {
                    map.put("otherCosts", new BigDecimal("0"));
                }
                if (partOrder.getDiscount() != null) {
                    map.put("discount", partOrder.getDiscount());
                } else {
                    map.put("discount", new BigDecimal("0"));
                }
                if (partOrder.getStatus() != null) {
                    map.put("status", partOrder.getStatus());
                } else {
                    map.put("status", partOrder.getStatus());
                }
                if (partOrder.getNotes() != null) {
                    map.put("notes", partOrder.getNotes());
                } else {
                    map.put("notes", partOrder.getNotes());
                }
                if (partOrder.getOrderDate() != null) {
                    map.put("orderDate", partOrder.getOrderDate().toString());
                } else {
                    map.put("orderDate", "");
                }
                if (partOrder.getEstimatedDate() != null) {
                    map.put("estimatedDate", partOrder.getEstimatedDate().toString());
                } else {
                    map.put("estimatedDate", "");
                }
                if (partOrder.getReceiptDate() != null) {
                    map.put("receiptDate", partOrder.getReceiptDate().toString());
                } else {
                    map.put("receiptDate", "");
                }
                if (partOrder.getShippingWarehouse() != null && partOrder.getShippingWarehouse().getName() != null) {
                    map.put("warehouse", partOrder.getShippingWarehouse().getName());
                } else {
                    map.put("warehouse", "");
                }
                if (partOrder.getResponsible() != null) {
                    String resp = "";
                    if (partOrder.getResponsible().getName() != null) {
                        resp += partOrder.getResponsible().getName();
                    }
                    if (partOrder.getResponsible().getSurname() != null) {
                        resp += " " + partOrder.getResponsible().getSurname();
                    }
                    map.put("responsible", resp);
                }
                if (partOrder.getSupplier() != null) {
                    map.put("supplier", partOrder.getSupplier().getName());

                    if (partOrder.getSupplier().getNif() != null) {
                        map.put("supplierNif", partOrder.getSupplier().getNif().getNumber());
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

    public static JasperReportBuilder generatePartOrderReport(PartOrder partOrder) {

        partTotal = new BigDecimal("0");

        JasperReportBuilder report = report();
        if (partOrder != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();

            SubreportBuilder partListSubreport = cmp.subreport(generatePartListReport(partOrder).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder totalSubreport = cmp.subreport(generateTotalReport(partOrder).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTORDERNO"), partOrder.getOrderNumber()),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(createSupplierComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIER"), partOrder.getSupplier())).heightFixedOnTop(),
                    cmp.hListCell(createPartOrderComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTORDER"), partOrder)).heightFixedOnTop()),
                    cmp.verticalGap(10),
                    cmp.verticalList(
                    partListSubreport,
                    cmp.verticalGap(10),
                    totalSubreport,
                    cmp.verticalGap(10),
                    createNotesComponent(partOrder)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(partOrder));
        }
        return report;

    }

    private static JasperReportBuilder generatePartListReport(PartOrder partOrder) {
        JasperReportBuilder report = report();
        if (partOrder != null) {
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
                    .setDataSource(createPartListDataSource(partOrder));
        }
        return report;
    }

    private static JasperReportBuilder generateTotalReport(PartOrder partOrder) {
        JasperReportBuilder report = report();
        if (partOrder != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  
            TextColumnBuilder<BigDecimal> amountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("AMOUNT"), exp.value(partTotal.add(otherCosts).add(shippingCosts)))
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
                    .setDataSource(createPartOrderDataSource(partOrder));
        }
        return report;
    }

   

    public static JasperReportBuilder generatePartOrderListReport(List<PartOrder> list) {
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
            TextColumnBuilder<String> orderNumberColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ORDERNUMBER"), "orderNumber", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> orderDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ORDERDATE"), "orderDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> estimatedDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), "estimatedDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> receiptDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RECEIPTDATE"), "receiptDate", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> warehouseColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WAREHOUSE"), "warehouse", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<BigDecimal> shippingCostsColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGCOSTS"), "shippingCosts", type.bigDecimalType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<BigDecimal> otherCostsColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OTHERCOSTS"), "otherCosts", type.bigDecimalType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> statusColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "status", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> responsibleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), "partOrderResponsible", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> supplierColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIER"), "client", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> supplierNifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SUPPLIERNIF"), "clientNif", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PARTORDERLIST"), ""))
                    .columns(
                    rowNumberColumn, orderNumberColumn,  orderDateColumn, estimatedDateColumn, receiptDateColumn, warehouseColumn, statusColumn, responsibleColumn, shippingCostsColumn, otherCostsColumn, supplierColumn, supplierNifColumn)
                    .columnGrid(
                    rowNumberColumn, orderNumberColumn, 
                    grid.horizontalColumnGridList()
                    .add(orderDateColumn).newRow()
                    .add(estimatedDateColumn),
                    grid.horizontalColumnGridList()
                    .add(receiptDateColumn).newRow()
                    .add(warehouseColumn),
                    grid.horizontalColumnGridList()
                    .add(shippingCostsColumn).newRow()
                    .add(otherCostsColumn),
                    grid.horizontalColumnGridList()
                    .add(statusColumn).newRow()
                    .add(responsibleColumn),
                    grid.horizontalColumnGridList()
                    .add(supplierColumn).newRow()
                    .add(supplierNifColumn))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartOrderListDataSource(list));
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

    private static ComponentBuilder<?, ?> createPartOrderComponent(String label, PartOrder partOrder) {
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (partOrder != null) {
            if (partOrder.getOrderDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ORDERDATE"), partOrder.getOrderDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ORDERDATE"), "");
            }
            if (partOrder.getEstimatedDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), partOrder.getEstimatedDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), "");
            }
            if (partOrder.getReceiptDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RECEIPTDATE"), partOrder.getReceiptDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RECEIPTDATE"), "");
            }
            if (partOrder.getShippingWarehouse() != null && partOrder.getShippingWarehouse().getName() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WAREHOUSE"), partOrder.getShippingWarehouse().getName());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("WAREHOUSE"), "");
            }
            if (partOrder.getShippingCosts() != null) {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGCOSTS"), partOrder.getShippingCosts());
            } else {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("SHIPPINGCOSTS"), new BigDecimal("0"));
            }
            if (partOrder.getOtherCosts() != null) {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OTHERCOSTS"), partOrder.getOtherCosts());
            } else {
                addCurrency(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("OTHERCOSTS"), new BigDecimal("0"));
            }
            if (partOrder.getStatus() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), partOrder.getStatus());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "");
            }
            if (partOrder.getResponsible() != null) {
                String resp = "";
                if (partOrder.getResponsible().getName() != null) {
                    resp += partOrder.getResponsible().getName();
                }
                if (partOrder.getResponsible().getSurname() != null) {
                    resp += " " + partOrder.getResponsible().getSurname();
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

    private static ComponentBuilder<?, ?> createNotesComponent(PartOrder partOrder) {
        if (partOrder != null && partOrder.getNotes() != null && !partOrder.getNotes().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTESLABEL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    cmp.text(partOrder.getNotes()),
                    cmp.verticalGap(10));
        }
        return cmp.filler();
    }

    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(12).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
    
    private static void addCurrency(HorizontalListBuilder list, String label, BigDecimal value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(12).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value).setPattern(getPattern())).newRow();
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
