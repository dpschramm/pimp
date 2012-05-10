package pimp.model;

/**
 * The status class is an enum used as a flag for items in the Model, or cache.
 * Upon commit, these items are checked and actions are performed depending on what their flag is.
 * FRESH - recently pulled or saved to the database - in sync.
 * NEW - has not been added to the DB yet.
 * UPDATED - has had changes performed. A NEW item cannot be set to UPDATED, otherwise it wouldn't get added
 * DELETED - due to be removed from the DB. If the item was NEW, it is simply removed from the cache instead of receiving this flag.
 */

public enum Status {
	FRESH, NEW, UPDATED, DELETED
}
