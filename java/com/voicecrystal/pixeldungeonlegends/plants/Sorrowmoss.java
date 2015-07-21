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

import com.voicecrystal.pixeldungeonlegends.Dungeon;
import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.MindVision;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Poison;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Weakness;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.effects.CellEmitter;
import com.voicecrystal.pixeldungeonlegends.effects.particles.PoisonParticle;
import com.voicecrystal.pixeldungeonlegends.items.potions.PotionOfToxicGas;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSpriteSheet;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;

public class Sorrowmoss extends Plant {

	private static final String TXT_DESC = 
		"A Sorrowmoss is a flower (not a moss) with razor-sharp petals, coated with a deadly venom.";
	
	{
		image = 2;
		plantName = "Sorrowmoss";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Poison.class ).set( Poison.durationFactor( ch ) * (4 + Dungeon.depth / 2) );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 3 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {

        public static final String AC_EAT	= "EAT";

        {
			plantName = "Sorrowmoss";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SORROWMOSS;
			
			plantClass = Sorrowmoss.class;
			alchemyClass = PotionOfToxicGas.class;
		}

        @Override
        public void execute(Hero hero, String action)
        {
            if(action.equals( AC_EAT )) {

                super.execute(hero, action);

                GLog.p("Now you can take a peek at the monsters' mind...");
                Buff.prolong(hero, MindVision.class, 5f);

                GLog.w("So much feelings...");
                Buff.prolong(hero, Weakness.class, 5f);
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
