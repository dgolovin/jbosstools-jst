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
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.Command;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class JQueryTagLib implements ICustomTagLibrary {

	private static XPathExpression EXPR_ID_VALUE;
	
	static {
		try {
			EXPR_ID_VALUE = XPathFactory.newInstance().newXPath().compile("//@id");
		} catch (XPathExpressionException e) {
			WebKbPlugin.getDefault().logError(e);
		}
	}

	private static final ImageDescriptor IMAGE = WebKbPlugin.getImageDescriptor(WebKbPlugin.class, "EnumerationProposal.gif"); //$NON-NLS-1$
	private static final String URI = "jQuery";

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
		List<TextProposal> proposals = new ArrayList<TextProposal>();

		if(query.getType()==KbQuery.Type.ATTRIBUTE_VALUE) {
			String mask = query.getValue();
			if(mask.startsWith("#")) {
				String idMask = mask.substring(1);
				ElementID[] ids = findAllIds(context);
				for (ElementID id : ids) {
					String idText = id.getId();
					if(idText.startsWith(idMask)) {
						String proposaltext = "#" + idText;
						TextProposal proposal = new TextProposal();
						proposal.setLabel(proposaltext);
						proposal.setReplacementString(proposaltext);
						proposal.setPosition(proposaltext.length());
						proposal.setImageDescriptor(IMAGE);
						proposal.setContextInfo(id.getDescription());
	
						proposals.add(proposal);
					}
				}
			}
		}

		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	/**
	 * Returns an array of all element ID (<tagname id="..."/>) defined in the page
	 * @param context
	 * @return
	 */
	public static ElementID[] findAllIds(IPageContext context) {
		final List<ElementID> ids = new ArrayList<ElementID>();

		IFile file = context.getResource();
		if(file!=null) {
			StructuredModelWrapper.execute(file, new Command() {
				public void execute(IDOMDocument xmlDocument) {
					findIdsInChildElements(ids, xmlDocument);					
				}
			});
		}
		return ids.toArray(new ElementID[ids.size()]);
	}

	private static void findIdsInChildElements(List<ElementID> ids,
			Node parentNode) {
		try {
			NodeList list = (NodeList) EXPR_ID_VALUE.evaluate(parentNode,
					XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node attr = list.item(i);
				IStructuredDocumentRegion s = ((IDOMNode) ((Attr) attr)
						.getOwnerElement()).getStartStructuredDocumentRegion();
				ids.add(new ElementID(attr.getNodeValue(), ((IDOMAttr) attr)
						.getValueRegionStartOffset() + 1, s.getText()));
			}
		} catch (XPathExpressionException e) {
			WebKbPlugin.getDefault().logError(e);
		}
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
		return "1";
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponents()
	 */
	@Override
	public IComponent[] getComponents() {
		return new IComponent[0];
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibrary#getComponents(java.lang.String)
	 */
	@Override
	public IComponent[] getComponents(String nameTemplate) {
		return new IComponent[0];
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
		return new IComponent[0];
	}

	@Override
	public ITagLibrary clone() throws CloneNotSupportedException {
		JQueryTagLib newLib = new JQueryTagLib();
		newLib.recognizer = recognizer;
		return newLib;
	}

	public static class ElementID {

		private String id;
		private int offset;
		private String description;

		public ElementID(String id, int offset, String description) {
			this.id = id;
			this.offset = offset;
			if(description!=null) {
				description = description.replace("<", "&lt;").replace(">", "&gt;");
			}
			this.description = description;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the offset
		 */
		public int getOffset() {
			return offset;
		}
	}
}