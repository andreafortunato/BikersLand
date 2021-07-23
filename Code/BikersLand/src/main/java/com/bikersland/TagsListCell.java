package com.bikersland;

import javafx.scene.control.ListCell;

public class TagsListCell extends ListCell {
	public TagsListCell() {
		super();
	}

	@Override
	protected void updateItem(Object item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		setText(empty ? null : (String) item);
	}
	
	
}
