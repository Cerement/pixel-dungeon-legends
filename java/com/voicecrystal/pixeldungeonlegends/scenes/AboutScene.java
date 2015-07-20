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
package com.voicecrystal.pixeldungeonlegends.scenes;

import android.content.Intent;
import android.net.Uri;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.voicecrystal.pixeldungeonlegends.PixelDungeon;
import com.voicecrystal.pixeldungeonlegends.effects.Flare;
import com.voicecrystal.pixeldungeonlegends.ui.Archs;
import com.voicecrystal.pixeldungeonlegends.ui.ExitButton;
import com.voicecrystal.pixeldungeonlegends.ui.Icons;
import com.voicecrystal.pixeldungeonlegends.ui.Window;

public class AboutScene extends PixelScene {

	private static final String TXT_NAGA =
			"Modder: Naga Chiang\n\n" +
			"This mod is based on Pixel Dungeon 1.7.5a. " +
			"Really appreciate that Watabou open sourced this great work. " +
			"Please, make sure to try out the original version!";

	private static final String LNK_NAGA = "pixeldungeon.watabou.ru";

	private static final String TXT = 
		"Code & Graphics: Watabou\n" +
		"Music: Cube_Code\n\n" + 
		"This game is inspired by Brian Walker's Brogue. " +
		"Try it on Windows, Mac OS or Linux - it's awesome! ;)\n\n" +
		"Please visit official website for additional info:";
	
	private static final String LNK = "pixeldungeon.watabou.ru";
	
	@Override
	public void create() {
		super.create();

		// Mod about
		BitmapTextMultiline textNaga = createMultiline( TXT_NAGA, 8 );
		textNaga.maxWidth = Math.min( Camera.main.width, 120 );
		textNaga.measure();
		add( textNaga );

		textNaga.x = align( (Camera.main.width - textNaga.width()) / 2 );
		textNaga.y = align( (Camera.main.height - textNaga.height()) / 4 ) + 8;

		BitmapTextMultiline titleNaga = createMultiline( "Pixel Dungeon: Legends", 8 );
		titleNaga.maxWidth = Math.min( Camera.main.width, 120 );
		titleNaga.measure();
		titleNaga.hardlight(Window.TITLE_COLOR);
		add(titleNaga);

		titleNaga.x = align((Camera.main.width - titleNaga.width()) / 2);
		titleNaga.y = align( textNaga.y - titleNaga.height() / 2) - 8;

//		BitmapTextMultiline link = createMultiline( LNK, 8 );
//		link.maxWidth = Math.min( Camera.main.width, 120 );
//		link.measure();
//		link.hardlight( Window.TITLE_COLOR );
//		add( link );

//		link.x = textNaga.x;
//		link.y = textNaga.y + textNaga.height();

		// Original about
		BitmapTextMultiline text = createMultiline( TXT, 8 );
		text.maxWidth = Math.min( Camera.main.width, 120 );
		text.measure();
		add( text );
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( (Camera.main.height - text.height()) * (3.0f/4.0f) ) + 8;

		BitmapTextMultiline title = createMultiline( "Pixel Dungeon", 8 );
		title.maxWidth = Math.min( Camera.main.width, 120 );
		title.measure();
		title.hardlight(0xFFFF44);
		add(title);

		title.x = align((Camera.main.width - title.width()) / 2);
		title.y = align( text.y - title.height() / 2) - 8;
		
		BitmapTextMultiline link = createMultiline( LNK, 8 );
		link.maxWidth = Math.min( Camera.main.width, 120 );
		link.measure();
		link.hardlight(0xFFFF44);
		add( link );
		
		link.x = text.x;
		link.y = text.y + text.height();
		
		TouchArea hotArea = new TouchArea( link ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK ) );
				Game.instance.startActivity( intent );
			}
		};
		add( hotArea );

		// Mod image
		Image imgNaga = Icons.NAGA.get();
		imgNaga.x = align( (Camera.main.width - imgNaga.width) / 2 );
		imgNaga.y = textNaga.y - imgNaga.height - 16;
		add( imgNaga );

		new Flare( 10, 64 ).color( 0x333333, true ).show( imgNaga, 0 ).angularSpeed = +40;

		// Original image
		Image wata = Icons.WATA.get();
		wata.x = align( (Camera.main.width - wata.width) / 2 );
		wata.y = text.y - wata.height - 16;
		add( wata );
		
		new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

		// UI
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeon.switchNoFade( TitleScene.class );
	}
}
