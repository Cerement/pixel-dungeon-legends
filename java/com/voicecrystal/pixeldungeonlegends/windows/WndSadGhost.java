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
package com.voicecrystal.pixeldungeonlegends.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.voicecrystal.pixeldungeonlegends.Dungeon;
import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.actors.mobs.npcs.Ghost;
import com.voicecrystal.pixeldungeonlegends.items.Item;
import com.voicecrystal.pixeldungeonlegends.items.quest.DriedRose;
import com.voicecrystal.pixeldungeonlegends.scenes.PixelScene;
import com.voicecrystal.pixeldungeonlegends.sprites.ItemSprite;
import com.voicecrystal.pixeldungeonlegends.ui.RedButton;
import com.voicecrystal.pixeldungeonlegends.ui.Window;
import com.voicecrystal.pixeldungeonlegends.utils.GLog;
import com.voicecrystal.pixeldungeonlegends.utils.Utils;

public class WndSadGhost extends Window {
	
	private static final String TXT_ROSE	= 
		"Yes! Yes!!! This is it! Please give it to me! " +
		"And you can take one of these items, maybe they " +
		"will be useful to you in your journey...";
	private static final String TXT_RAT	= 
		"Yes! The ugly creature is slain and I can finally rest... " +
		"Please take one of these items, maybe they " +
		"will be useful to you in your journey...";
	private static final String TXT_WEAPON	= "Ghost's weapon";
	private static final String TXT_ARMOR	= "Ghost's armor";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public WndSadGhost( final Ghost ghost, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), null ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline message = PixelScene.createMultiline( item instanceof DriedRose ? TXT_ROSE : TXT_RAT, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnWeapon = new RedButton( TXT_WEAPON ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.weapon );
			}
		};
		btnWeapon.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWeapon );
		
		RedButton btnArmor = new RedButton( TXT_ARMOR ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.armor );
			}
		};
		btnArmor.setRect( 0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnArmor );
		
		resize( WIDTH, (int)btnArmor.bottom() );
	}
	
	private void selectReward( Ghost ghost, Item item, Item reward ) {
		
		hide();
		
		item.detach( Dungeon.hero.belongings.backpack );
		
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, ghost.pos ).sprite.drop();
		}
		
		ghost.yell( "Farewell, adventurer!" );
		ghost.die( null );
		
		Ghost.Quest.complete();
	}
}
