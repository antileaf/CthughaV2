package com.cthugha.object;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.patches.SpiritField;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractSpirit {
  public static final AbstractSpirit SPIRIT_NONE = null;
  
  protected String id;
  
  protected String name;
  
  private AbstractCard attachedCard;

  public int amount;

  public int getAmount() {
    return this.amount;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public AbstractCard getAttachedCard() {
    return this.attachedCard;
  }
  
  public void attachToCard(AbstractCard card) {
    SpiritField.spirit.set(card, this);
    this.attachedCard = card;
  }
  
  public abstract Texture getTexture();

  public abstract void setTexture();
  
  public abstract void onDraw();
  
  public abstract void onUse();
  
  public abstract AbstractSpirit makeCopy();
  
  public String toString() {
    return "AbstractSpirit{id='" + this.id + '\'' + ", name='" + this.name + '\'' + '}';
  }
}
