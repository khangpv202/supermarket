package locator.khpv.com.supermarket.vegetable.model;

import java.util.List;

import locator.khpv.com.supermarket.common.TransferData;

/**
 * Created by Administrator on 4/19/2016.
 */
public class VegetableTransferModel implements TransferData<Vegetable> {
    Vegetable vegetable;

    @Override
    public void setTransferObject(Vegetable transferObject) {
        this.vegetable = transferObject;
    }

    @Override
    public Vegetable getTransferObject() {
        return vegetable;
    }
}
