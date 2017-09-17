import java.util.*;

/****************************************************************
 * studPlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minue the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

public class lvu2Player extends Player {
	/*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
	 */
	public void move(GameState state)
	{
		int maxDepth = 5;
		for(;;){
			move = maxAction(state, maxDepth);//System.out.println("move: "+move);
			maxDepth+=5;
		}
	}

	// Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call minAction with 5 parameters.
	public int maxAction(GameState state, int maxDepth)
	{
		int[] moveList = new int[6];
		Arrays.fill(moveList, Integer.MIN_VALUE);
		for(int i =0;i<6;i++){
			GameState altState = new GameState(state);
			if(!altState.illegalMove(i)){
				//				if(altState.applyMove(i)){
				//					return i;
				//				}else{
//				GameState takeState = new GameState(state);
//				takeState.applyMove(i);
//				
//				if(takeState.stoneCount(6)> state.stoneCount(6)+2){
//					return i;
//				}
				if(altState.applyMove(i)){
					moveList[i] = maxAction(altState, 1, maxDepth,Integer.MIN_VALUE,Integer.MAX_VALUE);
				}else{
					moveList[i] = minAction(altState, 1, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
				}
				//}
			}
		}

		int maxVal = moveList[0];
		int ind = 0;
		for(int i = 0; i < 6; i++){
			if(maxVal < moveList[i]){
				maxVal = moveList[i];
				ind =i;
			}
		}//System.out.println("0: "+moveList[0]+" 1: "+moveList[1]+" 2: "+moveList[2]+" 3: "+moveList[3]+" 4: "+moveList[4]+" 5: "+moveList[5]);
		return ind;
	}

	//return sbe value related to the best move for max player
	public int maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
	{
		if(state.gameOver()){
			return sbe(state);
		}else{
			int v = Integer.MIN_VALUE;
			if(currentDepth != maxDepth){
				for(int i = 0; i < 6; i++){
					GameState altState = new GameState(state);
					if(!altState.illegalMove(i)){
						if(altState.applyMove(i)){
							v = Math.max(v, maxAction(altState, currentDepth+1, maxDepth, alpha, beta));
						}else{
							v = Math.max(v, minAction(altState, currentDepth+1, maxDepth, alpha, beta));
						}
						if (v >= beta){
							return v;
						}else{
							alpha = Math.max(alpha, v);
						}
					}
				}return v;
			}else{
				return sbe(state);
			}
		}
	}
	//return sbe value related to the bset move for min player
	public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
	{
		if(state.gameOver()){
			return sbe(state);
		}else{
			int v = Integer.MAX_VALUE;
			if(currentDepth != maxDepth){
				for(int i = 7; i < 13; i++){
					GameState altState = new GameState(state);
					if(!altState.illegalMove(i)){
						if(altState.applyMove(i)){
							v = Math.min(v, minAction(altState, currentDepth+1, maxDepth, alpha, beta));
						}else{
							v = Math.min(v, maxAction(altState, currentDepth+1, maxDepth, alpha, beta));
						}
						if (alpha >= v){
							return v;
						}else{
							beta = Math.min(beta, v);
						}
					}else{

					}
				}return v;
			}else{
				return sbe(state);
			}
		}
	}

	//the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
	private int sbe(GameState state)
	{
		int yourBin = (state.stoneCount(0)+state.stoneCount(1)+state.stoneCount(2)+state.stoneCount(3)
				+state.stoneCount(4)+state.stoneCount(5)+state.stoneCount(6));
		int opBin = (state.stoneCount(7)+state.stoneCount(8)+state.stoneCount(9)+state.stoneCount(10)+
				state.stoneCount(11)+state.stoneCount(12)+state.stoneCount(13));
		int sbe = 0;
		//sbe += yourBin - opBin;
		sbe += (state.stoneCount(6)-state.stoneCount(13));
		
		if(state.neighborOf(5)==0){
			sbe-=state.stoneCount(5);
		}
		if(state.neighborOf(1)==0){
			sbe-=state.stoneCount(1);
		}
		if(state.neighborOf(2)==0){
			sbe-=state.stoneCount(2);
		}
		if(state.neighborOf(3)==0){
			sbe-=state.stoneCount(3);
		}
		if(state.neighborOf(4)==0){
			sbe-=state.stoneCount(4);
		}
		if(state.neighborOf(0)==0){
			sbe-=state.stoneCount(0);
		}


		return sbe;
	}
}

