package com.cthugha.variable;

import basemod.abstracts.DynamicVariable;
import com.cthugha.cards.AbstractCthughaCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ShunRanVariable extends DynamicVariable {
	@Override
	public String key() {
		return "shunRan"; // 因为一些原因首字母不能大写
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).isShunRanModified;
		return false;
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).shunRan;
		return -1;
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).baseShunRan;
		return -1;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractCthughaCard)
			return ((AbstractCthughaCard) card).upgradedShunRan;
		return false;
	}
}
