package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Player;
import com.rsbuddy.script.wrappers.Widget;

import org.rsbuddy.tabs.Inventory;

public class ExTrading {

	public static class AcceptBox {

		public static class getTraderName {

			public static String name() {
				if (!isOpen()) {
					return null;
				}
				return getWidget().getComponent(WIDGET_TRADE_TRADER).getText();
			}
		}

		public static final int WIDGET_TRADE = 334;
		public static final int WIDGET_TRADE_BUTTON_CLOSE = 6;
		public static final int WIDGET_TRADE_BUTTON_ACCEPT = 36;
		public static final int WIDGET_TRADE_BUTTON_DECLINE = 37;
		public static final int WIDGET_TRADE_TRADER = 54;
		public static final int WIDGET_TRADE_ACCEPTED = 34;

		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText(
					"Waiting for other")) {
				Task.sleep(100, 200);
				return true;
			} else {
				final Component button = getWidget().getComponent(
						WIDGET_TRADE_BUTTON_ACCEPT);
				Mouse.moveRandomly(100, 150);
				return button != null && button.click();
			}
		}

		public static boolean close() {
			if (!isOpen()) {
				return false;
			}
			final Component button = getWidget().getComponent(
					WIDGET_TRADE_BUTTON_CLOSE);
			Mouse.moveRandomly(100, 150);
			return button != null && button.click();
		}

		public static boolean decline() {
			if (isOpen()) {
				return false;
			}
			final Component button = getWidget().getComponent(
					WIDGET_TRADE_BUTTON_DECLINE);
			Mouse.moveRandomly(100, 150);
			return button != null && button.click();
		}

		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		public static boolean isOpen() {
			return isValid() && getWidget().getComponent(1).isVisible();
		}

		public static boolean isValid() {
			final Widget w = getWidget();
			return w != null && w.isValid();
		}

	}

	public static class OfferBox {

		public static class getTraderName {

			public static boolean isValid(final String playerName) {
				return playerName.equals(name());
			}

			public static String name() {
				if (!isOpen()) {
					return null;
				}
				return getWidget().getComponent(WIDGET_TRADE_TRADER).getText()
						.replace("Trading With: ", "");
			}
		}

		public static final int WIDGET_TRADE = 335;
		public static final int WIDGET_TRADE_BUTTON_CLOSE = 12;
		public static final int WIDGET_TRADE_BUTTON_ACCEPT = 16;
		public static final int WIDGET_TRADE_BUTTON_DECLINE = 18;
		public static final int WIDGET_TRADE_TRADER = 15;
		public static final int WIDGET_TRADE_ACCEPTED = 37;
		public static final int[] WIDGET_TRADE_SLOTS = { 30, 0, 1 };

		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText(
					"Waiting for other")) {
				Task.sleep(100, 200);
				return true;
			} else {
				final Component button = getWidget().getComponent(
						WIDGET_TRADE_BUTTON_ACCEPT);
				Mouse.moveRandomly(100, 150);
				return button != null && button.click();
			}
		}

		public static boolean close() {
			if (!isOpen()) {
				return false;
			}
			final Component button = getWidget().getComponent(
					WIDGET_TRADE_BUTTON_CLOSE);
			Mouse.moveRandomly(100, 150);
			return button != null && button.click();
		}

		public static boolean decline() {
			if (!isOpen()) {
				return false;
			}
			final Component button = getWidget().getComponent(
					WIDGET_TRADE_BUTTON_DECLINE);
			Mouse.moveRandomly(100, 150);
			return button != null && button.click();
		}

		public static boolean getItemsTraded() {
			int i = 1;
			while (i < 3) {
				if (getWidget().getComponent(WIDGET_TRADE_SLOTS[0])
						.getComponent(WIDGET_TRADE_SLOTS[i]).getItemId() > 0) {
					items[i] = getWidget().getComponent(WIDGET_TRADE_SLOTS[0])
							.getComponent(WIDGET_TRADE_SLOTS[i]).getItemId();
				}
				i += 1;
			}
			return true;
		}

		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		public static boolean isOpen() {
			return isValid() && getWidget().getComponent(1).isVisible();
		}

		public static boolean isValid() {
			final Widget w = getWidget();
			return w != null && w.isValid();
		}

		public static boolean offer(final int item, final int count) {
			final Item inv = Inventory.getItem(item);
			final int i = Inventory.getCount(true, item);
			if (count == 0 && i > 0) {
				inv.interact("Offer-All");
				Mouse.moveRandomly(100, 150);
				Task.sleep(800, 1000);
				return true;
			} else if (i >= count) {
				inv.interact("Offer-X");
			}
			return false;
		}

	}

	public enum TransactionScreen {
		CLOSED,
		FIRST,
		SECOND
	}

	public static int[] items = { 0, 0, 0 };

	public static TransactionScreen getTransactionScreen() {
		if (OfferBox.isOpen()) {
			return TransactionScreen.FIRST;
		} else if (AcceptBox.isOpen()) {
			return TransactionScreen.SECOND;
		}
		return TransactionScreen.CLOSED;
	}

	public static void tradeWith(final String name) {
		final Player t = Players.getNearest(name);
		if (t != null && t.interact("Trade with " + name)) {
			Mouse.moveRandomly(100, 150);
			Task.sleep(3000, 3500);
			return;
		}
		return;
	}
}