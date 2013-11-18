package model;
/*
 * class SOLITAIRECHECKERSMODEL
 * Purpose: This is the model for the game of solitaire 
 * presented to the user in any view
 * Use the Observer/Observable, Action/Reactor design patterns 
 * Implement the interfaces SCConstansts and Gameable
 * @author: Wendy-Anne Daniel
 * @date:October 2013
 * @version:1.0
 * @see:model
 *  
 * 
*/
import java.util.Observable;
//import java.util.Observer;
//import java.util.List;
import java.util.ArrayList;
//import java.util.Random;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;

public class SolitaireCheckersModel extends Observable implements SCConstants
																	, Gameable{
	
	// The 2-dimensional playing surface.
    private int[][] board;
   private int newRow;
    private int newCol;
    private int currentConfig;
    private int countGood;
    private int countBad;
    private int currentType;

    // The status of the game.
    private String      status;
   // ArrayList<String> possibleMoves;
    ArrayList<String> candidates;
    Map <String,Integer> countGoods;
 /*** CONSTRUCTORS*******/    
    /*
	 * The no argument constructor
	 * Sets the dimensions of the game board
	 * Creates the candidates and countGoods array lists
	 * Initializes the current board configuration with the default
	 * Sets the board state for the start of the game 
	 * 
 */
	public SolitaireCheckersModel() {
		
		//this( DEFAULT_ROWS, DEFAULT_COLUMNS );
		
		status="newGame";
		
		 candidates = new ArrayList<String>();
		  countGoods = new HashMap<String,Integer>();
		 
		 currentType=SOLITAIRE;
		 start();
		
	}
	
/*	public SolitaireCheckersModel(int rows,int cols){
	
	}*/
 /*** END CONSTRUCTORS*******/  	
/*** FLIP FUNCTIONS (CHANGE OF STATE)*******/ 	
	/*
	 * The function reverseFlip
	 * Reverses the state flip for a tile from a 354 to a 122
	 * This happens if the board state has indicated to the front end
	 * That one of many outcomes is possible
	 * @param int the row that the clicked image button is on
	 * @param int the column that the clicked image button is on
	 * */
	private void reverseFlip(int row, int column){
		try{
			//System.out.println("SCM: Start Reverse Flip");
			for (int i=0;i<board.length;i++){
				//System.out.println("SCM:  Reverse Flip #1" +i);
				for (int j=0;j<board.length;j++){
					
					//System.out.println("SCM:  Reverse Flip #2" +j);
					//there is a relationship between 3 and its adjacent 5
					if(board[i][j]==3){
						//System.out.println("SCM:  Reverse Flip #3" +j);
						if(i==row && j==column){
							board[i][j]=1;
							//System.out.println("SCM:  Reverse Flip #4" );
							if(i>0){
								if(board[i-1][j]==5  ){
									board[i-1][j]=2;countGood--;countBad++;
							}}
							//System.out.println("SCM:  Reverse Flip #5" );
							if(i<6){
								if( board[i+1][j]==5  ){
									board[i+1][j]=2;countGood--;countBad++;
							}}
							if(j<6){
							if( board[i][j+1]==5  ){
								board[i][j+1]=2;countGood--;countBad++;
							}}
							
							//System.out.println("SCM:  Reverse Flip #6" );
							if(j>0 ){
							if(board[i][j-1]==5){
								board[i][j-1]=2;countGood--;countBad++;
							}}
							
							//System.out.println("SCM:  Reverse Flip #7" );
						}//end if current vals
						else{board[i][j]=2;}
						
					}//end if 3
					if(board[i][j]==4){
						board[i][j]=2;
					}//end if 4
				}//end for j
			}//end for i
			cleanup();
			// this.updateObservers();
		}//end try
		catch(Exception ee){Log.d("reverseFlipException",ee.getMessage());}
	}

	/**
	 * The function cleanup
	 * Reverses the state of any cell from 5 to 1
	 * Is associated with the reverse flip and the 
	 * forward flip (flipJumpedPeg)
	 * */
	private void cleanup(){
		try{
			
			for (int i=0;i<board.length;i++){
				
				for (int j=0;j<board.length;j++){
	
					if(board[i][j]==5){
						
							board[i][j]=1;
			
					}//end if 5
					if(board[i][j]==3){
						
						board[i][j]=2;
		
				}//end if 5
					if(board[i][j]==4){
						
						board[i][j]=1;
		
				}//end if 4
					
				}//end for j
			}//end for i
			
		}catch(Exception ee){Log.d("cleanupException",ee.getMessage());}
	}
	/*
	 * The function flipJumpedPeg
	 * Changes the state of a board tile from 211 to 112
	 * The tile may be north, south, east or west
	 * If there is more than one outcome
	 * The tiles for the outcome are labeled 354
	 * @param int the row that the clicked image button is on
	 * @param int the column that the clicked image button is on
	 * */

  private void flipJumpedPeg( int row, int column ) {
    
    	status="started";
    	//ArrayList<String> candidates = new ArrayList<String>();
    	candidates.clear();
    	
    	//marks the middle north south slab
    	boolean found=false;
    	try{
    		cleanup();
    	if( (column >=0 && column<=6) ){
    		//System.out.println("SCM flipjumpedpeg: $$$$$in while loop 1  ");
    		
    			//System.out.println("SCM flipjumpedpeg: $$$$$going north $1  ");
    		if((row-1)>=0 && (row-2)>=0){
    			if((board[row-1][column]==1) && (board[row-2][column]==2)){
    				candidates.add("north");
            	found=true;}}
    		
    			
    		
    			//System.out.println("SCM flipjumpedpeg: $$$$$going south $2  ");
    			if((row+1)<=6 && (row+2)<=6){
    			if((board[row+1][column]==1) && (board[row+2][column]==2)){
    				candidates.add("south");
            	found=true;}}
    			
    			//System.out.println("SCM flipjumpedpeg: $$$$$after $2 ");
    			
    			if((column-1)>=0 && (column-2)>=0){
    			if((board[row][column-1])==1 && (board[row][column-2]==2)){
    				candidates.add("east");
            	found=true;}}
    			
    			
    			if((column+1)<=6 && (column+2)<=6){
    			if((board[row][column+1])==1 && (board[row][column+2]==2)){
					candidates.add("west");
            	found=true;}}
    	
    			//System.out.println("SCM flipjumpedpeg: $$$$$end of if $4++ ");
    		}//end outermost if
    	}
    	catch(Exception ee){Log.d("FlipJumpedPeg exception ",ee.getMessage());}
    		
    	
    	int pp=candidates.size();
    	//System.out.println("SCM flipjumpedpeg: $$$$$candidates size is "+pp);
    	
    	if(pp==1){
    		//System.out.println("SCM flipjumpedpeg: $$$$$pp is 1 ");
    		String a=candidates.get(0);
    		if(a=="north"){board[row-2][column]=1;newRow=row-2;newCol=column;
    		board[row][column]=2;
    		board[row-1][column]=2;}
    		if(a=="south"){board[row+2][column]=1;newRow=row+2;newCol=column;
    		board[row][column]=2;
    		//board[row-1][column]=2;}
    		board[row+1][column]=2;}
    		if(a=="west"){board[row][column+2]=1;newRow=row;newCol=column+2;
    		board[row][column]=2;
    		board[row][column+1]=2;}
    		if(a=="east"){board[row][column-2]=1;newRow=row;newCol=column-2;
    		//System.out.println("AAAAAA$$ in east");
    		board[row][column]=2;
    		board[row][column-1]=2;}
    		
    		countGood--;countBad++;
    		
    		//System.out.println("SCM@@@@@: count good is "+countGood);
    		//System.out.println("SCM@@@@@: count bad is "+countBad);
    		
    	}
    	else if(pp>1){
    		
    		//System.out.println("pp is greater than 1 ");
    		for(int i=0;i<pp;i++){
    			System.out.println("SCM in loop");
    			if(candidates.get(i)=="north"){
    				board[row-2][column]=3;
    				board[row-1][column]=5;
    				
    			}
    			if(candidates.get(i)=="south"){
    				board[row+2][column]=3;
    				board[row+1][column]=5;
    			
    			}
    			if(candidates.get(i)=="west"){
    				board[row][column+2]=3;
    				board[row][column+1]=5;
    		
    			}
    			if(candidates.get(i)=="east"){
    				board[row][column-2]=3;
    				board[row][column-1]=5;
    			
    			}
    			board[row][column]=4;
    		}
   
    		
    	}
    	
    	
    	// this.setStatus( "flipped " + newRow + ", " + newCol );
        // this.updateObservers();
    }
  
    /*
     * The function flipValueAt
     * Carries out the rules of the game
     * cell in state 1 must jump over another cell in state 1
     * to reach a cell in state2
     * the jumped-over cell changes its state to 2
     * @param int row
     * @param int column
     */
    public void flipValueAt( int row, int column ) {
     
    	if(board[row][column]==3){
    		reverseFlip( row, column );
    	}
    	else{
        this.flipJumpedPeg( row, column );}
       
        this.setStatus( "flipped " + (row + 1) + ", " + (column + 1) );
       
        this.updateObservers();
    }
  /*** END FLIP FUNCTIONS (CHANGE OF STATE)*******/    
 /*** ACCESSOR FUNCTIONS *******/   
    /**
     * Gets the number of columns for this game.
     * @return the integer number of columns
     */
    public int getColumns() {
        return ( board[0].length );
    }

    /**
     * Returns the number of tiles computed as: rows x columns
     * @return int the number of tiles
     */
    public int getNumberOfCells() {
        return (countGood +countBad);
    }

    /**
     * Returns the number of tiles that have been flipped.
     * @return int the number of flipped cells
     */
    public int getNumberOfFlippedCells() {
        final int cols = this.getColumns();
        final int rows = this.getRows();

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ( this.getValueAt(r, c)==1 )
                    count++;
            }
        }

        return count;
    }

    /**
     * Calculate the percent of completion.
     * @return  the percentage of completion
     */
    public int getProgress() {
    	
    	
    	int progressPercent=0;
        
        try{
        	Integer totalPegs=null;;
        	
        	switch(currentType){
    		
    		case(SOLITAIRE):
    			totalPegs=Integer.valueOf(countGoods.get("sol"));
    			break;
    		case(ARROW):
    			totalPegs=Integer.valueOf(countGoods.get("arrow"));
    			break;
    		case(CROSS):
    			totalPegs= Integer.valueOf(countGoods.get("cross"));
    			break;
    		case(DIAMOND):
    			totalPegs=Integer.valueOf(countGoods.get("diamond"));
    			break;
    		case(DOUBLE_ARROW):
    			totalPegs= Integer.valueOf(countGoods.get("darrow"));
    			break;
    		case(PLUS):
    			totalPegs= Integer.valueOf(countGoods.get("plus"));
    			break;
    		case(FIREPLACE):
    			totalPegs=Integer.valueOf(countGoods.get("fire"));
    			break;
    		case(PYRAMID):
    			totalPegs= Integer.valueOf(countGoods.get("pyramid"));
    			break;
    		}//end switch
    		
        	progressPercent=getSpecificProgress(totalPegs);
  
        }
        catch(Exception ee){Log.d("getProgress Ex",ee.getMessage());}
        return progressPercent;
    }
    
    /**
     * The function getSpecificProgress
     * @param tpegs - Integer represents total pegs for this pattern
     * @return int - the progress percent
     */
    public int getSpecificProgress(Integer tpegs) {
       
    	int progressPercent=0;
        try{
        	int total=0;
        	
        	if(tpegs!=null){
        		//System.out.println("SCM: in tpegs if ###2");
        		
        		total=tpegs.intValue();
        		//System.out.println("SCM: total is ###3 "+total);
        		//System.out.println("SCM: countGood is ###3b "+countGood);
        		
        		progressPercent=(countGood*100)/total;
        		//System.out.println("SCM: prog percent is  ###4 "+progressPercent);
        		
        	}
        	
 
        }
        catch(Exception ee){Log.d("getSpecificProgress Ex",ee.getMessage());}
        return progressPercent;
    }

    /**
     * Gets the number of rows for this game.
     * @return the integer number of rows
     */
    public int getRows() {
        return ( board.length );
    }

    /**
     * Gets the status.
     * @return String the status
     */
    public String getStatus() {
        return status;
    }
    public int getCountGood(){
    	return countGood;
    }
    public int getCountBad(){
    	return countBad;
    }

    
    /*
     * Gets the value at row,col on board
     * @param - int the row
     * @param - int the column
     * @return boolean - true or false
     */
    public int getValueAt( int row, int column ) {
    	int returnVal;
    
    	 returnVal=board[row][column];
    	
        return returnVal;
    }
	/*
	 * The function setSelectedBoard
	 * Creates the board for the type of configuration 
	 * the user selected
	 *  
	 * @param - int the type of board chosen in the view
	 * 
	 */
	public void setSelectedBoard(int type){
		
		currentType=type;
		initializeBoard();
		
				
		switch(type){
		
		case(SOLITAIRE):
			
			createSolLayout();
			currentConfig=SOLITAIRE;
			break;
		case(ARROW):
			
			createArrowLayout();
			currentConfig=ARROW;
			break;
		case(CROSS):
			
			createCrossLayout();
			currentConfig=CROSS;
			break;
		case(DIAMOND):
			
			createDiamondLayout();
			currentConfig=DIAMOND;
			break;
		case(DOUBLE_ARROW):
			
			createDarrowLayout();
			currentConfig=DOUBLE_ARROW;
			break;
		case(PLUS):
			
			createSimplePlusLayout();
			currentConfig=PLUS;
			break;
		case(FIREPLACE):
			
			createFireplaceLayout();
			currentConfig=FIREPLACE;
			break;
		case(PYRAMID):
			
			createPyramidLayout();
			currentConfig=PYRAMID;
			break;
		}//end switch
		
		//System.out.println("SCM:%%% after switch ");
		updateObservers();
	} 
  /*** END ACCESSOR FUNCTIONS *******/ 
 /*** STATE FUNCTIONS *******/    

   /*
    * The function isWon
    * Determines the final state of the board
    * @return int the state of the board
    * @see model.Gameable#isWon()
    */
   @Override
    public int isWon() {
        // The game is won if all of the cells are 2 and one cell is 1
	   //The game is super won if all cells are 2, one is 1 and the 1 is in the center
	   //i.e. board[3][3]==1
	   int returnval=0;
	      
       if(countGood==1  && board[3][3]==1) {returnval= 2;}
       else{
      if(countGood==1) returnval= 1;}
      
      return returnval;
        
    }

    
  
	/**
	 * The function isLost
	 * @return boolean
	 * @see model.Gameable#isLost()
	 * 
	 */

		@Override
	public boolean isLost() {
		boolean returnval=false;
	try{	
		//System.out.println("SCModel: start is Lost");
		int a=board.length;
	
		int losers=0;

		
		for(int i=0;i<=a-1;i++){
			
			
			for(int j=0;j<=a-1;j++){
				
				if(board[i][j]==1){
					//System.out.println("islost!! start i is #0 "+i);
					//System.out.println("islost start j is #0 "+j);
					//out of bounds condition
					if(i<6 && j<6 && i>0 && j>0){
					if(board[i+1][j]==2 || board[i+1][j]==0 ){
						if(board[i-1][j]==2 ||board[i-1][j]==0 ){
							if(board[i][j-1]==2  || board[i][j-1]==0){
								if(board[i][j+1]==2 || board[i][j+1]==0 ){
									losers++;
									//System.out.println("SCM:Adding loser #1");
									//System.out.println("SCM:i is #1 "+i);
									//System.out.println("SCM:j is #1 "+j);
								}//end if #5
								
							}//end if #4
							
						}//end if #3
								
					}//end if #2
					}//end out of bounds if
					else{
						if(i==6 ){
							
								if(board[5][j]==2  ){
									if(board[6][j-1]==2 ||board[6][j-1]==0 ){
										if(board[6][j+1]==2 || board[6][j+1]==0 ){
											losers++;
											//System.out.println("SCM:Adding loser #2");
											//System.out.println("SCM:i is #2 "+i);
											//System.out.println("SCM:j is #2 "+j);
										}//end if #5
										
									}//end if #4
									
								}//end if #3
										
							
						}//end i==6 if
						if(i==0 ){
							
							
							if(board[1][j]==2  ){
								if(board[0][j-1]==2 ||board[0][j-1]==0 ){
									if(board[0][j+1]==2 ||board[0][j+1]==0 ){
										losers++;
										//System.out.println("SCM:Adding loser #3");
										//System.out.println("SCM:i is #3 "+i);
										//System.out.println("SCM:j is #3 "+j);
										
									}//end if #5
									
								}//end if #4
								
							}//end if #3
									
						
					}//end i==0 if
						if(j==0 ){
							
							
							if(board[i+1][0]==2 || board[i+1][0]==0 ){
								if(board[i-1][0]==2 || board[i-1][0]==0 ){
									if(board[i][1]==2  ){
										losers++;
										//System.out.println("SCM:Adding loser #4");
										//System.out.println("SCM:i is #4 "+i);
										//System.out.println("SCM:j is #4 "+j);
										
									}//end if #5
									
								}//end if #4
								
							}//end if #3
									
						
					}//end j==0 if
						if(j==6 ){
							
							if(board[i+1][6]==2 || board[i+1][6]==0){
								if(board[i-1][6]==2 || board[i-1][6]==0 ){
									if(board[i][5]==2  ){
										losers++;
										//System.out.println("SCM:Adding loser #5");
										//System.out.println("SCM:i is #5 "+i);
										//System.out.println("SCM:j is #5 "+j);
										
									}//end if #5
									
								}//end if #4
								
							}//end if #3
									
						
					}//end j==0 if
						
					losers+=checkEdgeConditions(i,j);	
					}//end out of bounds else
				}//end first if
			}//end j loop
		}//end i loop
		//System.out.println("SCModel: !!!!!count good is "+countGood);
		//System.out.println("SCModel: !!!!!losers is "+losers);
		if(losers==countGood ){
			returnval=true;
		}
		if(checkRaggedArray1()) returnval=true;
		if(checkRaggedArray2()) returnval=true;
	}
	catch(Exception e){ Log.d("isLost exception :",e.getMessage());}
		return returnval;
	}//end function
		
	private int checkEdgeConditions(int i,int j){
		
		int losers2=0;
		try{
			if(i==0){
				//end condition 111
				if(board[0][2]==1 && board[0][3]==1 && board[0][4]==1 ){
					
					if(board[1][2]==2 && board[1][3]==2 && board[1][4]==2){
						losers2++;
						//System.out.println("SCM:Adding loser ##3");
						//System.out.println("SCM:i is ##3 "+i);
						//System.out.println("SCM:j is ##3 "+j);
						
					}//end if #5
			
			}//end if #3
				
		
			}
			if(i==6){
				
					if(board[6][2]==1 && board[6][3]==1 && board[6][4]==1 ){
						
							if(board[5][2]==2 && board[5][3]==2 && board[5][4]==2){
								losers2++;
								//System.out.println("SCM:Adding loser ##4");
								//System.out.println("SCM:i is ##4 "+i);
								//System.out.println("SCM:j is ##4 "+j);
								
							}//end if #5
					
					}//end if #3
					
					
			}
			if(j==0){
				if(board[2][0]==1 && board[3][0]==1 && board[4][0]==1 ){
					
					if(board[2][1]==2 && board[3][1]==2 && board[4][1]==2){
						losers2++;
						//System.out.println("SCM:Adding loser ##5");
						//System.out.println("SCM:i is ##5 "+i);
						//System.out.println("SCM:j is ##5 "+j);
						
					}//end if #5
			
			}//end if #3
			
			}
			if(j==6){
				if(board[2][6]==1 && board[3][6]==1 && board[4][6]==1 ){
					
					if(board[2][5]==2 && board[3][5]==2 && board[4][5]==2){
						losers2++;
						//System.out.println("SCM:Adding loser ##6");
						//System.out.println("SCM:i is ##6 "+i);
						//System.out.println("SCM:j is ##6 "+j);
						
					}//end if #5
			
			}//end if #3
			}
			
		}
		catch(Exception e){Log.d("SCMCheckEdgeConditions: ex ",e.getMessage());}
		//System.out.println("SCM:Adding loser2 is  "+losers2);
		return losers2;
	}
	
	private boolean checkRaggedArray1(){
		
		int numFound=0;
		boolean returnval=false;
		try{
			int[][] oneLostArray= {{0,0,1,1,1,0,0},{0,0,1,2,1,0,0},{1,1,1,1,1,1,1},{2,2,1,2,1,2,1},{1,1,1,1,1,1,1},
									{0,0,1,2,1,0,0},{0,0,1,2,1,0,0}};
			
			for (int i=0;i<7;i++){
				for (int j=0;j<oneLostArray[i].length;j++){
					if(board[i][j]==oneLostArray[i][j]) numFound++;
				}
			}
			if(numFound==49) returnval=true;
			else returnval=false;
			
			
		}
		catch(Exception e){Log.d("SCMCheckRaggedArray1: ex ",e.getMessage());}
		return returnval;
	}
private boolean checkRaggedArray2(){
		
		int numFound=0;
		boolean returnval=false;
		try{
			int[][] oneLostArray= {{0,0,1,1,1,0,0},{0,0,1,2,1,0,0},{1,1,1,1,1,1,1},{1,2,1,2,1,2,2},{1,1,1,1,1,1,1},
									{0,0,1,2,1,0,0},{0,0,1,2,1,0,0}};
			
			for (int i=0;i<7;i++){
				for (int j=0;j<oneLostArray[i].length;j++){
					if(board[i][j]==oneLostArray[i][j]) numFound++;
				}
			}
			if(numFound==49) returnval=true;
			else returnval=false;
			
			
		}
		catch(Exception e){Log.d("SCMCheckRaggedArray1: ex ",e.getMessage());}
		return returnval;
	}
		  /*
	     * The function reset
	     * Sets the board back to the current configuration
	     * @see model.Gameable#reset()
	     */
	    @Override
	    public void reset() {
	    	
	       setSelectedBoard(currentConfig);

	        this.setStatus( this.toString() );
	        this.updateObservers();
	    }  
 /*** END STATE FUNCTIONS *******/ 
 /*** INTERNAL MODEL FUNCTIONS *******/    
    /*
     * The function initializeBoard
     * Sets all of the board cells to false
     *
     */
	private void initializeBoard(){
		if(board==null){
		board=new int[7][7];}
		countGood=0;countBad=0;
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
				board[i][j]=0;
				
			}
		}
		createPlusLayout(2);
	}

	/*
	 * Creates the Arrow layout configuration 
	 *
	 */
	private void createArrowLayout(){
		
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
				
				
		
				while(i==0 && j==3){
					board[i][j]=1;
					j++;countGood++; countBad--;
				}
				while(i==1 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				if(i==3 && j==3){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==4  && j==3){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i>4 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
			}//end j loop
		}//end i loop
		countGoods.put("arrow",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Diamond layout configuration 
	 *
	 */
	private void createDiamondLayout(){
		
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
		
				while(i==0 && j==3){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==1 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==3 && (j>=0 && j<=6)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==4 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
				while(i==5 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==6 && (j==3)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
			}//end j loop
		}//end i loop
		board[3][3]=2;countBad++;countGood--;
		countGoods.put("diamond",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Double Arrow layout configuration 
	 * 
	 */
	private void createDarrowLayout(){
	
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
		
				while(i==0 && j==3){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==1 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==3 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==4 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==5 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==6 && (j==3)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
			}//end j loop
		}//end i loop
		countGoods.put("darrow",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Fireplace configuration 
	 *needs work
	 */
	private void createFireplaceLayout(){
	
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
		
				while((i>=0 && i<=2) && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
			
				while(i==3 && (j==2 || j==4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
			}//end j loop
		}//end i loop
		countGoods.put("fire",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Pyramid layout configuration 
	 *
	 */
	private void createPyramidLayout(){
	
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
				while(i==1 && j==3){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==3 && (j>=1 && j<=5)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==4 && (j>=0 && j<=6)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
				
				
			}//end j loop
		}//end i loop
		countGoods.put("pyramid",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Plus layout configuration 
	 *
	 */
	private void createPlusLayout(int state){
		int count=0;
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
		
				while(i==0 && (j>=2 && j<=4)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==1 && (j>=2 && j<=4)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==2 && (j>=0 && j<=6)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==3 && (j>=0 && j<=6)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==4 && (j>=0 && j<=6)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==5 && (j>=2 && j<=4)){
					board[i][j]=state;
					j++;count++;
				}
				while(i==6 && (j>=2 && j<=4)){
					board[i][j]=state;
					j++;count++;
				}
			}//end j loop
		}//end i loop
		
		
		if(state==2){countBad+=count;}
	}
	/*
	 * Creates the Solitaire layout configuration 
	 *
	 */
private void createSolLayout(){
	
	try{
		for (int i=0;i<=6;i++){
			
			for (int j=0;j<=6;j++){
		
				while(i==0 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==1 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && (j>=0 && j<=6)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==3 && (j>=0 && j<=6)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==4 && (j>=0 && j<=6)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==5 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==6 && (j>=2 && j<=4)){
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
			}//end j loop
		}//end i loop
		board[3][3]=2;countBad++;countGood--;
		countGoods.put("sol",Integer.valueOf(countGood));
	}//end try
		catch(Exception ee){Log.d("createSolLayoutEx",ee.getMessage());}//end catch
	}
	/*
	 * Creates the Cross layout configuration 
	 *
	 */
	private void createCrossLayout(){
		
	
		for (int i=0;i<=6;i++){
			
			
			for (int j=0;j<=6;j++){
				
				while((i==1 ) && (j==3 )){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
				while(i==2 && (j>=2 && j<=4)){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while((i>=3 && i<=4)  && (j==3)){
					
					board[i][j]=1;
					j++;countGood++;
				}
				
			}//end j loop
		}//end i loop
		countGoods.put("cross",Integer.valueOf(countGood));
	}
	/*
	 * Creates the Simple Plus layout configuration 
	 *
	 */
	private void createSimplePlusLayout(){
		
	
		for (int i=0;i<=6;i++){
			
			
			for (int j=0;j<=6;j++){
				
				while((i==1 ) && (j==3 )){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while(i==2 && j==3){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
				while(i==3 && (j>=1 && j<=5)){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				while((i==4 || i==5)   && (j==3)){
					
					board[i][j]=1;
					j++;countGood++;countBad--;
				}
				
			}//end j loop
		}//end i loop
		countGoods.put("plus",Integer.valueOf(countGood));
	}
 /*** END INTERNAL MODEL FUNCTIONS *******/ 	
		/*
	 * The function start
	 * @see model.Gameable#start()
	 */

	@Override
	public void start() {
		initializeBoard();
		
	}
	

	/*
	 * The function setStatus
	 * @param - String the current status
	 */
    private void setStatus( String status ) {
        this.status = status;
    }
    
    
    /*
     * The function updateObservers
     * Notifies all registered observers
     * that the state has changed
     */
    private void updateObservers() {
        this.setChanged();
        this.notifyObservers();
    }
    
 

	/*
	 * The function main
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//use main for unit testing of model
	}
	

}
