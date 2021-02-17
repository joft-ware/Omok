package Omok;

import java.util.Random;

interface AIinterface{	// interface
	
	public void makeplay();
	public int pow(int po,int player);
	
}

public class AI implements AIinterface{
	
	public static int[][] a = new int[20][20];	// AI matrix
	
	final int[] directionX = {-1,0,1,-1,1,-1,0,1};	// set 8 directions
    final int[] directionY = {-1,-1,-1,0,0,1,1,1};
    
    Random random=new Random();	// create random parameter
    
    public void makeplay () {
    	
    	for(int i=0; i<20; i++) {	// reset AI matrix
    		
    		for (int j=0; j<20; j++) {
    			
    			a[i][j]=0;
    			
    		}
    	}
    	
        int max=0;	// create variable
        int maxrandom=0;
        
        for (int i=1; i<=19; i++) {
        	
            for (int j=1; j<=19; j++) {
            	
            	if (Omok.matrix[i][j]==0) {
            	
            		for (int k=0;k<=3;k++) {
            		
            			int x=i+directionX[k];	// set direction
            			int y=j+directionY[k];
            			int xx=i+directionX[7-k];
            			int yy=j+directionY[7-k];
            			int cnt=0, p=-1, pp=-1;
            			
            			if (Omok.checkxy(x,y)) {	// forward direction
            				
            				p=Omok.matrix[x][y];
            				
            			}
            			
            			if (Omok.checkxy(xx,yy)) {	// backward direction
            				
            				pp=Omok.matrix[xx][yy];
            				
            			}
                      
            			if (!Omok.checkxy(x,y)) { // forward direction out of range
            				
            				if(!Omok.checkxy(xx,yy)) { // forward & backward direction out of range
            					
            					continue;
            					
            				}
            				
            				if (pp==1||pp==2) {	// forward direction out of range, backward direction exist

            					cnt=Omok.cal(Omok.matrix,pp,xx,yy,7-k);
            					a[i][j]+=pow(cnt,pp);
            					
            				}
            				
            			}
            			
            			else if (p==1 || p==2) {	// forward direction exist
            				
            				if (Omok.checkxy(xx,yy)) {
            					
            					if (p==pp) { // forward direction exist, backward same exist
            						
            						cnt=0;
            						
            						while (Omok.matrix[x][y]==p) {
            							
                                        cnt++;
                                        x+=directionX[k];
                                        y+=directionY[k];
                                        
                                        if (!Omok.checkxy(x,y)) {
                                        	
                                        	break;
                                        	
                                        }
                                        
            						}
            						
            						while (Omok.matrix[xx][yy]==pp) {
            							
                                        cnt++;
                                        xx+=directionX[7-k];
                                        yy+=directionY[7-k];
                                        
                                        if (!Omok.checkxy(xx,yy)) {
                                        	
                                        	
                                        	break;
                                        }

            						}
                               
            						if (Omok.checkxy(x,y)) {
            							
            							if (Omok.matrix[x][y]!=0&&cnt!=4) {
            								
            								cnt--;
            								
            							}
            							
            						}
            						
            						if (Omok.checkxy(xx,yy)) {
            							            							
            							if (Omok.matrix[xx][yy]!=0&&cnt!=4) {
            								
            								cnt--;
            								
            							}

            						}

                               a[i][j]+=pow(cnt,p);
                               
                            }
            				
                            else if (pp==3-p) {	// forward direction exist, backward direction different exist
                            	
                            
                                a[i][j]+=pow(Omok.cal(Omok.matrix,p,x,y,k),p);
                                a[i][j]+=pow(Omok.cal(Omok.matrix,pp,xx,yy,7-k),pp);
                                
                            }
            					
                            else {	// forward direction exist, backward direction empty
                            	
                            	a[i][j]+=pow(Omok.cal(Omok.matrix,p,x,y,k),p);
                            	
                            }
            					
            			}
            				
            			else {	// forward direction exist, backward direction out of range
            				
            				a[i][j]+=pow(Omok.cal(Omok.matrix,p,x,y,k),p);
            				
            			}
            				
            		}
            		
            		else if (!Omok.checkxy(xx,yy)) {	// forward direction empty, backward direction out of range
            			
            			continue;
                         
            		}
            		
            		else if (pp==1 || pp==2) {	// forward direction empty, backward direction exist
            			
                          a[i][j]+=pow(Omok.cal(Omok.matrix,pp,xx,yy,7-k),pp);
                          
            		}
            			
            		if (a[i][j]>max) {
            			
            			max=a[i][j];
            			maxrandom=random.nextInt(1000);
            			Omok.r=i;
            			Omok.c=j;
            			
            		}
            		
            		else if (a[i][j]==max) {
            			
            			int temp=random.nextInt(1000);
            			
            			if (temp>maxrandom) {
            				
            				max=a[i][j];
            				maxrandom=temp;
            				Omok.r=i;
            				Omok.c=j;
            				
            			}
            		}
            	}
            }
        }
        if (max==0) {	// default case
        	 
        	Omok.r=10;
        	Omok.c=10;
        	 
        }
         
        Omok.matrix[Omok.r][Omok.c]=2;
        
        }
         
    }
    
    public int pow (int po, int player) {	// calculate power
    	
    	int ret = 1;
    	
        for (int i=1; i<=po; i++) {
        	
        	ret*=Omok.level;
        	
        }
        
        return ret*player;
        
    }
    
    public static void printAIMatrix() {	// print AI matrix
    	
        for (int i=0; i<20; i++) {
        	
        	for (int j=0; j<20; j++) {
        		
        		if (i>0 || j>0) {
        			
                   System.out.printf("%7d", a[i][j]);
                   
                }
        		
                else {
                	
                	System.out.print("       ");
                	
                }
        		
        	}
        	
        	System.out.println();
        	System.out.println();
        	
        }
        
    }
    
}
