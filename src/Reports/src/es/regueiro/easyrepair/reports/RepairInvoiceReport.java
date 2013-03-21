package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.repair.LabourLine;
import es.regueiro.easyrepair.model.repair.RepairInvoice;
import es.regueiro.easyrepair.model.stock.PartLine;
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

public class RepairInvoiceReport {

    private static BigDecimal partTotal = new BigDecimal("0");
    private static BigDecimal labourTotal = new BigDecimal("0");
    private static AggregationSubtotalBuilder<BigDecimal> totalSum;

    private RepairInvoiceReport() {
    }

    private static JRDataSource createRepairInvoiceDataSource(RepairInvoice invoice) {
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
            if (invoice.getPaymentResponsible() != null) {
                map.put("paymentResponsible", invoice.getPaymentResponsible());
            } else {
                map.put("paymentResponsible", "");
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
            if (invoice.getOrder().getEstimate() != null) {
                if (invoice.getOrder().getEstimate().getDiscount() != null) {
                    map.put("discount", invoice.getOrder().getEstimate().getDiscount());
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
            if (invoice.getOrder() != null && invoice.getOrder().getVehicle() != null) {
                String veh = "";
                if (invoice.getOrder().getVehicle().getMake() != null) {
                    veh += invoice.getOrder().getVehicle().getMake();
                }
                if (invoice.getOrder().getVehicle().getModel() != null) {
                    veh += " " + invoice.getOrder().getVehicle().getModel();
                }
                map.put("vehicle", veh);
                if (invoice.getOrder().getVehicle().getRegistration() != null) {
                    map.put("vehicleRegistration", invoice.getOrder().getVehicle().getRegistration());
                } else {
                    map.put("vehicleRegistration", "");
                }


                if (invoice.getOrder().getVehicle().getOwner() != null) {
                    String cli = "";
                    if (invoice.getOrder().getVehicle().getOwner().getName() != null) {
                        cli += invoice.getOrder().getVehicle().getOwner().getName();
                    }
                    if (invoice.getOrder().getVehicle().getOwner().getSurname() != null) {
                        cli += " " + invoice.getOrder().getVehicle().getOwner().getSurname();
                    }
                    map.put("clientName", cli);
                    if (invoice.getOrder().getVehicle().getOwner().getNif() != null) {
                        map.put("clientNif", invoice.getOrder().getVehicle().getOwner().getNif().getNumber());
                    } else {
                        map.put("clientNif", "");
                    }

                }
            } else {
                map.put("vehicle", "");
                map.put("vehicleRegistration", "");
                map.put("clientName", "");
                map.put("clientNif", "");
            }


            myColl.add(map);
        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);

        return dataSource;
    }

    private static JRDataSource createPartListDataSource(RepairInvoice invoice) {

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

    private static JRDataSource createRepairInvoiceLabourListDataSource(RepairInvoice invoice) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (invoice != null) {
            List<LabourLine> labourList = invoice.getOrder().getLabourList();

            if (labourList != null && !labourList.isEmpty()) {
                for (LabourLine l : labourList) {
                    Map<String, Object> mapPart = new HashMap<String, Object>();
                    if (l.getLabour() != null) {
                        if (l.getLabour().getName() != null) {
                            mapPart.put("name", l.getLabour().getName());
                        } else {
                            mapPart.put("name", "");
                        }
                        if (l.getLabour().getPrice() != null) {
                            mapPart.put("hourPrice", l.getLabour().getPrice());
                            BigDecimal price = l.getLabour().getPrice().multiply(l.getHours());
                            if (l.getDiscount() != null) {
                                labourTotal = labourTotal.add(price.subtract(price.multiply(l.getDiscount().divide(new BigDecimal("100")))));
                            } else {
                                labourTotal = price;
                            }
                        } else {
                            mapPart.put("hourPrice", "");
                        }
                    } else {
                        mapPart.put("name", "");
                        mapPart.put("hourPrice", "");
                    }
                    if (l.getHours() != null) {
                        mapPart.put("hours", l.getHours());
                    } else {
                        mapPart.put("hours", new BigDecimal("0"));
                    }

                    if (l.getDiscount() != null) {
                        mapPart.put("discount", l.getDiscount());
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

    private static JRDataSource createRepairInvoiceListDataSource(List<RepairInvoice> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (RepairInvoice invoice : list) {
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
                if (invoice.getPaymentResponsible() != null) {
                    map.put("paymentResponsible", invoice.getPaymentResponsible());
                } else {
                    map.put("paymentResponsible", "");
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
                if (invoice.getOrder().getEstimate() != null) {
                    if (invoice.getOrder().getEstimate().getDiscount() != null) {
                        map.put("discount", invoice.getOrder().getEstimate().getDiscount());
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
                if (invoice.getOrder() != null && invoice.getOrder().getVehicle() != null) {
                    String veh = "";
                    if (invoice.getOrder().getVehicle().getMake() != null) {
                        veh += invoice.getOrder().getVehicle().getMake();
                    }
                    if (invoice.getOrder().getVehicle().getModel() != null) {
                        veh += " " + invoice.getOrder().getVehicle().getModel();
                    }
                    map.put("vehicle", veh);
                    if (invoice.getOrder().getVehicle().getRegistration() != null) {
                        map.put("vehicleRegistration", invoice.getOrder().getVehicle().getRegistration());
                    } else {
                        map.put("vehicleRegistration", "");
                    }


                    if (invoice.getOrder().getVehicle().getOwner() != null) {
                        String cli = "";
                        if (invoice.getOrder().getVehicle().getOwner().getName() != null) {
                            cli += invoice.getOrder().getVehicle().getOwner().getName();
                        }
                        if (invoice.getOrder().getVehicle().getOwner().getSurname() != null) {
                            cli += " " + invoice.getOrder().getVehicle().getOwner().getSurname();
                        }
                        map.put("clientName", cli);
                        if (invoice.getOrder().getVehicle().getOwner().getNif() != null) {
                            map.put("clientNif", invoice.getOrder().getVehicle().getOwner().getNif().getNumber());
                        } else {
                            map.put("clientNif", "");
                        }

                    }
                } else {
                    map.put("vehicle", "");
                    map.put("vehicleRegistration", "");
                    map.put("clientName", "");
                    map.put("clientNif", "");
                }
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateRepairInvoiceReport(RepairInvoice invoice) {
        partTotal = new BigDecimal("0");
        labourTotal = new BigDecimal("0");

        JasperReportBuilder report = report();
        if (invoice != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();

            SubreportBuilder partListSubreport = cmp.subreport(generatePartListReport(invoice).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder labourListSubreport = cmp.subreport(generateLabourListReport(invoice).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder totalSubreport = cmp.subreport(generateTotalReport(invoice).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRINVOICENO"), invoice.getInvoiceNumber()),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(createCustomerComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENT"), invoice.getOrder().getVehicle())).heightFixedOnTop(),
                    cmp.hListCell(createRepairInvoiceComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("INVOICE"), invoice)).heightFixedOnTop()),
                    cmp.verticalGap(10),
                    cmp.verticalList(
                    partListSubreport,
                    cmp.verticalGap(10),
                    labourListSubreport,
                    cmp.verticalGap(10),
                    totalSubreport))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(invoice));
        }
        return report;
    }

    private static JasperReportBuilder generatePartListReport(RepairInvoice invoice) {
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

    private static JasperReportBuilder generateTotalReport(RepairInvoice invoice) {
        JasperReportBuilder report = report();
        if (invoice != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            //init columns  
            TextColumnBuilder<BigDecimal> amountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("AMOUNT"), exp.value(partTotal.add(labourTotal)))
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
                    .setDataSource(createRepairInvoiceDataSource(invoice));
        }
        return report;
    }

    private static JasperReportBuilder generateLabourListReport(RepairInvoice invoice) {
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
            TextColumnBuilder<String> nameColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), "name", type.stringType())
                    .setFixedWidth(250);
            TextColumnBuilder<BigDecimal> hoursColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("HOURS"), "hours", type.bigDecimalType())
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            TextColumnBuilder<BigDecimal> hourPriceColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("HOURPRICE"), "hourPrice", type.bigDecimalType());
            TextColumnBuilder<BigDecimal> discountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DISCOUNT"), "discount", type.bigDecimalType());
            TextColumnBuilder<String> taxColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("IVA"), exp.text(NbPreferences.root().get("iva", "21") + "%"))
                    .setFixedColumns(3)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            //price = unitPrice * quantity  
            TextColumnBuilder<BigDecimal> priceColumn = hourPriceColumn.multiply(hoursColumn).subtract(hourPriceColumn.multiply(hoursColumn).multiply(discountColumn.divide(2, new BigDecimal("100"))))
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
                    rowNumberColumn, nameColumn, hoursColumn, hourPriceColumn, discountColumn, totalColumn, priceColumn, taxColumn, vatColumn)
                    .columnGrid(
                    rowNumberColumn, nameColumn, hoursColumn, hourPriceColumn, discountColumn,
                    grid.horizontalColumnGridList()
                    .add(totalColumn).newRow()
                    .add(priceColumn, taxColumn, vatColumn))
                    //subtotals  
                    .subtotalsAtSummary(
                    totalSum, sbt.sum(priceColumn), sbt.sum(vatColumn))
                    //band components  
                    .setDataSource(createRepairInvoiceLabourListDataSource(invoice));
        }
        return report;
    }

    public static JasperReportBuilder generateRepairInvoiceListReport(List<RepairInvoice> list) {
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
            TextColumnBuilder<String> clientColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENT"), "client", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> clientNifColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENTNIF"), "clientNif", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> vehicleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLE"), "vehicle", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> vehicleRegistration = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), "vehicleRegistration", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRINVOICELIST"), ""))
                    .columns(
                    rowNumberColumn, invoiceNumberColumn, invoiceDateColumn, acceptedDateColumn, estimatedPaymentDateColumn, paymentDateColumn, paymentMethodColumn, paymentResponsibleColumn, discountColumn, statusColumn, responsibleColumn, clientColumn, vehicleColumn, clientNifColumn, vehicleRegistration)
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
                    .add(clientColumn, vehicleColumn).newRow()
                    .add(clientNifColumn, vehicleRegistration))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createRepairInvoiceListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createCustomerComponent(String label, Vehicle vehicle) {
        String name = "";
        String vehicleName;
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (vehicle != null) {
            if (vehicle.getOwner().getSurname() != null) {
                name = vehicle.getOwner().getName() + " " + vehicle.getOwner().getSurname();
            } else {
                name = vehicle.getOwner().getName();
            }
            addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NAME"), name);
            if (vehicle.getOwner().getNif() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), vehicle.getOwner().getNif().getNumber());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NIF"), "");
            }
            if (vehicle.getMake() != null) {
                vehicleName = vehicle.getMake();
                if (vehicle.getModel() != null) {
                    vehicleName += " " + vehicle.getModel();
                }
            } else if (vehicle.getModel() != null) {
                vehicleName = vehicle.getModel();
            } else {
                vehicleName = "";
            }
            addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLE"), vehicleName);
            if (vehicle.getRegistration() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), vehicle.getRegistration());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), "");
            }
        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createRepairInvoiceComponent(String label, RepairInvoice invoice) {
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
            if (invoice.getPaymentResponsible() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTRESPONSIBLE"), invoice.getPaymentResponsible());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("PAYMENTRESPONSIBLE"), invoice.getPaymentResponsible());
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
            list.add(cmp.text(label + ":").setFixedColumns(12).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
}
