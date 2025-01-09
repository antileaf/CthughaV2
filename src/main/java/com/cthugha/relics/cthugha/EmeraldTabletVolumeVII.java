package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class EmeraldTabletVolumeVII extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(EmeraldTabletVolumeVII.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/EmeraldTabletVolumeVII.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/EmeraldTabletVolumeVII.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/EmeraldTabletVolumeVII.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.RARE;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

	public static final int VIGOR = 4;

	public EmeraldTabletVolumeVII() {
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
	public void atTurnStart() {
		this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
				new VigorPower(AbstractDungeon.player, VIGOR)));
	}

	@Override
	public String getUpdatedDescription() {
		return String.format(this.DESCRIPTIONS[0], VIGOR);
	}
}
