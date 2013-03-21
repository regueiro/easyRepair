package es.regueiro.easyrepair.ui.stock;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MaxLengthTextDocument extends PlainDocument {

    private int maxChars;

    public MaxLengthTextDocument(int maxChars) {
        super();
        this.maxChars = maxChars;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str != null && (getLength() + str.length() <= maxChars)) {
            super.insertString(offs, str, a);
        }
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }
    //getter e setter omitted
}
