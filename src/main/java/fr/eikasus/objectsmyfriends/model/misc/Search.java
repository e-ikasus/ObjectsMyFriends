package fr.eikasus.objectsmyfriends.model.misc;

public class Search
{
	private static final int OPENED_BIDS = 0x01;
	private static final int MY_CURRENT_BIDS = 0x02;
	private static final int MY_WON_BIDS = 0x04;
	private static final int MY_CURRENT_SALES = 0x08;
	private static final int MY_WAITING_SALES = 0x10;
	private static final int MY_ENDED_SALES = 0x20;
	private static final int MY_CANCELED_SALES = 0x40;

	/* ************* */
	/* Class members */
	/* ************* */

	int value;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public boolean isOpenedBids()
	{
		return ((value & OPENED_BIDS) != 0);
	}

	public Search setOpenedBids()
	{
		value |= OPENED_BIDS;

		return this;
	}

	public boolean isMyCurrentBids()
	{
		return ((value & MY_CURRENT_BIDS) != 0);
	}

	public Search setMyCurrentBids()
	{
		value |= MY_CURRENT_BIDS;

		return this;
	}

	public boolean isMyWonBids()
	{
		return ((value & MY_WON_BIDS) != 0);
	}

	public Search setMyWonBids()
	{
		value |= MY_WON_BIDS;

		return this;
	}

	public boolean isMyCurrentSales()
	{
		return ((value & MY_CURRENT_SALES) != 0);
	}

	public Search setMyCurrentSales()
	{
		value |= MY_CURRENT_SALES;

		return this;
	}

	public boolean isMyWaitingSales()
	{
		return ((value & MY_WAITING_SALES) != 0);
	}

	public Search setMyWaitingSales()
	{
		value |= MY_WAITING_SALES;

		return this;
	}

	public boolean isMyEndedSales()
	{
		return ((value & MY_ENDED_SALES) != 0);
	}

	public Search setMyEndedSales()
	{
		value |= MY_ENDED_SALES;

		return this;
	}

	public boolean isMyCanceledSales()
	{
		return ((value & MY_CANCELED_SALES) != 0);
	}

	public Search setMyCanceledSales()
	{
		value |= MY_CANCELED_SALES;

		return this;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public boolean asBuyer()
	{
		return (isOpenedBids() | isMyCurrentBids() | isMyWonBids());
	}

	public boolean asSeller()
	{
		return (isMyCurrentSales() || isMyWaitingSales() || isMyEndedSales() || isMyCanceledSales());
	}
}
