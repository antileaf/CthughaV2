package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.colorless.ZhuoShangDaJi;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JingZhuangZhuoShangJi extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(JingZhuangZhuoShangJi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/142.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public JingZhuangZhuoShangJi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 2;
        this.tags.add(CustomTags.Burn_Card);
        this.exhaust = true;
        this.cardsToPreview = (AbstractCard)new ZhuoShangDaJi();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MakeTempCardInHandAction(new ZhuoShangDaJi(), 2));
    }

}
