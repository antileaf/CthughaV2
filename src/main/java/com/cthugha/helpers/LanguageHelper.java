package com.cthugha.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public abstract class LanguageHelper {
	public static UIStrings shunRanUIStrings = CardCrawlGame.languagePack.getUIString(
			ModHelper.makeID("ShunRan"));

	public static String getShunRanString(ArrayList<Integer> levels) {
		return String.format(shunRanUIStrings.TEXT[0],
				levels.stream()
						.map(String::valueOf)
						.reduce((a, b) -> a + "/" + b)
						.orElse(""));
	}
}
