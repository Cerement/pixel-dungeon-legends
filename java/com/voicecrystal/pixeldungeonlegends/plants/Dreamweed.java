/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.voicecrystal.pixeldungeonlegends.plants;

import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.blobs.Blob;
import com.voicecrystal.pixeldungeonlegends.actors.blobs.ConfusionGas;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Slow;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Speed;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.effects.particles.EnergyParticle;
import com.voicecrystal.pixeldungeonlegends.items.potions.PotionOfInvisibility;
import com.voicecrystal.pixeldungeonlegends.scenes.GameScene;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSpriteSheet;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;

public class Dreamweed extends Plant {

	private static final String TXT_DESC = 
		"Upon touching a Dreamweed it secretes a glittering cloud of confusing gas.";
	
	{
		image = 3;
		plantName = "Dreamweed";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			GameScene.add( Blob.seed( pos, 400, ConfusionGas.class ) );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {

        public static final String AC_EAT	= "EAT";

		{
			plantName = "Dreamweed";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_DREAMWEED;
			
			plantClass = Dreamweed.class;
			alchemyClass = PotionOfInvisibility.class;
		}

        @Override
        public void execute(Hero hero, String action)
        {
            if(action.equals( AC_EAT )) {

                super.execute(hero, action);

                GLog.p("You feel like running!");
                Buff.prolong(hero, Speed.class, 5f);
                hero.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
            }

            else
            {
                super.execute(hero, action);
            }
        }

        @Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
