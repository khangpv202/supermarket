package locator.khpv.com.supermarket.vegetable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/15/2016.
 */
public class Vegetable {
    String calo;
    String cost;
    String mainImageID;
    String displayName;
    String displayMenu;
    @JsonIgnore
    List<Menu> menu = new ArrayList<>();

    public Vegetable() {
    }

    public String getMainImageID()
    {
        return mainImageID;
    }

    public void setMainImageID(String mainImageID)
    {
        this.mainImageID = mainImageID;
    }

    public String getCalo() {
        return calo;
    }

    public void setCalo(String calo) {
        this.calo = calo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public String getDisplayMenu()
    {
        return displayMenu;
    }

    public void setDisplayMenu(String displayMenu)
    {
        this.displayMenu = displayMenu;
    }
}