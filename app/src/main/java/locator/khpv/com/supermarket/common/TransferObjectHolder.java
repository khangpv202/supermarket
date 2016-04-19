package locator.khpv.com.supermarket.common;

/**
 * Created by Administrator on 4/19/2016.
 */
public class TransferObjectHolder<T> {

    private static TransferObjectHolder instance;
    private T transferData;

    public static TransferObjectHolder getInstance() {
        if (instance == null)
            instance = new TransferObjectHolder();
        return instance;
    }

    public T getTransferData() {
        return transferData;
    }

    public void setTransferData(T transferData) {
        this.transferData = transferData;
    }
}
