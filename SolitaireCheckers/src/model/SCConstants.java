package model;
/*
 * interface SCCONSTANTS
 * Purpose: Store constants associated with the solitaire game
 *  
 * @author: Wendy-Anne Daniel
 * @date: October 2013
 * @version:1.0
 * @see: model
 *  
 * 
*/

public interface SCConstants {
	
	/** Default game dimensions. */
    public static final int DEFAULT_COLUMNS = 7;
    public static final int DEFAULT_ROWS    = 7;
    
    public static final int SOLITAIRE = 1;
    public static final int ARROW = 2;
    public static final int CROSS    = 3;
    public static final int DIAMOND    = 4;
    public static final int DOUBLE_ARROW = 5;
    public static final int FIREPLACE    = 6;
    public static final int PLUS    = 7;
    public static final int PYRAMID    = 8;

    public static final String DEFAULT_MESSAGE_WON = new String( "Solitaire Checkers! - I win" );
    public static final String NORMAL_MESSAGE_WON = new String( "Solitaire Checkers! - Congratulations you won" );
    public static final String SUPER_MESSAGE_WON = new String( "Solitaire Checkers - Congratulations you are super winner!" );
    public static final String DEFAULT_MESSAGE_LOST = new String( "Solitaire Checkers - game is lost" );
    public static final String DEFAULT_TEXT_FALSE  = new String( "nopeg" );
    public static final String DEFAULT_TEXT_TRUE   = new String( "peg" );

	
}
