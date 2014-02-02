using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace Pong
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        //object lists
        List<Wall> walls;

        //game vars
        const int scoreToWin = 10;
        const int screenWidth = 1280;
        const int screenHeight = 720;
        int playerScore = 0;
        int enemyScore = 0;
        const int paddleWidth = 150;
        const int paddleHeight = 10;
        const int wallWidth = paddleHeight;
        const int ballWidth = 10;
        const int ballHeight = 10;
        const int scoreX = 1100;
        int horizontalMovementValue = 5;
        const int xBoundBuffer = 250;
        const int leftXBound = xBoundBuffer;
        const int rightXBound = screenWidth - xBoundBuffer;
        const int velocityXAddition = 2;
        const int velocityYAddition = 2;
        const int minEnemySpeed = 2;
        const int maxEnemySpeed = 8;
        int maxBallSpeed = 9;
        float enemySpeed = minEnemySpeed;

        //timer vars
        float timeSinceLastCollision = 500f;
        const float minTimeBetweenCollisions = 500f;
        float timeSinceLastWallCollision = 500f;
        const float minTimeBetweenWallCollisions = 500f;

        //textures
        Texture2D paddle;
        Texture2D ball;
        Texture2D sideWall;

        //font for score
        SpriteFont font;

        //sounds
        Song menuMusic;
        Song battleMusic;
        SoundEffect hit;

        //location and vector variables
        Vector2 playerLocation;
        Vector2 enemyLocation;
        Vector2 ballLocation;
        Vector2 ballVelocity;
        Rectangle Player;
        Rectangle Enemy;
        Rectangle Ball;
        
        //Vector2 playerNormal;
        //Vector2 enemyNormal;

        //bools
        Boolean noWalls = true;
        Boolean moving = false;
        Boolean gameOver = false;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

            playerLocation = new Vector2(screenWidth/2 - paddleWidth/2, 680);
            enemyLocation = new Vector2(screenWidth/2 - paddleWidth/2, 40);
            ballLocation = new Vector2(screenWidth/2 - ballWidth/2, screenHeight/2 - ballHeight/2);

            Player = new Rectangle((int)playerLocation.X, (int)playerLocation.Y, paddleWidth, paddleHeight);
            Enemy = new Rectangle((int)enemyLocation.X, (int)enemyLocation.Y, paddleWidth, paddleHeight);
            Ball = new Rectangle((int)ballLocation.X, (int)ballLocation.Y, ballWidth, ballHeight);

            //playerNormal = new Vector2();
            //enemyNormal = new Vector2();

            Random rand = new Random();
            int x = 0;
            int y = 0;
            while (!((x > 2 || x < -2) && (y > 2 || y < -2)))
            {
                x = rand.Next(-5, 5);
                y = rand.Next(-5, 5);
                ballVelocity = new Vector2(x, y);
            }
            walls = new List<Wall>();
        }

        //
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here
            this.graphics.PreferredBackBufferWidth = screenWidth;
            this.graphics.PreferredBackBufferHeight = screenHeight;
            this.graphics.ApplyChanges();

            base.Initialize();
        }

        //
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // TODO: use this.Content to load your game content here
            paddle = Content.Load<Texture2D>(@"Textures\Paddle");
            ball = Content.Load<Texture2D>(@"Textures\Ball");
            sideWall = Content.Load<Texture2D>(@"Textures\SideWall");

            font = Content.Load<SpriteFont>(@"Fonts\Font");

            menuMusic = Content.Load<Song>(@"Sounds\Me");
            battleMusic = Content.Load<Song>(@"Sounds\Skism");
            hit = Content.Load<SoundEffect>(@"Sounds\Hit");
        }

        //
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        //
        protected override void Update(GameTime gameTime)
        {
            moving = false;

            if (!gameOver)
            {
                timeSinceLastCollision += gameTime.ElapsedGameTime.Milliseconds;
                timeSinceLastWallCollision += gameTime.ElapsedGameTime.Milliseconds;
                //music
                /*if (MediaPlayer.State == MediaState.Stopped)
                {
                    MediaPlayer.Play(battleMusic);
                    MediaPlayer.IsRepeating = true;
                }*/
                // Allows the game to exit
                if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                    this.Exit();

                if (noWalls)
                {
                    noWalls = false;
                    walls.Add(new Wall(new Rectangle(leftXBound, 0, paddleHeight, screenHeight), new Vector2(1, 0), sideWall));
                    walls.Add(new Wall(new Rectangle(rightXBound + paddleHeight, 0, paddleHeight, screenHeight), new Vector2(-1, 0), sideWall));
                    //walls.Add(new Wall(new Rectangle(), new Vector2(), roof);
                }

                // TODO: Add your update logic here
                //handle player input
                KeyboardState keyState = Keyboard.GetState();
                if (keyState.IsKeyDown(Keys.Left) || keyState.IsKeyDown(Keys.A))
                {
                    playerLocation.X -= horizontalMovementValue;
                    moving = !moving;
                }
                if (keyState.IsKeyDown(Keys.Right) || keyState.IsKeyDown(Keys.D))
                {
                    playerLocation.X += horizontalMovementValue;
                    moving = !moving;
                }

                //MouseState ms = Mouse.GetState();
                //playerLocation.X = ms.X;

                //make sure player is within bounds
                if (playerLocation.X < leftXBound + paddleHeight)
                {
                    playerLocation.X = leftXBound + paddleHeight;
                }
                if (playerLocation.X > (rightXBound - paddleWidth))
                {
                    playerLocation.X = rightXBound - paddleWidth;
                }

                Player = new Rectangle((int)playerLocation.X, (int)playerLocation.Y, paddleWidth, paddleHeight);

                //update enemy
                if (Ball.Center.X < Enemy.Center.X)
                {
                    enemyLocation.X -= enemySpeed;
                }
                else if (Ball.Center.X > Enemy.Center.X)
                {
                    enemyLocation.X += enemySpeed;
                }
                else
                {
                    if (ballVelocity.X < 0)
                    {
                        enemyLocation.X -= enemySpeed;
                    }
                    else if (ballVelocity.X > 0)
                    {
                        enemyLocation.X += enemySpeed;
                    }
                }

                //enemyLocation = new Vector2(ballLocation.X - paddleWidth/2, enemyLocation.Y);

                if (enemyLocation.X < leftXBound + paddleHeight)
                {
                    enemyLocation.X = leftXBound + paddleHeight;
                }
                if (enemyLocation.X > (rightXBound - paddleWidth))
                {
                    enemyLocation.X = rightXBound - paddleWidth;
                }

                Enemy = new Rectangle((int)enemyLocation.X, (int)enemyLocation.Y, paddleWidth, paddleHeight);

                //update ball
                ballLocation += ballVelocity;
                //check bounds of ball
                if (ballLocation.Y < enemyLocation.Y - paddleHeight)
                {
                    playerScore++;
                    maxBallSpeed += 1;
                    enemySpeed += 1;
                    if (enemySpeed > ballVelocity.X)
                    {
                        if (ballVelocity.X < 0)
                        {
                            enemySpeed = -1 * ballVelocity.X;
                        }
                        else
                        {
                            enemySpeed = ballVelocity.X;
                        }
                    }

                    if (playerScore == scoreToWin)
                    {
                        gameOver = true;
                    }
                    resetBall();
                }
                else if (ballLocation.Y > playerLocation.Y + paddleHeight)
                {
                    enemyScore++;
                    horizontalMovementValue++;

                    if (enemyScore == scoreToWin)
                    {
                        gameOver = true;
                    }
                    resetBall();
                }
                else if (ballLocation.X < leftXBound || ballLocation.Y > rightXBound)
                {
                    resetBall();
                }
                Ball = new Rectangle((int)ballLocation.X, (int)ballLocation.Y, ballWidth, ballHeight);
                if (Ball.Intersects(Player) && timeSinceLastCollision >= minTimeBetweenCollisions)
                {
                    timeSinceLastCollision = 0.0f;
                    hit.Play();
                    ballVelocity.Y *= -1;
                    ballVelocity.Y -= 1;
                    if (ballVelocity.Y < -1 * maxBallSpeed)
                    {
                        ballVelocity.Y = -1 * maxBallSpeed;
                    }
                }
                else if (Ball.Intersects(Enemy) && timeSinceLastCollision >= minTimeBetweenCollisions)
                {
                    timeSinceLastCollision = 0.0f;
                    hit.Play();
                    ballVelocity.Y *= -1;
                    ballVelocity.Y += 1;
                    if (ballVelocity.Y > maxBallSpeed)
                    {
                        ballVelocity.Y = maxBallSpeed;
                    }
                    //enemySpeed = new Random().Next(minEnemySpeed, maxEnemySpeed);
                    //enemySpeed = ballVelocity.X - 1;
                }

                foreach (Wall w in walls)
                {
                    if (Ball.Intersects(w.bounds) && timeSinceLastWallCollision >= minTimeBetweenWallCollisions)
                    {
                        timeSinceLastWallCollision = 0.0f;
                        hit.Play();
                        ballVelocity.X *= -1;
                        if (ballVelocity.X < 0)
                        {
                            ballVelocity.X -= 1;
                        }
                        else
                        {
                            ballVelocity.X += 1;
                        }

                        if (ballVelocity.X < -1 * maxBallSpeed)
                        {
                            ballVelocity.X = -1 * maxBallSpeed;
                        }
                        else if (ballVelocity.X > maxBallSpeed)
                        {
                            ballVelocity.X = maxBallSpeed;
                        }
                    }
                }
            }
            else
            {
                KeyboardState ks = Keyboard.GetState();
                if (ks.IsKeyDown(Keys.Space))
                {
                    resetGame();
                }
            }

            base.Update(gameTime);
        }

        //
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.Black);

            // TODO: Add your drawing code here
            spriteBatch.Begin();

            spriteBatch.Draw(paddle, playerLocation, Color.White);
            spriteBatch.Draw(paddle, enemyLocation, Color.White);
            spriteBatch.Draw(ball, ballLocation, Color.White);

            spriteBatch.DrawString(font, "Score: " + playerScore.ToString(), new Vector2(scoreX, playerLocation.Y), Color.White);
            spriteBatch.DrawString(font, "Score: " + enemyScore.ToString(), new Vector2(scoreX, enemyLocation.Y), Color.White);

            foreach (Wall w in walls)
            {
                spriteBatch.Draw(w.texture, w.bounds, Color.White);
            }

            if (gameOver)
            {
                if (enemyScore == scoreToWin)
                {
                    spriteBatch.DrawString(font, "YOU LOSE!", new Vector2(), Color.White);
                }
                else
                {
                    spriteBatch.DrawString(font, "YOU WIN!", new Vector2(), Color.White);
                }
            }
            spriteBatch.End();

            base.Draw(gameTime);
        }

        public void resetBall()
        {
            ballLocation = new Vector2(screenWidth / 2 - ballWidth / 2, screenHeight / 2 - ballHeight / 2); 
            
            Random rand = new Random();
            int x = 0;
            int y = 0;
            while (!((x > 2 || x < -2) && (y > 2 || y < -2)))
            {
                x = rand.Next(-5, 5);
                y = rand.Next(-5, 5);
                ballVelocity = new Vector2(x, y);
            }

            //enemySpeed = new Random().Next(minEnemySpeed, maxEnemySpeed);
            //enemySpeed = ballVelocity.X - 1;
        }

        public void resetGame()
        {
            playerLocation = new Vector2(screenWidth / 2 - paddleWidth / 2, 680);
            enemyLocation = new Vector2(screenWidth / 2 - paddleWidth / 2, 40);
            ballLocation = new Vector2(screenWidth / 2 - ballWidth / 2, screenHeight / 2 - ballHeight / 2);

            Player = new Rectangle((int)playerLocation.X, (int)playerLocation.Y, paddleWidth, paddleHeight);
            Enemy = new Rectangle((int)enemyLocation.X, (int)enemyLocation.Y, paddleWidth, paddleHeight);
            Ball = new Rectangle((int)ballLocation.X, (int)ballLocation.Y, ballWidth, ballHeight);

            //playerNormal = new Vector2();
            //enemyNormal = new Vector2();

            Random rand = new Random();
            int x = 0;
            int y = 0;
            while (!((x > 2 || x < -2) && (y > 2 || y < -2)))
            {
                x = rand.Next(-5, 5);
                y = rand.Next(-5, 5);
                ballVelocity = new Vector2(x, y);
            }

            enemySpeed = minEnemySpeed;
            horizontalMovementValue = 5;
            maxBallSpeed = 5;
            enemyScore = 0;
            playerScore = 0;
            gameOver = false;
        }
    }
}
