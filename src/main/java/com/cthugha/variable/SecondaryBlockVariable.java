package com.cthugha.variable;

import basemod.abstracts.DynamicVariable;
import com.cthugha.cards.AbstractCthughaCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondaryBlockVariable extends DynamicVariable {
	@Override
	public String key() {
		return "cthughaB2";
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).isSecondaryBlockModified;
		return false;
	}
	
	public int value(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).secondaryBlock;
		return -1;
	}
	
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).baseSecondaryBlock;
		return -1;
	}
	
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).upgradedSecondaryBlock;
		return false;
	}
}
