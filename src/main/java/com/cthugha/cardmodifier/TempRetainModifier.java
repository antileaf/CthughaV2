//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import com.cthugha.strings.CthughaCardModifierStrings;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TempRetainModifier extends AbstractCardModifier {
	public static final String ID = CthughaHelper.makeID(TempRetainModifier.class.getSimpleName());
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

	public TempRetainModifier() {
	}

	@Override
	public boolean removeAtEndOfTurn(AbstractCard card) {
		return true;
	}

	@Override
	public String modifyDescription(String rawDescription, AbstractCard card) {
		return rawDescription + " NL " + uiStrings.TEXT[0];
	}

	@Override
	public boolean shouldApply(AbstractCard card) {
		return !card.selfRetain && !card.retain &&
				card.type != AbstractCard.CardType.CURSE &&
				card.type != AbstractCard.CardType.STATUS;
	}

	@Override
	public void onInitialApplication(AbstractCard card) {
		card.retain = true;
	}

	@Override
	public void onRemove(AbstractCard card) {
		card.retain = false;
	}

	@Override
	public AbstractCardModifier makeCopy() {
		return new TempRetainModifier();
	}

	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
}
