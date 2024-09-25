package com.cthugha.cards;

import java.util.HashMap;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;

public class DianJiangChun_QingJiXiShang extends CustomCard {

    public static final String ID = ModHelper.MakePath(DianJiangChun_QingJiXiShang.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/121.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public DianJiangChun_QingJiXiShang() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
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
        this.addToBot(new DrawCardAction(this.magicNumber, new AbstractGameAction() {
            public void update() {
                HashMap<AbstractCard.CardType, Integer> cardTypeMap = new HashMap<AbstractCard.CardType, Integer>();
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    cardTypeMap.put(c.type, 1);
                }
                int base = 1;
                if (cardTypeMap.size() >= 4) {
                    base *= 2;
                }
                if (cardTypeMap.size() >= 5) {
                    base *= 2;
                }
                if (cardTypeMap.size() >= 1) {
                    this.addToBot(new GainBlockAction(p, 3 * base));
                }
                if (cardTypeMap.size() >= 2) {
                    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1 * base)));
                }
                if (cardTypeMap.size() >= 3) {
                    this.addToBot(new GainEnergyAction(1 * base));
                }

                this.isDone = true;
                return;
            };
        }));
    }
}
