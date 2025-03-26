package com.cthugha.dungeons.the_tricuspid_gate.proceed;

import basemod.ReflectionHacks;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmUseCardPopup;
import com.cthugha.patches.dungeon.GoToFifthActPatch;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

public class ConfirmProceedPopup extends ConfirmPopup {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			CthughaHelper.makeID(ConfirmProceedPopup.class.getSimpleName()));

	private final boolean goToFifthAct;

	public ConfirmProceedPopup(boolean goToFifthAct) {
		super(uiStrings.TEXT[0], uiStrings.TEXT[goToFifthAct ? 2 : 1], null);

		this.goToFifthAct = goToFifthAct;
	}

	@Override
	protected void yesButtonEffect() {
		if (goToFifthAct) {
			CardCrawlGame.music.fadeOutBGM();
			CardCrawlGame.music.fadeOutTempBGM();
			AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
			AbstractDungeon.fadeOut();
			AbstractDungeon.isDungeonBeaten = true;
			CardCrawlGame.nextDungeon = TheTricuspidGate.ID;
		}
		else {
			CardCrawlGame.music.fadeOutBGM();
			MapRoomNode node = new MapRoomNode(3, 4);
			node.room = new TrueVictoryRoom();
			AbstractDungeon.nextRoom = node;
			AbstractDungeon.closeCurrentScreen();
			AbstractDungeon.nextRoomTransitionStart();

			AbstractDungeon.overlayMenu.proceedButton.hide();
			GoToFifthActPatch.Fields.another.get(AbstractDungeon.overlayMenu).hide();
		}
//			ReflectionHacks.privateMethod(ProceedButton.class, "goToTrueVictoryRoom")
//					.invoke(AbstractDungeon.overlayMenu.proceedButton);
	}

	@Override
	protected void noButtonEffect() {
		AbstractDungeon.closeCurrentScreen();
	}
}
