import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction {

    public String transactionId;
    public PublicKey sender;
    public PublicKey reciepient;
    public HashMap cart;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; //A rough count of how many transactions have been generated

    // Constructor:
    public Transaction(PublicKey seller, PublicKey buyer, HashMap cart, ArrayList<TransactionInput> inputs) {
        this.sender = seller;
        this.reciepient = buyer;
        this.cart = cart;
        this.inputs = inputs;
    }
    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        cart + sequence
        );
    }


    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + cart	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + cart	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }




    //Returns true if new transaction could be created.
    public boolean processTransaction() {

        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.UTXO = TestChain.UTXOs.get(i.transactionOutputId);
        }


        //generate transaction outputs:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, cart,transactionId)); //send value to recipient


        //add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            TestChain.UTXOs.put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            TestChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public HashMap getInputsValue() {
        HashMap carts = null;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            carts = i.UTXO.cart;
        }
        return carts;
    }

    //returns sum of outputs:
    public HashMap getOutputsValue() {
        HashMap carts = null;
        for(TransactionOutput o : outputs) {
            carts = o.cart;
        }
        return carts;
    }

}

