/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.test.ca;

import org.jboss.tools.jst.jsp.test.ca.CAHtml5PaletteTemplatesTest;
import org.jboss.tools.jst.web.ui.palette.internal.HtmlPaletteTagLib;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary;

public class CAPaletteHTML5TemplatesTest extends CAHtml5PaletteTemplatesTest {
	private static final String PAGE_NAME = "ca_13_templates.html"; //$NON-NLS-1$

	public CAPaletteHTML5TemplatesTest() {}

	public void testImageByImg() throws Exception {
		doTestTemplate("", "img", decorateDisplay("Image"), 0);
	}

	public void testImage() throws Exception {
		doTestTemplate("", "image", decorateDisplay("Image"), 0);
	}

	public void testLabel() throws Exception {
		//first element is JQM Checkbox.
		doTestTemplate("", "label", decorateDisplay("Label"), 1);
	}

	public void testAudio() throws Exception {
		doTestTemplate("", "audio", decorateDisplay("Audio"), 0);
	}

	public void testVideo() throws Exception {
		doTestTemplate("", "video", decorateDisplay("Video"), 0);
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

	@Override
	protected String decorateDisplay(String display) {
		return display + " - HTML 5.0";
	}

	@Override
	protected PaletteTagLibrary getTagLib() {
		return new HtmlPaletteTagLib();
	}
}