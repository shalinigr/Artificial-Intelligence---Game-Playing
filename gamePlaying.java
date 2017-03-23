import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class homework {
	//TODO: 
	//Read input.txt(N,MODE,YOUPLAY,DEPTH,CELLVALUES AND BOARD STATES)
	//obtain current board state and values
	//Perform the algorithm based on mode
	//check algorithm according to requirement
	//run for stake and raid and choose best move
	//game score computation
	//return ouput.txt
	enum gameMode {
		MINIMAX,
		ALPHABETA
	}
	public static int N = -1;
	public static gameMode mode = null;
	public static String play = "";
	public static int depth = -1;
	public static int[][] cellValues = null;
	public static String[][] boardState = null;	
	public static int finalRowCell = -1;
	public static int finalColCell = -1;
	public static int finalMaxStakeScore = -1;
	public static int finalMaxRaidScore = -1;
	public static String moveMade = "";
	public static void main(String[] args) {	
		try
		{
			String inputFileName = "input.txt";					
			BufferedReader br = new BufferedReader(new FileReader(inputFileName));	
			N =Integer.parseInt(br.readLine());
			if( N < 0 || N > 26)
			{br.close();				
			return; // Do this to make sure N doesn't exceed  26 or negative
			}
			cellValues = new int[N][N];
			boardState = new String[N][N];
			mode = gameMode.valueOf(br.readLine());
			play = br.readLine();
			depth = Integer.parseInt(br.readLine());
			for(int i=0;i<N;i++)
			{
				String str = br.readLine();
				String [] strArr = str.split(" ");
				for(int j=0;j<N;j++)
				{

					cellValues[i][j] =Integer.parseInt(strArr[j]);
				}
			}
			for(int i=0;i<N;i++)
			{
				String str = br.readLine();				
				for(int j=0;j<N;j++)
				{
					if( str.charAt(j) == '.')
						boardState[i][j] =".";
					else if(str.charAt(j) == 'X')
						boardState[i][j] = "X";
					else
						boardState[i][j] = "O";
				}
			}
			br.close();
			if(mode == gameMode.MINIMAX)
				minimax();
			else if(mode  == gameMode.ALPHABETA)
				alphabeta();	
			finalMove("output.txt");

		}
		catch(Exception ex)
		{
			System.out.println("error");
		}	

	}
	public static boolean isRaidPossible(String[][] brdState, int rowCell, int colCell,String currPlay)
	{			
		String opponent = "";
		if(currPlay.equals("X"))
			opponent = "O";
		else
			opponent = "X";
		brdState[rowCell][colCell] = currPlay;
		boolean myMove = false;
		boolean opponentMove = false;	
		//check for same move neighbors on all fours
		if(rowCell!=0)
		{
			if(brdState[rowCell][colCell].equals(brdState[rowCell-1][colCell]))
				myMove = true;				
		}
		if(colCell != 0)
		{
			if(brdState[rowCell][colCell].equals(brdState[rowCell][colCell-1]))
				myMove = true;

		}
		if(rowCell!= N - 1)
		{
			if(brdState[rowCell][colCell].equals(brdState[rowCell+1][colCell]))
				myMove = true;

		}
		if(colCell!= N - 1)
		{
			if(brdState[rowCell][colCell].equals(brdState[rowCell][colCell+1]))
				myMove = true;

		}

		//check for opponent neighbors

		if(rowCell!=0)
		{
			if(brdState[rowCell-1][colCell].equals(opponent))
				opponentMove = true;				
		}
		if(colCell != 0)
		{
			if(brdState[rowCell][colCell-1].equals(opponent))
				opponentMove = true;
		}
		if(rowCell!= N - 1)
		{
			if(brdState[rowCell+1][colCell].equals(opponent))
				opponentMove = true;
		}
		if(colCell!= N - 1)
		{		
			if( brdState[rowCell][colCell+1].equals(opponent))
				opponentMove = true;
		}
		brdState[rowCell][colCell] = ".";
		boolean isRaidPossible = myMove && opponentMove;
		return isRaidPossible;
	}

	private static String[][] raid(String[][] brdState,int rowCell,int colCell,String currPlay)
	{

		String opponent = "";
		if(currPlay.equals("X"))
			opponent = "O";
		else
			opponent = "X";
		String[][] localBrdState = boardCopy(brdState);	
		localBrdState[rowCell][colCell] = currPlay;
		if(colCell != 0){

			if(localBrdState[rowCell][colCell-1].equals(opponent)) localBrdState[rowCell][colCell-1] = currPlay;
		}
		if( colCell != N-1){

			if(localBrdState[rowCell][colCell+1].equals(opponent))localBrdState[rowCell][colCell+1] = currPlay;			
		}
		if(rowCell != 0){

			if(localBrdState[rowCell - 1][colCell].equals(opponent)) localBrdState[rowCell - 1][colCell] = currPlay;
		}
		if( rowCell != N-1){

			if(localBrdState[rowCell +1][colCell].equals(opponent))localBrdState[rowCell +1][colCell] = currPlay;		

		}

		return localBrdState;
	}

	private static String[][] makeMove(int rowCell, int colCell,String[][] brdState,String currPlay)
	{
		String[][] move = boardCopy(brdState);
		if(currPlay.equals("X"))
			move[rowCell][colCell] = "X";
		else if(currPlay.equals("O"))
			move[rowCell][colCell] = "O";
		return move;

	}
	private static boolean terminalTest(int currDepth, String[][] brdState)
	{

		int occupied = 0;
		int totalCells = N*N;
		for(int i=0;i<N;i++) //evaluate if board is full
		{
			for(int j=0;j<N;j++)
			{
				if(!brdState[i][j].equals("."))occupied++;			  
			}
		}

		if(currDepth == 0 || occupied == totalCells)				
			return true;		
		else
			return false;
	}
	private static int eval(String[][] brdState)
	{
		int XScore=0;
		int OScore = 0;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(!brdState[i][j].equals("."))
				{
					if(new String(brdState[i][j]).equals("X"))
						XScore+= cellValues[i][j];
					else
						OScore+= cellValues[i][j];
				}
			}
		}

		if(play.equals("X"))		
			return (XScore - OScore);			
		else			
			return(OScore - XScore);

	}
	private static int max(int currDepth,String[][] brdState,int alpha,int beta)
	{
		int gameScore = -1;
		int prevScore = Integer.MIN_VALUE;	
		int stakeI = -1;
		int stakeJ = -1;
		int raidI = -1;
		int raidJ = -1;
		int stakeVal = -1;
		int raidVal = -1;
		String[][] localBrdState = new String[N][N];
		if(terminalTest(currDepth,brdState))
		{			
			gameScore = eval(brdState);	
			return gameScore;
		}

		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(brdState[i][j].equals("."))
				{
					localBrdState = makeMove(i, j,brdState,play);					
					gameScore = min(currDepth-1,localBrdState,alpha,beta);

					if(gameScore > prevScore)
					{
						prevScore = gameScore;//best score until now
						stakeI =i; // best move row cell value
						stakeJ = j;// best move col cell value
					}
					brdState[i][j] = ".";
					if(mode == gameMode.ALPHABETA)
					{
						if(prevScore>=beta)
						{
							stakeVal = prevScore;
							finalMaxStakeScore = stakeVal;
							return stakeVal;
						}
						alpha=Math.max(alpha, prevScore);
					}
				}
			}
		}
		stakeVal = prevScore;
		prevScore = Integer.MIN_VALUE;
		gameScore = -1;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(brdState[i][j].equals("."))
				{
					if(isRaidPossible(brdState, i, j,play))
					{
						localBrdState = raid(brdState,i,j,play);					
						gameScore = min(currDepth-1,localBrdState,alpha,beta);

						if(gameScore > prevScore)
						{						
							prevScore = gameScore;//best score until now
							raidI =i; // best move row cell value
							raidJ = j;// best move col cell value
						}
						if(mode == gameMode.ALPHABETA)
						{
							if(prevScore>=beta)
							{
								raidVal = prevScore;
								finalMaxRaidScore = raidVal;
								return raidVal;
							}
							alpha=Math.max(alpha, prevScore);
						}
					}
				}

			}
		}
		raidVal = prevScore;
		if(stakeVal >= raidVal)
		{
			finalRowCell = stakeI;
			finalColCell = stakeJ;
			finalMaxStakeScore = stakeVal;
			moveMade = "Stake";
			return finalMaxStakeScore;		
		}
		else
		{
			finalRowCell = raidI;
			finalColCell = raidJ;
			finalMaxRaidScore = raidVal;
			moveMade = "Raid";
			return finalMaxRaidScore;
		}



	}
	private static int  min(int currDepth,String[][] brdState,int alpha,int beta)
	{
		int gameScore = -1;
		int prevScore = Integer.MAX_VALUE;		
		int stakeI = -1;
		int stakeJ = -1;
		int raidI = -1;
		int raidJ = -1;
		int stakeVal = -1;
		int raidVal = -1;
		String currPlay = "";
		if(play.equals("X"))
			currPlay  = "O";
		else
			currPlay = "X";
		String[][] localBrdState = new String[N][N];
		if(terminalTest(currDepth,brdState))
		{			
			gameScore =  eval(brdState);
			return gameScore;

		}

		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(brdState[i][j].equals("."))
				{
					localBrdState = makeMove(i, j,brdState,currPlay);					
					gameScore = max(currDepth-1,localBrdState,alpha,beta);

					if(gameScore < prevScore)
					{
						prevScore = gameScore;//best score until now
						stakeI =i; // best move row cell value
						stakeJ = j;// best move col cell value
					}
					brdState[i][j] = ".";
					if(mode == gameMode.ALPHABETA){
						if(prevScore <= alpha)
						{
							stakeVal = prevScore;
							finalMaxStakeScore = stakeVal;
							return stakeVal;
						}
						beta=Math.min(beta, prevScore);
					}
				}
			}
		}
		stakeVal = prevScore;
		prevScore = Integer.MAX_VALUE;
		gameScore = -1;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(brdState[i][j].equals("."))
				{
					if(isRaidPossible(brdState, i, j,currPlay))
					{
						localBrdState = raid(brdState,i,j,currPlay);					
						gameScore = max(currDepth-1,localBrdState,alpha,beta);
						//if(prevScore == -1)
						//prevScore = gameScore;
						if(gameScore < prevScore)
						{						
							prevScore = gameScore;//best score until now
							raidI =i; // best move row cell value
							raidJ = j;// best move col cell value
						}
						if(mode == gameMode.ALPHABETA){
							if(prevScore <= alpha)
							{
								raidVal = prevScore;
								finalMaxRaidScore = raidVal;
								return raidVal;
							}
							beta=Math.min(beta, prevScore);
						}
					}
				}

			}
		}
		raidVal = prevScore;	
		if(stakeVal <= raidVal)	
		{
			finalRowCell = stakeI;
			finalColCell = stakeJ;
			finalMaxStakeScore = stakeVal;
			moveMade = "Stake";
			return finalMaxStakeScore;		
		}
		else	
		{

			finalRowCell = raidI;
			finalColCell = raidJ;
			moveMade = "Raid";
			finalMaxRaidScore = raidVal;
			return finalMaxRaidScore;
		}

	}
	private static String[][] boardCopy(String[][] board)
	{
		String[][] copiedBoard = new String[N][N];
		for(int i = 0; i < N;i++)
		{
			for(int j = 0; j < N;j++)
			{
				copiedBoard[i][j] = board[i][j];
			}
		}
		return copiedBoard;
	}
	private static void minimax()
	{
		int currDepth = depth;
		String[][] brdState = new String[N][N];
		brdState = boardState;
		int score = max(currDepth,brdState,Integer.MIN_VALUE,Integer.MAX_VALUE);		
		//System.out.println(score);
	}
	private static void finalMove(String outputFile)
	{	
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			StringBuilder sb = new StringBuilder();
			char col = (char)((int)'A' + finalColCell);
			sb.append(col);
			sb.append(finalRowCell+1);
			sb.append(' ');		

			sb.append(moveMade);

			//sb.append("\n");
			bw.write(sb.toString());
			bw.newLine();


			if(moveMade.equals("Stake"))	
			{
				boardState[finalRowCell][finalColCell] = play;


			}
			else
			{
				boardState = raid(boardState, finalRowCell, finalColCell, play);

			}
			for(int i = 0;i<N;i++)
			{
				sb =new StringBuilder();
				for(int j = 0;j<N;j++)
				{
					sb.append(boardState[i][j]);
				}
				//sb.append("\n");
				bw.write(sb.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();


		}
		catch(Exception ex)
		{

		}
	}
	private static void alphabeta()
	{
		int currDepth = depth;
		String[][] brdState = new String[N][N];
		brdState = boardState;
		int score = max(currDepth,brdState,Integer.MIN_VALUE,Integer.MAX_VALUE);
		//System.out.println(score);
	}

}
