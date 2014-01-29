using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ArdentDawn
{
    class Enemy : Object
    {
        
        public int damage;

        float timeAttack = 0;
        const float gruntMovementSpeed = 0.6f;
        const float assassinMovementSpeed = 1.1f;
        const float tankMovementSpeed = 0.5f;

        EnemyType enemyType;
        public Tower target;

        public Boolean dead = false;
        Boolean facingRight = false, facingDown = false, facingLeft = false, facingUp = false;
        Vector2 goal;

        public Boolean stunned = false;
        public float timeSinceStun = 0.0f;
        public float maxTimeStunned = 1000.0f;

        public List<Texture2D> textures;
        public int frame = 0;
        public float timeSinceFrameChange = 0.0f;
        public float timeBetweenFrames = 100.0f;

        public int moneyForKilling;

        public Enemy(Movespeed m, ArmorValue a, Damage d, int health, Sprite s, Vector2 l, EnemyType enemyType, List<Texture2D> textures)
            : base(m, a, d, health, s, l)
        {
            this.enemyType = enemyType;
            this.textures = textures;

            Random rand = new Random();

            int x = rand.Next(229, 229 + 185);
            int y = 403;
            goal = new Vector2(x, y);

            if (enemyType == EnemyType.Grunt)
            {
                moneyForKilling = 50;
                int x2 = rand.Next(229, 229 + 185);
                int y2 = 403;
                goal = new Vector2(x2, y2);
            }
            else if (enemyType == EnemyType.Assassin)
            {
                moneyForKilling = 100;
            }
            else
            {
                moneyForKilling = 200;
            }
        }

        public void update(GameTime elapsedTime, List<Tower> towers, List<Trap> traps, HomeBase homeTarget, List<Enemy> enemies, int levelCount)
        {
            timeAttack += elapsedTime.ElapsedGameTime.Milliseconds;
           
            float currx = location.X;
            bool blocked = false;
            Boolean isAttacking = false;
            EnemyType value = enemyType;

            int multiplier = 1;
            if (levelCount >= 1)
            {
                multiplier = 2;
            }

            if (!stunned)
            {
                if (enemyType == EnemyType.Grunt)
                {
                    if (goal == null)
                    {
                        Random rand = new Random(0);
                        int x = rand.Next(229, 229 + 185);
                        int y = 403;
                        goal = new Vector2(x, y);
                    }

                    if (timeAttack > 3000f)
                    {
                        damage = 3;
                        timeAttack = 0;
                    }
                    else
                    {
                        damage = 0;
                    }
                    Vector2 current = new Vector2(location.X, location.Y - 1);
                    Vector2 northNeighbor = new Vector2(location.X, location.Y - 1);
                    Vector2 southNeighbor = new Vector2(location.X, location.Y + 1);
                    Vector2 eastNeighbor = new Vector2(location.X + 1, location.Y);
                    Vector2 westNeighbor = new Vector2(location.X - 1, location.Y);

                    Vector2 relative = goal - current;

                    foreach (Tower t in towers)
                    {
                        Rectangle targRectangle = new Rectangle((int)t.location.X, (int)t.location.Y, 64, 64);
                        if (bounds.Intersects(targRectangle) && location.X < 320)
                        {
                            location.X -= gruntMovementSpeed * multiplier;
                            blocked = true;
                        }
                        else if (bounds.Intersects(targRectangle) && location.X >= 320)
                        {
                            location.X += gruntMovementSpeed * multiplier;
                            blocked = true;
                        }
                    }
                    if (bounds.Intersects(homeTarget.bounds))
                    {
                        isAttacking = true;
                        homeTarget.collide(this);
                    }


                    if (!blocked && !isAttacking)
                    {

                        if (relative.X > 0)
                        {
                            facingRight = true;
                            location.X += gruntMovementSpeed * multiplier;
                        }
                        if (relative.X < 0)
                        {
                            facingLeft = true;
                            location.X -= gruntMovementSpeed * multiplier;
                        } if (relative.Y > 0)
                        {
                            facingDown = true;
                            location.Y += gruntMovementSpeed * multiplier;
                        } if (relative.Y < 0)
                        {
                            facingUp = true;
                            location.Y -= gruntMovementSpeed * multiplier;
                        }
                    }




                }
                target = null;

                if (value == EnemyType.Tank)
                {
                    if (timeAttack > 300f)
                    {
                        damage = 15;
                        timeAttack = 0;
                    }
                    else
                    {
                        damage = 0;
                    }
                    if (!(findClosestTower(towers) == null))
                    {


                        target = findClosestTower(towers);
                        Rectangle targetRectangle = new Rectangle((int)target.location.X, (int)target.location.Y, 64, 64);
                        goal = new Vector2(target.location.X, target.location.Y);

                        if(bounds.Intersects(targetRectangle))
                        {
                            target.collide(this);
                            isAttacking = true;
                        }
                    }
                    else
                    {
                        Random rand = new Random();
                        //Rectangle targetRectangle = new Rectangle((int)target.location.X, (int)target.location.Y, 64, 64);
                        int x = rand.Next(229, 229 + 185);
                        int y = 403;
                        goal = new Vector2(x, y);
                        if (timeAttack > 1000f)
                        {
                            damage = 5;
                        }

                        if(bounds.Intersects(homeTarget.bounds))
                        {
                            homeTarget.collide(this);
                            isAttacking = true;
                        }
                    }


                    Vector2 current = new Vector2(location.X, location.Y - 1);
                    Vector2 northNeighbor = new Vector2(location.X, location.Y - 1);
                    Vector2 southNeighbor = new Vector2(location.X, location.Y + 1);
                    Vector2 eastNeighbor = new Vector2(location.X + 1, location.Y);
                    Vector2 westNeighbor = new Vector2(location.X - 1, location.Y);

                    Vector2 relative = goal - current;
                    if (!isAttacking)
                    {
                        if (relative.X > 0)
                        {
                            facingRight = true;
                            location.X += tankMovementSpeed * multiplier;
                        }
                        if (relative.X < 0)
                        {
                            facingLeft = true;
                            location.X -= tankMovementSpeed * multiplier;
                        } if (relative.Y > 0)
                        {
                            facingDown = true;
                            location.Y += tankMovementSpeed * multiplier;
                        } if (relative.Y < 0)
                        {
                            facingUp = true;
                            location.Y -= tankMovementSpeed * multiplier;
                        }

                    }


                }

                if (value == EnemyType.Assassin)
                {
                    Vector2 current = new Vector2(location.X, location.Y - 1);
                    Vector2 northNeighbor = new Vector2(location.X, location.Y - 1);
                    Vector2 southNeighbor = new Vector2(location.X, location.Y + 1);
                    Vector2 eastNeighbor = new Vector2(location.X + 1, location.Y);
                    Vector2 westNeighbor = new Vector2(location.X - 1, location.Y);

                    Vector2 relative = goal - current;

                    if (timeAttack > 3000f)
                    {
                        damage = 3;
                        timeAttack = 0;
                    }
                    else {
                        damage = 0;
                    }

                    foreach (Tower t in towers)
                    {
                        Rectangle targRectangle = new Rectangle((int)t.location.X, (int)t.location.Y, 64, 64);
                        if (bounds.Intersects(targRectangle) && location.X < 320)
                        {
                            location.X -= assassinMovementSpeed * multiplier;
                            blocked = true;
                        }
                        else if (bounds.Intersects(targRectangle) && location.X >= 320)
                        {
                            location.X += assassinMovementSpeed * multiplier;
                            blocked = true;
                        }
                    }


                    if (bounds.Intersects(homeTarget.bounds))
                    {
                        isAttacking = true;
                        homeTarget.collide(this);
                    }

                    if (!blocked && !isAttacking)
                    {

                        if (relative.X > 0)
                        {
                            facingRight = true;
                            location.X += assassinMovementSpeed * multiplier;
                        }
                        if (relative.X < 0)
                        {
                            facingLeft = true;
                            location.X -= assassinMovementSpeed * multiplier;
                        } if (relative.Y > 0)
                        {
                            facingDown = true;
                            location.Y += assassinMovementSpeed * multiplier;
                        } if (relative.Y < 0)
                        {
                            facingUp = true;
                            location.Y -= assassinMovementSpeed * multiplier;
                        }
                    }
                }

                bounds = new Rectangle((int)location.X, (int)location.Y, sprite.frame.Width, sprite.frame.Height);
                foreach (Enemy e in enemies)
                {
                    if (bounds.Intersects(e.bounds) && (location.X != e.location.X && location.Y != e.location.Y))
                    {
                        location.X = currx;
                    }
                }

                foreach (Trap t in traps)
                {
                    if (bounds.Intersects(t.bounds) && t.activate())
                    {
                        timeSinceStun = 0.0f;
                        stunned = true;
                    }
                }
            }
            else
            {
                timeSinceStun += elapsedTime.ElapsedGameTime.Milliseconds;
                if (timeSinceStun >= maxTimeStunned)
                {
                    stunned = false;
                }
            }

            timeSinceFrameChange += elapsedTime.ElapsedGameTime.Milliseconds;
            if (timeSinceFrameChange >= timeBetweenFrames)
            {
                timeSinceFrameChange = 0.0f;
                frame++;
                frame %= textures.Count;
            }
        }

        public void draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(textures[frame], location, Color.White);
        }

        public void collide(Bullet bill)
        {
            this.health -= bill.damage;
            if (this.health <= 0)
            {
                dead = true;
            }
        }

        public Tower blockingTower(List<Tower> towers)
        {

            //this is a humbling experience

            //i was gonna call this "targetrekt"
            //320 is the width half way point
            goal = new Vector2(target.location.X, target.location.Y);

            foreach (Tower t in towers)
            {
                Rectangle targetRectangle = new Rectangle((int)target.location.X, (int)target.location.Y, 64, 64);

                if (bounds.Intersects(targetRectangle) || (location.X <= 320))
                {
                    location.X--;
                }
            }

            return null;
        }

        public Tower findClosestTower(List<Tower> towers)
        {
            Tower currentTower = null;

            if (towers.Count < 1)
            {
                return null;
            }

            foreach (Tower e in towers)
            {
                if (currentTower == null)
                {
                    currentTower = e;
                }
                else
                {
                    Vector2 tower = e.location;
                    Vector2 tank = this.location;
                    Vector2 cTower = currentTower.location;

                    double distance1 = (Math.Sqrt(Math.Pow(Math.Abs(tower.X - tank.X), 2) + Math.Pow(Math.Abs(tower.Y - tank.Y), 2)));
                    double distance2 = (Math.Sqrt(Math.Pow(Math.Abs(cTower.X - tower.X), 2) + Math.Pow(Math.Abs(cTower.Y - tower.Y), 2)));

                    if (distance1 < distance2)
                    {
                        currentTower = e;
                    }
                }

            }


            return currentTower;

        }
    }
}
