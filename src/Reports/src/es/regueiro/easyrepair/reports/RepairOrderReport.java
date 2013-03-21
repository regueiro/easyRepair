package es.regueiro.easyrepair.reports;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.repair.LabourLine;
import es.regueiro.easyrepair.model.repair.RepairOrder;
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

public class RepairOrderReport {

    private static BigDecimal partTotal = new BigDecimal("0");
    private static BigDecimal labourTotal = new BigDecimal("0");
    private static AggregationSubtotalBuilder<BigDecimal> totalSum;

    private RepairOrderReport() {
    }

    private static JRDataSource createRepairOrderDataSource(RepairOrder repairOrder) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();

        if (repairOrder != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (repairOrder.getOrderNumber() != null) {
                map.put("orderNumber", repairOrder.getOrderNumber());
            } else {
                map.put("orderNumber", "");
            }
            if (repairOrder.getKilometres() != null) {
                map.put("kilometres", repairOrder.getKilometres());
            } else {
                map.put("kilometres", "");
            }
            if (repairOrder.getGasTankLevel() != null) {
                map.put("gasTankLevel", repairOrder.getGasTankLevel());
            } else {
                map.put("gasTankLevel", "");
            }
            if (repairOrder.getDescription() != null) {
                map.put("description", repairOrder.getDescription());
            } else {
                map.put("description", "");
            }
            if (repairOrder.getStatus() != null) {
                map.put("status", repairOrder.getStatus());
            } else {
                map.put("status", "");
            }
            if (repairOrder.getNotes() != null) {
                map.put("notes", repairOrder.getNotes());
            } else {
                map.put("notes", "");
            }
            if (repairOrder.getOrderDate() != null) {
                map.put("orderDate", repairOrder.getOrderDate().toString());
            } else {
                map.put("orderDate", "");
            }
            if (repairOrder.getEstimatedDate() != null) {
                map.put("estimatedDate", repairOrder.getEstimatedDate().toString());
            } else {
                map.put("estimatedDate", "");
            }
            if (repairOrder.getFinishDate() != null) {
                map.put("finishDate", repairOrder.getFinishDate().toString());
            } else {
                map.put("finishDate", "");
            }
            if (repairOrder.getDeliveryDate() != null) {
                map.put("deliveryDate", repairOrder.getDeliveryDate().toString());
            } else {
                map.put("deliveryDate", "");
            }
            if (repairOrder.getEstimate() != null && repairOrder.getEstimate().getDiscount() != null) {
                map.put("discount", repairOrder.getEstimate().getDiscount());
            } else {
                map.put("discount", new BigDecimal("0"));
            }
            if (repairOrder.getResponsible() != null) {
                String resp = "";
                if (repairOrder.getResponsible().getName() != null) {
                    resp += repairOrder.getResponsible().getName();
                }
                if (repairOrder.getResponsible().getSurname() != null) {
                    resp += " " + repairOrder.getResponsible().getSurname();
                }
                map.put("responsible", resp);
            }
            if (repairOrder != null && repairOrder.getVehicle() != null) {
                String veh = "";
                if (repairOrder.getVehicle().getMake() != null) {
                    veh += repairOrder.getVehicle().getMake();
                }
                if (repairOrder.getVehicle().getModel() != null) {
                    veh += " " + repairOrder.getVehicle().getModel();
                }
                map.put("vehicle", veh);
                if (repairOrder.getVehicle().getRegistration() != null) {
                    map.put("vehicleRegistration", repairOrder.getVehicle().getRegistration());
                } else {
                    map.put("vehicleRegistration", "");
                }

                if (repairOrder.getVehicle().getOwner() != null) {
                    String cli = "";
                    if (repairOrder.getVehicle().getOwner().getName() != null) {
                        cli += repairOrder.getVehicle().getOwner().getName();
                    }
                    if (repairOrder.getVehicle().getOwner().getSurname() != null) {
                        cli += " " + repairOrder.getVehicle().getOwner().getSurname();
                    }
                    map.put("clientName", cli);
                    if (repairOrder.getVehicle().getOwner().getNif() != null) {
                        map.put("clientNif", repairOrder.getVehicle().getOwner().getNif().getNumber());
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

    private static JRDataSource createPartListDataSource(RepairOrder repairOrder) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (repairOrder != null) {
            List<PartLine> partList = repairOrder.getPartsList();

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

    private static JRDataSource createRepairOrderLabourListDataSource(RepairOrder repairOrder) {

        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (repairOrder != null) {
            List<LabourLine> labourList = repairOrder.getLabourList();

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

    private static JRDataSource createRepairOrderListDataSource(List<RepairOrder> list) {
        Collection<Map<String, ?>> myColl = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (RepairOrder repairOrder : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (repairOrder.getOrderNumber() != null) {
                    map.put("orderNumber", repairOrder.getOrderNumber());
                } else {
                    map.put("orderNumber", repairOrder.getOrderNumber());
                }
                if (repairOrder.getKilometres() != null) {
                    map.put("kilometres", repairOrder.getKilometres());
                } else {
                    map.put("kilometres", repairOrder.getKilometres());
                }
                if (repairOrder.getGasTankLevel() != null) {
                    map.put("gasTankLevel", repairOrder.getGasTankLevel());
                } else {
                    map.put("gasTankLevel", repairOrder.getGasTankLevel());
                }
                if (repairOrder.getDescription() != null) {
                    map.put("description", repairOrder.getDescription());
                } else {
                    map.put("description", repairOrder.getDescription());
                }
                if (repairOrder.getStatus() != null) {
                    map.put("status", repairOrder.getStatus());
                } else {
                    map.put("status", repairOrder.getStatus());
                }
                if (repairOrder.getNotes() != null) {
                    map.put("notes", repairOrder.getNotes());
                } else {
                    map.put("notes", repairOrder.getNotes());
                }
                if (repairOrder.getOrderDate() != null) {
                    map.put("orderDate", repairOrder.getOrderDate().toString());
                } else {
                    map.put("orderDate", "");
                }
                if (repairOrder.getEstimatedDate() != null) {
                    map.put("estimatedDate", repairOrder.getEstimatedDate().toString());
                } else {
                    map.put("estimatedDate", "");
                }
                if (repairOrder.getFinishDate() != null) {
                    map.put("finishDate", repairOrder.getFinishDate().toString());
                } else {
                    map.put("finishDate", "");
                }
                if (repairOrder.getDeliveryDate() != null) {
                    map.put("deliveryDate", repairOrder.getDeliveryDate().toString());
                } else {
                    map.put("deliveryDate", "");
                }
                if (repairOrder.getEstimate() != null && repairOrder.getEstimate().getDiscount() != null) {
                    map.put("discount", repairOrder.getEstimate().getDiscount());
                } else {
                    map.put("discount", new BigDecimal("0"));
                }
                if (repairOrder.getResponsible() != null) {
                    String resp = "";
                    if (repairOrder.getResponsible().getName() != null) {
                        resp += repairOrder.getResponsible().getName();
                    }
                    if (repairOrder.getResponsible().getSurname() != null) {
                        resp += " " + repairOrder.getResponsible().getSurname();
                    }
                    map.put("responsible", resp);
                }
                if (repairOrder != null && repairOrder.getVehicle() != null) {
                    String veh = "";
                    if (repairOrder.getVehicle().getMake() != null) {
                        veh += repairOrder.getVehicle().getMake();
                    }
                    if (repairOrder.getVehicle().getModel() != null) {
                        veh += " " + repairOrder.getVehicle().getModel();
                    }
                    map.put("vehicle", veh);
                    if (repairOrder.getVehicle().getRegistration() != null) {
                        map.put("vehicleRegistration", repairOrder.getVehicle().getRegistration());
                    } else {
                        map.put("vehicleRegistration", "");
                    }

                    if (repairOrder.getVehicle().getOwner() != null) {
                        String cli = "";
                        if (repairOrder.getVehicle().getOwner().getName() != null) {
                            cli += repairOrder.getVehicle().getOwner().getName();
                        }
                        if (repairOrder.getVehicle().getOwner().getSurname() != null) {
                            cli += " " + repairOrder.getVehicle().getOwner().getSurname();
                        }
                        map.put("clientName", cli);
                        if (repairOrder.getVehicle().getOwner().getNif() != null) {
                            map.put("clientNif", repairOrder.getVehicle().getOwner().getNif().getNumber());
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

    public static JasperReportBuilder generateRepairOrderReport(RepairOrder repairOrder) {

        partTotal = new BigDecimal("0");
        labourTotal = new BigDecimal("0");

        JasperReportBuilder report = report();
        if (repairOrder != null) {
            //init styles  
            StyleBuilder columnStyle = stl.style(es.regueiro.easyrepair.reports.Templates.columnStyle)
                    .setBorder(stl.pen1Point());
            StyleBuilder subtotalStyle = stl.style(columnStyle)
                    .bold();
            StyleBuilder shippingStyle = stl.style(es.regueiro.easyrepair.reports.Templates.boldStyle)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            SubreportBuilder partListSubreport = cmp.subreport(generatePartListReport(repairOrder).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder labourListSubreport = cmp.subreport(generateLabourListReport(repairOrder).setTextStyle(stl.style().setFontSize(8)));
            SubreportBuilder totalSubreport = cmp.subreport(generateTotalReport(repairOrder).setTextStyle(stl.style().setFontSize(8)));

            //configure report  
            report
                    .setTemplate(es.regueiro.easyrepair.reports.Templates.reportTemplate)
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(subtotalStyle) //columns  
                    //band components 
                    .title(
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERNO"), repairOrder.getOrderNumber()),
                    cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                    cmp.hListCell(
                    cmp.verticalList(
                    createCustomerComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("CLIENT"), repairOrder.getVehicle()),
                    createOrderDescriptionComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DESCRIPTION"), repairOrder))).heightFixedOnTop(),
                    cmp.hListCell(createRepairOrderComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDER"), repairOrder)).heightFixedOnTop()),
                    cmp.verticalGap(10),
                    cmp.verticalList(
                    partListSubreport,
                    cmp.verticalGap(10),
                    labourListSubreport,
                    cmp.verticalGap(10),
                    totalSubreport,
                    cmp.verticalGap(10),
                    createNotesComponent(repairOrder)))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createPartListDataSource(repairOrder));
        }
        return report;

    }

    private static JasperReportBuilder generatePartListReport(RepairOrder repairOrder) {
        JasperReportBuilder report = report();
        if (repairOrder != null) {
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
                    .setDataSource(createPartListDataSource(repairOrder));
        }
        return report;
    }

    private static JasperReportBuilder generateTotalReport(RepairOrder repairOrder) {
        JasperReportBuilder report = report();
        if (repairOrder != null) {
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
                    .setDataSource(createRepairOrderDataSource(repairOrder));
        }
        return report;
    }

    private static JasperReportBuilder generateLabourListReport(RepairOrder repairOrder) {
        JasperReportBuilder report = report();
        if (repairOrder != null) {
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
                    .setDataSource(createRepairOrderLabourListDataSource(repairOrder));
        }
        return report;
    }

    public static JasperReportBuilder generateRepairOrderListReport(List<RepairOrder> list) {
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
            TextColumnBuilder<String> orderNumberColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERNUMBER"), "orderNumber", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> orderDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERDATE"), "orderDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> estimatedDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), "estimatedDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> finishDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FINISHDATE"), "finishDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> deliveryDateColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DELIVERYDATE"), "deliveryDate", type.stringType())
                    .setFixedWidth(75)
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> descriptionColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DESCRIPTION"), "description", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> kilometresColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("KILOMETRES"), "kilometres", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> gasTankLevelColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("GASTANKLVL"), "gasTankLevel", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> statusColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "status", type.stringType())
                    .setHeight(30)
                    .setTitleFixedRows(2);
            TextColumnBuilder<String> responsibleColumn = col.column(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("RESPONSIBLE"), "repairOrderResponsible", type.stringType())
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
                    es.regueiro.easyrepair.reports.Templates.createTitleComponent(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERLIST"), ""))
                    .columns(
                    rowNumberColumn, orderNumberColumn, descriptionColumn, orderDateColumn, kilometresColumn, gasTankLevelColumn, estimatedDateColumn, finishDateColumn, deliveryDateColumn, statusColumn, responsibleColumn, clientColumn, vehicleColumn, clientNifColumn, vehicleRegistration)
                    .columnGrid(
                    rowNumberColumn, orderNumberColumn, descriptionColumn,
                    grid.horizontalColumnGridList()
                    .add(orderDateColumn).newRow()
                    .add(estimatedDateColumn),
                    grid.horizontalColumnGridList()
                    .add(finishDateColumn).newRow()
                    .add(deliveryDateColumn),
                    grid.horizontalColumnGridList()
                    .add(kilometresColumn).newRow()
                    .add(gasTankLevelColumn),
                    grid.horizontalColumnGridList()
                    .add(statusColumn).newRow()
                    .add(responsibleColumn),
                    grid.horizontalColumnGridList()
                    .add(clientColumn, vehicleColumn).newRow()
                    .add(clientNifColumn, vehicleRegistration))
                    .pageFooter(
                    es.regueiro.easyrepair.reports.Templates.footerComponent)
                    .setDataSource(createRepairOrderListDataSource(list));
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

    private static ComponentBuilder<?, ?> createOrderDescriptionComponent(String label, RepairOrder repairOrder) {
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (repairOrder != null) {
            if (repairOrder.getDescription() != null) {
                list.add(cmp.text(repairOrder.getDescription()));
            } else {
                list.add(cmp.text(""));
            }
        }
        return cmp.verticalList(
                cmp.text(label).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                list);
    }

    private static ComponentBuilder<?, ?> createRepairOrderComponent(String label, RepairOrder repairOrder) {
        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        if (repairOrder != null) {
            if (repairOrder.getOrderDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERDATE"), repairOrder.getOrderDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("REPAIRORDERDATE"), "");
            }
            if (repairOrder.getEstimatedDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), repairOrder.getEstimatedDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("ESTIMATEDDATE"), "");
            }
            if (repairOrder.getFinishDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FINISHDATE"), repairOrder.getFinishDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("FINISHDATE"), "");
            }
            if (repairOrder.getDeliveryDate() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DELIVERYDATE"), repairOrder.getDeliveryDate().toString());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("DELIVERYDATE"), "");
            }
            if (repairOrder.getKilometres() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("KILOMETRES"), repairOrder.getKilometres());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("KILOMETRES"), "");
            }
            if (repairOrder.getGasTankLevel() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("GASTANKLEVEL"), repairOrder.getGasTankLevel());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("GASTANKLEVEL"), "");
            }
            if (repairOrder.getStatus() != null) {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), repairOrder.getStatus());
            } else {
                addAttribute(list, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("STATUS"), "");
            }
            if (repairOrder.getResponsible() != null) {
                String resp = "";
                if (repairOrder.getResponsible().getName() != null) {
                    resp += repairOrder.getResponsible().getName();
                }
                if (repairOrder.getResponsible().getSurname() != null) {
                    resp += " " + repairOrder.getResponsible().getSurname();
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

    private static ComponentBuilder<?, ?> createNotesComponent(RepairOrder repairOrder) {
        if (repairOrder != null && repairOrder.getNotes() != null && !repairOrder.getNotes().isEmpty()) {
            return cmp.verticalList(
                    cmp.text(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/reports/Bundle").getString("NOTESLABEL")).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle),
                    cmp.text(repairOrder.getNotes()),
                    cmp.verticalGap(10));
        }
        return cmp.filler();
    }

    private static void addAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + ":").setFixedColumns(12).setStyle(es.regueiro.easyrepair.reports.Templates.boldStyle), cmp.text(value)).newRow();
        }
    }
    
}
