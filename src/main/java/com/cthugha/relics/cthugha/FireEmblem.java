package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

public class FireEmblem extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(FireEmblem.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/FireEmblem.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/FireEmblem.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/FireEmblem.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

	public static int MAX_HP = 6;

	public FireEmblem() {
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
	public void justEnteredRoom(AbstractRoom room) {
		if (AbstractDungeon.actNum == 3 && room instanceof MonsterRoomBoss) {
			this.flash();

			int key = 0;
			if (Settings.hasEmeraldKey)
				key++;
			if (Settings.hasRubyKey)
				key++;
			if (Settings.hasSapphireKey)
				key++;

			AbstractDungeon.player.increaseMaxHp(MAX_HP * key, true);

			if (!Settings.hasRubyKey)
				AbstractDungeon.effectsQueue.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.RED));
			if (!Settings.hasEmeraldKey)
				AbstractDungeon.effectsQueue.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.GREEN));
			if (!Settings.hasSapphireKey)
				AbstractDungeon.effectsQueue.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.BLUE));
		}
	}

	@Override
	public boolean canSpawn() {
		return Settings.isFinalActAvailable && !Settings.isEndless && AbstractDungeon.floorNum < 50;
	}

	@Override
	public String getUpdatedDescription() {
		return String.format(this.DESCRIPTIONS[0], MAX_HP);
	}
}
