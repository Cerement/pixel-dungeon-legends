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

import java.util.ArrayList;

import com.voicecrystal.pixeldungeonlegends.Badges;
import com.voicecrystal.pixeldungeonlegends.Statistics;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Hunger;
import com.voicecrystal.pixeldungeonlegends.effects.Speck;
import com.voicecrystal.pixeldungeonlegends.effects.SpellSprite;
import com.voicecrystal.pixeldungeonlegends.items.scrolls.ScrollOfRecharging;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.voicecrystal.pixeldungeonlegends.Assets;
import com.voicecrystal.pixeldungeonlegends.Dungeon;
import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Barkskin;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.actors.hero.HeroSubClass;
import com.voicecrystal.pixeldungeonlegends.effects.CellEmitter;
import com.voicecrystal.pixeldungeonlegends.effects.particles.LeafParticle;
import com.voicecrystal.pixeldungeonlegends.items.Dewdrop;
import com.voicecrystal.pixeldungeonlegends.items.Generator;
import com.voicecrystal.pixeldungeonlegends.items.Item;
import com.voicecrystal.pixeldungeonlegends.levels.Level;
import com.voicecrystal.pixeldungeonlegends.levels.Terrain;
import com.voicecrystal.pixeldungeonlegends.sprites.PlantSprite;
import com.voicecrystal.pixeldungeonlegends.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Plant implements Bundlable {

	public String plantName;
	
	public int image;
	public int pos;
	
	public PlantSprite sprite;
	
	public void activate( Char ch ) {
		
		if (ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
			Buff.affect( ch, Barkskin.class ).level( ch.HT / 3 );
		}
		
		wither();
	}
	
	public void wither() {
		Dungeon.level.uproot( pos );
		
		sprite.kill();
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( LeafParticle.GENERAL, 6 );
		}
		
		if (Dungeon.hero.subClass == HeroSubClass.WARDEN) {
			if (Random.Int( 5 ) == 0) {
				Dungeon.level.drop( Generator.random( Generator.Category.SEED ), pos ).sprite.drop();
			}
			if (Random.Int( 5 ) == 0) {
				Dungeon.level.drop( new Dewdrop(), pos ).sprite.drop();
			}
		}
	}
	
	private static final String POS	= "pos";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
	}
	
	public String desc() {
		return null;
	}
	
	public static class Seed extends Item {
		
		public static final String AC_PLANT	= "PLANT";
        public static final String AC_EAT	= "EAT";

        private static final float TIME_TO_EAT	= 1f;

        public float energy = Hunger.HUNGRY * 0.25f;
        public String message = "Eh, it tested like grass.";
		
		private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";
		
		private static final float TIME_TO_PLANT = 1f;
		
		{
			stackable = true;	
			defaultAction = AC_THROW;
		}
		
		protected Class<? extends Plant> plantClass;
		protected String plantName;
		
		public Class<? extends Item> alchemyClass;
		
		@Override
		public ArrayList<String> actions( Hero hero ) {
			ArrayList<String> actions = super.actions( hero );
			actions.add( AC_PLANT );
            actions.add( AC_EAT );
			return actions;
		}
		
		@Override
		protected void onThrow( int cell ) {
			if (Dungeon.level.map[cell] == Terrain.ALCHEMY || Level.pit[cell]) {
				super.onThrow( cell );
			} else {
				Dungeon.level.plant( this, cell );
			}
		}
		
		@Override
		public void execute( Hero hero, String action ) {
			if (action.equals( AC_PLANT )) {
							
				hero.spend( TIME_TO_PLANT );
				hero.busy();
				((Seed)detach( hero.belongings.backpack )).onThrow( hero.pos );
				
				hero.sprite.operate( hero.pos );
				
			}
            else if(action.equals( AC_EAT )) {
                detach( hero.belongings.backpack );

                ((Hunger)hero.buff( Hunger.class )).satisfy( energy );

                switch (hero.heroClass) {
                    case WARRIOR:
                        if (hero.HP < hero.HT) {
                            hero.HP = Math.min( hero.HP + 2, hero.HT );
                            hero.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
                        }
                        break;
                    case MAGE:
                        break;
                    case ROGUE:
                    case HUNTRESS:
                        break;
                }

                hero.sprite.operate( hero.pos );
                hero.busy();
                SpellSprite.show(hero, SpellSprite.FOOD);
                Sample.INSTANCE.play( Assets.SND_EAT );

                hero.spend( TIME_TO_EAT );

                Statistics.foodEaten++;
                Badges.validateFoodEaten();
            }
            else {
				
				super.execute (hero, action );
				
			}
		}
		
		public Plant couch( int pos ) {
			try {
				if (Dungeon.visible[pos]) {
					Sample.INSTANCE.play( Assets.SND_PLANT );
				}
				Plant plant = plantClass.newInstance();
				plant.pos = pos;
				return plant;
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		public boolean isUpgradable() {
			return false;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public int price() {
			return 10 * quantity;
		}
		
		@Override
		public String info() { 
			return String.format( TXT_INFO, Utils.indefinite( plantName ), desc() );
		}
	}
}
