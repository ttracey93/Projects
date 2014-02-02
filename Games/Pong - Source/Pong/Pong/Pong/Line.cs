using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace Pong
{
    class Line
    {
        public Vector2 Start;
        public Vector2 End;

        public Line(Vector2 l, Vector2 e)
        {
            Start = l;
            End = e;
        }
    }
}
