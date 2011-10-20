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

		/**
		 * Accepts the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was accepted.
		 */
		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText("Waiting for other")) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_ACCEPT).interact("Accept");
		}

		/**
		 * Closes the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was closes; <tt>false</tt>
		 *         otherwise.
		 */
		public static boolean close() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_CLOSE).interact("Close");
		}

		/**
		 * Declines the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was declined; <tt>false</tt>
		 *         otherwise.
		 */
		public static boolean decline() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_DECLINE).interact("Decline");
		}

		/**
		 * Gets the <tt>Player</tt> that we are trading with.
		 * 
		 * @return The <tt>Player</tt> that we are trading with.
		 */
		public static Player getTrader() {
			if (!isOpen()) {
				return null;
			}
			return Players.getNearest(getWidget().getComponent(WIDGET_TRADE_TRADER).getText());
		}

		/**
		 * Gets the widget of the trade screen.
		 * 
		 * @return The widget of the trade screen.
		 */
		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		/**
		 * Checks whether the offer window is open.
		 * 
		 * @return <tt>true</tt> if open; <tt>false</tt> otherwise.
		 */
		public static boolean isOpen() {
			return isValid() && getWidget().getComponent(1).isVisible();
		}

		/**
		 * Checks whether the widget is valid.
		 * 
		 * @return <tt>true</tt> if valid; <tt>false</tt> otherwise.
		 */
		public static boolean isValid() {
			final Widget w = getWidget();
			return w != null && w.isValid();
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

		/**
		 * Accepts the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was accepted.
		 */
		public static boolean accept() {
			if (!isOpen()) {
				return false;
			}
			if (getWidget().getComponent(WIDGET_TRADE_ACCEPTED).containsText("Waiting for other")) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_ACCEPT).interact("Accept");
		}

		/**
		 * Closes the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was closes; <tt>false</tt>
		 *         otherwise.
		 */
		public static boolean close() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_CLOSE).interact("Close");
		}

		/**
		 * Declines the current trade.
		 * 
		 * @return <tt>true</tt> if the trade was declined; <tt>false</tt>
		 *         otherwise.
		 */
		public static boolean decline() {
			if (!isOpen()) {
				return true;
			}
			return getWidget().getComponent(WIDGET_TRADE_BUTTON_DECLINE).interact("Decline");
		}

		/**
		 * Gets the number of free slots the other player has.
		 * 
		 * @return The number of free slots.
		 */
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

		/**
		 * Gets the items that are currently being offered.
		 * 
		 * @return The items that are currently being offered.
		 */
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

		/**
		 * Gets the <tt>Player</tt> that we are trading with.
		 * 
		 * @return The <tt>Player</tt> that we are trading with.
		 */
		public static Player getTrader() {
			if (!isOpen()) {
				return null;
			}
			return Players.getNearest(getWidget().getComponent(WIDGET_TRADE_TRADER).getText());
		}

		/**
		 * Gets the items offered by the other player.
		 * 
		 * @return The items offered by the other player.
		 */
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

		/**
		 * Gets the widget of the trade screen.
		 * 
		 * @return The widget of the trade screen.
		 */
		public static Widget getWidget() {
			return Widgets.get(WIDGET_TRADE);
		}

		/**
		 * Checks whether the offer window is open.
		 * 
		 * @return <tt>true</tt> if open; <tt>false</tt> otherwise.
		 */
		public static boolean isOpen() {
			return isValid() && getWidget().getComponent(1).isVisible();
		}

		/**
		 * Checks whether the widget is valid.
		 * 
		 * @return <tt>true</tt> if valid; <tt>false</tt> otherwise.
		 */
		public static boolean isValid() {
			final Widget w = getWidget();
			return w != null && w.isValid();
		}

		/**
		 * Offers the item with the specified id for trade.
		 * 
		 * @param id
		 *            The id of the item.
		 * @param count
		 *            The amount of the item to offer.
		 * @return <tt>true</tt> if the item was offered successfuly;
		 *         <tt>false</tt> otherwise.
		 */
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

	public static enum TransactionScreen {
		CLOSED,
		FIRST,
		SECOND;
	}

	public static int WIDGET_CHAT = 137;
	public static int WIDGET_CHAT_COMPONENT = 58;

	/**
	 * Accepts a trade from the <tt>Player</tt> with the specified name.
	 * 
	 * @param name
	 *            The name of the <tt>Player</tt>.
	 * @return <tt>true</tt> if the trade was accepted; <tt>false</tt>
	 *         otherwise.
	 */
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

	/**
	 * Gets the current transaction screen. CLOSED - There is no trade
	 * happening; FIRST - The first screen for offering items; SECOND - The last
	 * screen for accepting.
	 * 
	 * @return The current transaction screen or null if widget is closed
	 */
	public static TransactionScreen getTransactionScreen() {
		if (OfferScreen.isOpen()) {
			return TransactionScreen.FIRST;
		} else if (AcceptScreen.isOpen()) {
			return TransactionScreen.SECOND;
		}
		return TransactionScreen.CLOSED;
	}

	/**
	 * Trades with the <tt>Player</tt> that has the specified name.
	 * 
	 * @param name
	 *            The name of the <tt>Player</tt>.
	 * @return <tt>true</tt> if the trade offer was sent; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean tradeWith(final String name) {
		if (OfferScreen.isOpen()) {
			return OfferScreen.getTrader().getName().equalsIgnoreCase(name);
		} else if (AcceptScreen.isOpen()) {
			return AcceptScreen.getTrader().getName().equalsIgnoreCase(name);
		}
		final Player t = Players.getNearest(name);
		if (t == null || !t.isOnScreen() || t.interact("Trade with " + name)) {
			return false;
		}
		for (int i = 0; !Game.getLastMessage().toLowerCase().contains("sending trade offer...") && i < 10; i += 1) {
			Task.sleep(250, 300);
		}
		return Game.getLastMessage().toLowerCase().contains("sending trade offer...");
	}
}