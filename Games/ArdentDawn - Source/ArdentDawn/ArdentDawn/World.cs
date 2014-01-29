using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace ArdentDawn
{
    class World
    {
        HomeBase player;
        List<Trap> traps;
        List<Trap> trapsToRemove;
        List<Tower> towers;
        List<Tower> towersToRemove;
        List<Enemy> enemies;
        List<Enemy> enemiesToRemove;

        //Textures
        Texture2D gruntSprite;
        Texture2D assassinSprite;
        Texture2D tankSprite;
        Texture2D bulletSprite;
        Texture2D towerSprite;
        Texture2D trapSprite;
        Texture2D gr1, gr2, ar1, ar2;
        Texture2D healthBar;
        Texture2D ti, tii, tri, trii;
        Texture2D towerSpriteIn, trapSpriteIn;

        //Texture lists
        List<Texture2D> gruntList;
        List<Texture2D> assassinList;
        List<Texture2D> tankList;

        //ints
        int levelCounter = 0;
        const int maxTowers = 8;

        //asd
        public Boolean levelOver = false;
        public Boolean lost = false;
        public Boolean won = false;

        //timing mechanisms
        float timeSinceLastStructure = 500.0f;
        float minTimeBewtweenStructures = 1000.0f;
        float timeSinceLevelOver = 0.0f;
        float minTimeBetweenLevels = 2000.0f;

        //level list
        List<Level> levels;

        //mouse vector

        //font
        SpriteFont font;

        //bounds
        Rectangle worldBounds = new Rectangle(0,0,640,480);

        public World(Texture2D gruntSprite, Texture2D assassinSprite, Texture2D tankSprite, Texture2D bulletSprite, Texture2D towerSprite, Texture2D trapSprite,
            Texture2D gr1, Texture2D gr2, Texture2D ar1, Texture2D ar2, Texture2D tr1, Texture2D tr2, SpriteFont font, Texture2D healthBar, Texture2D ti, Texture2D tii,
            Texture2D tri, Texture2D trii, Texture2D towerSpriteIn, Texture2D trapSpriteIn) 
        {
            traps = new List<Trap>();
            towers = new List<Tower>();
            enemies = new List<Enemy>();

            gruntList = new List<Texture2D>();
            assassinList = new List<Texture2D>();
            tankList = new List<Texture2D>();

            //static tower
            towers.Add(new Tower(Movespeed.Zero, ArmorValue.High, Damage.High, 100, new Sprite(towerSprite), new Vector2(300, 200), bulletSprite, healthBar));

            List<Level> hordes = new List<Level>();
            //levels = new List<Level>();
            //levels.Add(new Level(10, 5, 2, this, hordes));
            levels = new List<Level>();
            levels.Add(new Level(10, 5, 0, this, hordes));
            levels.Add(new Level(18, 2, 0, this, hordes));
            levels.Add(new Level(18, 5, 2, this, hordes));

            hordes.Add(new Level(10, 3, 2, this, new List<Level>()));

            levels.Add(new Level(10, 4, 1, this, hordes));

            hordes = new List<Level>();
            hordes.Add(new Level(5, 5, 5, this, new List<Level>()));
            hordes.Add(new Level(1, 6, 3, this, new List<Level>()));

            levels.Add(new Level(8, 5, 2, this, hordes));

            hordes = new List<Level>();
            hordes.Add(new Level(3, 6, 3, this, new List<Level>()));
            hordes.Add(new Level(8, 7, 4, this, new List<Level>()));

            levels.Add(new Level(6, 10, 4, this, hordes));

            hordes = new List<Level>();
            hordes.Add(new Level(2, 9, 1, this, new List<Level>()));
            hordes.Add(new Level(1, 4, 2, this, new List<Level>()));
            hordes.Add(new Level(2, 16, 3, this, new List<Level>()));

            levels.Add(new Level(7, 6, 1, this, hordes));

            this.gruntSprite = gruntSprite;
            this.bulletSprite = bulletSprite;
            this.towerSprite = towerSprite;
            this.tankSprite = tankSprite;
            this.assassinSprite = assassinSprite;
            this.trapSprite = trapSprite;
            this.healthBar = healthBar;
            this.gr1 = gr1;
            this.gr2 = gr2;
            this.ar1 = ar1;
            this.ar2 = ar2;
            this.ti = ti;
            this.tii = tii;
            this.tri = tri;
            this.trii = trii;
            this.trapSpriteIn = trapSpriteIn;
            this.towerSpriteIn = towerSpriteIn;

            gruntList.Add(gruntSprite);
            gruntList.Add(gr1);
            gruntList.Add(gruntSprite);
            gruntList.Add(gr2);

            assassinList.Add(assassinSprite);
            assassinList.Add(ar1);
            assassinList.Add(assassinSprite);
            assassinList.Add(ar2);

            tankList.Add(tankSprite);
            tankList.Add(tr1);
            tankList.Add(tankSprite);
            tankList.Add(tr2);

            this.font = font;

            player = new HomeBase(Movespeed.Zero, ArmorValue.VeryHigh, Damage.Zero, 500, new Sprite(gruntSprite), Vector2.Zero);
            
            //enemies.Add(new Enemy(Movespeed.Slow, ArmorValue.Low, Damage.High, 1, new Sprite(tankSprite), new Vector2(0,10), EnemyType.Tank, tankList));
            //enemies.Add(new Enemy(Movespeed.Slow, ArmorValue.Low, Damage.High, 1, new Sprite(gruntSprite), new Vector2(0,30), EnemyType.Grunt, gruntList));
            //enemies.Add(new Enemy(Movespeed.Slow, ArmorValue.Low, Damage.High, 1, new Sprite(gruntSprite), new Vector2(0, 50), EnemyType.Grunt, gruntList));
            //enemies.Add(new Enemy(Movespeed.Slow, ArmorValue.Low, Damage.High, 1, new Sprite(gruntSprite), new Vector2(0, 70), EnemyType.Grunt, gruntList));
            //tow = new Tower(Movespeed.Zero, ArmorValue.Medium, Damage.High, 100, new Sprite(placeHolder), new Vector2(640/2, 480/2), s);
            //towers.Add(tow);

            levels[0].startLevel();
        }

        public void update(GameTime elapsedTime)
        {
            timeSinceLastStructure += elapsedTime.ElapsedGameTime.Milliseconds;

            towersToRemove = new List<Tower>();
            trapsToRemove = new List<Trap>();
            enemiesToRemove = new List<Enemy>();

            foreach (Enemy e in enemies)
            {
                e.update(elapsedTime, towers, traps, player, enemies, levelCounter);
                if (e.dead)
                {
                    player.cash += e.moneyForKilling;
                    enemiesToRemove.Add(e);
                }
            }

            foreach (Enemy e in enemiesToRemove)
            {
                enemies.Remove(e);
            }

            foreach (Tower t in towers)
            {
                t.update(elapsedTime, enemies);
                if (enemies == null || enemies.Count < 1)
                {
                    t.target = null;
                }
                if (t.dead)
                {
                    towersToRemove.Add(t);
                }
            }

            if(!won && !lost)
            {
                levels[levelCounter].update(elapsedTime);
            }

            foreach (Tower t in towersToRemove)
            {
                towers.Remove(t);
            }

            foreach (Trap t in traps)
            {
                t.update(elapsedTime);
                if (t.done)
                {
                    trapsToRemove.Add(t);
                }
            }

            foreach (Trap t in trapsToRemove)
            {
                traps.Remove(t);
            }

            if (!won && !lost)
            {
                if (enemies.Count < 1 && levels[levelCounter].isDone())
                {
                    levelOver = true;

                }
            }

            if (levelOver)
            {
                timeSinceLevelOver += elapsedTime.ElapsedGameTime.Milliseconds;
                if (timeSinceLevelOver >= minTimeBetweenLevels)
                {
                    timeSinceLevelOver = 0.0f;
                    levelOver = false;
                    levelCounter++;

                    if (levelCounter < 7)
                    {
                        levels[levelCounter].startLevel();
                    }
                    else
                    {
                        won = true;
                    }
                    
                }
            }

            if(enemies.Count > 0 && towers.Count < 1 && !player.canAffordTower())
            {
                levelOver = true;
                lost = true;
            }

            if (player.dead)
            {
                lost = true;
            }
        }

        public void draw(SpriteBatch spriteBatch)
        {
            
            spriteBatch.DrawString(font, "LEVEL " + (levelCounter+1), new Vector2(50,0), Color.MidnightBlue);
            
            if (lost)
            {
                spriteBatch.DrawString(font, "YOU LOSE!", new Vector2(260, 240), Color.Purple);
            }

            foreach (Tower t in towers)
            {
                t.draw(spriteBatch);
            }

            foreach (Trap t in traps)
            {
                t.draw(spriteBatch);
            }

            foreach (Enemy e in enemies)
            {
                e.draw(spriteBatch);
            }

            if (levelOver)
            {
                spriteBatch.DrawString(font, "LEVEL COMPLETE", new Vector2(260, 240), Color.Purple);
            }

            spriteBatch.DrawString(font, "Gold: " + player.cash, new Vector2(400, 0), Color.Brown);
            spriteBatch.DrawString(font, "Health: " + player.health, new Vector2(405, 415), Color.Red);

            if (timeSinceLastStructure >= minTimeBewtweenStructures)
            {
                if (player.canAffordTower())
                {
                    //spriteBatch.Draw(ti, new Rectangle(70, 412, 16, 16), Color.White);
                    spriteBatch.Draw(towerSprite, new Vector2(70, 412), null, Color.White, 0.0f, Vector2.Zero, 0.5f, SpriteEffects.None, 0.0f);
                }
                else
                {
                    spriteBatch.Draw(towerSpriteIn, new Vector2(70, 412), null, Color.White, 0.0f, Vector2.Zero, 0.5f, SpriteEffects.None, 0.0f);
                }

                if (player.canAffordTrap())
                {
                    spriteBatch.Draw(trapSprite, new Rectangle(110, 412, 32, 32), Color.White);
                }
                else
                {
                    spriteBatch.Draw(trapSpriteIn, new Rectangle(110, 412, 32, 32), Color.White);
                }
            }
            else
            {
                spriteBatch.Draw(towerSpriteIn, new Vector2(70, 412), null, Color.White, 0.0f, Vector2.Zero, 0.5f, SpriteEffects.None, 0.0f);
                spriteBatch.Draw(trapSpriteIn, new Rectangle(110, 412, 32, 32), Color.White);
            }
        }

        public void addTower(Rectangle bounds, MouseState mouseState)
        {
            if (timeSinceLastStructure >= minTimeBewtweenStructures)
            {
                if(canAddHere(bounds) && player.canAffordTower() && towers.Count < maxTowers)
                {
                    //play sound effect for confirmation
                    player.cash -= player.priceOfTower;
                    timeSinceLastStructure = 0.0f;
                    towers.Add(new Tower(Movespeed.Zero, ArmorValue.Medium, Damage.High, 100, new Sprite(towerSprite), new Vector2(mouseState.X-32, mouseState.Y-32), bulletSprite, healthBar));
                }
                else
                {
                    //play sound effect for denial
                }
            }
        }

        public void addEnemy(int x, int y, EnemyType enemyType)
        {
            int multiplier = 1;
            if(levelCounter >= 3){
                multiplier = 2;
            }
                if (enemyType == EnemyType.Grunt)
                {
                    enemies.Add(new Enemy(Movespeed.Medium, ArmorValue.Low, Damage.Low, 4*multiplier, new Sprite(gruntSprite), new Vector2(x, y), EnemyType.Grunt, gruntList));
                }
                else if (enemyType == EnemyType.Assassin)
                {
                    enemies.Add(new Enemy(Movespeed.Fast, ArmorValue.Low, Damage.High, 50 * multiplier, new Sprite(assassinSprite), new Vector2(x, y), EnemyType.Assassin, assassinList));
                }
                else
                {
                    enemies.Add(new Enemy(Movespeed.Slow, ArmorValue.VeryHigh, Damage.High, 400 * multiplier, new Sprite(tankSprite), new Vector2(x, y), EnemyType.Tank, tankList));
                }
            
        }

        public void addTrap(Rectangle bounds, MouseState mouseState)
        {
            if (canAddHere(bounds) && timeSinceLastStructure >= minTimeBewtweenStructures && player.canAffordTrap())
            {
                player.cash -= player.priceOfTrap;
                timeSinceLastStructure = 0.0f;
                traps.Add(new Trap(Movespeed.Zero, ArmorValue.Invulnerable, Damage.Zero, -1, new Sprite(trapSprite), new Vector2(mouseState.X-16, mouseState.Y-16)));
            }
        }

        public Boolean canAddHere(Rectangle bounds)
        {
            Boolean temp = true;

            foreach (Tower t in towers)
            {
                if (bounds.Intersects(t.bounds))
                {
                    temp = false;
                }
            }

            foreach (Enemy e in enemies)
            {
                if (bounds.Intersects(e.bounds))
                {
                    temp = false;
                }
            }

            foreach (Trap t in traps)
            {
                if (bounds.Intersects(t.bounds))
                {
                    temp = false;
                }
            }

            if(bounds.X < 0 || bounds.X > 640 - bounds.Width || bounds.Y < 0 || bounds.Y > 480 - bounds.Height)
            {
                temp = false;
            }

            return temp;
        }
    }
}
