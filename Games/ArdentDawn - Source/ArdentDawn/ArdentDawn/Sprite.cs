using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ArdentDawn
{
    class Sprite
    {
        public Texture2D frame;

        public Sprite(Texture2D texture)
        {
            frame = texture;
        }
    }
}
