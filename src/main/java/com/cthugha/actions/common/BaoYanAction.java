package com.cthugha.actions.common;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.utils.BaoYanHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BaoYanAction extends AbstractGameAction {
	private final AbstractCthughaCard card;
	private final Runnable trueFollowUp, falseFollowUp;

	public BaoYanAction(AbstractCthughaCard card, Runnable trueFollowUp, Runnable falseFollowUp, float duration) {
		this.card = card;
		this.trueFollowUp = trueFollowUp;
		this.falseFollowUp = falseFollowUp;
		this.duration = this.startDuration = duration;
	}

	public BaoYanAction(AbstractCthughaCard card, Runnable trueFollowUp) {
		this(card, trueFollowUp, null, 0.0F);
	}

	public BaoYanAction(AbstractCthughaCard card, AbstractGameAction followUpAction) {
		this(card, () -> AbstractDungeon.actionManager.addToTop(followUpAction), null, 0.0F);
	}

	public BaoYanAction(AbstractCthughaCard card, Runnable trueFollowUp, Runnable falseFollowUp) {
		this(card, trueFollowUp, falseFollowUp, 0.0F);
	}

	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (BaoYanHelper.canTriggerBaoYanOnUse(this.card))
				this.trueFollowUp.run();
			else if (this.falseFollowUp != null)
				this.falseFollowUp.run();
		}

		if (this.startDuration == 0.0F)
			this.isDone = true;
		else
			this.tickDuration();
	}
}
