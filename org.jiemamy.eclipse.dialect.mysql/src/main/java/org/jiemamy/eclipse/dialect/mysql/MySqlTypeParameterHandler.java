/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/03/19
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

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dialect.Necessity;
import org.jiemamy.dialect.mysql.MySqlDialect;
import org.jiemamy.dialect.mysql.parameter.MySqlParameterKeys;
import org.jiemamy.eclipse.core.ui.editor.diagram.EditListener;
import org.jiemamy.eclipse.core.ui.editor.diagram.node.table.TypeParameterHandler;
import org.jiemamy.eclipse.core.ui.utils.SwtUtil;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.utils.LogMarker;

/**
 * MySQL用の{@link TypeParameterHandler}実装クラス。
 * 
 * @author daisuke
 */
public class MySqlTypeParameterHandler implements TypeParameterHandler {
	
	private static Logger logger = LoggerFactory.getLogger(MySqlTypeParameterHandler.class);
	
	private Button chkUnsigned;
	
	private JmColumn column;
	
	private MySqlDialect dialect = new MySqlDialect();
	

	public void createControl(JmColumn column, Composite composite, EditListener editListener) {
		logger.trace(LogMarker.LIFECYCLE, "createControl");
		this.column = column;
		
		DataType dataType = column.getDataType();
		Map<TypeParameterKey<?>, Necessity> typeParameterSpecs =
				dialect.getTypeParameterSpecs(dataType.getRawTypeDescriptor());
		for (TypeParameterKey<?> key : typeParameterSpecs.keySet()) {
			if (key.equals(MySqlParameterKeys.UNSIGNED)) {
				chkUnsigned = new Button(composite, SWT.CHECK);
				chkUnsigned.setText("UNSIGNED"); // RESOURCE
				chkUnsigned.addKeyListener(editListener);
				break;
			}
		}
	}
	
	public void disable() {
		SwtUtil.setEnabledIfAlive(chkUnsigned, false);
	}
	
	public void enable() {
		SwtUtil.setEnabledIfAlive(chkUnsigned, true);
	}
	
	public void setParametersFromControl() {
		SimpleDataType dataType = (SimpleDataType) column.getDataType();
		
		if (SwtUtil.isAlive(chkUnsigned)) {
			boolean value = chkUnsigned.getSelection();
			dataType.putParam(MySqlParameterKeys.UNSIGNED, value);
		}
	}
	
	public void setParametersToControl() {
		DataType dataType = column.getDataType();
		
		Boolean unsigned = dataType.getParam(MySqlParameterKeys.UNSIGNED);
		if (unsigned != null && chkUnsigned != null) {
			chkUnsigned.setSelection(unsigned);
		}
	}
}
