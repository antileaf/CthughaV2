package com.cthugha.ui.campfire;

import basemod.ReflectionHacks;
import com.cthugha.effect.campfire.*;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.relics.cthugha.AshesToAshes;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.*;

import java.util.ArrayList;

public class InstantlyRefineOption extends AbstractCampfireOption {
	public static final String ID = CthughaHelper.makeID(InstantlyRefineOption.class.getSimpleName());

	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

	public InstantlyRefineOption(boolean active) {
		this.label = uiStrings.TEXT[0];
		this.usable = active;

		this.description = uiStrings.TEXT[1];

		this.img = ImageMaster.loadImage("cthughaResources/img/UI/campfire/refine.png");
	}

	@Override
	public void useOption() {
		RestRoom restRoom = (RestRoom) AbstractDungeon.getCurrRoom();
		ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(
				restRoom.campfireUI, CampfireUI.class, "buttons");

		AbstractDungeon.effectsQueue.add(new RefineEffect(buttons));
		if (AbstractDungeon.player.hasRelic(AshesToAshes.ID))
			AbstractDungeon.player.getRelic(AshesToAshes.ID).flash();

//		for (AbstractCampfireOption option : buttons) {
//			if (!option.usable)
//				continue;
//
//			if (option instanceof RestOption) {
//				AbstractDungeon.effectsQueue.add(new AnonymousEffect(() -> CardCrawlGame.sound.play("SLEEP_BLANKET")));
//				AbstractDungeon.effectsQueue.add(new SleepEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//			else if (option instanceof SmithOption) {
//				AbstractDungeon.effectsQueue.add(new SmithEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//			else if (option instanceof RecallOption) {
//				AbstractDungeon.effectsQueue.add(new RecallEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//			else if (option instanceof TokeOption) {
//				AbstractDungeon.effectsQueue.add(new TokeEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//			else if (option instanceof DigOption) {
//				AbstractDungeon.effectsQueue.add(new DigEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//			else if (option instanceof LiftOption) {
//				AbstractDungeon.effectsQueue.add(new LiftEffect());
//				AbstractDungeon.effectsQueue.add(new WaitEffect());
//			}
//		}
//
//		AbstractDungeon.effectsQueue.add(new AnonymousEffect(() -> {
//			((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
//			AbstractRoom.waitTimer = 0.0F;
//			AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
//			((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
//		}));


	}
}
