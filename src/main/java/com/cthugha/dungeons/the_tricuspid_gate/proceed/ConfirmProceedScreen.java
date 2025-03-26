package com.cthugha.dungeons.the_tricuspid_gate.proceed;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmUseCardPopup;
import com.cthugha.enums.CurrentScreenEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfirmProceedScreen extends CustomScreen {
	private static final Logger logger = LogManager.getLogger(ConfirmProceedScreen.class.getName());

	private ConfirmProceedPopup popup;

	public ConfirmProceedScreen() {
		super();

		this.popup = null;
	}

	@Override
	public AbstractDungeon.CurrentScreen curScreen() {
		return CurrentScreenEnum.PROCEED_CONFIRM_SCREEN;
	}

	public void open(boolean goToFifthAct) {
		logger.info("Open, goToFifthAct: {}", goToFifthAct);

		this.popup = new ConfirmProceedPopup(goToFifthAct);

		reopen();
	}

	@Override
	public void reopen() {
		AbstractDungeon.screen = this.curScreen();
		AbstractDungeon.isScreenUp = true;
		if (this.popup != null) {
			this.popup.show();
		}

		logger.info("Opened ConfirmUseCardScreen");
	}

	@Override
	public void close() {
		if (this.popup != null) {
			this.popup.hide();
		}
		AbstractDungeon.isScreenUp = false;
		AbstractDungeon.overlayMenu.cancelButton.hide();
	}

	@Override
	public void update() {
		this.popup.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		if (this.popup != null) {
			this.popup.render(sb);
		}
	}

	@Override
	public void openingSettings() {
		AbstractDungeon.previousScreen = this.curScreen();
		if (this.popup != null) {
			this.popup.hide();
		}
	}
}
