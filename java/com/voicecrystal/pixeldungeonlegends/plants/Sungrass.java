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
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Bleeding;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Cripple;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Light;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Poison;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Weakness;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.effects.CellEmitter;
import com.voicecrystal.pixeldungeonlegends.effects.Flare;
import com.voicecrystal.pixeldungeonlegends.effects.Speck;
import com.voicecrystal.pixeldungeonlegends.effects.particles.ShaftParticle;
import com.voicecrystal.pixeldungeonlegends.items.potions.PotionOfHealing;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSpriteSheet;
import com.voicecrystal.pixeldungeonlegends.ui.BuffIndicator;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant {

	private static final String TXT_DESC = "Sungrass is renowned for its sap's healing properties.";
	
	{
		image = 4;
		plantName = "Sungrass";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Health.class );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {

        public static final String AC_EAT	= "EAT";

        {
			plantName = "Sungrass";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SUNGRASS;
			
			plantClass = Sungrass.class;
			alchemyClass = PotionOfHealing.class;
		}

        @Override
        public void execute(Hero hero, String action)
        {
            if(action.equals( AC_EAT )) {

                super.execute(hero, action);

                GLog.p("Nutrients make you healthy.");
                Buff.detach( hero, Poison.class );
                Buff.detach( hero, Cripple.class );
                Buff.detach( hero, Weakness.class );
                Buff.detach(hero, Bleeding.class);
                new Flare( 6, 32 ).show( hero.sprite, 2f ) ;

                GLog.p("You glow in the darkness.");
                Buff.affect(hero, Light.class, 50f);
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
	
	public static class Health extends Buff {
		
		private static final float STEP = 5f;
		
		private int pos;
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
			return super.attachTo( target );
		}
		
		@Override
		public boolean act() {
			if (target.pos != pos || target.HP >= target.HT) {
				detach();
			} else {
				target.HP = Math.min( target.HT, target.HP + target.HT / 10 );
				target.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
			spend( STEP );
			return true;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public String toString() {
			return "Herbal healing";
		}
		
		private static final String POS	= "pos";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
		}
	}
}
