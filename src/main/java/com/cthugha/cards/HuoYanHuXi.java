package com.cthugha.cards;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HuoYanHuXi extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(HuoYanHuXi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/119.png";
    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public HuoYanHuXi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 0;

        MultiCardPreview.add(this, ModHelper.getAttackBurn(), ModHelper.getAttackBurn());
        MultiCardPreview.multiCardPreview.get(this).get(1).upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    private void changeBurnAttackTypeCardsInGroup(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group) {
            if (ModHelper.IsBurn(c)) {
                if (cardGroup.type == CardGroup.CardGroupType.HAND)
                    c.superFlash();

                ModHelper.changeBurn(c);
            }
        }
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            for (CardGroup group : new CardGroup[] {
                    AbstractDungeon.player.hand,
                    AbstractDungeon.player.drawPile,
                    AbstractDungeon.player.discardPile,
                    AbstractDungeon.player.exhaustPile })
                changeBurnAttackTypeCardsInGroup(group);

            CardCrawlGame.music.fadeOutTempBGM();
            CardCrawlGame.music.playTempBgmInstantly("Cthugha-USAO.mp3");
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.retain = this.selfRetain = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
