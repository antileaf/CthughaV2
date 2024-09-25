package com.cthugha.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;

public class DianJiangChun_ZiYangHanShi extends CustomCard {

    public static final String ID = ModHelper.MakePath(DianJiangChun_ZiYangHanShi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/116.png";
    private static final int COST = -1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public DianJiangChun_ZiYangHanShi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;

        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1)
            effect = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        int actualEffect = effect;
        if (actualEffect > 0) {

            DianJiangChun_ZiYangHanShi self = this;

            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (p.hand.size() == 0) {
                        this.addToBot(new GainEnergyAction(actualEffect));
                        this.addToBot(new DrawCardAction(actualEffect));
                        this.isDone = true;
                        return;
                    }

                    this.addToBot(new SelectCardsInHandAction(actualEffect, "丢弃", false, false,
                            card -> true,
                            abstractCards -> {
                                int count = actualEffect;
                                int burnCount = 0;
                                for (AbstractCard card : abstractCards) {
                                    this.addToBot(new DiscardSpecificCardAction(card));
                                    if (ModHelper.IsBurnCard(card)) {
                                        burnCount++;
                                    }
                                }

                                if (burnCount > 0) {
                                    int n = (int) burnCount / 2;
                                    count = count + self.magicNumber * n;
                                }
                                this.addToBot(new GainEnergyAction(count));
                                this.addToBot(new DrawCardAction(count));
                            }));
                    this.isDone = true;
                }

            });

        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }
}
