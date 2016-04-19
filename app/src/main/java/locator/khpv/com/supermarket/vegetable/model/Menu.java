package locator.khpv.com.supermarket.vegetable.model;

/**
 * Created by Administrator on 4/18/2016.
 */
public class Menu {
    private String displayName;
    private String howTo;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHowTo() {
        return howTo;
    }

    public void setHowTo(String howTo) {
        this.howTo = howTo;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "displayName='" + displayName + '\'' +
                ", howTo='" + howTo + '\'' +
                '}';
    }
}
