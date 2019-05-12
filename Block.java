import java.io.*; 
import java.util.*;
import java.util.Random;
import java.sql.Timestamp;


public class Block {


	 private int index; // the index of the block in the list
	 private java.sql.Timestamp timestamp; // time at which transaction has been processed
	 private Transaction transaction; // the transaction object
	 private String nonce; // random string (for proof of work)
	 private String previousHash; // previous hash (set to "" in first block)(in first block, set to string of zeroes of size of complexity "00000")
	 private String hash; // hash of the block (hash of string obtained from previous variables via toString() method)
	 private int trials;

	 //This is a constructor which has been made for all the given three blocks that are added to the block chain
	 public Block(int index, java.sql.Timestamp timestamp, Transaction transaction, String nonce, String hash, String previousHash){
	 	this.index = index; 
	 	this.timestamp = timestamp; 
	 	this.transaction = transaction; 
	 	this.nonce = nonce; 
	 	this.previousHash = previousHash; 
	 	this.hash = hash; 

	 }

	 //This a second constructor which has been made for all the new blocks that are added to the block chain
	 public Block(int index, java.sql.Timestamp timestamp, Transaction transaction, String previousHash){
	 	this.index = index; 
	 	this.timestamp = timestamp; 
	 	this.transaction = transaction; 
	 	this.previousHash = previousHash; 
	 	this.hash = hashGenerator(); //The hash for a new block is generated here via the hashGenerator() method here
	 	this.nonce = nonce; //while the hashGenerator generates a hash for this block it also updates the nonce for this block accordingly
	 	this.trials = trials; //This variable here keeps track of the number of trials for nonce generation of this block 


	 }

	 public String toString() {
		 return timestamp.toString() + ":" + transaction.toString()
		 + "." + nonce+ previousHash;
	}

	// getter for index of block
	public int getIndex(){
		return this.index;
	}

	// getter for timestamp of transaction of block
	public java.sql.Timestamp getTimestamp(){
		return this.timestamp;
	}

	// getter for transaction of block
	public Transaction getTransaction(){
		return this.transaction;
	}

	// getter for nonce of block
	public String getNonce(){
		return this.nonce;
	}

	// getter for previous hash of block
	public String getPreviousHash(){
		return this.previousHash;
	}

	// getter for hash of block
	public String getHash(){
		return this.hash;
	}

	public int getTrials(){
		return this.trials; 
	}

	/**
	 * Method that produces different combinations of nonce until one unique combination 
	 * produces a hash that started with 5 zeros
     * @return Hash of hex character string.
     */
	public String hashGenerator(){
		trials = 0;   

		nonce = ""; 

		try{

			while(!(Sha1.hash(toString()).substring(0,5).equals("00000"))){

				nonce = "";
				for(int j=0; j<3; j++){
					nonce += (char)((int)(Math.random()*((127-34)+1)) + 33); //generating a random string (nonce) of length of 3 of ascii characters
				}

				for(int i= 0; i<22; i++){
					trials++; 
					if(Sha1.hash(toString()).substring(0,5).equals("00000")){ // validating if the nonce is producing a hash that starts with 00000
						break; 
					}
					nonce += (char)((int)(Math.random()*((127-34)+1)) + 33); // increasing the length of nonce until 25 characters
				}

				hash = Sha1.hash(toString()); //updating and saving the value of the correct hash
		}

		}

		catch(Exception e){
			System.out.println("Error");
		}

		return hash;  
	}

}