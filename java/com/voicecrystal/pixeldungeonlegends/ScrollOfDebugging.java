package com.voicecrystal.pixeldungeonlegends;

import java.util.ArrayList;

import com.voicecrystal.pixeldungeonlegends.actors.hero.Hero;
import com.voicecrystal.pixeldungeonlegends.items.Item;
import com.voicecrystal.pixeldungeonlegends.items.scrolls.ScrollOfEnchantment;

public class ScrollOfDebugging extends Item {

	{
		name = "Scroll of Debugging";
		image = 40;
	}
	
	public static final String AC_READ	= "READ";
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			new ScrollOfEnchantment().collect();
			
		} else {
		
			super.execute( hero, action );
			
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
}
