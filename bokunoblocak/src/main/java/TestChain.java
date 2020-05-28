import java.io.File;
import java.io.FileOutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;


import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;

public class TestChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static Cart buyer;
    public static Cart seller1;
    public static Cart seller2;
    public static Cart seller3;
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>(); //list of all unspent transactions.

    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Create the new wallets
        buyer = new Cart();
        seller1 = new Cart();
        seller2 = new Cart();
        seller3 = new Cart();

        HashMap<Integer, String> items = new HashMap<>();
        items.put(1, "item1");
        items.put(2, "item2");
        items.put(3, "item3");


        Cart cartdase = new Cart();


        genesisTransaction = new Transaction(cartdase.publicKey, seller1.publicKey, items, null);
        genesisTransaction.generateSignature(cartdase.privateKey);     //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.cart, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nseller1's left goods is : " + seller1.getBalance());
        System.out.println("\nseller1's is send following goods to buyer");
        block1.addTransaction(seller1.sendFunds(buyer.publicKey, items));
        addBlock(block1);
        System.out.println("\nseller1's left goods is : " + seller1.getBalance());
        System.out.println("buyer's left goods is: " + buyer.getBalance());
        System.out.println("seller2's left goods is: " + seller2.getBalance());
        System.out.println("seller3's left goods is: " + seller3.getBalance());


        Block block2 = new Block(genesis.hash);
        block2.addTransaction(buyer.sendFunds(seller3.publicKey, items));
        addBlock(block2);
        System.out.println("\nseller1's left goods is : " + seller1.getBalance());
        System.out.println("buyer's left goods is: " + buyer.getBalance());
        System.out.println("seller2's left goods is: " + seller2.getBalance());
        System.out.println("seller3's left goods is: " + seller3.getBalance());


        System.out.println("\nseller1's left goods is : " + seller1.getBalance());
        System.out.println("buyer's left goods is: " + buyer.getBalance());
        System.out.println("seller2's left goods is: " + seller2.getBalance());
        System.out.println("seller3's left goods is: " + seller3.getBalance());


        System.out.println("block的nonce " + block1.nonce);
        System.out.println("block的timeStamp " + block1.timeStamp);

//        JSONObject.fromObject(blockchain);

//        writeTxt("test.txt",json);

//

    }

    public static void writeTxt(String file_name, String content) {
        FileOutputStream fileOutputStream = null;
        File file = new File("D:\\作业\\大三\\BCD\\" + file_name);
        try {
            if (file.exists()) {
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isChainValid() {
      /*  Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }


                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");*/
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}




























/*

public class TestChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public static int difficulty = 3;
    public static float minimumTransaction = 0.1f;
    public static Cart Buyer;
    public static Cart Seller;
//    public static Cart walletB;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        Buyer = new Cart();
        Seller = new Cart();
//        walletB = new Cart();
        Cart coinbase = new Cart();

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, Seller.publicKey, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.cart, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);
        System.out.println("previousHash is :"+null);
        //testing
        Block block1 = new Block(genesis.hash);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
//        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        HashMap<String,Double> w = new HashMap<>();
        w.put("water",133.0);
        w.put("shit",152.4);

        block1.addTransaction(Seller.sendFunds(Buyer.publicKey,w));
        addBlock(block1);

//        System.out.println("cart A is: " + Cart_A.getItem());

        System.out.println("previousHash is :"+block1.previousHash);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);


        HashMap<String,Double> p = new HashMap<>();
        p.put("water",1233.0);
        p.put("shit",1522.4);

//        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));

        block2.addTransaction(Seller.sendFunds(Buyer.publicKey,p));
        addBlock(block2);

        System.out.println(block2.transactions.get(0));
        System.out.println("previousHash is :"+block2.previousHash);


        Block block3 = new Block(block2.hash);
        addBlock(block3);
        System.out.println("previousHash is :"+block3.previousHash);


//        Block block2 = new Block(block1.hash);
//        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
//        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
//        addBlock(block2);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        Block block3 = new Block(block2.hash);
//        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
//        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();

    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }
            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);
                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
//                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
//                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
//                    return false;
//                }

//                for(TransactionInput input: currentTransaction.inputs) {
//                    tempOutput = tempUTXOs.get(input.transactionOutputId);
//                    if(tempOutput == null) {
//                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
//                        return false;
//                    }

//                    if(input.UTXO.value != tempOutput.value) {
//                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
//                        return false;
//                    }
//                    tempUTXOs.remove(input.transactionOutputId);
                }
//                for(TransactionOutput output: currentTransaction.outputs) {
//                    tempUTXOs.put(output.id, output);
//                }
//                if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
//                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
//                    return false;
//                }
//                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
//                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
//                    return false;
//                }

//            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
*/
