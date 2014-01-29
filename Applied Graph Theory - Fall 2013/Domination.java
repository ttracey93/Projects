package domination;

import java.util.Random;
import java.util.Scanner;

class RandomGraph {
    public  int current_edge_weight;                     // used in
							//    next_neighbor
    public int[][] M;					// adjacency matrix to represent a graph
    private int n;					// number of cols/rows
    private int nVerts;					// number of vertices
    private int x;					// row pointer
    private int y;                                       // column pointer
    private int[] next;  
    public int[] D2;// array to track next neighbor

    public RandomGraph(int a, int prob, long theseed) {
        double b;			// declare & initialize variables
        int i, j;

        // more initializations
        n = a;						// initialize n to number of cols/rows
        x = 0; 						// initialize number of rows to zero
        y = 0;                                            // initialize number of columns to zero
        M = new int[n][n];             			// initialize 2D array to all zeros
        nVerts = n;					// initialize number of vertices
        next = new int[n];   
        D2 = new int[n];// next neighbor for each vertex

        for(i=0; i < nVerts; i++) {			// initialize next neighbor
            next[i]=-1;
            D2[i] = -1;
        }

        Random generator = new Random();
        Random generator2 = new Random(theseed);
        Random mygenerator; 

        if (theseed == -1) {
            mygenerator = generator;
        }
        else {
            mygenerator = generator2;
        }
            
        for(i=0; i < nVerts; i++) {
            for(j=0; j < nVerts; j++) {
                if (i == j) {
                    M[i][j]=0;
                }
                else if (j < i) {
                    M[i][j] = M[j][i];
                }
                else {
                    b = mygenerator.nextDouble() * 100;
                    if (b <= prob) {
                        M[i][j] = 1;
                    }
                    else {
                        M[i][j] = 0;                  
                    }
                }
            }
        }
    }// end constructor
//------------------------------------------------------------------------
    public void insertVertex(int a, int x, int y) { //insert a vertex
        if(x == y) {					// if M[i][i]
            if(a != 0) {
                System.out.println("Cannot initialize graph, M[i][i] must be zero!  Exiting...");
                System.exit(0);
            }// end if
        }// end outer if

        M[x][y] = a;					// insert vertex into matrix M

    }// end method insertVertex()
//------------------------------------------------------------------------
    public void display() {
        System.out.println("");    				// display the array
        for(int row=0; row<n; row++) {
            for(int col=0; col<n; col++) {
                System.out.print(M[row][col] + " ");
            }
            System.out.println("");
        }// end for
    }// end method display()
//------------------------------------------------------------------------
    public int vertices()
    {
        return nVerts;					// return the number of vertices
    }// end method vertices()
//------------------------------------------------------------------------
    public int edgeLength(int a, int b)
    {
        return M[a][b];	
    }// end method edgeLength()
//------------------------------------------------------------------------
    public int nextneighbor(int v)
    {
        next[v] = next[v] + 1; 				// initialize next[v] to the next neighbor

        if(next[v] < nVerts) {
            while(M[v][next[v]] == 0 && next[v] < nVerts) {
                next[v] = next[v] + 1;                         // initialize next[v] to the next neighbor

                if(next[v] == nVerts) {
                    break;
                }
            }// end while

        }// end if

        if(next[v] >= nVerts) {
            next[v]=-1;                                    // reset to -1
            current_edge_weight = -1;
        }
        else {
            current_edge_weight = M[v][next[v]];
        }

        return next[v];      				// return next neighbor of v to be processed

    }// end method nextneighbor
//---------------------------------------------------------------------------
    public void resetnext()
    {
        for (int i=0; i < nVerts; i++) {	// reset the array next to all -1's
            next[i] = -1;
        }
    }// end method resetnext()

}// end class graph

public class Domination {
    public static void main(String[] args) {
        //vars
        long startTime = System.currentTimeMillis();
        long finishTime;
        Scanner in = new Scanner(System.in);
        Random rand = new Random();
        RandomGraph[] graphs;
        int[] domSet;
        int[] romanWeight = null;
        int[][] D3 = null;
        int numGraphs;
        int edgeProb;
        int numVerts;
        int domIsLower = 0;
        long seed = -1;
        
        //get input
        System.out.print("Number of graphs to make? ");
        numGraphs = in.nextInt();
        domSet = new int[numGraphs];
        graphs = new RandomGraph[numGraphs];
        D3 = new int[numGraphs][30];
        romanWeight = new int[numGraphs];
        for(int i=0; i<numGraphs; i++) {
            domSet[i] = -1;
            for(int j=0; j<30; j++) {
                D3[i][j] = -1;
            }
        }
        
        //make graphs
        for(int i=0; i<numGraphs; i++) {
            edgeProb = rand.nextInt(70) + 10;
            numVerts = rand.nextInt(18) + 12;
            graphs[i] = new RandomGraph(numVerts, edgeProb, seed);
            System.out.println("Graph " + i + " NumVerts: " + numVerts + " EdgeProb: " + edgeProb);
            //graphs[i].display();
            System.out.println();
        }
        
        //find dominating set for each graph
        int temp = 0, temp2 = 0, temp3 = 0;
        
        for(int i=0; i<numGraphs; i++) {
            int[][] M = graphs[i].M;
            int[] D = new int[graphs[i].vertices()];
            int[] D2 = new int[graphs[i].vertices()];
            for(int j=0; j<graphs[i].vertices(); j++) {
                D[j] = 0;
            }
            int sizeOfDom = 0;
            temp=0;
            for(int v=0; v<graphs[i].vertices(); v++) {
                for(int v2=v; temp<graphs[i].vertices(); temp++) {
                    v2 %= graphs[i].vertices();
                    if(D[v2] == 0) {
                        D[v2] = 1;
                        D2[v2] = 1;
                        sizeOfDom++;
                        for(int j=0; j<graphs[i].vertices(); j++) {
                            if(M[v2][j] == 1) {
                                D[j] = 1;
                            }
                        }
                    }
                    v2++;
                }
                temp=0;
                if(domSet[i] > sizeOfDom || domSet[i] == -1) {
                    domSet[i] = sizeOfDom;
                    graphs[i].D2 = D2;
                    for(int k=0; k<graphs[i].vertices(); k++) {
                        D3[i][k] = D2[k];
                    }
                }
                for(int p=0; p<graphs[i].vertices(); p++) {
                    D2[p] = 0;
                }
                sizeOfDom = 0;
                for(int j=0; j<graphs[i].vertices(); j++) {
                    D[j] = 0;
                }
            }
            //for(int q=0; q<graphs[i].vertices(); q++) {
            //    System.out.print(D3[i][q] + " ");
            //}
        }
        
        //find roman weight for each graph
        temp = 0;
        temp2 = 0;
        temp3 = 0;
        
        for(int i=0; i<numGraphs; i++) {
            int[][] M = graphs[i].M;
            int[] W = new int[graphs[i].vertices()];
            for(int v=0; v<graphs[i].vertices(); v++) {
                if(D3[i][v] == 1) {
                    W[v] = 2;
                }
                else {
                    W[v] = 0;
                }
            }
            temp = 0;
            for(int v=0; v<graphs[i].vertices(); v++) {
                temp += W[v];
            }
            romanWeight[i] = temp;
            temp = 0;
            for(int v=0; v<graphs[i].vertices(); v++) {
                if(W[v] == 2) {
                    //check neighbors
                    for(int n=0; n<graphs[i].vertices(); n++) {
                        if(W[n] == 0) {
                            if(M[v][n] == 1) {
                                for(int n2=0; n2<graphs[i].vertices(); n2++) {
                                    if(M[n][n2] == 1) {
                                        if(W[n2] == 2) {
                                            temp++;
                                        }
                                    }
                                }
                            }
                        }
                        if(temp > 1) {
                            temp = 0;
                            temp2++;
                        }
                        else {
                            break;
                        }
                        if(n == graphs[i].vertices()) {
                            W[v] = 1;
                        }
                    }
                }
            }
            for(int v=0; v<graphs[i].vertices(); v++) {
                if(W[v] == 1) {
                    for(int n=0; n<graphs[i].vertices(); n++) {
                        if(M[v][n] == 1) {
                            if(W[n] == 2) {
                                W[v] = 0;
                            }
                        }
                    }
                }
            }
            temp = 0;
            for(int v=0; v<graphs[i].vertices(); v++) {
                temp += W[v];
            }
            romanWeight[i] = temp;
            temp = 0;
        }        
        
        temp = 0;
        temp2 = 0;
        //output graph data for all graphs
        for(int i=0; i<numGraphs; i++) {
            //output min dom size
            for(int j=0; j<graphs[i].vertices(); j++) {
                if(D3[i][j] == 1) {
                    temp++;
                }
            }
            System.out.println("Minimum dominating set size for graph " + i + " is " + temp);
            
            //output roman size
            System.out.println("Minimum roman weighting for graph " + i + " is " + romanWeight[i]);
            
            
            //output if dom size is half or less than roman
            if(temp <= romanWeight[i]/2.0) {
                temp2++;
            }
            temp = 0;
        }
        
        //output % of graphs wiht dom num half or less of roman weight
        temp3 = 0;
        double percentGraphs = (double)temp2/(double)numGraphs;
        System.out.println("The percentage of graphs with dom number half or less of roman weight is " + percentGraphs*100 + "%");
        
        finishTime = System.currentTimeMillis();
        long finalTime = finishTime - startTime;
        double actualTime = finalTime/(10000.0);
        
        System.out.println("Time to finish testing: " + actualTime + " seconds");
        
    }
}
