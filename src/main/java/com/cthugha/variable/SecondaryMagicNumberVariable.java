package com.cthugha.variable;

import basemod.abstracts.DynamicVariable;
import com.cthugha.cards.AbstractCthughaCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondaryMagicNumberVariable extends DynamicVariable {
	@Override
	public String key() {
		return "cthughaM2";
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).isSecondaryMagicNumberModified;
		return false;
	}
	
	public int value(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).secondaryMagicNumber;
		return -1;
	}
	
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).baseSecondaryMagicNumber;
		return -1;
	}
	
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).upgradedSecondaryMagicNumber;
		return false;
	}
}
