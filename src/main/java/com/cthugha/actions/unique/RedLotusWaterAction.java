//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.actions.unique;

import basemod.BaseMod;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class RedLotusWaterAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(RedLotusWaterAction.class.getName());

	private int count;

	private RedLotusWaterAction(int count) {
		this.count = count;

		this.actionType = ActionType.DRAW;
	}

	public RedLotusWaterAction() {
		this(0);
	}
	
	public void update() {
		if (!this.isDone) {
			if (this.count > BaseMod.MAX_HAND_SIZE) {
				logger.info("count > MAX_HAND_SIZE. Check your code.");
				this.isDone = true;
				return;
			}

			if (AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
				AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
				this.isDone = true;
				return;
			}

			if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
				logger.info("Hand is full");
				this.isDone = true;
				return;
			}

			if (AbstractDungeon.player.drawPile.size() +
					AbstractDungeon.player.discardPile.size() == 0) {
				logger.info("No cards in draw or discard pile");

				this.isDone = true;
				return;
			}

			int balance = 0;

			for (AbstractCard c : AbstractDungeon.player.hand.group)
				balance += CthughaHelper.isBurnCard(c) ? 1 : -1;

			if (balance != 0) { // Need to draw more cards
				this.addToTop(new RedLotusWaterAction(this.count + 1));
				this.addToTop(new DrawCardAction(1));
			}

			this.isDone = true;
		}
	}
}
