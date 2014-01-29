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

//TYLER

namespace ArdentDawn
{
    enum Movespeed { Zero, Slow, Medium, Fast=4 };
    enum ArmorValue { Invulnerable=-1, Low=1, Medium=2, High=3, VeryHigh=4 };
    enum Damage { Zero, Low, Medium, High };
    enum Helth { Low = 2, Medium = 4, High = 8 };
    enum EnemyType { Grunt, Assassin, Tank };

    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        //Audio files
        Song menuTheme;
        Song gameMusic;
        Song endMusic;
        static SoundEffect shot;
        Song o1, o2;
        Song end1, end2, end3;
        SoundEffect damage1, damage2, damage3, damage4, damage5, menuBack, menuSelect, menuUp;

        //world object
        World world = null;
        //texture
        Texture2D background;
        Texture2D gruntSprite;
        Texture2D gr1;
        Texture2D gr2;
        Texture2D bulletSprite;
        Texture2D towerSprite;
        Texture2D tankSprite;
        Texture2D assassinSprite;
        Texture2D trapSprite;
        Texture2D ar1;
        Texture2D ar2;
        Texture2D tr1, tr2;
        Texture2D menu;
        Texture2D howToPlay, credits;
        Texture2D overlay;
        Texture2D c1p1, c1p2, c1p3, c1p4, c1p5, c1p6;
        Texture2D e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12;
        Texture2D healthBar;
        Texture2D ti, tii, tri, trii;
        Texture2D towerSpriteIn, trapSpriteIn;

        //vid shit
        //cutscene lists
        List<Texture2D> opening;
        List<Texture2D> ending;
        int openingFrame = 1;
        int endingFrame = 1;
        List<float> openingVoiceTimes, endingVoiceTimes;

        Rectangle play = new Rectangle(44, 140, 166, 58);
        Rectangle creditsR = new Rectangle(44, 228, 166, 58);
        Rectangle quit = new Rectangle(44, 315, 166, 58);

        SpriteFont font;

        //mouse vector
        Rectangle mouseLocation;

        //timers
        float timeSinceLastClick = 200.0f;
        float minTimeBetweenClicks = 400.0f;

        int rounds = 0;

        int screen_width = 640;
        int screen_height = 480;

        Boolean playSelection = true;

        enum GameStates {StartScreen, HowToPlay, Opening, Ending, Credits, Playing, Lost, Cutscene, Win };
        GameStates gameState = GameStates.StartScreen;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
        }

        //
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here
            this.graphics.PreferredBackBufferWidth = 640;
            this.graphics.PreferredBackBufferHeight = 480;
            this.graphics.ApplyChanges();
            this.IsMouseVisible = true;

            base.Initialize();
        }

        //
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // TODO: use this.Content to load your game content here
            background = Content.Load<Texture2D>(@"Textures\ArdentDawnBG");
            gruntSprite = Content.Load<Texture2D>(@"Textures\GruntSprite");
            bulletSprite = Content.Load<Texture2D>(@"Textures\Bullet");
            towerSprite = Content.Load<Texture2D>(@"Textures\Tower");
            trapSprite = Content.Load<Texture2D>(@"Textures\Trap");
            assassinSprite = Content.Load<Texture2D>(@"Textures\AssassinSprite");
            tankSprite = Content.Load<Texture2D>(@"Textures\TankSprite");
            gr1 = Content.Load<Texture2D>(@"Textures\GruntRun1");
            gr2 = Content.Load<Texture2D>(@"Textures\GruntRun2");
            ar1 = Content.Load<Texture2D>(@"Textures\AssassinRun1");
            ar2 = Content.Load<Texture2D>(@"Textures\AssassinRun2");
            tr1 = Content.Load<Texture2D>(@"Textures\TankRun1");
            tr2 = Content.Load<Texture2D>(@"Textures\TankRun2");
            menu = Content.Load<Texture2D>(@"Textures\Menu");
            howToPlay = Content.Load<Texture2D>(@"Textures\HowToPlay");
            credits = Content.Load<Texture2D>(@"Textures\Credits");
            overlay = Content.Load<Texture2D>(@"Textures\ButtonMouseOver");
            c1p1 = Content.Load<Texture2D>(@"Textures\Opening1");
            c1p2 = Content.Load<Texture2D>(@"Textures\Opening2");
            c1p3 = Content.Load<Texture2D>(@"Textures\Opening3");
            c1p4 = Content.Load<Texture2D>(@"Textures\Opening4");
            c1p5 = Content.Load<Texture2D>(@"Textures\Opening5");
            c1p6 = Content.Load<Texture2D>(@"Textures\Opening6");
            healthBar = Content.Load<Texture2D>(@"Textures\HealthBar");
            ti = Content.Load<Texture2D>(@"Textures\TowerIcon");
            tii = Content.Load<Texture2D>(@"Textures\TowerInactiveIcon");
            tri = Content.Load<Texture2D>(@"Textures\TrapIcon");
            trii = Content.Load<Texture2D>(@"Textures\TrapInactiveIcon");
            towerSpriteIn = Content.Load<Texture2D>(@"Textures\TowerInactive");
            trapSpriteIn = Content.Load<Texture2D>(@"Textures\TrapInactive");
            e1 = Content.Load<Texture2D>(@"Textures\Ending1");
            e2 = Content.Load<Texture2D>(@"Textures\Ending2");
            e3 = Content.Load<Texture2D>(@"Textures\Ending3");
            e4 = Content.Load<Texture2D>(@"Textures\Ending4");
            e5 = Content.Load<Texture2D>(@"Textures\Ending5");
            e6 = Content.Load<Texture2D>(@"Textures\Ending6");
            e7 = Content.Load<Texture2D>(@"Textures\Ending7");
            e8 = Content.Load<Texture2D>(@"Textures\Ending8");
            e9 = Content.Load<Texture2D>(@"Textures\Ending9");
            e10 = Content.Load<Texture2D>(@"Textures\Ending10");
            e11 = Content.Load<Texture2D>(@"Textures\Ending11");
            e12 = Content.Load<Texture2D>(@"Textures\Ending12");

            font = Content.Load<SpriteFont>(@"Fonts\Pericles24");

            //audio files
            menuTheme = Content.Load<Song>(@"Sounds\Theme");
            gameMusic = Content.Load<Song>(@"Sounds\Battle");;

            //System.IO.FileStream fs1 = new System.IO.FileStream(@"Damage 01.wav", System.IO.FileMode.Open);
            //damage1 = SoundEffect.FromStream(fs1);
            //fs1.Dispose();

            o1 = Content.Load<Song>(@"Sounds\FF_Cine1_VA1");
            o2 = Content.Load<Song>(@"Sounds\FF_Cine1_VA2");
            end1 = Content.Load<Song>(@"Sounds\FF_Cine2_VA1");
            end2 = Content.Load<Song>(@"Sounds\FF_Cine2_VA2");
            end3 = Content.Load<Song>(@"Sounds\FF_Cine2_VA3");
        }

        //
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        //
        protected override void Update(GameTime gameTime)
        {
            timeSinceLastClick += gameTime.ElapsedGameTime.Milliseconds;
            // Allows the game to exit
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                this.Exit();
            if (world == null)
            {
                world = new World(gruntSprite, assassinSprite, tankSprite, bulletSprite, towerSprite, trapSprite, gr1, gr2, ar1, ar2, tr1, tr2, font, healthBar, ti, tii, tri, trii, towerSpriteIn, trapSpriteIn);
                opening = new List<Texture2D>();
                opening.Add(c1p1);
                opening.Add(c1p2);
                opening.Add(c1p3);
                opening.Add(c1p4);
                opening.Add(c1p4);
                opening.Add(c1p5);
                opening.Add(c1p6);

                ending = new List<Texture2D>();
                ending.Add(e1);
                ending.Add(e2);
                ending.Add(e3);
                ending.Add(e4);
                ending.Add(e5);
                ending.Add(e6);
                ending.Add(e7);
                ending.Add(e8);
                ending.Add(e9);
                ending.Add(e10);
                ending.Add(e11);
                ending.Add(e12);
                ending.Add(e5);
                ending.Add(c1p1);
                ending.Add(c1p2);
            }
            // TODO: Add your update logic here
            mouseLocation = new Rectangle(Mouse.GetState().X, Mouse.GetState().Y, 1, 1);

            if (world.won || world.lost)
            {
                //timeSinceLastClick = 0.0f;
                
                gameState = GameStates.Ending;
            }

            if (gameState == GameStates.StartScreen)
            {
                if (MediaPlayer.State == MediaState.Stopped)
                {
                    MediaPlayer.Play(menuTheme);
                }
                MouseState mouseState = Mouse.GetState();
                if (mouseState.LeftButton == ButtonState.Pressed && timeSinceLastClick >= minTimeBetweenClicks)
                {
                    timeSinceLastClick = 0.0f;
                    if (mouseLocation.Intersects(play))
                    {
                        gameState = GameStates.Opening;
                        //menuSelect.Play();
                        playVA();
                    }
                    else if (mouseLocation.Intersects(creditsR))
                    {
                        gameState = GameStates.Credits;
                        //menuSelect.Play();
                    }
                    else if (mouseLocation.Intersects(quit))
                    {
                        //menuSelect.Play();
                        this.Exit();
                    }
                }
            }
            else if (gameState == GameStates.Opening)
            {
                if (openingFrame == 3)
                {
                    playVA();
                }
                if (Mouse.GetState().LeftButton == ButtonState.Pressed && timeSinceLastClick >= minTimeBetweenClicks)
                {
                    timeSinceLastClick = 0.0f;
                    openingFrame++;
                    if (openingFrame > 6)
                    {
                        openingFrame = 1;
                        gameState = GameStates.HowToPlay;
                    }
                }
            }
            else if (gameState == GameStates.Ending)
            {
                if (endingFrame == 3 || endingFrame == 6 || endingFrame == 9 || endingFrame == 12)
                {
                    playVA();
                }
                if (Mouse.GetState().LeftButton == ButtonState.Pressed && timeSinceLastClick >= minTimeBetweenClicks)
                {
                    timeSinceLastClick = 0.0f;
                    endingFrame++;
                    if (endingFrame > 15)
                    {
                        //endingFrame = 1;
                        gameState = GameStates.Credits;
                    }
                }
            }
            else if (gameState == GameStates.HowToPlay)
            {

                if (Mouse.GetState().LeftButton == ButtonState.Pressed && timeSinceLastClick >= minTimeBetweenClicks)
                {
                    timeSinceLastClick = 0.0f;
                    gameState = GameStates.Playing;
                    //MediaPlayer.Stop();
                }
            }
            else if (gameState == GameStates.Credits)
            {

                if (Mouse.GetState().LeftButton == ButtonState.Pressed && timeSinceLastClick >= minTimeBetweenClicks)
                {
                    timeSinceLastClick = 0.0f;
                    gameState = GameStates.StartScreen;
                }
            }
            else if (gameState == GameStates.Playing)
            {
                if (MediaPlayer.State == MediaState.Stopped)
                {
                    MediaPlayer.Play(gameMusic);
                }
                timeSinceLastClick = 0.0f;
                MouseState mouseState = Mouse.GetState();
                KeyboardState keyState = Keyboard.GetState();
                if (mouseState.LeftButton == ButtonState.Pressed || keyState.IsKeyDown(Keys.D1))
                {
                    world.addTower(new Rectangle(mouseState.X, mouseState.Y, 64, 64), mouseState);
                }
                if (mouseState.RightButton == ButtonState.Pressed || keyState.IsKeyDown(Keys.D2))
                {
                    world.addTrap(new Rectangle(mouseState.X, mouseState.Y, 32, 32), mouseState);
                }

                world.update(gameTime);
            }


            base.Update(gameTime);
        }

        //
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);

            // TODO: Add your drawing code here
            spriteBatch.Begin();

            //draw BG
            if (gameState == GameStates.StartScreen)
            {
                spriteBatch.Draw(menu, new Vector2(0,0), Color.White);
                if (mouseLocation.Intersects(play))
                {
                    spriteBatch.Draw(overlay, play, Color.White);
                    if (playSelection)
                    {
                        playSelection = false;
                        //menuSelect.Play();
                    }
                }
                else if (mouseLocation.Intersects(creditsR))
                {
                    spriteBatch.Draw(overlay, creditsR, Color.White);
                    if (playSelection)
                    {
                        playSelection = false;
                        //menuSelect.Play();
                    }
                }
                else if (mouseLocation.Intersects(quit))
                {
                    spriteBatch.Draw(overlay, quit, Color.White);
                    if (playSelection)
                    {
                        playSelection = false;
                        //menuSelect.Play();
                    }
                }
                else
                {
                    playSelection = true;
                }
            }
            else if (gameState == GameStates.Opening)
            {

                spriteBatch.Draw(opening[openingFrame-1], new Rectangle(0, 0, 640, 480), Color.White);
            }
            else if (gameState == GameStates.Ending)
            {
                if (endingFrame <= 15)
                {
                    spriteBatch.Draw(ending[endingFrame - 1], new Rectangle(0, 0, 640, 480), Color.White);
                }
            }
            else if (gameState == GameStates.HowToPlay)
            {
                spriteBatch.Draw(howToPlay, new Vector2(0, 0), Color.White);
            }
            else if (gameState == GameStates.Credits)
            {
                spriteBatch.Draw(credits, new Vector2(0, 0), Color.White);
            }
            else if (gameState == GameStates.Playing)
            {
                spriteBatch.Draw(background, new Rectangle(0, 0, screen_width, screen_height), Color.White);
                world.draw(spriteBatch);
            }

            spriteBatch.End();
            base.Draw(gameTime);
        }

        public static void playShot()
        {
            //shot.Play();
        }

        public void playVA()
        {
            MediaPlayer.Stop();
            if (gameState == GameStates.Opening)
            {
                if (openingFrame == 1)
                {
                    MediaPlayer.Play(o1);
                }
                else
                {
                    MediaPlayer.Play(o2);
                }
            }
            else
            {
                if(endingFrame == 3)
                {
                    MediaPlayer.Play(end1);
                }
                else if (endingFrame == 6)
                {
                    MediaPlayer.Play(end2);
                }
                else if (endingFrame == 9)
                {
                    MediaPlayer.Play(end3);
                }
                else
                {
                    MediaPlayer.Play(o1);
                }
            }
        }
    }
}
