package com.cthugha.cards.cthugha;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ZhaoZhuoShangTianQue extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(ZhaoZhuoShangTianQue.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/炤灼伤天阙.png";
    private static final int COST = 3;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public ZhaoZhuoShangTianQue() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.block = this.baseBlock = 20;
        this.tags.add(CustomTags.Burn_Card);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
            this.initializeDescription();
        }
    }

    @Override
    public boolean onFlareSelectedBy(AbstractCthughaCard card) {
        this.addToBot(new NewQueueCardAction(this, true, false, true));

        this.addToBot(new AnonymousAction(() -> {
            this.addToTop(new ExhaustSpecificCardAction(card,
                    AbstractDungeon.player.hand.contains(card) ? AbstractDungeon.player.hand :
                            AbstractDungeon.player.drawPile.contains(card) ? AbstractDungeon.player.drawPile :
                                    AbstractDungeon.player.discardPile));
        }));

        return true;
    }
}
