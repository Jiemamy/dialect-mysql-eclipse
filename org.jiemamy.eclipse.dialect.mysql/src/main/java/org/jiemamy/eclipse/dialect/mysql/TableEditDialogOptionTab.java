/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/24
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy.eclipse.dialect.mysql;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import org.jiemamy.dialect.mysql.parameter.MySqlParameterKeys;
import org.jiemamy.dialect.mysql.parameter.StandardEngine;
import org.jiemamy.dialect.mysql.parameter.StorageEngineType;
import org.jiemamy.eclipse.core.ui.editor.diagram.AbstractTab;
import org.jiemamy.model.table.SimpleJmTable;

/**
 * テーブル編集ダイアログの「MySQL」タブ。
 * 
 * @author daisuke
 */
public class TableEditDialogOptionTab extends AbstractTab {
	
	private final SimpleJmTable table;
	
	private Combo cmbEngine;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param parentTabFolder 親となるタブフォルダ
	 * @param style SWTスタイル値
	 * @param table 編集対象テーブル
	 */
	public TableEditDialogOptionTab(TabFolder parentTabFolder, int style, SimpleJmTable table) {
		super(parentTabFolder, style, "MySQL");
		this.table = table;
		
		Composite composite = new Composite(parentTabFolder, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(composite, SWT.NULL);
		label.setText("エンジン"); // RESOURCE
		
		cmbEngine = new Combo(composite, SWT.READ_ONLY);
		cmbEngine.add("");
		for (StandardEngine type : StandardEngine.values()) {
			cmbEngine.add(type.toString());
		}
		
		StorageEngineType storageEngine = table.getParam(MySqlParameterKeys.STORAGE_ENGINE);
		if (storageEngine != null) {
			cmbEngine.setText(storageEngine.toString());
		}
		
		getTabItem().setControl(composite);
	}
	
	@Override
	public boolean isTabComplete() {
		return true;
	}
	
	@Override
	public void okPressed() {
		super.okPressed();
		
		if (StringUtils.isEmpty(cmbEngine.getText()) == false) {
			StandardEngine engine = StandardEngine.valueOf(cmbEngine.getText());
			table.putParam(MySqlParameterKeys.STORAGE_ENGINE, engine);
		} else {
			table.removeParam(MySqlParameterKeys.STORAGE_ENGINE);
		}
	}
	
}
