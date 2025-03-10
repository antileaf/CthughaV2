package com.cthugha.glow;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.utils.ZhiLiaoHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ZhiLiaoGlowInfo extends CardBorderGlowManager.GlowInfo {
	@Override
	public boolean test(AbstractCard card) {
		return card instanceof AbstractCthughaCard &&
				((AbstractCthughaCard) card).canZhiLiao &&
				ZhiLiaoHelper.canTriggerZhiLiao();
	}

	@Override
	public Color getColor(AbstractCard card) {
		return Color.GREEN.cpy();
	}

	@Override
	public String glowID() {
		return CthughaHelper.makeID("ZhiLiaoGlow");
	}
}
