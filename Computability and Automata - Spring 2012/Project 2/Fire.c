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

//rows and columns of the grid
const int height = 25;
const int width = 25;
//turn counters
int copTurns = 0, robberTurns = 0;
int rx, ry, cx, cy;
bool caught = false;

//enum to track types of "tiles"
enum {COP, ROBBER, EMPTY};

//struct for a pseudo object for the grid tiles
//state refers to the enum above
typedef struct
{
    int x;
    int y;
    int state;
} TILE_T;

//three functions
//draw the grid to the console
void drawGrid(TILE_T grid[height][width]);
//decide the robbers movement
void moveRobber(TILE_T grid[height][width]);
//decide the movement of the cop(s)
bool moveCops(TILE_T grid[height][width]);

int main(void)
{
    //output a description of what is going on and wait for the user to do anything
    printf("This program simulates a Robber being chased by one Cop.\n");
    printf("This program was written by Tyler Tracey (n00767255).\n");
    printf("The .'s simulate an empty space in the grid. The C is the\n");
    printf("cop and the R is the robber. The flashing is due to the clrscrn\n");
    printf("function. Sometimes the Robber and Cop will go off of the top of\n");
    printf("the screen, but that is because of the height of the grid.\n");
    printf("Thank you and enjoy grading. Press <ENTER> to begin the program:");
    getchar();

    //make a grid of tiles and start the random seeding for use later
    TILE_T grid[height][width];
    srand(time(NULL));

    //counter and temporary random variables
    int i,j,ranHeight,ranWidth;
    //set every tile in the grid to empty
    for(i = 0; i<height; i++)
    {
        for(j = 0; j<width; j++)
        {
            grid[i][j].x = i;
            grid[i][j].y = j;
            grid[i][j].state = EMPTY;
        }
    }

    //draw the beginning grid
    drawGrid(grid);
    sleep(1);

    //get random starting points for the robber
    ranHeight = rand() % height;
    ranWidth = rand() % width;

    //set the robbers state
    rx = ranHeight;
    ry = ranWidth;
    grid[ranHeight][ranWidth].state = ROBBER;
    drawGrid(grid);
    sleep(1);

    //refresh the random vars and start the cop on a new random spot
    //0.0001 chance they both start on the same location
    ranHeight = rand() % height;
    ranWidth = rand() % width;

    //set the first cops state
    cx = ranHeight;
    cy = ranWidth;
    grid[ranHeight][ranWidth].state = COP;
    drawGrid(grid);
    sleep(1);

    //until the robber has been caught, update the grid
    while(!caught)
    {
        //move the robber and draw the grid
        moveRobber(grid);
        drawGrid(grid);
        sleep(1);

        //move the cops and make sure the robber hasn't been caught yet
        caught = moveCops(grid);
        drawGrid(grid);
        sleep(1);
    }

    printf("\tPress [ENTER] to end the program");
    getchar();

    return 0;
}

void drawGrid(TILE_T grid[height][width])
{
    int i, j;
    system("cls");
    //clearScreen();

    //print every tile in as readable a manner as possible
    for(i=0; i<height; i++)
    {
        for(j=0; j<width; j++)
        {
            if(grid[i][j].state == EMPTY)
            {
                printf(". ");
            }
            else if(grid[i][j].state == COP)
            {
                printf("C ");
            }
            else if(grid[i][j].state == ROBBER)
            {
                printf("R ");
            }
        }
        printf("\n");
    }

    printf("Cop Turns: %d\n",copTurns);
    printf("Robber Turns: %d\n", robberTurns);
    printf("Total Turns: %d\n", copTurns+robberTurns);
}


void moveRobber(TILE_T grid[height][width])
{
    robberTurns++;
    int ran = rand() % 4;
    bool changed = false;

    switch(ran)
    {
        //change the state of the current position and move the robber right
        case 0:
            grid[rx][ry].state = EMPTY;
            rx++;
            if(rx < 0)
            {
                rx = width;
            }
            rx %= width;
            break;
            //move the robber left
        case 1:
            grid[rx][ry].state = EMPTY;
            rx--;
            if(rx < 0)
            {
                rx = width;
            }
            rx %= width;
            break;
            //move the robber down
        case 2:
            grid[rx][ry].state = EMPTY;
            ry++;
            if(ry < 0)
            {
                ry = height;
            }
            ry %= height;
            break;
            //move the robber up
        case 3:
            grid[rx][ry].state = EMPTY;
            ry--;
            if(ry < 0)
            {
                ry = height;
            }
            ry %= height;
            break;
        default:
            break;
    }

    //set the state of the robbers new tile
    grid[rx][ry].state = ROBBER;

}

bool moveCops(TILE_T grid[height][width])
{
    copTurns++;
    bool caught;

    //move the cop based on the location of the robber
    if(cx < rx)
    {
        grid[cx][cy].state = EMPTY;
        cx++;
        grid[cx][cy].state = COP;
    }
    else if(cx > rx)
    {
        grid[cx][cy].state = EMPTY;
        cx--;
        grid[cx][cy].state = COP;
    }

    if(cy < ry)
    {
        grid[cx][cy].state = EMPTY;
        cy++;
        grid[cx][cy].state = COP;
    }
    else if(cy > ry)
    {
        grid[cx][cy].state = EMPTY;
        cy--;
        grid[cx][cy].state = COP;
    }

    if(cx == rx && cy == ry)
    {
        return true;
    }
    else
    {
        return false;
    }
}

//i tried to implement a function that would let me clear the screen
//faster than the system call to get rid off, or at least decrease,
//the screen flickering in the console. No luck.
int clearScreen ( void )
{
  int i;

  for ( i = 0; i < height; i++ )
    putchar ( '\n' );

  return 0;
}
