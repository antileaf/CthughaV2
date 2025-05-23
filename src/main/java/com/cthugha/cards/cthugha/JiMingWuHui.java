package com.cthugha.cards.cthugha;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JiMingWuHui extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(JiMingWuHui.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/155.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public JiMingWuHui() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 5;
        this.shunRan = this.baseShunRan = 2;

        this.rawDescription = String.format(cardStrings.DESCRIPTION, "");
        this.initializeDescription();
    }

    @Override
    public void applyPowersToBlock() {
        this.baseBlock = this.magicNumber + (int) AbstractDungeon.player.exhaustPile.group.stream()
                .filter(CthughaHelper::isBurnCard)
                .count();

        super.applyPowersToBlock();

        this.rawDescription = String.format(cardStrings.DESCRIPTION,
                CthughaHelper.isInBattle() && this.block != -1 ? cardStrings.EXTENDED_DESCRIPTION[0] : "");
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = String.format(cardStrings.DESCRIPTION, "");
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
    }

    @Override
    public void onFlare(int level) {
        if (level >= this.shunRan) {
            this.costForTurn = 0;
//            this.addToBot(new AnonymousAction(this::applyPowers));
            this.addToBot(new NewQueueCardAction(this, true, false, true));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeShunRan(-1);
            this.initializeDescription();
        }
    }
}
