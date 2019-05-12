public class Transaction {
	 private String sender;
	 private String receiver;
	 private int amount;

	 // Transaction constructor that contains information of sender, receiver and amount 
	 public Transaction (String sender, String receiver, int amount){
	 	this.sender = sender;
	 	this.receiver = receiver; 
	 	this.amount = amount; 

	 }
	 public String toString() {
	 	return sender + ":" + receiver + "=" + amount;
	}
	// getter for sender
	public String getSender(){
		return this.sender; 
	}
	// getter for receiver
	public String getReceiver(){
		return this.receiver; 
	}
	// getter for amount
	public int getAmount(){
		return this.amount; 
	}

}