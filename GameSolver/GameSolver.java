import java.util.ArrayList;
import java.io.*;

public class GameSolver
{
    private static String [] players;   
    private static int [] actions;      
    private static float [] payoff;     
    private static int NoOfPlayers;     
    private static StrategyProfile [] possibleProfiles; 
    private static int NoOfStrategyProfiles;
    private static int [] temp;    
    private static int [] strongDomStrat;
    private static ArrayList<ArrayList<Integer>> weakDomStrat;    
    public static void prints(String arg){
        System.out.println(arg);
	}
    public static void printi(int arg){
     System.out.println(arg);
	}
	public static void printarr(int [] arg)
	{   prints("array print");
		for(int k=0;k<arg.length;k++)
     		System.out.print(arg[k]+" ");
     	prints("");
	}
    public static void next_permutation_profile(int i, int N,int playerkachangenahikarna)      //during call, pass i = 0
    {   
        if(i<N)   
        {               
            if(temp[i]+1>actions[i])        
            {                               
                temp[i] = 1;
                next_permutation_profile(i+1,N,playerkachangenahikarna);
            }
            else
            {
                temp[i] = temp[i] + 1;
            }
        }
    }    
    public static int findIndexOfProfile(int [] temp)
    {
        int i;
        for(i=0;i<NoOfStrategyProfiles;i++)
            if(possibleProfiles[i].compareProfile(temp)==true)
                return i;

        return -1;        
    }
    
    public static float getPayoffOfProfile(int [] temp, int i) //to get Ui for given strategy profile
    {
        int index = findIndexOfProfile(temp);
        if(index==-1)
        {
            System.err.println("Error!");
        }
        
        return possibleProfiles[index].getPayoff(i);
        
    }
    public static void resettemp(){
        for(int l=0;l<NoOfPlayers;l++)
                temp[l]=1;
    }
    public static void findDominantStrategies(int i)  	//finds all dominant strategies for player 'i'
    {   int totalactions=1;
        for(int k=0;k<NoOfPlayers;k++){
            if(k!=i){
                totalactions*=actions[k];
            }
        } 
        int [][]profilepattern = new int[totalactions][NoOfPlayers];
        int totalprofilepattern=0;
        for(int m=0;m<NoOfStrategyProfiles;m++){
            
            if(temp[i]==1){
                for(int n=0;n<NoOfPlayers;n++){
                    profilepattern[totalprofilepattern][n]=temp[n];                    
                }
                totalprofilepattern++;
            }
            next_permutation_profile(0,NoOfPlayers,0);
        }

        float []payoffcomparemain = new float[totalactions];               
        float []payoffcomparetemp = new float[totalactions];               
        for(int j=0;j<totalactions;j++)
            payoffcomparemain[j]=Integer.MIN_VALUE;;
        int strongactionno =0;
        int isstrong =0 ;
        int []weakly = new int[actions[i]];
        int weakly_pointer=0,strongdominant=0;
        int finalstrongdominant =0;
        for(int action=1;action<=actions[i];action++){
            for(int j=0;j<totalprofilepattern;j++){
                profilepattern[j][i]=action;
                payoffcomparetemp[j]=(getPayoffOfProfile(profilepattern[j],i));                
            }
                        
            boolean isgreater=false,isequal=false,isless=false;
            for(int j=0;j<totalactions;j++){
                if(payoffcomparetemp[j]>=payoffcomparemain[j]){
                    if(payoffcomparetemp[j]==payoffcomparemain[j]){                                                
                        isequal =true;
                    }else{
                        isgreater=true;
                    }
                    
                }else{
                    isless=true;
                }      
                payoffcomparemain[j]=Math.max(payoffcomparemain[j],payoffcomparetemp[j]);
            }
            
            if((!isless && !isequal))
                strongdominant=action;
            
            if(isequal || (isgreater&&isless))
                strongdominant=0;
            

            if( (isless && isgreater)  || (isequal && isgreater) || (!isless && !isequal) ){
                weakly_pointer=0;
            }            
             if( (isequal && isgreater && !isless) || (!isless && !isequal) || (!isless && !isgreater)  ){
               weakly[weakly_pointer]=action;
               weakly_pointer++;
            }
            
            
        }//for loop end
        
        strongDomStrat[i]=strongdominant;                
        ArrayList<Integer> inner = new ArrayList<Integer>();     
        for(int j=0;j<weakly_pointer;j++){
            inner.add(weakly[j]);
        }
        weakDomStrat.add(inner);        
    }

/***********************************************************************************************************************************/    
    public static void main(String[] args) throws IOException
    {   prints("------------------------------------");
        int i,index;
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        
        reader.readLine();  
        String alpha = reader.readLine();   
        reader.readLine();    
        String beta = reader.readLine();    
        String [] splitBeta = beta.split(" ");
        
        payoff = new float[splitBeta.length];
        for(i=0;i<payoff.length;i++)
        {
            payoff[i] = Float.parseFloat(splitBeta[i]);
        }       
        
        int startIndex = alpha.indexOf('{');
        int endIndex = alpha.indexOf('}');
        String playerList = alpha.substring(startIndex+2,endIndex-1);
        int lastStartIndex = alpha.lastIndexOf('{');
        int lastEndIndex = alpha.lastIndexOf('}');
        String actionList = alpha.substring(lastStartIndex+2,lastEndIndex-1);
        String [] action = actionList.split(" ");
        String test = playerList.replaceAll("\" \"", ":");
        String test2 = test.substring(1,test.length()-1);
        players = test2.split(":");                
        actions = new int[action.length];
        
        NoOfStrategyProfiles = 1;
        
        for(i=0;i<action.length;i++)
        {
            actions[i] = Integer.parseInt(action[i]);
            NoOfStrategyProfiles = NoOfStrategyProfiles*actions[i];        	
        }      
        
        NoOfPlayers = players.length;
        
        possibleProfiles = new StrategyProfile[NoOfStrategyProfiles];
        for(i=0;i<NoOfStrategyProfiles;i++)
        {
            possibleProfiles[i] = new StrategyProfile(NoOfPlayers);         
        }
                
        
        temp = new int[NoOfPlayers];
        for(i=0;i<NoOfPlayers;i++)  
        {
            temp[i] = 1;
        }
        
        int k=0,j;
        for(i=0;i<NoOfStrategyProfiles;i++)
        {
            for(j=0;j<NoOfPlayers;j++)
            {
                    possibleProfiles[i].addStrategy(temp[j]);
                    possibleProfiles[i].addPayoff(payoff[k]);
                    k++;
            }  
            next_permutation_profile(0,NoOfPlayers,-1);          
        }
                
        strongDomStrat = new int[NoOfPlayers];
        weakDomStrat = new ArrayList<ArrayList<Integer> >();         
         
        for(i=0;i<NoOfPlayers;i++){        
            findDominantStrategies(i);
        }
        boolean flag=false;
        boolean strong=true;
        boolean weak=true;
        for(i=0;i<NoOfPlayers;i++){
            if(strongDomStrat[i]>0){
                prints("Player "+(i+1)+" strongdominant strategy:"+strongDomStrat[i]);
            }else{
                flag=true;
                strong=strong & false;
                prints("Player "+(i+1)+" has NO strongly dominant strategy");
            }
            
        }
        
        boolean flagagain=false;
        for(i=0;i<NoOfPlayers;i++){
            if(flag){
                if(weakDomStrat.get(i).size()>0){
                    prints("------------------------------------");
                    for( j=0;j<weakDomStrat.get(i).size();j++){
                        prints("Player "+(i+1)+" has weakly strategy:"+weakDomStrat.get(i).get(j));
                    }
                }else{
                    weak=weak & false;
                }
                flagagain=true;
            }
        }
        if(flagagain){
                prints("------------------------------------");
                prints(" NO strongly dominant equilibirium");
        }
        prints("------------------------------------");
        if(strong){
            prints("strong dominant equilibirium exists:");
            for(i=0;i<NoOfPlayers;i++)
                prints("\tPlayer "+(i+1)+":"+strongDomStrat[i]);
        }else{
            if(weak){
                resettemp();
                prints("All the weakly equilibirium:");
                printallweakly(weakDomStrat);
            }else{
                prints(" NO weakly dominant equilibirium");
            }
        }
        prints("------------------------------------");
    }//Main function ends here

    public static void printallweakly(ArrayList<ArrayList<Integer> >weakDomStrat){
        int i=0,j=0;
        int []tempcompare = new int[NoOfPlayers];
        boolean firsttimerun=true;

        while(true){
            boolean flag=false;
            for(i=0;i<NoOfPlayers;i++){
                System.out.print("\t"+weakDomStrat.get(i).get(temp[i]-1)+" ");
            }
            prints("");
            
            next_permutation_weakDomStrat(0,NoOfPlayers);
        
            for(i=0;i<NoOfPlayers && !firsttimerun;i++)
                if(temp[i]==1)
                    flag=true;
                else{
                    flag=false;
                    break;
                    }

            firsttimerun=false;            
            if(flag) break;
        }
    }
    public static void next_permutation_weakDomStrat(int i, int N)
    {   
        if(i<N)   
        {               
            if(temp[i]+1>weakDomStrat.get(i).size())        
            {                               
                temp[i] = 1;
                next_permutation_weakDomStrat(i+1,N);
            }
            else
            {
                temp[i] = temp[i] + 1;
            }
        }
    } 
}
/*******************************************************************************************************************/

class StrategyProfile       //The strategy profile and the corresponding payoffs
{
    private static int size;
    private ArrayList payoffs;
    private ArrayList profile;
    
    StrategyProfile(int n)  //constructor
    {
        size = n;
        payoffs = new ArrayList(size);
        profile = new ArrayList(size);
    }
    
    ArrayList getProfile()
    {
        return this.profile;
    }
    
    boolean compareProfile(int [] temp) //compares strategy profile to temp[] values
    {
        int i;
        for(i=0;i<size;i++)
        {
            if(profile.get(i)!=temp[i])
            {
                return false;
            }
        }
        return true;        //returns true if equal, false otherwise
    }
    
    ArrayList getPayoffs()
    {
        return this.payoffs;
    }
    
    //following next_permutation_profiletions used to store and get individual values
    void addPayoff(float payoff)
    {
        payoffs.add(payoff);
    }
    
    float getPayoff(int index)
    {
        return (float)payoffs.get(index);
    }
    
    void addStrategy(int s)
    {
        profile.add(s);
    }
    
    int getStrategy(int index)
    {
        return (int)profile.get(index);
    }
}


