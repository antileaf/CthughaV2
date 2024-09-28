package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JiMingWuHui extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(JiMingWuHui.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/155.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public JiMingWuHui() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 5;
        this.shunRan = this.baseShunRan = 2;
    }

    @Override
    public void applyPowers() {
        boolean hasNotUpdatedDesc = this.block == -1;
        this.baseBlock = this.magicNumber + (int) AbstractDungeon.player.exhaustPile.group.stream()
                .filter(ModHelper::IsBurnCard)
                .count();

        super.applyPowers();

        if (hasNotUpdatedDesc && this.block != -1)
            this.initializeDescription();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = String.format(cardStrings.DESCRIPTION,
                ModHelper.isInBattle() && this.block != -1 ? cardStrings.EXTENDED_DESCRIPTION[0] : "");

        super.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.costForTurn = 0;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, false));
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
