package es.regueiro.easyrepair.reports;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.StretchType;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.NbPreferences;

public class ShopReport {

    private ShopReport() {
    }

    public static HorizontalListBuilder generateShopData() {
        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder shopNameStyle = stl.style(boldStyle).setFontSize(14);

        HorizontalListBuilder shopData = cmp.horizontalList();

        if (!StringUtils.isEmpty(NbPreferences.root().get("logo", ""))) {
            Image im = loadAndResizeImage(NbPreferences.root().get("logo", ""));
            if (im != null) {
                shopData.add(cmp.image(im));
            }
        }

        VerticalListBuilder shopText = cmp.verticalList();
        if (!NbPreferences.root().get("shopName", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("shopName", "")).setStyle(shopNameStyle).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        if (!NbPreferences.root().get("address1", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("address1", "")).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        if (!NbPreferences.root().get("address2", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("address2", "")).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        if (!NbPreferences.root().get("address3", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("address3", "")).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        if (!NbPreferences.root().get("phone", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("phone", "")).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        if (!NbPreferences.root().get("email", "").equals("")) {
            shopText.add(cmp.text(NbPreferences.root().get("email", "")).setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
        shopText.setStretchType(StretchType.NO_STRETCH);
        shopData.add(shopText);
        return shopData;
    }
    
    private static Image loadAndResizeImage(String path) {
        Image img = null;
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(path));
            float width = bimg.getWidth();
            float height = bimg.getHeight();
            if (width > 256 || height > 256) {
                if (width > height) {
                    float proportion = width / 256;
                    img = bimg.getScaledInstance(256, Math.round(height / proportion), Image.SCALE_SMOOTH);
                } else {
                    float proportion = height / 256;
                    img = bimg.getScaledInstance(Math.round(width / proportion), 256, Image.SCALE_SMOOTH);
                }
            } else {
                img = bimg;
            }
        } catch (IOException e) {
        }
        return img;
    }
}
