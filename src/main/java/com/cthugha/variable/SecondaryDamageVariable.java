package com.cthugha.variable;

import basemod.abstracts.DynamicVariable;
import com.cthugha.cards.AbstractCthughaCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondaryDamageVariable extends DynamicVariable {
	@Override
	public String key() {
		return "cthughaD2";
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).isSecondaryDamageModified;
		return false;
	}
	
	public int value(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).secondaryDamage;
		return -1;
	}
	
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).baseSecondaryDamage;
		return -1;
	}
	
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).upgradedSecondaryDamage;
		return false;
	}
}
