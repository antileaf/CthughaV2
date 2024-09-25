package com.cthugha.cards.deprecated;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
// import shadowverse.characters.AbstractShadowversePlayer;

@Deprecated
public abstract class DEPRECATEDAbstractRightClickCard extends CustomCard {
  private boolean RclickStart;
  
  private boolean Rclick;
  
  private boolean dCheck;
  
  private long lastClick;
  
  private static final int DURATION = 300;
  
  public DEPRECATEDAbstractRightClickCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
    super(id, name, img, cost, rawDescription, type, color, rarity, target);
    this.Rclick = false;
    this.RclickStart = false;
    this.dCheck = false;
    // this.tags.add(AbstractShadowversePlayer.Enums.FUSION);
  }
  
  protected abstract void onRightClick();
  
  protected void onDoubleRightClick() {}
  
  private long deltaTime() {
    return System.currentTimeMillis() - this.lastClick;
  }
  
  private boolean doubleClick() {
    boolean b = (deltaTime() < 300L);
    this.lastClick = System.currentTimeMillis();
    return b;
  }
  
  public void update() {
    super.update();
    if (AbstractDungeon.player == null)
      return; 
    if (this.RclickStart && InputHelper.justReleasedClickRight) {
      if (isHoveredInHand(1.0F))
        this.Rclick = true; 
      this.RclickStart = false;
    } 
    if (AbstractDungeon.player.hand.group.contains(this) && this.hb != null && this.hb.hovered && InputHelper.justClickedRight)
      this.RclickStart = true; 
    if (deltaTime() >= 300L && this.dCheck) {
      this.dCheck = false;
      onRightClick();
    } 
    if (this.Rclick) {
      this.Rclick = false;
      this.dCheck = true;
      if (doubleClick())
        onDoubleRightClick(); 
    } 
  }
}
