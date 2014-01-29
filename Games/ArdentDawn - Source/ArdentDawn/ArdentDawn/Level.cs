using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace ArdentDawn
{
    class Level
    {
        int gruntCount;
        int assassinCount;
        int tankCount;
        World world;

        List<Level> hordes;

        int hordesRemaining;
        int hordesMade;

        public float timeSinceLastHorde = 0.0f;
        public float minTimeBetweenHardes = 5000.0f;

        public Level(int g, int a, int t, World world, List<Level> h)
        {
            gruntCount = g;
            assassinCount = a;
            tankCount = t;
            this.world = world;
            hordes = h;
            hordesMade = 0;
            hordesRemaining = hordes.Count;
        }

        public void startLevel()
        {
            Random rand = new Random();

            for (int i = 0; i < gruntCount; i++)
            {
                int x = rand.Next(40, 600);
                int y = rand.Next(-400, -16);

                world.addEnemy(x, y, EnemyType.Grunt);
            }

            for (int i = 0; i < assassinCount; i++)
            {
                int x = rand.Next(40, 600);
                int y = rand.Next(-400, -32);

                world.addEnemy(x, y, EnemyType.Assassin);
            }

            for (int i = 0; i < tankCount; i++)
            {
                int x = rand.Next(40, 600);
                int y = rand.Next(-400, -64);

                world.addEnemy(x, y, EnemyType.Tank);
            }


        }

        public void addHorde(Level h)
        {
            hordesMade++;
            hordesRemaining--;
            gruntCount = h.gruntCount;
            assassinCount = h.assassinCount;
            tankCount = h.tankCount ;
            startLevel();
        }

        public void update(GameTime elapsedTime)
        {
            timeSinceLastHorde += elapsedTime.ElapsedGameTime.Milliseconds;
            if (timeSinceLastHorde >= minTimeBetweenHardes && hordesRemaining > 0)
            {
                timeSinceLastHorde = 0.0f;
                addHorde(hordes[hordesMade]);
            }
        }

        public Boolean isDone()
        {
            if (hordesRemaining <= 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
