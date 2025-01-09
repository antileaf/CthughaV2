package com.cthugha.blight;

import basemod.patches.whatmod.WhatMod;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;

import java.util.Arrays;

public class TheBurningOne extends AbstractBlight {
	public static final String SIMPLE_NAME = TheBurningOne.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	private static final BlightStrings blightStrings = CardCrawlGame.languagePack.getBlightString(ID);

	public TheBurningOne() {
		super(
				ID,
				blightStrings.NAME,
				(WhatMod.enabled ? Arrays.stream(WhatMod.findModName(TheBurningOne.class).split(" "))
						.map(s -> "#p" + s)
						.reduce(" ", (a, b) -> a + b) + " NL " : "")
						+ blightStrings.DESCRIPTION[0],
				"",
				true
		);

		this.img = ImageMaster.loadImage("cthughaResources/img/blights/TheBurningOne.png");
		this.outlineImg = ImageMaster.loadImage("cthughaResources/img/blights/outline/TheBurningOne.png");
	}
}
