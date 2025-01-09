package com.cthugha.relics.publics;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.EvolvePower;

public class Frangiclave extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(Frangiclave.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/碎门之钥.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/碎门之钥.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/碎门之钥.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	private static final int DRAW = 2;

	public Frangiclave() {
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
		return String.format(this.DESCRIPTIONS[0], DRAW);
	}

	@Override
	public void onEquip() {
		if (CthughaHelper.isInBattle()) {
			this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new EvolvePower(AbstractDungeon.player, DRAW)));
		}
	}

	@Override
	public void atBattleStart() {
		this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

		this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
				new EvolvePower(AbstractDungeon.player, DRAW)));
	}
}
