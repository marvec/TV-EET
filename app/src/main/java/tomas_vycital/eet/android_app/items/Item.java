package tomas_vycital.eet.android_app.items;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import tomas_vycital.eet.android_app.VAT;

public class Item implements Comparable<Item> {
    public static final DecimalFormat priceFormat = new DecimalFormat("0.00");

    private final int price;
    private final String name;
    private final VAT vat;
    private final String category;

    Item(String name, int price, VAT vat, String category) {
        this.name = name;
        this.price = price;
        this.vat = vat;
        this.category = category;
    }

    Item(String name, String priceStr, VAT vat, String category) {
        String[] priceParts = priceStr.replaceAll("[^\\d,.]", "").split("[,.]");
        int price = Integer.valueOf(priceParts[0]) * 100;
        if (priceParts.length > 1) {
            switch (priceParts[1].length()) {
                case 0:
                    priceParts[1] = "0";
                    break;
                case 1:
                    priceParts[1] += "0";
                    break;
                case 2:
                    break;
                default:
                    priceParts[1] = priceParts[1].substring(0, 2);
            }
            price += Integer.valueOf(priceParts[1]);
        }

        this.name = name;
        this.price = price;
        this.vat = vat;
        this.category = category;
    }

    public Item(JSONObject object) throws JSONException {
        this.name = (String) object.get("name");
        this.price = (int) object.get("price");
        this.vat = VAT.fromID((Integer) object.get("VAT"));
        this.category = (String) object.get("category");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("price", this.price);
        json.put("VAT", this.vat.getID());
        json.put("category", this.category);
        return json;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    String getPriceStr() {
        return Item.priceFormat.format(this.price / 100.0) + " kč";
    }

    public String getPriceRawStr() {
        return Item.priceFormat.format(this.price / 100.0);
    }

    public String getPriceRawStr(int amount) {
        return Item.priceFormat.format(amount * this.price / 100.0);
    }

    public VAT getVAT() {
        return this.vat;
    }

    public int getVATH() {
        return (int) (this.price * this.vat.get());
    }

    public int getVATPercentage() {
        return this.vat.getPercentage();
    }

    String getBrief() {
        return this.name + " (" + this.price / 100f + ",-)";
    }

    @Override
    public int compareTo(@NonNull Item another) {
        return this.name.compareTo(another.name);
    }

    String getCategory() {
        return this.category;
    }
}
