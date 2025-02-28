package com.cthugha.power;

import com.cthugha.actions.utils.AnonymousAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.patches.SpiritField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class XuanCaoXiaoYanPower extends AbstractPower {
	public static final String POWER_ID = CthughaHelper.makeID(XuanCaoXiaoYanPower.class.getSimpleName());
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RetainCardsAction");

	public XuanCaoXiaoYanPower(AbstractCreature owner, int amount) {
		this.ID = POWER_ID;
		this.name = powerStrings.NAME;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		CthughaHelper.loadPowerRegion(this, "喧嘈嚣焰");
//        this.img = new Texture("cthughaResources/img/power/214_32.png");
	}

	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
	}

	public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
		if (isPlayer && !AbstractDungeon.player.hand.isEmpty()) {

			// addToBot((AbstractGameAction) new RetainBurnAction(this.owner, this.amount));

			this.addToBot(
					new SelectCardsInHandAction(this.amount, uiStrings.TEXT[0], true, true,
							CthughaHelper::isBurn,
							chosen -> {
								for (AbstractCard card : chosen) {
									if (!card.isEthereal) {
										card.retain = true;

										this.addToBot(new AnonymousAction(() -> {
											card.dontTriggerOnUseCard = true;
											AbstractSpirit spirit = SpiritField.spirit.get(card);
											if (spirit != null) {
												spirit.onUse();
											}
											card.use(AbstractDungeon.player, null);
											card.dontTriggerOnUseCard = false;
										}));
									}
								}

//								this.addToBot(new WaitAction(0.25F));

								// 让未保留的灼伤正常触发
                                this.addToBot(new AbstractGameAction() {
                                    public void update() {
                                        for (AbstractCard card : AbstractDungeon.player.hand.group) {
                                            if (CthughaHelper.isBurn(card) && !card.retain) {
                                                AbstractDungeon.actionManager.cardQueue
                                                        .add(new CardQueueItem(card, true));
                                            }
                                        }

                                        this.isDone = true;
                                    }
                                });

							}));
		}
	}

}
