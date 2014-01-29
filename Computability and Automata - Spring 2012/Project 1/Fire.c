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

const int height = 25;
const int width = 25;
int copTurns = 0, robberTurns = 0;
int rx, ry, cx, cy;
bool caught = false;


enum {COP, ROBBER, EMPTY};

typedef struct
{
    int x;
    int y;
    int state;
} TILE_T;

void drawGrid(TILE_T grid[height][width]);
void moveRobber(TILE_T grid[height][width]);
bool moveCops(TILE_T grid[height][width]);

int main(void)
{
    printf("This program simulates a Robber being chased by one Cop.\n");
    printf("This program was written by Tyler Tracey (n00767255).\n");
    printf("The .'s simulate an empty space in the grid. The C is the\n");
    printf("cop and the R is the robber. The flashing is due to the clrscrn\n");
    printf("function. Sometimes the Robber and Cop will go off of the top of\n");
    printf("the screen, but that is because of the height of the grid.\n");
    printf("Thank you and enjoy grading. Press <ENTER> to begin the program:");
    getchar();

    TILE_T grid[height][width];
    srand(time(NULL));

    int i,j,ranHeight,ranWidth;
    for(i = 0; i<height; i++)
    {
        for(j = 0; j<width; j++)
        {
            grid[i][j].x = i;
            grid[i][j].y = j;
            grid[i][j].state = EMPTY;
        }
    }

    drawGrid(grid);
    sleep(1);

    ranHeight = rand() % height;
    ranWidth = rand() % width;

    rx = ranHeight;
    ry = ranWidth;
    grid[ranHeight][ranWidth].state = ROBBER;
    drawGrid(grid);
    sleep(1);

    ranHeight = rand() % height;
    ranWidth = rand() % width;

    cx = ranHeight;
    cy = ranWidth;
    grid[ranHeight][ranWidth].state = COP;
    drawGrid(grid);
    sleep(1);

    while(!caught)
    {
        moveRobber(grid);
        drawGrid(grid);
        sleep(1);

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
        case 0:
            grid[rx][ry].state = EMPTY;
            rx++;
            if(rx < 0)
            {
                rx = width;
            }
            rx %= width;
            break;
        case 1:
            grid[rx][ry].state = EMPTY;
            rx--;
            if(rx < 0)
            {
                rx = width;
            }
            rx %= width;
            break;
        case 2:
            grid[rx][ry].state = EMPTY;
            ry++;
            if(ry < 0)
            {
                ry = height;
            }
            ry %= height;
            break;
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

    grid[rx][ry].state = ROBBER;

}

bool moveCops(TILE_T grid[height][width])
{
    copTurns++;
    bool caught;

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
