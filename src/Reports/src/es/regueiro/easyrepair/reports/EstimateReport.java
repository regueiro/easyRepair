package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.repair.Estimate;
import es.regueiro.easyrepair.model.repair.LabourLine;
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

public class EstimateReport {

    private static BigDecimal partTotal = new BigDecimal("0");
    private static BigDecimal labourTotal = new BigDecimal("0");
    private static AggregationSubtotalBuilder<BigDecimal> totalSum;

    private EstimateReport() {
    }

    private static JRDataSource createEstimateDataSource(Estimate estimate) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();

        if (estimate != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("estimateNumber", estimate.getEstimateNumber());
            map.put("status", estimate.getStatus());
            if (estimate.getEstimateDate() != null) {
                map.put("estimateDate", estimate.getEstimateDate().toString());
            } else {
                map.put("estimateDate", "");
            }
            if (estimate.getAcceptedDate() != null) {
                map.put("acceptedDate", estimate.getAcceptedDate().toString());
            } else {
                map.put("acceptedDate", "");
            }
            if (estimate.getDiscount() != null) {
                map.put("discount", estimate.getDiscount());
            } else {
                map.put("discount", new BigDecimal("0"));
            }
            if (estimate.getResponsible() != null) {
                String resp = estimate.getResponsible().getName();
                if (estimate.getResponsible().getSurname() != null) {
                    resp += " " + estimate.getResponsible().getSurname();
                }
                map.put("responsible", resp);
            }
            if (estimate.getOrder() != null && estimate.getOrder().getVehicle() != null) {

                String veh = estimate.getOrder().getVehicle().getMake();
                veh += " " + estimate.getOrder().getVehicle().getModel();
                map.put("vehicle", veh);
                map.put("vehicleRegistration", estimate.getOrder().getVehicle().getRegistration());

                if (estimate.getOrder().getVehicle().getOwner() != null) {
                    String resp = estimate.getOrder().getVehicle().getOwner().getName();
                    resp += " " + estimate.getOrder().getVehicle().getOwner().getSurname();
                    map.put("clientName", resp);
                    if (estimate.getOrder().getVehicle().getOwner().getNif() != null) {
                        map.put("clientNif", estimate.getOrder().getVehicle().getOwner().getNif().getNumber());
                    }

                }
            }

            map.put("notes", estimate.getNotes());

            myColl.add(map);
        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);

        return dataSource;
    }

    private static JRDataSource createPartListDataSource(Estimate estimate) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (estimate != null) {
            List<PartLine> partList = estimate.getOrder().getPartsList();

            if (partList != null && !partList.isEmpty()) {
                for (PartLine p : partList) {
                    Map<String, Object> mapPart = new HashMap<String, Object>();
                    if (p.getPart() != null) {
                        mapPart.put("make", p.getPart().getMake());
                        mapPart.put("model", p.getPart().getModel());
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

    private static JRDataSource createEstimateLabourListDataSource(Estimate estimate) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (estimate != null) {
            List<LabourLine> labourList = estimate.getOrder().getLabourList();

            if (labourList != null && !labourList.isEmpty()) {
                for (LabourLine l : labourList) {
                    Map<String, Object> mapPart = new HashMap<String, Object>();
                    if (l.getLabour() != null) {
                        mapPart.put("name", l.getLabour().getName());
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
                    }
                    mapPart.put("hours", l.getHours());


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

    private static JRDataSource createEstimateListDataSource(List<Estimate> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Estimate estimate : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("estimateNumber", estimate.getEstimateNumber());
                if (estimate.getEstimateDate() != null) {
                    map.put("estimateDate", estimate.getEstimateDate().toString());
                }
                if (estimate.getAcceptedDate() != null) {
                    map.put("acceptedDate", estimate.getAcceptedDate().toString());
                }
                map.put("status", estimate.getStatus());
                if (estimate.getDiscount() != null) {
                    map.put("discount", estimate.getDiscount().toPlainString() + "%");
                }
                if (estimate.getResponsible() != null) {
                    String resp = estimate.getResponsible().getName();
                    if (estimate.getResponsible().getSurname() != null) {
                        resp += " " + estimate.getResponsible().getSurname();
                    }
                    map.put("estimateResponsible", resp);
                }

                if (estimate.getOrder().getVehicle() != null) {
                    Vehicle veh = estimate.getOrder().getVehicle();
                    String vehicleName = "";
                    if (veh.getMake() != null) {
                        vehicleName = veh.getMake();
                        if (veh.getModel() != null) {
                            vehicleName += " " + veh.getModel();
                        }
                    } else if (veh.getModel() != null) {
                        vehicleName = veh.getModel();
                    }
                    map.put("vehicle", vehicleName);
                    map.put("vehicleRegistration", veh.getRegistration());
                    if (veh.getOwner() != null) {
                        if (veh.getOwner() != null) {
                            String cli = veh.getOwner().getName();
                            if (veh.getOwner().getSurname() != null) {
                                cli += " " + veh.getOwner().getSurname();
                            }
                            map.put("client", cli);
                        }
                    }
                    if (veh.getOwner().getNif() != null) {
                        map.put("clientNif", veh.getOwner().getNif().getNumber());
                    }
                }
                map.put("notes", estimate.getNotes());
                myColl.add(map);
            }

        }
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(myColl);
        return dataSource;
    }

    public static JasperReportBuilder generateEstimateReport(Estimate estimate) {

        partTotal = new BigDecimal("0");
        labourTotal = new BigDecimal("0");

        JasperReportBuilder report = report();
        if (estimate != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            StyleBuilder shippingStyle = stl.style(es.regueiro.easyrepair.reports.Templates.boldStyle)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            SubreportBuilder partListSubreport = cmp.subreport(generatePartListReport(estimate).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder labourListSubreport = cmp.subreport(generateLabourListReport(estimate).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder totalSubreport = cmp.subreport(generateTotalReport(estimate).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATENO"), estimate.getEstimateNumber()),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(createCustomerComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENT"), estimate.getOrder().getVehicle())).heightFixedOnTop(),
                    cmp.hListCell(createEstimateComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATE"), estimate)).heightFixedOnTop()),
                    cmp.verticalGap(10),
                    cmp.verticalList(
                    partListSubreport,
                    cmp.verticalGap(10),
                    labourListSubreport,
                    cmp.verticalGap(10),
                    totalSubreport))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(estimate));
        }
        return report;

    }

    private static JasperReportBuilder generatePartListReport(Estimate estimate) {
        JasperReportBuilder report = report();
        if (estimate != null) {
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
                    .setDataSource(createPartListDataSource(estimate));
        }
        return report;
    }

    private static JasperReportBuilder generateTotalReport(Estimate estimate) {
        JasperReportBuilder report = report();
        if (estimate != null) {
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
                    .setDataSource(createEstimateDataSource(estimate));
        }
        return report;
    }

    private static JasperReportBuilder generateLabourListReport(Estimate estimate) {
        JasperReportBuilder report = report();
        if (estimate != null) {
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
                    .setDataSource(createEstimateLabourListDataSource(estimate));
        }
        return report;
    }

    public static JasperReportBuilder generateEstimateListReport(List<Estimate> list) {
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
            TextColumnBuilder<String> estimateNumberColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATENUMBER"), "estimateNumber", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> estimateDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDATE"), "estimateDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> acceptedDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), "acceptedDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> statusColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "status", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> discountColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DISCOUNT"), "discount", type.stringType())
                    .setFixedColumns(5)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> responsibleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), "estimateResponsible", type.stringType())
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
            TextColumnBuilder<String> vehicleRegistration = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("VEHICLEREGISTRATION"), type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);


            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATELIST"), ""))
                    .columns(
                    rowNumberColumn, estimateNumberColumn, estimateDateColumn, acceptedDateColumn, discountColumn, statusColumn, responsibleColumn, clientColumn, vehicleColumn, clientNifColumn, vehicleRegistration)
                    .columnGrid(
                    rowNumberColumn, estimateNumberColumn,
                    grid.horizontalColumnGridList()
                    .add(estimateDateColumn).newRow()
                    .add(acceptedDateColumn),
                    grid.horizontalColumnGridList()
                    .add(statusColumn).newRow()
                    .add(responsibleColumn),
                    grid.horizontalColumnGridList()
                    .add(clientColumn, vehicleColumn).newRow()
                    .add(clientNifColumn, vehicleRegistration))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createEstimateListDataSource(list));
        }
        return report;
    }

    private static ComponentBuilder<?, ?> createCustomerComponent(String label, Vehicle vehicle) {
        String name;
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
            addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REGISTRATION"), vehicle.getRegistration());
        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createEstimateComponent(String label, Estimate estimate) {
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (estimate != null) {
            if (estimate.getEstimateDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDATE"), estimate.getEstimateDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDATE"), "");
            }
            if (estimate.getAcceptedDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), estimate.getAcceptedDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ACCEPTEDDATE"), "");
            }
            addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), estimate.getStatus());
            if (estimate.getResponsible() != null) {
                String resp = estimate.getResponsible().getName();
                if (estimate.getResponsible().getSurname() != null) {
                    resp += " " + estimate.getResponsible().getSurname();
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
