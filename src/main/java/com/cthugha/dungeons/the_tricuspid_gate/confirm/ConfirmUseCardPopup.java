package com.cthugha.dungeons.the_tricuspid_gate.confirm;

import basemod.ReflectionHacks;
import com.cthugha.patches.dungeon.ConfirmationPatch;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;

public class ConfirmUseCardPopup extends ConfirmPopup {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			CthughaHelper.makeID(ConfirmUseCardPopup.class.getSimpleName()));

	public ConfirmUseCardPopup() {
		super(uiStrings.TEXT[0], uiStrings.TEXT[1], null);
	}

	@Override
	protected void yesButtonEffect() {
		if (AbstractDungeon.player.hoveredCard == null)
			System.out.println("Hovered card is null!");

		ConfirmationPatch.Fields.needConfirm.set(AbstractDungeon.actionManager.cardQueue.get(0), 0);

		AbstractDungeon.closeCurrentScreen();
	}

	@Override
	protected void noButtonEffect() {
		AbstractDungeon.actionManager.cardQueue.remove(0);
		AbstractDungeon.player.releaseCard();

		AbstractPlayer p = AbstractDungeon.player;
//		InputHelper.justClickedLeft = false;
//		p.releaseCard();

		AbstractDungeon.closeCurrentScreen();
	}
}
