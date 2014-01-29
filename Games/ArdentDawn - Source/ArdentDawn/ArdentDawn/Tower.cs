using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ArdentDawn
{
    class Tower : Object
    {
        public Enemy target;
        public float timeSinceLastShot = 0f;
        public const float minTimeBetweenShots = 750f;
        public List<Bullet> bullets;
        public List<Bullet> bulletsToRemove;
        public Texture2D bullet;
        public Boolean dead = false;
        public Texture2D healthBar;

        public Tower(Movespeed m, ArmorValue a, Damage d, int health, Sprite s, Vector2 l, Texture2D bullet, Texture2D healthBar)
            : base(m, a, d, health, s, l)
        {
            bullets = new List<Bullet>();
            this.bullet = bullet;
            bounds = new Rectangle((int)location.X+32, (int)location.Y+32, 64, 64);
            this.healthBar = healthBar;
        }

        public void update(GameTime elapsedTime, List<Enemy> enemies)
        {
            target = findClosestEnemy(enemies);
            bulletsToRemove = new List<Bullet>();

            timeSinceLastShot += elapsedTime.ElapsedGameTime.Milliseconds;
            if (timeSinceLastShot >= minTimeBetweenShots)
            {
                shoot();
            }

            foreach(Bullet bill in bullets)
            {
                bill.update(elapsedTime);
                if (bill.done)
                {
                    bulletsToRemove.Add(bill);
                }
            }

            foreach (Bullet bill in bulletsToRemove)
            {
                bullets.Remove(bill);
            }

            //findClosestEnemy(enemies);

            if (target == null && bullets.Count > 0)
            {
                bullets = new List<Bullet>();
            }

            if (this.health <= 0)
            {
                dead = true;
            }
        }

        public void draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(this.sprite.frame, this.location, Color.White);

            foreach (Bullet bill in bullets)
            {
                bill.draw(spriteBatch);
            }

            //spriteBatch.Draw(healthBar, new Rectangle((int)this.location.X, (int)this.location.Y + 59, 64*(this.health/100), 5), Color.White);
           
        }

        public Enemy findClosestEnemy(List<Enemy> enemies)
        {
            Enemy currentEnemy = null;

            if (enemies.Count < 1)
            {
                return null;
            }

            foreach (Enemy e in enemies)
            {
                if (currentEnemy == null)
                {
                    currentEnemy = e;
                }
                else
                {
                    Vector2 enemy = e.location;
                    Vector2 tower = this.location;
                    Vector2 cEnemy = currentEnemy.location;

                    double distance1 = (Math.Sqrt(Math.Pow(Math.Abs(enemy.X - tower.X), 2) + Math.Pow(Math.Abs(enemy.Y - tower.Y), 2)));
                    double distance2 = (Math.Sqrt(Math.Pow(Math.Abs(cEnemy.X - tower.X), 2) + Math.Pow(Math.Abs(cEnemy.Y - tower.Y), 2)));

                    if (distance1 < distance2)
                    {
                        currentEnemy = e;
                    }
                }
            }

            return currentEnemy;
        }

        public void shoot()
        {
            if(!(target == null)) 
            {
                Game1.playShot();
                timeSinceLastShot = 0.0f;
                bullets.Add(new Bullet(location, 2, new Sprite(bullet), target, 40));
            }
        }

        public void collide(Enemy enemy)
        {
            this.health -= (int)Math.Floor(0.9*enemy.damage);
            if (this.health <= 0)
            {
                dead = true;
            }
        }
    }
}
