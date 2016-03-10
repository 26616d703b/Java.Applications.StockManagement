package com.ampersand.sm.observers;

public interface DatabaseObservable {

	public void addObserver(DatabaseObserver observer);

	public void notifyObservers(boolean closing_connections_to_database);

	public void removeObservers();
}
