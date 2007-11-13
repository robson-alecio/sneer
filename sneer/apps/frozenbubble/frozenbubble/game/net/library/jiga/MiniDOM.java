/*
 *                               [ JIGA ]
 *
 * Copyright (c) 2003 Glenn Sanson <glenn.sanson at free.fr>
 *
 * This code is distributed under the GNU Library General Public License 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version. 
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *               
 *                 [http://glenn.sanson.free.fr/jiga/]
 */

package frozenbubble.game.net.library.jiga;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * A simplified XML parser. This parser should understand any well-formed but
 * uncommented xml document. Possible usages :
 * <ul>
 * <li>Structured game data (could be used to load/save game state using a
 * server-side store mechanism)
 * <li>Messages exchange in network mode
 * </ul>
 * 
 * @author Glenn Sanson
 */
@SuppressWarnings("unchecked")
public class MiniDOM {

    /** internal xml header */
    private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

    /** indent char */
    private final static String INDENT_STRING = "  "; // 2 whitespaces

    /** Node name */
    private String name;

    /** Node value */
    private String value;

    /** Parent node (null for root node) */
    private MiniDOM parent;

    /** Children list */
    private Vector children;

    /** Attributes list */
	private Hashtable attributes;

    /* CONSTRUCTORS */

    /**
     * Creates a new instance of MiniDOM
     * 
     * @param name1
     *            the name of the node
     */
    public MiniDOM(String name1) {
        this.name = name1;
    }

    /* OPERATIONS ON VALUES OF THE NODE */

    /**
     * Defines the parent node of the current node
     * 
     * @param parent1
     *            the new parent of this node
     */
    public void setParent(MiniDOM parent1) {
        this.parent = parent1;
    }

    /**
     * Retrieves the parent node of the current node
     * 
     * @return the current parent of this node
     */
    public MiniDOM getParent() {
        return this.parent;
    }

    /**
     * Retrieves the name of the current node
     * 
     * @return the name of the node
     */
    public String getName() {
        return this.name;
    }

    /**
     * Defines the value of the current node
     * 
     * @param value1
     *            the value of the node
     */
    public void setValue(String value1) {
        this.value = value1;
    }

    /**
     * Retrieves the value of the current node
     * 
     * @return the value of the node, or <code>null</code> if no value is
     *         defined
     */
    public String getValue() {
        return this.value;
    }

    /* OPERATIONS ON CHILDREN */

    /**
     * Checks if the current node has children
     * 
     * @return true only if it has at least 1 child
     */
    public boolean hasChildren() {
        if (children == null) {
            return false;
        }

        // As there is a removeChild function, number of elements must be
        // checked
        return (children.size() != 0);
    }

    /**
     * Adds a child to the current Node. As a node can't be the child of more
     * than one parent, ths child node is automatically removed from its last
     * parent's children list
     * 
     * @param child
     *            the child to add
     */
    public void addChild(MiniDOM child) {
        if (children == null) {
            children = new Vector();
        }

        if (child.getParent() != null) {
            // Remove node from its current parent
            child.getParent().removeChild(child);
        }

        children.addElement(child);
        child.setParent(this);
    }

    /**
     * Removes a child of the current node (if it exists)
     * 
     * @param child
     *            the child to remove
     */
    public void removeChild(MiniDOM child) {
        if (children == null) {
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            if (children.elementAt(i) == child) {
                children.removeElementAt(i);
                return;
            }
        }
    }

    /**
     * Retrieves the first child of the current node with a given name
     * 
     * @param name1
     *            The name of the child node to find
     * @return a node or <code>null</code> if there's no corresponding node
     */
    public MiniDOM getChild(String name1) {
        if (children == null) {
            return null;
        }

        for (int i = 0; i < children.size(); i++) {
            if (((MiniDOM) children.elementAt(i)).getName().equals(name1)) {
                return (MiniDOM) children.elementAt(i);
            }
        }

        return null;
    }

    /**
     * Retrieves all children of the current node with a given name
     * 
     * @param name1
     *            The name of the child node to find
     * @return a list of nodes or <code>null</code> if there's no
     *         corresponding node
     */
    public MiniDOM[] getChildren(String name1) {
        if (children == null) {
            return null;
        }

        // Count number of nodes
        int nbNodes = 0;

        for (int i = 0; i < children.size(); i++) {
            if (((MiniDOM) children.elementAt(i)).getName().equals(name1)) {
                nbNodes++;
            }
        }

        if (nbNodes == 0) {
            return null;
        }

        // Generate output
        MiniDOM[] output = new MiniDOM[nbNodes];
        nbNodes = 0;

        for (int i = 0; i < children.size(); i++) {
            if (((MiniDOM) children.elementAt(i)).getName().equals(name1)) {
                output[nbNodes] = (MiniDOM) children.elementAt(i);
                nbNodes++;
            }
        }

        return output;
    }

    /**
     * Retrieves all children of the current node
     * 
     * @return a list of nodes or <code>null</code> if there's no
     *         corresponding node
     */
    public MiniDOM[] getAllChildren() {
        if (children == null) {
            return null;
        }

        MiniDOM[] output = new MiniDOM[children.size()];

        for (int i = 0; i < children.size(); i++) {
            output[i] = (MiniDOM) children.elementAt(i);
        }

        return output;
    }

    /* OPERATIONS ON ATTRIBUTES */

    /**
     * Adds an attribute to the current node. Synthax :
     * <code>&gt;node key="value" ...</code>
     * 
     * @param key
     *            the name of the attribute
     * @param value1
     *            the value of the attribute
     */
    public void addAttribute(String key, String value1) {
        if (attributes == null) {
            attributes = new Hashtable();
        }

        attributes.put(key, value1);
    }

    /**
     * Retrieves the value of an attribute of the current node
     * 
     * @param key
     *            the name of the attribute
     * @return the value associated with this key, or <code>null</code> if
     *         there's no corresponding value
     */
    public String getAttribute(String key) {
        if (attributes == null) {
            return null;
        }

        return (String) attributes.get(key);
    }

    /**
     * Retrieves keys of all attributes of the current node
     * 
     * @return A list representing all keys
     */
    @SuppressWarnings("unchecked")
	public String[] getAllAttributes() {
        if (attributes == null) {
            return new String[0];
        }

        String[] output = new String[attributes.size()];
        // Reverse output order
        int index = output.length - 1;
        for (Enumeration e = attributes.keys(); e.hasMoreElements();) {
            output[index] = (String) e.nextElement();
            index--;
        }

        return output;
    }

    /* CONVERSIONS */

    /**
     * Converts a data tree to a flat output any subtree of the tree may be
     * converted
     * 
     * @param header
     *            a boolean used to define whether a standard xml header is
     *            needed or not
     * @param indent
     *            a boolean used to define whether a output should be indented
     *            or not
     * @return a flat representation of the tree having the current node as root
     *         node
     */
    public String serialize(boolean header, boolean indent) {
        StringBuffer buffer = new StringBuffer();

        // Add header if requested
        if (header) {
            buffer.append(XML_HEADER);

            if (indent) {
                buffer.append("\r\n");
            }
        }

        if (indent) {
            this.serialize(buffer, 0);
        } else {
            this.serialize(buffer, -1);
        }

        return buffer.toString();
    }

    /**
     * Converts a data tree to a flat output any subtree of the tree may be
     * converted
     * 
     * @param buffer
     *            the <code>StringBuffer</code> used to build the output
     * @param indentLevel
     *            indentation level, or -1 if no indentation is intended.
     *            Construction from root starts at 0
     */
    public void serialize(StringBuffer buffer, int indentLevel) {
        // indent info
        String indent = new String();
        if (indentLevel != -1) {
            for (int i = 0; i < indentLevel; i++) {
                indent += INDENT_STRING;
            }
        }

        buffer.append(indent);

        // start tag
        buffer.append("<");
        buffer.append(this.name);

        // Attributes
        if (this.attributes != null) {
            String[] keys = this.getAllAttributes();
            for (int i = 0; i < keys.length; i++) {
                buffer.append(" ");
                buffer.append(keys[i]);
                buffer.append("=\"");
                buffer.append((String) this.attributes.get(keys[i]));
                buffer.append("\"");
            }
        }

        // Check is current node has a value
        if (this.value != null) {
            buffer.append(">");
            buffer.append(this.value);
            buffer.append("</");
            buffer.append(this.name);
            buffer.append(">");
        }
        // Check if current node has children
        else if (this.hasChildren()) {
            // Complete start tag
            buffer.append(">");

            // Complete content
            int childLevel = indentLevel;
            if (indentLevel != -1) {
                childLevel++;
                buffer.append("\r\n");
            }
            for (int i = 0; i < this.children.size(); i++) {
                ((MiniDOM) children.elementAt(i)).serialize(buffer, childLevel);
            }

            // Complete end tag
            buffer.append(indent);
            buffer.append("</");
            buffer.append(this.name);
            buffer.append(">");
        }
        // Tag has no inner content <tag />
        else {
            buffer.append(" />");
        }

        if (indentLevel != -1) {
            buffer.append("\r\n");
        }
    }

    /**
     * Converts a flat input to a data tree
     * 
     * @param input
     *            the flat input to convert
     * @return the root node of the generated data tree
     */
    public static MiniDOM getTree(String input) throws InvalidConstructionException {

        // search for xml header
        // check if target is 'xml'
        int beginTag = input.indexOf("<?");
        if (beginTag != -1) {
            int endTag = input.indexOf("?>", beginTag);

            String content = input.substring(beginTag + 2, endTag).trim().toUpperCase();
            if (!content.startsWith("XML")) {
                throw new InvalidConstructionException("Target declared in header should be 'xml'");
            }

            beginTag = input.indexOf("<", endTag);
        } else {
            beginTag = input.indexOf("<");
        }

        MiniDOM rootNode = null;
        MiniDOM currentNode = null;

        while (beginTag != -1) {
            int endTag = input.indexOf(">", beginTag);

            if (endTag == -1) {
                throw new InvalidConstructionException("Unended tag");
            }

            // Get content inside <...>
            String content = input.substring(beginTag + 1, endTag).trim();

            if (content.startsWith("/")) {
                // closing tag </node>
                // check if it really close a <node>
                String verifTagName = content.substring(1).trim();

                if (currentNode == null || !currentNode.getName().equals(verifTagName)) {
                    // Name of the node to close is not throws same (<a></b>)
                    // or there is no node to close (begining of the tree)
                    throw new InvalidConstructionException("</" + verifTagName + "> does not match the right tag");
                }

                if (verifTagName.endsWith("/")) {
                    // Node is </node />
                    throw new InvalidConstructionException("Constructions of type </node /> are not allowed");
                }

                if (currentNode.hasChildren() && currentNode.value != null) {
                    // Check if both a value and children were set
                    // Wrong construction example : <parent>123<child
                    // /></parent>
                    throw new InvalidConstructionException("A tag can't have a value and children at the same time");
                }

                if (currentNode == rootNode) {
                    // Tree completed (root node closed)
                    // What's left in the String is ignored
                    return rootNode;
                } 
                    // Child node completed, going back to its parent node
                    currentNode = currentNode.getParent();
               
            } else { // Creation of a new tag

                // Check if the tag is directly closed
                // <node />
                boolean noInnerData = false;

                if (content.endsWith("/")) {
                    content = content.substring(0, content.length() - 1).trim();
                    noInnerData = true;
                }

                // Analyze content data

                // Check if there's something to read (at least a tag name)
                if (content.length() == 0) {
                    throw new InvalidConstructionException("Tag with no name");
                }

                // Get tag name
                int contentPosition = content.indexOf(" ");
                if (contentPosition == -1) {
                    // No attributes
                    MiniDOM newNode = new MiniDOM(content);

                    if (currentNode == null) {
                        currentNode = newNode;
                        rootNode = newNode;
                    } else {
                        currentNode.addChild(newNode);
                        currentNode = newNode;
                    }
                } else {
                    // node with attributes
                    MiniDOM newNode = new MiniDOM(content.substring(0, contentPosition));

                    if (currentNode == null) {
                        currentNode = newNode;
                        rootNode = newNode;
                    } else {
                        currentNode.addChild(newNode);
                        currentNode = newNode;
                    }

                    // Attributes
                    content = content.substring(contentPosition).trim();

                    while (content.length() != 0) {

                        // Search attributes
                        // (key="value")1+

                        // Search for key
                        contentPosition = content.indexOf("=");

                        if (contentPosition == -1) {
                            throw new InvalidConstructionException("Invalid attributes set [" + content + "]");
                        }

                        String key = content.substring(0, contentPosition).trim();
                        contentPosition = content.indexOf("\"", contentPosition + 1);

                        // search for value
                        if (contentPosition == -1) {
                            throw new InvalidConstructionException("Invalid attributes set [" + content + "]");
                        }

                        content = content.substring(contentPosition + 1).trim();

                        contentPosition = content.indexOf("\"");

                        if (contentPosition == -1) {
                            throw new InvalidConstructionException("Unmatched double quote [\"] in attributes list");
                        }

                        String value = content.substring(0, contentPosition); // No
                        // trim
                        // here

                        // Add new attribute to attribute list
                        // If an attribute with the same name already exists
                        // for this node, it is automatically removed
                        currentNode.addAttribute(key, value);

                        content = content.substring(contentPosition + 1).trim();
                    }
                }

                // change level for node <node />
                if (noInnerData) {
                    currentNode = currentNode.getParent();

                    if (currentNode == null) {
                        // If the document is limited to a root node with no
                        // content
                        return rootNode;
                    }
                }
            }

            // Search for next tag
            // Check if data between current tag and next tag is a value or just
            // crap

            beginTag = input.indexOf("<", endTag + 1);

            if (beginTag != -1) {
                // beginTag -> begin of next tag
                // endTag -> End of current Tag
                String possibleValue = input.substring(endTag + 1, beginTag).trim();

                if (!"".equals(possibleValue)) {
                    currentNode.setValue(possibleValue);
                }
            } else {
                // If no new tag is found, and root node is still incomplete,
                // then the data stream is incomplete
                throw new InvalidConstructionException("Incomplete document");
            }
        }

        if (rootNode == null) {
            throw new InvalidConstructionException("No document found");
        }

        return rootNode;
    }
}