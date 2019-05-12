public class PeerValidation {

	public static void main (String[] args){
		//creating an instance of a blockchain
		BlockChain check = new BlockChain();

		//opening the file of the classmate to create a blockchain
		check = check.fromFile("blockchain_sgham022.txt");  

		//checking if the classmate's blockchain is valid
		if(check.validateBlockchain()){
			//if valid block is added to a verified file
			check.toFile("blockchain_sgham022_wzafa074.txt"); 
		}
		else{
			//if not valid, the system prints "invalid"
			System.out.println("invalid");
		}

	}
	
}