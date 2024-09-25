package com.cthugha.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.cthugha.relics.LieSiTaShuJian;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

@Deprecated
public abstract class AbstractShunRanCard extends CustomCard implements RightClickableCard {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");

    // 卡牌能否瞬燃
    protected boolean canShunRan = true;

    public AbstractShunRanCard(String arg0, String arg1, String arg2, int arg3, String arg4, CardType arg5,
            CardColor arg6, CardRarity arg7, CardTarget arg8) {
        super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        this.tags.add(CustomTags.Shun_Ran);
    }

    protected abstract void doShunRan(int size);

    // 每回合开始移除瞬燃标记
    public void atTurnStart() {
        this.tags.remove(CustomTags.Shun_Ran_Triggered);
    }

    @Override
    public void onRightClick() {
        // 能否进行瞬燃
        if (!canShunRan) {
            return;
        }

        // 已经触发瞬燃的情况
        if (this.hasTag(CustomTags.Shun_Ran_Triggered)) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX,
                    AbstractDungeon.player.dialogY, 3.0F, "使用过瞬燃了", true));
            return;
        }

        // 打入已触发瞬燃的标签
        if (!this.hasTag(CustomTags.Shun_Ran_Triggered) && AbstractDungeon.player != null) {
            this.tags.add(CustomTags.Shun_Ran_Triggered);
        }

        // 没有灼伤牌时，触发瞬燃0
        boolean HandsHaveBurnCard = false; // 手牌是否有灼伤牌
        for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
            AbstractCard card = AbstractDungeon.player.hand.group.get(i);
            if (ModHelper.IsBurnCard(card)) {
                HandsHaveBurnCard = true;
                break;
            }
        }
        if (!HandsHaveBurnCard) {
            this.calculateCardDamage(null);
            this.doShunRan(0);
            return;
        }

        // 选择瞬燃
        this.addToBot(
                new SelectCardsInHandAction(10, uiStrings.TEXT[0], true, true,
                        card -> ModHelper.IsBurnCard(card),
                        abstractCards -> {

                            int count = abstractCards.size();

                            // 存在卡牌 复灼伤还 的情况
                            int fuZhuoShangHuanCount = 0;
                            for (AbstractCard card : abstractCards) {
                                if (card.cardID == FuZhuoShangHuan.ID) {
                                    if (card.magicNumber > fuZhuoShangHuanCount) {
                                        fuZhuoShangHuanCount = card.magicNumber;
                                    }
                                }
                            }
                            if (fuZhuoShangHuanCount != 0) {
                                count = fuZhuoShangHuanCount;
                            }

                            // 存在卡牌 炤灼伤天阙 的情况
                            for (AbstractCard card : abstractCards) {
                                if (card.cardID == ZhaoZhuoShangTianQue.ID) {
                                    // card.use(AbstractDungeon.player, null);
                                    card.costForTurn = 0;
                                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, true));
                                    this.addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
                                    return;
                                }
                            }

                            // 存在遗物 书简 的情况
                            if (AbstractDungeon.player.hasRelic(LieSiTaShuJian.ID)) {
                                if (count > 7) {
                                    count = 7;
                                }
                            }

                            for (AbstractCard card : abstractCards) {
                                this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                            }

                            // this.calculateCardDamage(null);
                            this.doShunRan(count);
                        }));
    }

}
