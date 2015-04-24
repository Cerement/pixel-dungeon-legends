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
package com.voicecrystal.pixeldungeonlegends.items.wands;

import com.watabou.noosa.audio.Sample;
import com.voicecrystal.pixeldungeonlegends.Assets;
import com.voicecrystal.pixeldungeonlegends.actors.Actor;
import com.voicecrystal.pixeldungeonlegends.actors.Char;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Buff;
import com.voicecrystal.pixeldungeonlegends.actors.buffs.Poison;
import com.voicecrystal.pixeldungeonlegends.effects.MagicMissile;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfPoison extends Wand {

	{
		name = "Wand of Poison";
	}
	
	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );
		if (ch != null) {

			Buff.affect( ch, Poison.class ).set( Poison.durationFactor( ch ) * (5 + level()) );
			
		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.poison( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"The vile blast of this twisted bit of wood will imbue its target " +
			"with a deadly venom. A creature that is poisoned will suffer periodic " +
			"damage until the effect ends. The duration of the effect increases " +
			"with the level of the staff.";
	}
}
