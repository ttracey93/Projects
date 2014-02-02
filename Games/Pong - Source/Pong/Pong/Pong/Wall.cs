using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Pong
{
    class Wall
    {
        public Vector2 normal;
        public Rectangle bounds;
        public Texture2D texture;

        public Wall(Rectangle b, Vector2 n, Texture2D t)
        {
            normal = n;
            bounds = b;
            texture = t;
        }
    }
}
