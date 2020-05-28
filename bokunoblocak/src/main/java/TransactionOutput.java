import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;

public class TransactionOutput {
    public String id;
    public PublicKey reciepient; //接受者（买家）
    public HashMap cart; //货
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, HashMap cart, String parentTransactionId) {
        this.reciepient = reciepient;
        this.cart = cart;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+cart+parentTransactionId);
    }

    //Check if  belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

}