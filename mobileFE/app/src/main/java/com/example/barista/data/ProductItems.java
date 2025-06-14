package com.example.barista.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductItems {
    public static final String ID = "productItems";
    private List<ProductItem> items;
    private final Map<String, ProductItem> itemMap;

    public ProductItems(List<ProductItem> items) {
        this.items = items;
        this.itemMap = new HashMap<>();

        for (ProductItem item : items) {
            itemMap.put(item.getId(), item);
        }

    }

    public List<ProductItem> getItems() {
        return items;
    }


    public void addItem(ProductItem item) {
        items.add(item);
        itemMap.put(item.getId(), item);
    }

    public void removeItem(ProductItem item) {
        items.remove(item);
        itemMap.remove(item.getId());
    }

    public ProductItem getItem(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public ProductItem getItemById(String id) {
        return itemMap.get(id);
    }
}
