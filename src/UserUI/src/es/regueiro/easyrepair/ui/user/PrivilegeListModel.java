package es.regueiro.easyrepair.ui.user;

import java.util.*;
import javax.swing.AbstractListModel;

class PrivilegeListModel extends AbstractListModel {

    List<Object> model;

    public PrivilegeListModel() {
        model = new ArrayList<Object>();
    }

    @Override
    public int getSize() {
        return model.size();
    }

    @Override
    public Object getElementAt(int index) {
        return model.toArray()[index];
    }

    public void add(Object element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void addAll(Object elements[]) {
        Collection<Object> c = Arrays.asList(elements);
        model.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(Object element) {
        return model.contains(element);
    }

    public Iterator iterator() {
        return model.iterator();
    }

    public boolean removeElement(Object element) {
        boolean removed = model.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }

    public int getElementIndex(Object element) {
        int i = 0;
        for (Object o : model) {
            if (!o.equals(element)) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }
}
