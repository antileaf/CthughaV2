package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class TengYanFeiMang extends CustomCard {

    public static final String ID = ModHelper.makeID(TengYanFeiMang.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/109.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TengYanFeiMang() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 1;
        this.magicNumber = this.baseMagicNumber = -999; // Specially handled, cause -1 is also valid
    }

    @Override
    public void applyPowers() {
        boolean hasNotUpdatedDesc = this.magicNumber == -999;
        this.magicNumber = this.baseMagicNumber = Math.max((int) AbstractDungeon.player.drawPile.group.stream()
                .filter(ModHelper::isBurnCard)
                .count() - 1, 0);

        super.applyPowers();
        if (hasNotUpdatedDesc && this.magicNumber != -999)
            this.initializeDescription();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = String.format(cardStrings.DESCRIPTION,
                ModHelper.isInBattle() && this.magicNumber != -999 ? cardStrings.EXTENDED_DESCRIPTION[0] : "");

        super.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = (int) AbstractDungeon.player.drawPile.group.stream()
                .filter(ModHelper::isBurnCard)
                .count();

        for (int i = 0; i < count - 1; i++) {
            this.addToBot(new DrawCardAction(p, 1));
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.initializeDescription();
        }
    }
}
