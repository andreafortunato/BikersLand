package com.bikersland.utility;

import javafx.scene.control.ListCell;

public class TagsListCell extends ListCell<String> {
	public TagsListCell() {
		super();
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		setText(empty ? null : item);
	}
	
	
}
