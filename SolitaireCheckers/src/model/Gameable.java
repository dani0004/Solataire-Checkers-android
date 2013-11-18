package model;

/** <<interface>> Gameable
*
* Declares the various methods that gameable
* objects must implement.
*
* @author Gerald.Hurdle@AlgonquinCollege.com
* @version 1.0
*/

public interface Gameable {
	
	

	/* Answers whether or not the game is lost. */
    public boolean isLost();

    /* Answers whether or not the game is won. */
    public int isWon();

    /* Resets the game so it can be played again. */
    public void reset();

    /* Starts the game! */
    public void start();
}
