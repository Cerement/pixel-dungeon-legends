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
import com.voicecrystal.pixeldungeonlegends.ResultDescriptions;
import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.blobs.Blob;
import com.voicecrystal.pixeldungeonlegends.actors.blobs.Fire;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Combo;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.effects.CellEmitter;
import com.voicecrystal.pixeldungeonlegends.effects.particles.EnergyParticle;
import com.voicecrystal.pixeldungeonlegends.effects.particles.FlameParticle;
import com.voicecrystal.pixeldungeonlegends.items.potions.PotionOfLiquidFlame;
import com.voicecrystal.pixeldungeonlegends.scenes.GameScene;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSpriteSheet;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;
import com.voicecrystal.pixeldungeonlegends.utils.Utils;

public class Firebloom extends Plant {

	private static final String TXT_DESC = "When something touches a Firebloom, it bursts into flames.";
	
	{
		image = 0;
		plantName = "Firebloom";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		GameScene.add( Blob.seed( pos, 2, Fire.class ) );
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 5 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed implements Hero.Doom {

        public static final String AC_EAT	= "EAT";

		{
			plantName = "Firebloom";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_FIREBLOOM;
			
			plantClass = Firebloom.class;
			alchemyClass = PotionOfLiquidFlame.class;
		}

        @Override
        public void execute(Hero hero, String action)
        {
            if(action.equals( AC_EAT )) {

                super.execute(hero, action);

                GLog.n("Something is burning inside. You feel painful.");
                hero.damage((int)(hero.HT * 0.2), this);

                GLog.p("An overwhelming power charged on your arms.");
                hero.focusFactor += 2f;
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

        @Override
        public void onDeath() {
            Dungeon.fail( Utils.format(ResultDescriptions.FIREBLOOM, Dungeon.depth) );
            GLog.n( "You are devoured by the inner flame..." );
        }
	}
}
