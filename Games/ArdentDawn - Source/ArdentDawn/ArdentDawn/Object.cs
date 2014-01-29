using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace ArdentDawn
{
    class Object
    {
        public int health;
        public Movespeed moveSpeed;
        public ArmorValue armor;
        public Damage damage;
        public Vector2 location;
        public Sprite sprite;
        public Rectangle bounds;

        public Object(Movespeed m, ArmorValue a, Damage d, int health, Sprite s, Vector2 l)
        {
            this.health = health;
            moveSpeed = m;
            armor = a;
            damage = d;
            sprite = s;
            location = l;

            bounds = new Rectangle(0, 0, s.frame.Width, s.frame.Height);
        }
    }
}
