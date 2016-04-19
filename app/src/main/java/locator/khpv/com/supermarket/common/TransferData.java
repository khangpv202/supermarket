package locator.khpv.com.supermarket.common;

/**
 * Created by Administrator on 4/19/2016.
 */
public interface TransferData<T> {
    void setTransferObject(T transferObject);

    T getTransferObject();
}
