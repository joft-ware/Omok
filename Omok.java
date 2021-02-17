package Omok;

import java.util.Calendar;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;

public class Omok {
	
	static int[][] matrix=new int[20][20];	// OMOK matrix
	static int r=10, c=10, level=1;	// parameter
	
	public static void main(String[] args) throws IOException{
		
		rank[] rr=new rank[200];	// storage for rank data
		Scanner input=new Scanner(System.in);	// create scanner
		BufferedReader br=new BufferedReader(new FileReader("data.txt"));	// create FILE reader
		
		int menu;	// create parameter
		int sum=0;
		
		while (true) {	// while loop for get data
			
			String name=br.readLine();
			
			if (name==null)
				break;
			
			int score=Integer.parseInt(br.readLine());
			String today=br.readLine();
			
			rr[++sum]=new rank(name,score,today);
			
		}
		
		br.close();	// close buffer
		
		while (true) {
			
			cleanMatrix(matrix);	// put 0 in all elements of matrix
			printMenu();	// print menu
			menu=input.nextInt();	// user input
			
			if (menu==0) {	// terminal OMOK game
				
				System.out.println("TERMINAL OMOK GAME");
				break;
				
			}
			
			else if (menu==1) {	// play with AI
				
				AI ai=new AI();	// make OMOK AI
				
				System.out.println("type user name");	// get name from user
				String name;
				name=input.next();
				
				System.out.println("Choose level (easy 1 <- -> 10 hard)");	// set level
				level=input.nextInt();
				
				System.out.println("===============     START     ===============");	// start game
				
				long startTime=System.currentTimeMillis();	// check start time
				
				boolean turn=Preference.turn;	// get turn information
				
				if (turn) {	// if turn is true, AI start first
					
					ai.makeplay();
					AI.printAIMatrix();
					printMatrix(matrix,r,c,1);
					
				}
				
				else {	// else, user first start and print default matrix
					
					printMatrix(matrix,0,0,1);
					
				}
				
				while (true) {	// play game
					
					while (true) {	// get play information from user
						
						r=input.nextInt();
						c=input.nextInt();
						
						if (checkRC(r,c,matrix,1)) {
							
							break;
							
						}
						
					}
					
					if (omokcheck(matrix,1)) {	// case for user win
						
						long endTime=System.currentTimeMillis();	// check end time
						printMatrix(matrix,r,c,2);
						System.out.printf("winner is %s\n", name);	// print play information
						System.out.println("Play time: "+(endTime-startTime)/1000.0f+"seonds");
						System.out.println("===============     FINISH     ===============\n");
						
						int check=0;	// check if user is already exist
						
						for (int i=1; i<=sum; i++) {
							
							if (name.equals(rr[i].name)) {	// already exist
								
								check=i;
								break;
								
							}
							
						}
						
						if (check==0) {	// user is new in the game so save new information
							 
							Calendar calendar=Calendar.getInstance();
							java.util.Date date=calendar.getTime();
							rr[++sum]=new rank(name,level,(new SimpleDateFormat("yyyyMMddHHmmss").format(date)));
							
						}
						
						else {	// user is already exist so just update score
							
							rr[check].score+=level;
							
						}
						
						for (int i=1; i<=sum; i++) {	// sort rank data
							
							for (int j=1; j<=i-1; j++) {
								
								if (rr[i].score>rr[j].score) {
									
									rankchange(rr[i],rr[j]);
									
								}
								
							}
							
						}

						BufferedWriter out = new BufferedWriter(new FileWriter("data.txt"));	///Create FILE writer
						for (int i=1; i<=sum; i++) {	// save new data
							
							String score=String.valueOf(rr[i].score);	// cast integer to string
							
							out.write(rr[i].name);
							out.write("\n");
	                    	out.write(score);
	                    	out.write("\n");
	                    	out.write(rr[i].time);
	                    	out.write("\n");
	                    	
						}
                    	
                    	out.close();	// close buffer
						break;	// exit player mode 1
						
					}
					
					ai.makeplay();	// AI play game
					AI.printAIMatrix();	// print AI matrix
					
					if (omokcheck(matrix,2)) {	// if AI wins
						
						long endTime=System.currentTimeMillis();	// check end time

						printMatrix(matrix,r,c,1);
						System.out.println("winner is AI");	// print play information
						System.out.println("Play time: "+(endTime-startTime)/1000.0f+"seonds");
						System.out.println("===============     FINISH     ===============\n");
						
	                    break;
	                    
					}
					
					else {	// print play situation
						
						printMatrix(matrix,r,c,1);
						
					}
					
				}
			}
			
			else if (menu==2) {	// play together
				
				String name1, name2;	// get name informations
				System.out.println("type first user name");
				name1=input.next();
				System.out.println("type second user name");
				name2=input.next();
				r=0;
				c=0;
				
				System.out.println("===============     START     ===============");	// start game
				
				while (true) {	// play game
					
					printMatrix(matrix,r,c,1);
					System.out.printf("%s's turn\n", name1);
					
					while (true) {	// get play information from user
						
						r=input.nextInt();
						c=input.nextInt();
						
						if (checkRC(r,c,matrix,1)) {
							
							break;
						
						}
						
					}
					
					if (omokcheck(matrix,1)) {	// if player 1 wins
						
						printMatrix(matrix,r,c,2);
						System.out.printf("winner is %s\n", name1);
						System.out.println("===============     FINISH     ===============\n");
						break;
						
					}
					
					printMatrix(matrix,r,c,2);
					System.out.printf("%s's turn\n", name2);
					
					while(true) {	// get play information from user
						
						r=input.nextInt();
						c=input.nextInt();
						
						if (checkRC(r,c,matrix,2)) {
							
							break;
						
						}
						
					}
					
					if (omokcheck(matrix,2)) {	// if player 2 wins
						
						printMatrix(matrix,r,c,1);
						System.out.printf("winner is %s\n", name2);
						System.out.println("===============     FINISH     ===============\n");
						break;
						
					}
					
				}
			}
			
			else if (menu==3) {	// show rank information
				
				for (int i=1; i<=sum; i++) {	// sort rank
					
					for (int j=1; j<=i-1; j++) {
						
						if (rr[i].score>rr[j].score) {
							
							rankchange(rr[i],rr[j]);
							
						}
						
					}
					
				}
				
				for (int i=1; i<=sum; i++) {	// print rank
					
					rankprint(rr[i],i);
					
				}
				
				System.out.println();
				
			}
			
			else if (menu==4) {	// change turn
				
				if (Preference.turn==true) {
					
					Preference.turn=false;
					System.out.println("Now you play first\n");
					
				}
				
				else {
					
					Preference.turn=true;
					System.out.println("Now AI play first\n");
					
				}
			}
			
			else {	// wrong input from user
				
				System.out.println("type 0 - 4\n");
				
			}
		}
	}
   
    public static boolean omokcheck (int matrix[][], int p) {	// check someone win
        
        int[] directionX = {1,-1,0,1};	// set direction
        int[] directionY = {0,1,1,1};
        
        int x, y, cnt;	// create parameter
        
        for (int i=1; i<=19; i++) {
        
            for (int j=1; j<=19; j++) {
            	
            	if (matrix[i][j]==p) {
            		
            		for (int k=0; k<=3; k++) {
            			
            			x=i;	// set parameter data
            			y=j;
            			cnt=0;
            			
            			while (matrix[x][y]==p) {	// calculate numbers
            				
            				cnt++;
            				x+=directionX[k];
            				y+=directionY[k];
            				
            				if (!checkxy(x,y)) {
            					
            					break;
            					
            				}
            			}
            			
            			if (cnt==5) {	// someone win the game
            				
            				return true;
            				
            			}
            			
            		}
            		
            	}
            	
            }
            
        }
        
        return false;
        
    }
    
    public static int cal (int matrix[][], int p, int x, int y, int k) {	// calculate in matrix

    	final int[] directionX = {-1,0,1,-1,1,-1,0,1};	// set direction
    	final int[] directionY = {-1,-1,-1,0,0,1,1,1};
    	
    	int cnt=0;	// create parameter
    	
    	while (matrix[x][y]==p) {	// calculate connectivity
    		
            cnt++;
            x+=directionX[k];
            y+=directionY[k];
            
            if(!checkxy(x,y)) {
            	
            	break;
            	
            }
            
        }
    	
    	if (checkxy(x,y)) {	
    		
    		if (matrix[x][y]!=0 && cnt!=4) {
    			
    			cnt--;
    			
    		}
    		
    	}
    	
    	return cnt;
    	
    }
   
   
    public static void printMatrix (int matrix[][], int rx, int ry, int k) {	// print matrix
	   
    	for (int i=0; i<20; i++) {
		   
    		for (int j=0; j<20; j++) {
    			
    			if (i>0 && j>0) {
    				
    				if (i==rx&&j==ry) {
					   
    					if(k==1)
    						System.out.print(" бс");
    					if(k==2)
    						System.out.print(" бр");
    				}
				   
    				else if (matrix[i][j]==0) {
					   
    					System.out.print(" +");
					   
    				}
				   
    				else if (matrix[i][j]==1) {
					   
    					System.out.print(" б█");
					   
    				}
				   
    				else if (matrix[i][j]==2) {
					   
    					System.out.print(" б▄");
					   
    				}
				   
    			}
			   
    			else if (i==0 || j==0) {
				   
    				System.out.printf("%2d",matrix[i][j]);
				   
    			}
			   
    			else {
				   
    				System.out.print("  ");
				   
    			}
    			
    		}
		   
    		System.out.println();
		   
    	}
	   
    }
   
   
    public static void printMenu() {	// print MENU
	   
    	System.out.println("OMOK GAME");
    	System.out.println("SELECT MENU");
    	System.out.println("(1) 1 player mode");
    	System.out.println("(2) 2 players mode");
    	System.out.println("(3) Rank board");
    	System.out.println("(4) Change turn");
    	System.out.println("(0) Exit");
       
    }
   
    public static boolean checkxy (int x, int y) {	// check x and y is in range
	   
    	if(x>0 && y<20 && x<20 && y>0) {
    	  
    		return true;
    	  
    	}
	   
    	return false;
	   
    }
   
   
   
    public static boolean checkRC (int r, int c, int matrix[][], int k) {	// check if r and c is in range
	   
    	boolean occupied;	// create parameter
    	boolean outofindex;
       
    	if (r>0 && r<20 && c>0 && c<20) {
    		
    		if (matrix[r][c]==0) {
    			matrix[r][c]=k;
    			occupied=true;
    			outofindex=true;
    		}
    		
    		else {
    			
    			occupied=false;
    			outofindex=true;
    			
    		}
    		
    	}
    	
        else {
        	
        	outofindex=false;
        	occupied=false;
        	
        }
    	
        if (outofindex) {
        	
        	if (occupied) {
        		
        		return true;
        		
        	}
        	
        	else {
        		
        		System.out.println("already occupied try again");
        		return false;
        		
        	}
        	
        }
        
        else {
        	
           System.out.println("row and cloumn should be bigger than 0 and smaller than 20 try again");
           return false;
           
        }
        
    }
   
    public static void cleanMatrix (int matrix[][]) {	// put all 0 in matrix
    	
    	for (int i=0; i<20; i++) {
    		
    		matrix[0][i]=i;
    		
        }
    	
        for (int i=1; i<20; i++) {
        	
        	matrix[i][0]=i;
        	
        }
        
        for (int i=1; i<20; i++) {
        	
        	for (int j=1; j<20; j++) {
        		
        		matrix[i][j]=0;
        		
        	}
        	
        }
        
    }
    
    private static void rankprint (rank rr,int i) {	// print rank in format
    	
    	System.out.print("No. "+i+": "+rr.name+ " has "+rr.score+" points in ");
    	String q=rr.time;
    	System.out.printf("%s-%s-%s %s:%s:%s\n",q.substring(0,4),q.substring(4,6),q.substring(6,8),q.substring(8,10),q.substring(10,12),q.substring(12,14));
    }
    
    private static void rankchange (rank r1, rank r2) {	// change rank
    	
    	int temp;	// create parameter
    	String temp2;
    	String temp3;
    	
    	temp2=r1.name;	// change name
    	r1.name=r2.name;
    	r2.name=temp2;
    	
    	temp=r1.score;	// change score
    	r1.score=r2.score;
    	r2.score=temp;
    	
    	temp3=r1.time;	// change time
    	r1.time=r2.time;
    	r2.time=temp3;
    	
	}
}