package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class TunYunFuHai extends CustomCard {

    public static final String ID = ModHelper.MakePath(TunYunFuHai.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/152.png";
    private static final int COST = 5;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private int rawCost = COST;

    public TunYunFuHai() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 5;
        this.block = this.baseBlock = 15;
        this.magicNumber = this.baseMagicNumber = 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(1);
            this.upgradeBlock(7);
            rawCost = this.cost;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage)));
        }
        this.addToBot(new GainBlockAction(p, this.block));
    }

    public void applyPowers() {
        super.applyPowers();
        this.calcAndSetCost(this);
    }

    private void calcAndSetCost(TunYunFuHai _inst) {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (ModHelper.IsBurn(card)) {
                count++;
            }
        }
        int curCost = _inst.rawCost - count;
        if (curCost < 0) {
            curCost = 0;
        }
        _inst.upgradeBaseCost(curCost);
        _inst.magicNumber = count;
    }

}
