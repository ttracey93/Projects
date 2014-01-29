using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ArdentDawn
{
    class Bullet
    {
        public Vector2 location;
        public Enemy target;
        public Vector2 targetLocation;
        public Vector2 velocity;
        public int speed;
        public int damage;
        public Sprite sprite;
        public Boolean done = false;
        public Rectangle bounds;

        public Bullet(Vector2 l, int s, Sprite sp, Enemy t, int d)
        {
            Vector2 temp = l;
            temp.X += 32;
            temp.Y += 32;
            location = temp;
            speed = s;
            sprite = sp;
            target = t;
            damage = d;

        }

        public void update(GameTime elapsedTime)
        {
            targetLocation = target.location;
            bounds = new Rectangle((int)location.X, (int)location.Y, 8, 8);

            velocity = Vector2.Zero;

            if (targetLocation.X > location.X)
            {
                velocity.X += 1;
            }
            else if(targetLocation.X < location.X)
            {
                velocity.X -= 1;
            }

            if (targetLocation.Y > location.Y)
            {
                velocity.Y += 1;
            }
            else if (targetLocation.Y < location.Y)
            {
                velocity.Y -= 1;
            }

            velocity *= speed;
            location += velocity;

            if(bounds.Intersects(target.bounds))
            {
                target.collide(this);
                done = true;
            }
        }

        public void draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(sprite.frame, location, Color.White);
        }
    }
}
