package com.cthugha.dungeons.the_tricuspid_gate.confirm;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.enums.CurrentScreenEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfirmUseCardScreen extends CustomScreen {
	private static final Logger logger = LogManager.getLogger(ConfirmUseCardScreen.class.getName());

	private final ConfirmUseCardPopup confirmPopup;

	public ConfirmUseCardScreen() {
		super();

		this.confirmPopup = new ConfirmUseCardPopup();
	}

	@Override
	public AbstractDungeon.CurrentScreen curScreen() {
		return CurrentScreenEnum.USE_CARD_CONFIRM_SCREEN;
	}

	public void open() {
		logger.info("Open");

		reopen();
	}

	@Override
	public void reopen() {
		AbstractDungeon.screen = this.curScreen();
		AbstractDungeon.isScreenUp = true;
		this.confirmPopup.show();

		logger.info("Opened ConfirmUseCardScreen");
	}

	@Override
	public void close() {
		this.confirmPopup.hide();
		AbstractDungeon.isScreenUp = false;
		AbstractDungeon.overlayMenu.cancelButton.hide();
	}

	@Override
	public void update() {
		this.confirmPopup.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		this.confirmPopup.render(sb);
	}

	@Override
	public boolean allowOpenDeck() {
		return true;
	}

	@Override
	public void openingDeck() {
		AbstractDungeon.previousScreen = this.curScreen();
		this.confirmPopup.hide();
	}

	@Override
	public boolean allowOpenMap() {
		return true;
	}

	@Override
	public void openingMap() {
		AbstractDungeon.previousScreen = this.curScreen();
		this.confirmPopup.hide();
	}

	@Override
	public void openingSettings() {
		AbstractDungeon.previousScreen = this.curScreen();
		this.confirmPopup.hide();
	}
}
