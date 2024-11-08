package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FuZhuoShangHuan extends AbstractCthughaCard {

    public static final String ID = ModHelper.makeID(FuZhuoShangHuan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/135.png";

    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public FuZhuoShangHuan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 6;
        this.magicNumber = this.baseMagicNumber = 6;
//        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 2;
        this.tags.add(CustomTags.Burn_Card);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(2);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage)));
    }

    // 硬编码就硬编码吧，无所谓了
    public void triggerOnExhaust() {
        if (this.baseMagicNumber > 2) {
            FuZhuoShangHuan card = (FuZhuoShangHuan) this.makeStatEquivalentCopy();

            card.baseDamage -= 2;
            if (card.baseDamage < 0) {
                card.baseDamage = 0;
            }

            card.baseMagicNumber -= 2;
            if (card.baseMagicNumber < 0) {
                card.baseMagicNumber = 0;
            }

            this.addToBot(new MakeTempCardInHandAction(card));
        }
    }
}
