package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.helpers.ModHelper;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.patch.SpiritField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class XuanCaoQiYanPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.MakePath(XuanCaoQiYanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RetainCardsAction");

    public XuanCaoQiYanPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = new Texture("cthughaResources/img/power/214_32.png");
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer && !AbstractDungeon.player.hand.isEmpty()) {

            // addToBot((AbstractGameAction) new RetainBurnAction(this.owner, this.amount));

            this.addToBot(
                    new SelectCardsInHandAction(this.amount, uiStrings.TEXT[0], true, true,
                            card -> (ModHelper.IsBurn(card)),
                            abstractCards -> {
                                for (AbstractCard card : abstractCards) {
                                    if (!card.isEthereal) {
                                        card.retain = true;

                                        this.addToBot(new AbstractGameAction() {
                                            public void update() {
                                                card.dontTriggerOnUseCard = true;
                                                AbstractSpirit spirit = SpiritField.spirit.get(card);
                                                if (spirit != null) {
                                                    spirit.onUse();
                                                }
                                                card.use(null, null);
                                                card.dontTriggerOnUseCard = false;

                                                this.isDone = true;
                                            }
                                        });
                                    }
                                }

                                // 让未保留的灼伤正常触发
                                this.addToBot(new AbstractGameAction() {
                                    public void update() {
                                        for (AbstractCard card : AbstractDungeon.player.hand.group) {
                                            if (ModHelper.IsBurn(card) && !card.retain) {
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
