package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.ui.campfire.InstantlyRefineOption;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public class AshesToAshes extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(AshesToAshes.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/AshesToAshes.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/AshesToAshes.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/AshesToAshes.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.SHOP;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

	public AshesToAshes() {
		super(
				ID,
				ImageMaster.loadImage(IMG_PATH),
				ImageMaster.loadImage(IMG_OTL),
				RELIC_TIER,
				LANDING_SOUND
		);

		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}

	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	@Override
	public boolean canSpawn() {
		return AbstractDungeon.floorNum < 48 || Settings.isEndless;
	}

	@Override
	public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
		options.add(new InstantlyRefineOption(true));
	}
}
