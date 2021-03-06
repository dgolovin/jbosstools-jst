/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.internal.ionic.palette;

import org.jboss.tools.jst.angularjs.internal.ionic.IonicRecognizer;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary;

/**
 * @author Alexey Kazakov
 */
@SuppressWarnings("restriction")
public class IonicTagLib extends PaletteTagLibrary {

	private final static int RELEVANCE = generateUniqueRelevance();

	public IonicTagLib() {
		super(null, "ionic", null, "ionicpalette", true);
		IHTMLLibraryVersion version = IonicVersion.IONIC_1_0;
		this.name = "Ionic " + version + " templates";
		setPaletteLibraryVersion(version);
		setVersion(version.toString());
	}

	@Override
	public ITagLibRecognizer getTagLibRecognizer() {
		return new IonicRecognizer();
	}

	@Override
	protected int getRelevance() {
		return RELEVANCE;
	}

	@Override
	protected String getCategory() {
		return IonicConstants.IONIC_CATEGORY;
	}
}