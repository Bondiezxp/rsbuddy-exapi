package com.rsbuddy.script.methods;

import com.rsbuddy.script.action.Action;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Condition;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Player;
import com.rsbuddy.script.wrappers.Widget;

import java.util.LinkedList;

import org.rsbuddy.tabs.Inventory;

/**
 * @author Ramus
 */
public class ExTrading {

	public static class AcceptScreen {

		public static final int WIDGET_TRADE = 334;
		public static final int WIDGET_TRADE_ACCEPTED = 34;
		public static final int WIDGET_TRADE_BUTTON_ACCEPT = 36;
		public static final int WIDGET_TRADE_BUTTON_CLOSE = 6;
		public static final int WIDGET_TRADE_BUTTON_DECLINE = 37;
		public static final int WIDGET_TRADE_TRADER = 54;

		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText("Waiting for other")) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_ACCEPT).interact("Accept");
		}

		public static boolean close() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_CLOSE).interact("Close");
		}

		public static boolean decline() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_DECLINE).interact("Decline");
		}

		public static Player getTrader() {
			if (!isOpen()) {
				return null;
			}
			return Players.getNearest(getWidget().getComponent(WIDGET_TRADE_TRADER).getText());
		}

		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		public static boolean isOpen() {
			final Widget w = getWidget();
			return w != null && w.isValid() && getWidget().getComponent(0).isVisible();
		}
	}

	public static class OfferScreen {

		public static final int WIDGET_TRADE = 335;
		public static final int WIDGET_TRADE_ACCEPTED = 37;
		public static final int WIDGET_TRADE_BUTTON_ACCEPT = 16;
		public static final int WIDGET_TRADE_BUTTON_CLOSE = 12;
		public static final int WIDGET_TRADE_BUTTON_DECLINE = 18;
		public static final int WIDGET_TRADE_MY_ITEMS = 30;
		public static final int WIDGET_TRADE_FREE_SLOTS = 21;
		public static final int WIDGET_TRADE_TRADER_ITEMS = 33;
		public static final int WIDGET_TRADE_TRADER = 22;

		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText("Waiting for other")) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_ACCEPT).interact("Accept");
		}

		public static boolean close() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_CLOSE).interact("Close");
		}

		public static boolean decline() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_DECLINE).interact("Decline");
		}

		public static int getFreeSlots() {
			if (!isOpen()) {
				return 0;
			}
			final String text = getWidget().getComponent(WIDGET_TRADE_FREE_SLOTS).getText().substring(4, 6).trim();
			try {
				return Integer.parseInt(text);
			} catch (final Exception e) {
			}
			return 0;
		}

		public static Item[] getMyItems() {
			if (!isOpen()) {
				return null;
			}
			final Component itemsComp = getWidget().getComponent(WIDGET_TRADE_MY_ITEMS);
			final LinkedList<Item> items = new LinkedList<Item>();
			for (int i = 0; i < 28; i += 1) {
				final Component item = itemsComp.getComponent(i);
				if (item == null || item.getItemId() == -1 || item.getItemStackSize() == 0) {
					continue;
				}
				items.add(new Item(item));
			}
			return items.toArray(new Item[items.size()]);
		}

		public static Player getTrader() {
			if (!isOpen()) {
				return null;
			}
			return Players.getNearest(getWidget().getComponent(WIDGET_TRADE_TRADER).getText());
		}

		public static Item[] getTraderItems() {
			if (!isOpen()) {
				return null;
			}
			final Component itemsComp = getWidget().getComponent(WIDGET_TRADE_TRADER_ITEMS);
			final LinkedList<Item> items = new LinkedList<Item>();
			for (int i = 0; i < 28; i += 1) {
				final Component item = itemsComp.getComponent(i);
				if (item == null || item.getItemId() == -1 || item.getItemStackSize() == 0) {
					continue;
				}
				items.add(new Item(item));
			}
			return items.toArray(new Item[items.size()]);
		}

		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		public static boolean isOpen() {
			final Widget w = getWidget();
			return w != null && w.isValid() && getWidget().getComponent(0).isVisible();
		}

		public static boolean offer(final int id, final int count) {
			if (!isOpen()) {
				return false;
			}
			final Item item = Inventory.getItem(id);
			final int startCount = Inventory.getCount(true, id);
			final int endCount = count == 0 ? 0 : startCount - count;
			if (startCount == 0) {
				return false;
			}
			if (count == 0) {
				if (!item.interact(startCount > 1 ? "Offer-All" : "Offer")) {
					return false;
				}
			} else if (count == 1) {
				if (!item.interact("Offer")) {
					return false;
				}
			} else {
				if (!item.interact("Offer-" + count)) {
					if (item.interact("Offer-X")) {
						Task.sleep(1000, 1300);
						Keyboard.sendText(String.valueOf(count), true);
					} else {
						return false;
					}
				}
			}
			Action.sleep(2500, new Condition() {

				@Override
				public boolean isValid() {
					return Inventory.getCount(id) == endCount;
				}
			});
			return Inventory.getCount(id) == endCount;
		}
	}

	public enum TradeScreen {

		CLOSED,
		OFFERS,
		FINAL;
	}

	public static int WIDGET_CHAT = 137;
	public static int WIDGET_CHAT_COMPONENT = 58;

	public static boolean acceptTradeFrom(final String name) {
		if (OfferScreen.isOpen()) {
			return OfferScreen.getTrader().getName().equalsIgnoreCase(name);
		} else if (AcceptScreen.isOpen()) {
			return AcceptScreen.getTrader().getName().equalsIgnoreCase(name);
		}
		final Component talkComponent = Widgets.getComponent(WIDGET_CHAT, WIDGET_CHAT_COMPONENT);
		if (talkComponent == null || !talkComponent.isValid() || !talkComponent.isVisible()) {
			return false;
		}
		for (int i = 0; i < talkComponent.getComponentCount(); i += 1) {
			final Component msg = talkComponent.getComponent(i);
			if (msg == null || !msg.isValid() || !msg.isVisible()) {
				continue;
			}
			return msg.interact("Accept trade");
		}
		return false;
	}

	public static TradeScreen getCurrentScreen() {
		if (OfferScreen.isOpen()) {
			return TradeScreen.OFFERS;
		} else if (AcceptScreen.isOpen()) {
			return TradeScreen.FINAL;
		}
		return TradeScreen.CLOSED;
	}

	public static boolean tradeWith(final String name) {
		if (OfferScreen.isOpen()) {
			return OfferScreen.getTrader().getName().equalsIgnoreCase(name);
		} else if (AcceptScreen.isOpen()) {
			return AcceptScreen.getTrader().getName().equalsIgnoreCase(name);
		}
		final Player t = Players.getNearest(name);
		if (t == null || t.interact("Trade with " + name)) {
			return false;
		}
		for (int i = 0; !Game.getLastMessage().toLowerCase().contains("sending trade offer...") && i < 10; i += 1) {
			Task.sleep(250, 300);
		}
		return Game.getLastMessage().toLowerCase().contains("sending trade offer...");
	}
}