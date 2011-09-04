package com.rsbuddy.script.wrappers;

public class BankItem {

	private final int amount;
	private final BankEvent event;
	private final int[] ids;

	/**
	 * Used for storing data when banking.
	 * 
	 * @param even
	 *            The bank event to do.
	 * @param amount
	 *            The amount to withdraw/deposit. Use -1 when depositing all or
	 *            depositing all except.
	 * @param ids
	 *            The item ids to bank.
	 */
	public BankItem(final BankEvent event, final int amount, final int... ids) {
		this.amount = amount;
		this.event = event;
		this.ids = ids;
	}

	public int getAmount() {
		return amount;
	}

	public BankEvent getEvent() {
		return event;
	}

	public int[] getIds() {
		return ids;
	}
}