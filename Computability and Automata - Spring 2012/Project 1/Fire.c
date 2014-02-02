#ifdef __unix__
#include <unistd.h>
#elif defined _WIN32
# include <windows.h>
#define sleep(x) Sleep(1000 * x)
#endif

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>

//constants and global.
const int height = 25;
const int width = 25;
int burned = 0;
int pro = 0;
int unpro = 0;

//An enum to keep traqck of what each TILE_T is easily
enum {EMPTY, UNPROTECTED, PROTECTED, BURNED};

//A new type, TILE_T. These "objects" will be used in the grid.
typedef struct
{
    int x;
    int y;
    int state;
} TILE_T;

//prototypes
void drawGrid(TILE_T grid[height][width]);
bool updateFire(TILE_T grid[height][width],bool go);
void updateProtection(TILE_T grid[height][width], bool go);

//main
int main(void)
{
    //make an array of Tiles and set unpro to the appropriate amount
    TILE_T grid[height][width];
    unpro = height*width;

    srand(time(NULL));

    //make every tile in the array unprotected
    int i,j,ranHeight,ranWidth;
    for(i = 0; i<height; i++)
    {
        for(j = 0; j<width; j++)
        {
            grid[i][j].x = i;
            grid[i][j].y = j;
            grid[i][j].state = UNPROTECTED;
        }
    }

    //an explanation of what the program does. Specifically for the professor. Talks about
    //a few flaws in the program as well.
    printf("This is the FireFighter simulation, coded by Tyler Tracey.\n\n");
    printf("\tThis program uses the letter F to simulate a burned area,\n");
    printf("\t0's to simulate an unprotected area, and 1's to simulate\n");
    printf("\ta protected space. The grid is %d by %d represented by ASCII\n",height,width);
    printf("\tcharacters. There may be some slight flashing due to rapid\n");
    printf("\tclearing of the terminal to redraw the grid cleanly. Also,\n");
    printf("\tyou will have to close the program yourself. It will be clear\n");
    printf("\tto you when execution is finished. I wanted to have a natural\n");
    printf("\tpause similar to this one, but something wasn't working correctly.\n\n");
    printf("\tPress [ENTER] to start the program");
    getchar();

    //clear the screen, draw the fresh grid, wait a second

    drawGrid(grid);
    sleep(1);

    //start the fire in a random location
    ranHeight = rand() % height;
    ranWidth = rand() % width;

    grid[ranHeight][ranWidth].state = BURNED;

    //draw the grid with the new fire, inc and dec globals

    burned++;
    unpro--;
    drawGrid(grid);
    sleep(1);

    ranHeight = rand() % height;
    ranWidth = rand() % width;

    grid[ranHeight][ranWidth].state = PROTECTED;

    //same stuff for the first firefighter

    pro++;
    unpro--;
    drawGrid(grid);
    sleep(1);

    bool spreadable = true;

    //whiel the fire is still spreading, update the grid continuously
    while(spreadable)
    {
        spreadable = updateFire(grid,spreadable);

        drawGrid(grid);
        sleep(1);

        updateProtection(grid,spreadable);

        drawGrid(grid);
        sleep(1);
    }

    //end prompt. NOT WORKING CORRECTLY. Sometimes it appears, sometimes it does not.
    printf("\tPress [ENTER] to end the program");
    getchar();

    return 0;
}//end main

//draw function. displays the grid
void drawGrid(TILE_T grid[height][width])
{
    int i, j;
    system("cls");

    //iterate through the grid drawing the appropriate symbol to the console for each tile
    for(i=0; i<height; i++)
    {
        for(j=0; j<width; j++)
        {
            if(grid[i][j].state == EMPTY)
            {
                printf("  ");
            }
            else if(grid[i][j].state == UNPROTECTED)
            {
                printf("0 ");
            }
            else if(grid[i][j].state == PROTECTED)
            {
                printf("1 ");
            }
            else if(grid[i][j].state == BURNED)
            {
                printf("F ");
            }
        }
        //if at the end of a row, print a newline
        printf("\n");
    }

    float denom = 25.0*25.0;

    //print info about the grid
    printf("Burned: %d\n",burned);
    printf("Protected: %d\n",pro);
    printf("Unprotected: %d\n", unpro);
    printf("Percent burned: %f\n",(burned/denom)*100);
}//end draw

//updatefire function: updates the locations of the fire
bool updateFire(TILE_T grid[height][width], bool go)
{
    int i,j,count=0,initial=0,iterator=0;

    //simply count how many burned tiles there are
    for(i=0; i<height; i++)
    {
        if(!go)
        {
            break;
        }

        for(j=0; j<width; j++)
        {
            if(grid[i][j].state == BURNED)
            {
                initial++;
            }
        }
    }

    TILE_T pGrid[initial];

    //add all the burned tiles to a new, secondary array
    for(i=0; i<height; i++)
    {
        if(!go)
        {
            break;
        }
        for(j=0; j<width; j++)
        {
            if(grid[i][j].state == BURNED)
            {
                pGrid[count++] = grid[i][j];
            }
        }
    }

    count=0;

    //iterate through the new array and change the tiles around each burned tile that CAN be changed
    for(count=0; count<initial; count++)
    {
        if(!go)
        {
            break;
        }

        //use the new array as indices for the old array
        if(pGrid[count].x-1 > -1 && grid[pGrid[count].x-1][pGrid[count].y].state == UNPROTECTED)
        {
            grid[pGrid[count].x-1][pGrid[count].y].state = BURNED;
            iterator++;
            burned++;
            unpro--;
        }

        if(pGrid[count].x+1 < width+1 && grid[pGrid[count].x+1][pGrid[count].y].state == UNPROTECTED)
        {
            grid[pGrid[count].x+1][pGrid[count].y].state = BURNED;
            iterator++;
            burned++;
            unpro--;
        }

        if(pGrid[count].y-1 > -1 && grid[pGrid[count].x][pGrid[count].y-1].state == UNPROTECTED)
        {
            grid[pGrid[count].x][pGrid[count].y-1].state = BURNED;
            iterator++;
            burned++;
            unpro--;
        }

        if(pGrid[count].y+1 < height && grid[pGrid[count].x][pGrid[count].y+1].state == UNPROTECTED)
        {
            grid[pGrid[count].x][pGrid[count].y+1].state = BURNED;
            iterator++;
            burned++;
            unpro--;
        }
    }

    //if nothing was burned, stop the loop in main
    if(iterator == 0)
    {
        return false;
    }
    else
    {
        return true;
    }
}//end fire

//updateProtection: updates the locations of the protected Tiles
void updateProtection(TILE_T grid[height][width], bool go)
{
    static int firefighters = 0;
    int i,j,x,y,count,check;
    firefighters+=3;

    //count how many unprotected spaces are left
    for(i=0; i<height; i++)
    {
        if(!go)
        {
            break;
        }

        for(j=0; j<width; j++)
        {
            if(grid[i][j].state == UNPROTECTED)
            {
                count++;
            }
        }
    }

    //if there are less spaces than firefighters, we will use the as the loop condition instead
    //of the numFireFighters.
    if(count < firefighters)
    {
        check = count;
    }
    else
    {
        check = firefighters;
    }

    //protect as many unpro spaces as there are firefighters
    for(i=0; i<check; )
    {
        x=rand() % width;
        y=rand() % height;

        if(grid[x][y].state == UNPROTECTED)
        {
            grid[x][y].state = PROTECTED;
            i++;
            pro++;
            unpro--;
        }
        else
        {
            continue;
        }
    }
}
