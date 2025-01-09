package com.cthugha.cards.cthugha;

import com.cthugha.actions.common.DrawSpecificCardAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChunHuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(ChunHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/148.png";

    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChunHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 2;
        this.magicNumber = this.baseMagicNumber = 2;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.shining())
            this.addToBot(new DrawCardAction(p, this.secondaryMagicNumber));
    }

    public void triggerWhenDrawn() {
        this.addToBot(new DrawSpecificCardAction(CthughaHelper::isBurnCard, this.secondaryMagicNumber));
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new DrawCardAction(AbstractDungeon.player, this.magicNumber));
        }
    }

}
