using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace ArdentDawn
{
    class HomeBase : Object
    {
        public int cash;
        public int priceOfTrap = 250;
        public int priceOfTower = 1000;
        public Boolean dead = false;
        public Rectangle bounds = new Rectangle(229, 402, 185, 30);

        public HomeBase(Movespeed m, ArmorValue a, Damage d, int health, Sprite s, Vector2 l)
            : base(m, a, d, health, s, l)
        {
            cash = 1500;
        }

        public Boolean canAffordTrap()
        {
            if (cash >= priceOfTrap)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public Boolean canAffordTower()
        {
            if (cash >= priceOfTower)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void collide(Enemy enemy)
        {
            this.health -= enemy.damage;
            if (this.health <= 0)
            {
                dead = true;
            }
        }
    }
}
