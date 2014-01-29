using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ArdentDawn
{
    class Trap : Object
    {
        float elapsedTime = 0.0f;
        float maxTime = 7000.0f;
        public Boolean done = false;
        float timeSinceLastActivated = 1000.0f;
        float minTimeBetweenActivations = 700.0f;

        public Trap(Movespeed m, ArmorValue a, Damage d, int health, Sprite s, Vector2 l)
            : base(m, a, d, health, s, l)
        {
            bounds = new Rectangle((int)location.X, (int)location.Y, s.frame.Width, s.frame.Height);
        }

        public void update(GameTime elapsedTime)
        {
            this.elapsedTime += elapsedTime.ElapsedGameTime.Milliseconds;

            if (this.elapsedTime >= maxTime)
            {
                done = true;
            }
        }

        public void draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(sprite.frame, location, Color.White);
        }

        public Boolean activate()
        {
            if (timeSinceLastActivated >= minTimeBetweenActivations)
            {
                timeSinceLastActivated = 0.0f;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
