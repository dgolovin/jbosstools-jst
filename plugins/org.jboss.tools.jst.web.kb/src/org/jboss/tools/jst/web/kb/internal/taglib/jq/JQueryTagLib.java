/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.jq;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.JQueryRecognizer;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class JQueryTagLib implements ICustomTagLibrary {

	private static final String SHARP = "#";
	private static final String VERSION = "1";
	private static final IComponent[] EMPTY = new IComponent[0];
	private static final ImageDescriptor IMAGE = WebKbPlugin.getImageDescriptor(WebKbPlugin.class, "EnumerationProposal.gif"); //$NON-NLS-1$
	private static final String URI = "jQuery";
	protected static final XPath XPATH = XPathFactory.newInstance().newXPath();

	private ITagLibRecognizer recognizer;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary#getRecognizer()
	 */
	@Override
	public ITagLibRecognizer getRecognizer() {
		if(recognizer==null) {
			recognizer = new JQueryRecognizer();
		}
		return recognizer;
	}

	@Override
	public void setRecognizer(ITagLibRecognizer recognizer) {
		this.recognizer = recognizer;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		final List<TextProposal> proposals = new ArrayList<TextProposal>();
		IFile file = context.getResource();
		if (query.getType() == KbQuery.Type.ATTRIBUTE_VALUE && file != null) {
			final String mask = query.getValue();
			if (mask.startsWith(SHARP)) {
				StructuredModelWrapper.execute(file, new ICommand() {
					public void execute(IDOMDocument xmlDocument) {
						try {
							NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile(
											"//*/@id[starts-with(.,'" + mask.substring(1) + "')]")
												.evaluate(xmlDocument,XPathConstants.NODESET);
							for (int i = 0; i < list.getLength(); i++) {
								IDOMAttr attr = ((IDOMAttr)  list.item(i));
								IStructuredDocumentRegion s = ((IDOMNode)attr.getOwnerElement()).getStartStructuredDocumentRegion();
								String pt = SHARP + attr.getNodeValue();
								proposals.add(new TextProposal(IMAGE,pt,pt,pt.length(),s.getText()));
							}
						} catch (XPathExpressionException e) {
							WebKbPlugin.getDefault().logError(e);
						}
					}
				});
			}
		}

		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getResource()
	 */
	@Override
	public IResource getResource() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getSourcePath()
	 */
	@Override
	public IPath getSourcePath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getDefaultNameSpace()
	 */
	@Override
	public INameSpace getDefaultNameSpace() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getURI()
	 */
	@Override
	public String getURI() {
		return URI;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getVersion()
	 */
	@Override
	public String getVersion() {
		return VERSION;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponents()
	 */
	@Override
	public IComponent[] getComponents() {
		return EMPTY;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponents(java.lang.String)
	 */
	@Override
	public IComponent[] getComponents(String nameTemplate) {
		return EMPTY;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponent(java.lang.String)
	 */
	@Override
	public IComponent getComponent(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponentByType(java.lang.String)
	 */
	@Override
	public IComponent getComponentByType(String type) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponents(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	public IComponent[] getComponents(KbQuery query, IPageContext context) {
		return EMPTY;
	}

	@Override
	public ITagLibrary clone() throws CloneNotSupportedException {
		JQueryTagLib newLib = new JQueryTagLib();
		newLib.recognizer = recognizer;
		return newLib;
	}
}