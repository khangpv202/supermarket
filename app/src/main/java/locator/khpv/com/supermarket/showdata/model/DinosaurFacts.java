package locator.khpv.com.supermarket.showdata.model;

/**
 * Created by Administrator on 4/15/2016.
 */
public class DinosaurFacts {
    long height;
    double length;
    long weight;
    String vanished;
    String appeared;
    String order;

    public DinosaurFacts() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public long getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public long getWeight() {
        return weight;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public String getVanished() {
        return vanished;
    }

    public void setVanished(String vanished) {
        this.vanished = vanished;
    }

    public String getAppeared() {
        return appeared;
    }

    public void setAppeared(String appeared) {
        this.appeared = appeared;
    }

    @Override
    public String toString() {
        return "DinosaurFacts{" +
                "height=" + height +
                ", length=" + length +
                ", weight=" + weight +
                ", vanished='" + vanished + '\'' +
                ", appeared='" + appeared + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}