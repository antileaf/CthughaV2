package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class SoulCoin extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(SoulCoin.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/SoulCoin.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/SoulCoin.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/SoulCoin.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.COMMON;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

	public static final int STRENGTH = 3;

	private boolean canTrigger = false;
	private boolean activated = false;

	public SoulCoin() {
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
	public void onEnterRoom(AbstractRoom room) {
		this.activated = false;
		this.stopPulse();
	}

	@Override
	public void atBattleStart() {
		this.addToBot(new AnonymousAction(() -> this.canTrigger = true));
	}

	@Override
	public void onDrawOrDiscard() {
		if (!CthughaHelper.isInBattle())
			return;

		if (!this.canTrigger)
			return;

		int count = (int) AbstractDungeon.player.hand.group.stream()
				.filter(CthughaHelper::isBurnCard)
				.count();

		boolean ret = count * 2 >= AbstractDungeon.player.hand.size();

		if (ret && !this.activated) {
			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, STRENGTH)));

			this.activated = true;
			this.beginLongPulse();
		} else if (!ret && this.activated) {
			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, -STRENGTH)));

			this.activated = false;
			this.stopPulse();
		}
	}

	@Override
	public void onVictory() {
		this.activated = false;
		this.stopPulse();
	}

	@Override
	public String getUpdatedDescription() {
		return String.format(this.DESCRIPTIONS[0], STRENGTH);
	}
}
