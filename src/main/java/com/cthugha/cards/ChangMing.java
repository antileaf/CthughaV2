package com.cthugha.cards;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChangMing extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(ChangMing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/136.png";

    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChangMing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 3;
        this.shunRan = this.baseShunRan = 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Do nothing
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new DrawCardAction(this.magicNumber, new AnonymousAction(() -> {
                DrawCardAction.drawnCards.forEach(c -> {
                    if (c.costForTurn >= 0)
                        c.costForTurn += 1;
                });
            })));
        }
    }
}
