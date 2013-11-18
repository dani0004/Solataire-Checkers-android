package com.algonquincollege.dani0004.solitairecheckers;
/*
 * class MAINACTIVITY
 * Purpose: Display a solitaire board game to the user
 * Permit the user to change the configuration of the board
 * through a Spinner
 * Track the user progress through a progress bar
 * Allow the user to reset configurations through a Options Menu item
 * Use the Observer/Observable, Action/Reactor design patterns
 * Track different states of the board game
 * Be the listener for Spinner Adapter activity
 * Be the listener for Dialog box activity 
 * @author: Wendy-Anne Daniel
 * @date: October 2013
 * @version:1.0
 * @see:com.algonquincollege.dani0004.solitairecheckers
 *  
 * 
*/
import java.util.Observable;

import com.algonquincollege.dani0004.solitairecheckers.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
//import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import model.*;
import android.content.res.Resources;
import java.util.ArrayList;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
//import android.view.View.OnClickListener;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MenuInflater;

public class MainActivity extends Activity implements  java.util.Observer,
													AdapterView.OnItemSelectedListener,
													DialogInterface.OnClickListener	{
	
	private Spinner gameSpinner;
	private model.SolitaireCheckersModel scModel;
	private boolean firstTimeThrough;
	
	private ProgressBar mProgress;
	 
    private Handler mHandler = new Handler();
    private int progressStatus;
    private Thread pthread;
    final Context context=this;
    private int currentItemPos;
    private Button resetButton;
    AlertDialog.Builder adbuilder;
    AlertDialog infoAlert ;
    private boolean messageShown=false;
   
    
 
	/*
	 * The method onCreate inherited from the parent activity class
	 * Registers a listener for the spinner
	 * Creates the observable
	 * Adds itself as an observer
	 * Sets the board state for the start of the game 
	 * 
 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//System.out.println("MA onCreate:### start");
		setContentView(R.layout.activity_main);
		//System.out.println("MA onCreate: ###before adding spinner listener");
		firstTimeThrough=false;
		
		//System.out.println("MA onCreate: ###before adding model");
		//The model has to be there before the spinner listener is created
		scModel=new model.SolitaireCheckersModel();
		//System.out.println("MA onCreate: ###after adding model");
		scModel.addObserver(this);
		
		resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setVisibility(View.INVISIBLE);
		
		addSpinnerListener();
		gameSpinner = (Spinner) findViewById(R.id.spinner1);
		gameSpinner.setOnItemSelectedListener(this);
		
			 
		 mProgress = (ProgressBar) findViewById(R.id.progressBar);
		 progressStatus=0;
		 mProgress.setMax(100);
		 startProgressThread();
		 
		 //create the known dialog parameters in dialog Builder
		adbuilder=new AlertDialog.Builder(context);
 		adbuilder.setCancelable(false);
 		adbuilder.setPositiveButton(R.string.OK,this);
 		
    		
		 
	
	}
 /** MENU FUNCTIONS *******/ 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main, menu);
		//show the reset button
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            resetButton.setVisibility(View.VISIBLE);
	            return true;
	    }

	    return super.onKeyDown(keycode, e);
	}
	
	public void handleResetButton(View v){
		 scModel.reset();
		 resetButton.setVisibility(View.INVISIBLE);
	}
	public void showPopup(View v){
		PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.main, popup.getMenu());
	    
	    popup.show();
	}
	
	 /* The function onOptionsItemSelected
	 * Called when a menu item is clicked 
	 * @param MenuItem - the item that was clicked
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        // GET from the view which menu item was selected
	        case R.id.action_reset:
	            // SET the model to its new state!
	            scModel.reset();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

  /** USER INTERFACE (MODEL VIEW) FUNCTIONS *******/ 	  
	  /* The function handleImageButton
		 * Is the click event handler for the image buttons
		 * @param View - the button that was clicked
		 * @return void
		 */
		public void handleImageButton(View v){
				//System.out.println("MainActivity:handle image button click");
				int rID;
			try{
				String name = getResources( ).getResourceEntryName( v.getId( ) );
				
				int r = name.charAt( 1 ) - '0';
				int c = name.charAt( 3 ) - '0';
				
		
				
					scModel.flipValueAt( r, c );
			
				
			}
			catch(Exception e){
				Log.d("Exception: ",e.getMessage());
			}
			
			}
	  /*
		 * The function update is called by the Observable
		 * when a change has occurred in the model
		 * The call occurs through the SolitaireCheckers notifyObservers()
		 * @param Observable (SolitaireCheckers.class)
		 * @param Object any data returned to the Observer
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */

		@Override
		public void update(Observable ob, Object pp){
			//System.out.println("MainActivity:$$$$In update");
			int resID; ImageButton imageView;
			
			
			for(int i=0;i<=6;i++){
				
				for (int j=0;j<=6;j++){
					//System.out.println("MainActivity:$$$$In j loop "+j);
					int bbb=scModel.getValueAt(i,j);
					//System.out.println("MainActivity:$$$$After SCM call "+bbb);
					if(bbb==0){
						resID = getResources().getIdentifier("r"+i+"c"+j, "id", "com.algonquincollege.dani0004.solitairecheckers");
						imageView = (ImageButton) findViewById(resID);
						imageView.setVisibility(View.INVISIBLE);
						//imageView.setTag("0");
						
					}//end if
					if(bbb==1 || bbb==4){
						
						resID = getResources().getIdentifier("r"+i+"c"+j, "id", "com.algonquincollege.dani0004.solitairecheckers");
						imageView = (ImageButton) findViewById(resID);
						imageView.setVisibility(View.VISIBLE);
						//imageView.setBackgroundResource(android.R.drawable.btn_default);
						
						imageView.getBackground().setColorFilter(Color.LTGRAY,PorterDuff.Mode.MULTIPLY);
						imageView.setImageResource(R.drawable.peg);
						imageView.setEnabled(true);
						//imageView.setTag("1");
						//progressStatus=scModel.getProgress();
						
					}//end if
					if(bbb==2 ){
						
						resID = getResources().getIdentifier("r"+i+"c"+j, "id", "com.algonquincollege.dani0004.solitairecheckers");
						
						imageView = (ImageButton) findViewById(resID);
						imageView.setVisibility(View.VISIBLE);
						imageView.setImageResource(R.drawable.nopeg);
						imageView.getBackground().setColorFilter(Color.LTGRAY,PorterDuff.Mode.MULTIPLY);
						imageView.setEnabled(false);
						//imageView.setTag("2");
						//progressStatus=scModel.getProgress();
						
					}//end if
					if(bbb==3){
						resID = getResources().getIdentifier("r"+i+"c"+j, "id",  "com.algonquincollege.dani0004.solitairecheckers");
						imageView = (ImageButton) findViewById(resID);
						imageView.setVisibility(View.VISIBLE);
						//imageView.getBackground().setColorFilter(Color.YELLOW,PorterDuff.Mode.MULTIPLY);
						//imageView.getBackground().setColorFilter(Color.YELLOW,PorterDuff.Mode.OVERLAY);
						imageView.getBackground().setColorFilter(Color.YELLOW,PorterDuff.Mode.DARKEN);
						imageView.setEnabled(true);
							
							
						
					}//end if
					
				}//end j loop
			}//end i loop
			
			
			progressStatus=scModel.getProgress();
			
			String status=scModel.getStatus();
			
			//System.out.println("MainActivity:checking status "+status);
			
			if((scModel.getCountGood()==1)){
				//System.out.println("MainActivity:checking status");
				
				//check if the game is lost or won
				if(scModel.isWon()==1){
					
					ShowMessage(model.SCConstants.NORMAL_MESSAGE_WON);
					
				}//end iswon if
				else{
				if(scModel.isWon()==2){
					ShowMessage(model.SCConstants.SUPER_MESSAGE_WON);
				}//end iswon #2
				
				}
				
			}//end if countGood
			else{
				if(scModel.isLost()){
					ShowMessage(model.SCConstants.DEFAULT_MESSAGE_LOST);
				}//end islost if
				}//end else
			
			
			
		}//end update function
		
 /** SPINNER FUNCTIONS *******/ 
		/*
		 * The method addSpinnerListener
		 * Creates an array adapter using the string array in the resources and
		 * a default spinner layout
		 * Applies the adapter to the spinner
		 * Registers the listener for the adapter (this) 
		 * Passes the item selected in the spinner to the activity 
		 * implementation of the abstract method in the AdapterView.OnItemSelectedListener
		 * @see AdapterView.OnItemSelectedListener
	 */
		private void addSpinnerListener() {
			//System.out.println("MA addSpinnerListener: start");
			gameSpinner = (Spinner) findViewById(R.id.spinner1);
			
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
			        R.array.checkers_arrays, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
			// Apply the adapter to the spinner
			gameSpinner.setAdapter(adapter);
			
			
		  }
		  /*
			 * This method is called by event listener on the Adapter 
			 * Passes the item selected in the spinner to the activity 
			 * implementation of the abstract method in the AdapterView.OnItemSelectedListener
			 * @see AdapterView.OnItemSelectedListener
		 */
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		
			//System.out.println("Main Activity:###in spinner listner");
			String a=null;
			currentItemPos=pos;
			if(!firstTimeThrough){
			 a=parent.getItemAtPosition(pos).toString();}
			else{a="solitaire";firstTimeThrough=false;}
	
			
			getValSelected(a);
			
			
			//System.out.println("Main Activity:###selected config is "+a);
		  }
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }		
		 /*
		 * The function getValSelected
		 * called by the event listener on the adapter for the spinner
		 * Determines the index of the item the user selected
		 * in the view drop down list (spinner)
		 * by comparing it to items in the resource array
		 * sends a message to the Observable to change the board 
		 * configuration according to what was selected 
		 * @param - String the item selected
		 *
		 */
		private void getValSelected(String selection){
			System.out.println("Main Activity: @@@start get val selected "+selection);
			//newBoard=true;
			String[] b=getResources().getStringArray(R.array.checkers_arrays);
			
			 int selectedIndex=0;boolean found=false;
			
			 
			 for(int i=0;i<b.length;i++){
				 
				 if(b[i].trim().equalsIgnoreCase(selection.trim())){
					 //send call to observable to set the board
					 selectedIndex=i+1;
					 found=true;
					// System.out.println("getValselected: @@@found "+selectedIndex);
					 break;
				 }
				 else{
					 //Send msg to view that config does not exist
				 }//end else
			 
			 }//end for
			 //System.out.println("getValselected: @@@before model call ");
			 if(found){scModel.setSelectedBoard(selectedIndex);}
		
		}//end function getValSelected
		
 /** PROGRESS BAR FUNCTIONS *******/ 	
		/* The function startProgressThread
		 * Starts a thread for this activity 
		 * The conditions for the thread to run are:
		 * if the progress bar values lie between 0 and 100%
		 * if the thread is manually stopped on reset
		 * @see java.lang.Thread#run()
		 */
		//
		private void startProgressThread(){
		
			pthread=new Thread(new Runnable(){
				 public void run() {
					 
					 while (progressStatus <= 100  ) {
						 
						 mHandler.post(new Runnable() {
		                        public void run() {
		                            mProgress.setProgress(progressStatus);}//end run
		                        });//end handler post
						 
						 
					 }//end while
					 
				 }//end abstract method implementation run
				
			});//end Thread constructor
			
			pthread.start();
		
		}//end start progress thread
		
		/*The Callback function Handler
		 * for the thread
		 *  @see android.os.Handler#handleMessage(android.os.Message)
		 *
		 */	
		Handler handler = new Handler(new Handler.Callback() {

			/*The implementation of the abstract function
			 * @param - android.os.Message the message sent by the Thread
			 * 
			 */	
	       @Override
	        public boolean handleMessage(final Message msg) {
	            runOnUiThread(new Runnable() {
	                 public void run() {
	                	     mProgress.setProgress(progressStatus);
	                 }
	             });
	            return false;
	        }
	    });//end handler callback
		
		/*The function handleReset
		 * @param - View
		 * 
		 */	
      
 /*       public void handleReset(View v) {
        	try{
				//String name = getResources( ).getIdentifier( v.getId( ) );
        		//if(v.getId()==R.id.reset)
        	}
        	catch(Exception ee){}
        }*/
   /** DIALOG FUNCTIONS *******/     
        /*The function ShowMessage
         * Displays a dialog box
         * MainActivity is the listener
		 * @param - String the message to display
		 * 
		 */	
      
        public void ShowMessage(String message) {
        	try{
        		
        		adbuilder.setMessage(message);
           		
        		adbuilder.show();
           		

        	}
        	catch(Exception ee){System.out.println("MAShow Message: dialog builder is null "+ee.getMessage());}
        }
  
		
		//public void onClick(DialogInterface d,int m){
        public void onClick(DialogInterface d,int m){
				d.dismiss();

		}
       

}//end of class
