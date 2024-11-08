package com.cthugha.cards;

import basemod.BaseMod;
import com.cthugha.Cthugha_Core;
import com.cthugha.actions.ZhiLiaoAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class XianYi extends AbstractCthughaCard {

    public static final String ID = ModHelper.makeID(XianYi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/128.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public XianYi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.shunRan = this.baseShunRan = 8;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AnonymousAction(() -> {
                int toDraw = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
                if (toDraw > 0)
                    this.addToTop(new DrawCardAction(AbstractDungeon.player, toDraw));
        }));

        this.addToBot(new ZhiLiaoAction(this, new ApplyPowerAction(p, p,
                new IntangiblePlayerPower(p, this.magicNumber))));
    }

    @Override
    public void onShunRan(int level) {
        if (this.shunRan == -1) {
            Cthugha_Core.logger.info("XianYi: shunRan is -1");
            return;
        }

        if (level >= this.shunRan) {
            this.addToBot(new AnonymousAction(() -> {
                int toDraw = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
                if (toDraw > 0)
                    this.addToTop(new DrawCardAction(AbstractDungeon.player, toDraw));

            }));

            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new IntangiblePlayerPower(AbstractDungeon.player, this.magicNumber)));
        }
    }

}
