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
package com.voicecrystal.pixeldungeonlegends.items.armor.glyphs;

import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Burning;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Frost;
import com.voicecrystal.pixeldungeonlegends.effects.CellEmitter;
import com.voicecrystal.pixeldungeonlegends.effects.particles.FlameParticle;
import com.voicecrystal.pixeldungeonlegends.effects.particles.SnowParticle;
import com.voicecrystal.pixeldungeonlegends.items.armor.Armor;
import com.voicecrystal.pixeldungeonlegends.items.armor.Armor.Glyph;
import com.voicecrystal.pixeldungeonlegends.levels.Level;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSprite;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class AntiEntropy extends Glyph {

	private static final String TXT_ANTI_ENTROPY	= "%s of anti-entropy";
	
	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, armor.level );
		
		if (Level.adjacent( attacker.pos, defender.pos ) && Random.Int( level + 6 ) >= 5) {
			
			Buff.prolong( attacker, Frost.class, Frost.duration( attacker ) * Random.Float( 1f, 1.5f ));
			CellEmitter.get( attacker.pos ).start( SnowParticle.FACTORY, 0.2f, 6 );
			
			Buff.affect( defender, Burning.class ).reignite( defender );
			defender.sprite.emitter().burst( FlameParticle.FACTORY, 5 );

		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_ANTI_ENTROPY, weaponName );
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
