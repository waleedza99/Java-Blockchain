import java.io.*; 
import java.lang.*; 
import java.util.*; 
import java.sql.Timestamp;

public class BlockChain{

	private ArrayList<Block> blocklist;  
	private static Scanner x; 

	public BlockChain(){
		blocklist = new ArrayList<Block>();

	}

	/**
     * This method adds a block passed as a paramter to the array list of blocks. 
     * 
     * @param block to be added
     */

	public void addBlock(Block block){
		blocklist.add(block); 

	}

	/**
     * This method creates a block from the given file which must be passed as a parameter to this method.
     * This method reads the file and produces a block for every transaction accordingly. 
     * This method converts all components of a block to their corresponding data type. 
     * 
     * @param file name to be open, read and closed. 
     * @return BlockChain created after reading the file.
     */

	public static BlockChain fromFile(String fileName){

		String temp = "00000"; // initializing the first block's previous has to be 00000 first. 
		BlockChain blockChain = new BlockChain(); 
		Block block;
		Transaction transaction; 

		try{
			x = new Scanner(new File(fileName));
		}
		catch(Exception e){
			System.out.println("could not find file");
		} 

		while(x.hasNext()){

			String previousHash = temp; 
			int index = Integer.parseInt(x.next());
			Long date = Long.parseLong(x.next()); 
			Timestamp timestamp = new Timestamp(date); 
			String sender = x.next();
			String receiver = x.next();
			int amount = Integer.parseInt(x.next());
			String nonce = x.next();
			String hash = x.next();
			temp = hash; 

			transaction = new Transaction (sender, receiver, amount); 
			block = new Block (index, timestamp, transaction, nonce, hash, previousHash); 
			blockChain.addBlock(block); 

		}

		x.close();
		return blockChain; 

	}

	/**
     * This method iterates the blocklist (blockchain) and adds every component of a block 
     * on a new line in a file.  
     * 
     * @param file name to be created as.  
     */

	public void toFile(String fileName){

		File file = new File(fileName);

		try{
			file.createNewFile();
			PrintWriter j = new PrintWriter(file);

			for(int i=0; i<blocklist.size(); i++){

				j.println(i); 
				j.println(blocklist.get(i).getTimestamp().getTime());
				j.println(blocklist.get(i).getTransaction().getSender());
				j.println(blocklist.get(i).getTransaction().getReceiver());
				j.println(Integer.toString(blocklist.get(i).getTransaction().getAmount()));
				j.println(blocklist.get(i).getNonce());
				j.println(blocklist.get(i).getHash());
			}

		j.close(); 

		}
		catch(IOException e){
			System.out.println("caught an error");
		}

	}

	/**
     * This method iterates through the entire block chain to find out the
     * the current balance of a user by adding all amounts received and subtracting
     * all amounts sent
     * 
     * @param username of the user who's balance is to be found 
     * @return balance of the user as an integer
     */

	public int getBalance(String username){
		int balance=0; 

		for(int i=0; i<blocklist.size(); i++){

			if(blocklist.get(i).getTransaction().getSender().equals(username) && 
				!(blocklist.get(i).getTransaction().getReceiver().equals(username))){

				balance -= blocklist.get(i).getTransaction().getAmount();	// subtracting money this user sent
			}
			else if (blocklist.get(i).getTransaction().getReceiver().equals(username) &&
				!(blocklist.get(i).getTransaction().getSender().equals(username))){

				balance += blocklist.get(i).getTransaction().getAmount();; // adding money this user received
			}

		}
		return balance; 
	}

	/**
     * This method iterates through the entire blockchain and performs a series 
     * of tests through if statements to find if the blockchain is valid or not.
     * 
     * @return true if blockchain is valid and false if invalid.
     */

	public boolean validateBlockchain(){

		if(blocklist.size() == 0 || blocklist.size() < 3){
			System.out.println("1");
			return false; 
		}

		for(int i=0; i<blocklist.size(); i++){

			try{

				if(!(Sha1.hash(blocklist.get(i).toString()).substring(0,5).equals("00000"))){ //checking if very hash in the blockchain starts with 00000
					return false;

				}

				if(!(blocklist.get(i).getHash().equals(Sha1.hash(blocklist.get(i).toString())))){ //checking if the hash of every block is valid
					return false; 
				}

			}

			catch(Exception e){
				System.out.println("Invalid Blockchain");

			}

				if(i != blocklist.get(i).getIndex()){ //checking if the index of every block is matching 
					return false;
				}

				if(i==0 && !(blocklist.get(i).getPreviousHash().equals("00000"))){ //checking if the first block's previous hash equals 00000
					return false;

				}

				if (i!=0 && !(blocklist.get(i).getPreviousHash().equals(blocklist.get(i-1).getHash()))){ //checking if the currents block's previous hash matches the hash of the previous block
					return false; 

				}
			}


		return true; 
	}

	
	public static void main(String [] args){

		System.out.println("******************* Welcome to WZ Blockchain Creater *******************" + "\n");
		Timestamp  timestamp;
		ArrayList<Integer> numbOfTrials = new ArrayList<Integer>(); //created an array to store all the trials of nonce generation
		int index = 2; //index starts at 2 because 3 blocks have already been added
		boolean validationResult;

		BlockChain blockchain = new BlockChain(); 
		blockchain = blockchain.fromFile("bitcoinBank.txt"); //adding the three given blocks
		validationResult = blockchain.validateBlockchain();  //validating the blockchain

		Scanner input = new Scanner(System.in);
		System.out.print("Would you like to make a transaction (please enter yes or no): "); //prompting the user for a transaction
		String decision = input.nextLine();
		while(!(decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("NO"))){ //blocking the user from entering nonsensical values
			input = new Scanner(System.in);
			System.out.print("Please enter yes or no only: "); //asking the user to enter an input again
			decision = input.nextLine();
		}



		while(decision.toUpperCase().equals("YES") && validationResult == true){

			input = new Scanner(System.in);
			System.out.print("Please enter the name of the sender: "); //asking the user to enter the name of the sender
			String sender = input.nextLine();

			input = new Scanner(System.in);
			System.out.print("Please enter the name of the receiver: "); //asking the user to enter the name of the receiver
			String receiver = input.nextLine();

			input = new Scanner(System.in);
			System.out.print("Please enter the amount: ");	//asking the user to enter the amount
			int amount = Integer.parseInt(input.nextLine());

			if(blockchain.getBalance(sender) >= amount){ //verifying if the sender has enough money to send
				Transaction transaction = new Transaction(sender, receiver, amount);
				timestamp = new Timestamp(System.currentTimeMillis());
				System.out.print("Mining........");
				String previousHash = blockchain.blocklist.get(index).getHash();
				index++;
				Block block = new Block(index, timestamp, transaction, previousHash);
				blockchain.addBlock(block);
				numbOfTrials.add(blockchain.blocklist.get(index).getTrials()); //adding the trials of nonce generation of this block to an array

			}

			else{
				System.out.println("The sender's balance is below the amount being sent. Cannot create a transaction!!"); // informing user that the sender's balance is below the amount being sent
				input = new Scanner(System.in);
				System.out.print("Would like to try again (please enter yes or no): "); //prompting the user for a transaction
				decision = input.nextLine();

				while(!(decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("NO"))){ //blocking the user from entering nonsensical values
					input = new Scanner(System.in);
					System.out.print("Please enter yes or no only: "); //asking the user to enter an input again
					decision = input.nextLine();
				}

				if(!(decision.toUpperCase().equals("YES"))){ //breaking out of the loop if the user decides not to make another transaction
					break; 
				}
			}

			if(!(blockchain.validateBlockchain())){ //verifying if the blockchain is still valid after adding a block
				System.out.println("Blockchain has become invalid!!");
				break;
			}
	
			else if((blockchain.blocklist.size() - 1) == index){
				System.out.println("Success: transaction has been made!!!"); //informing the user that the transaction has been made
				input = new Scanner(System.in);
				System.out.print("Would like to make another transaction (please enter yes or no): ");
				decision = input.nextLine();

				while(!(decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("NO"))){ //blocking the user from entering nonsensical values
					input = new Scanner(System.in);
					System.out.print("Please enter yes or no only: "); //asking the user to enter an input again
					decision = input.nextLine();
				}

				if(!(decision.toUpperCase().equals("YES"))){ //breaking out of the loop if the user decides not to make any more transactions
					break; 
				}
			}

		}

		if(index == 2){
			System.out.println("Thank you for participating! The program has been terminated."); // ending the program here if the user decided not to make any transactions (i.e. did not add any blocks)
		} 
		else{
			System.out.println("The following array represents the number of trials for creating a nonce for each block you added: ");
			System.out.println(numbOfTrials + "\n"); //displaying the the trials for nonce generation for every block in an array
			blockchain.toFile("blockchain_wzafa074.txt"); //saving the blockchain to a text file
			System.out.println("Thank you for participating! Your blockchain has been created!!"); // terminating the block
		}
		

	}
}