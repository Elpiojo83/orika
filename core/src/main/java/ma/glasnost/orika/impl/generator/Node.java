/*
 * Orika - simpler, better and faster Java bean mapping
 *
 *  Copyright (C) 2011-2019 Orika authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ma.glasnost.orika.impl.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import ma.glasnost.orika.MapEntry;
import ma.glasnost.orika.impl.util.StringUtil;
import ma.glasnost.orika.metadata.FieldMap;
import ma.glasnost.orika.metadata.Property;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

public class Node {

  public final Node parent;
  public final Property property;
  public final FieldMap value;
  public final NodeList children;
  private final boolean isSource;
  public MultiOccurrenceVariableRef multiOccurrenceVar;
  public MultiOccurrenceVariableRef newDestination;
  public VariableRef elementRef;
  public VariableRef nullCheckFlag;
  public VariableRef shouldAddToCollectorFlag;
  public boolean addedToCollector;

  private Node(
      Property property,
      FieldMap fieldMap,
      Node parent,
      NodeList nodes,
      boolean isSource,
      int uniqueIndex) {

    this.isSource = isSource;
    String name = (isSource ? "source" : "destination");
    String propertySuffix = StringUtil.capitalize(property.getName());
    this.value = fieldMap;
    this.parent = parent;
    this.property = property;

    if (property.isMultiOccurrence()) {

      /*
       * Use a List for storing elements intended for an Array; this allows flexibility in case we
       * can't (or it's too difficult to) determine the total size up front.
       *
       * Also, use a List (of Map.Entry) for elements intended for a Map; since we need to add the
       * Entry as soon as it's created (while it has null key and value), we can't put() it into
       * it's destination map until the other properties have been given values.
       */
      Type<?> elementType = buildElementType(property, isSource);
      Type<?> destinationType = buildDestinationType(property, elementType);

      this.newDestination =
          new MultiOccurrenceVariableRef(
              destinationType, "new_" + name + propertySuffix + uniqueIndex);
      String multiOccurrenceName;
      if (parent != null) {
        multiOccurrenceName = name(parent.elementRef.name(), name + propertySuffix);
      } else {
        multiOccurrenceName = name;
      }
      this.multiOccurrenceVar = new MultiOccurrenceVariableRef(property, multiOccurrenceName);
      this.elementRef =
          new VariableRef(elementType, property.getName() + "_" + name + uniqueIndex + "Element");
      if (elementType != null && elementType.isPrimitive()) {
        this.nullCheckFlag =
            new VariableRef(
                TypeFactory.valueOf(Boolean.TYPE),
                property.getName() + "_" + name + uniqueIndex + "ElementIsNull");
      }
      this.shouldAddToCollectorFlag =
          new VariableRef(
              TypeFactory.valueOf(Boolean.TYPE),
              property.getName() + "_" + name + uniqueIndex + "ElementShouldBeAddedToCollector");
    }

    if (nodes != null) {
      nodes.add(this);
      this.children = new NodeList(nodes);
    } else if (parent != null) {
      parent.children.add(this);
      this.children = new NodeList(parent.children);
    } else {
      this.children = new NodeList();
    }
  }

  private Node(Property property, Node parent, boolean isSource, int uniqueIndex) {
    this(property, null, parent, null, isSource, uniqueIndex);
  }

  private Node(
      Property property, FieldMap fieldMap, Node parent, boolean isSource, int uniqueIndex) {
    this(property, fieldMap, parent, null, isSource, uniqueIndex);
  }

  private Node(
      Property property, FieldMap fieldMap, NodeList nodes, boolean isSource, int uniqueIndex) {
    this(property, fieldMap, null, nodes, isSource, uniqueIndex);
  }

  public static Node findFieldMap(final FieldMap map, final NodeList nodes, boolean useSource) {
    LinkedList<Property> path = new LinkedList<Property>();
    Property container = useSource ? map.getSource() : map.getDestination();
    while (container.getContainer() != null) {
      path.addFirst(container.getContainer());
      container = container.getContainer();
    }
    Node currentNode = null;
    NodeList children = nodes;

    for (Property pathElement : path) {
      currentNode = null;
      for (Node node : children) {
        if (node.property.equals(pathElement)) {
          currentNode = node;
          children = currentNode.children;
          break;
        }
      }
      if (currentNode == null) {
        return null;
      }
    }

    for (Node node : children) {
      if (map.equals(node.value)) {
        return node;
      }
    }
    return null;
  }

  private Type<?> primitiveSafeListType(Type<?> type) {
    if (type.isPrimitive()) {
      return type.getWrapperType();
    } else {
      return type;
    }
  }

  private String name(String value1, String defaultValue) {
    if (value1 != null && !"".equals(value1)) {
      return value1;
    } else {
      return defaultValue;
    }
  }

  /** @return true if this Node has no children */
  public boolean isLeaf() {
    return children.isEmpty();
  }

  /** @return true if this Node has no parent */
  public boolean isRoot() {
    return parent == null;
  }

  public FieldMap getMap() {
    TreeMap<Integer, FieldMap> nodes = new TreeMap<Integer, FieldMap>();

    for (Node child : children) {
      if (child.value != null) {

        int depth = 0;
        FieldMap value = child.value;
        Property prop = isSource ? value.getSource() : value.getDestination();
        while (prop.getContainer() != null) {
          ++depth;
          prop = prop.getContainer();
        }

        if (!nodes.containsKey(depth)) {
          nodes.put(depth, value);
        }
      }
    }
    if (!nodes.isEmpty()) {
      return nodes.get(nodes.firstKey());
    } else {
      return null;
    }
  }

  public String toString() {
    return toString("");
  }

  private String toString(String indent) {
    StringBuilder out = new StringBuilder();
    out.append(indent + this.property.toString());
    if (!this.children.isEmpty()) {
      out.append(" {");

      for (Node child : children) {
        out.append("\n" + child.toString("  " + indent));
      }
      out.append("\n" + indent + "}");
    }
    return out.toString();
  }

  private Type<?> buildElementType(Property property, boolean isSource) {
    Type<?> elementType = null;
    if (property.isMap()) {
      if (isSource) {
        @SuppressWarnings("unchecked")
        Type<?> entryType = MapEntry.entryType((Type<Map<Object, Object>>) property.getType());
        elementType = entryType;
      } else {
        @SuppressWarnings("unchecked")
        Type<?> entryType =
            MapEntry.concreteEntryType((Type<Map<Object, Object>>) property.getType());
        elementType = entryType;
      }
    } else if (property.isCollection()) {
      elementType = property.getElementType();
    } else if (property.isArray()) {
      elementType = property.getType().getComponentType();
    }
    return elementType;
  }

  private Type<?> buildDestinationType(Property property, Type<?> elementType) {
    if (property.getType().isArray()) {
      return TypeFactory.valueOf(
          ArrayList.class, primitiveSafeListType(property.getType().getComponentType()));
    } else if (property.getType().isMap()) {
      return TypeFactory.valueOf(ArrayList.class, elementType);
    } else {
      return property.getType();
    }
  }

  public static class NodeList extends ArrayList<Node> {

    private static final long serialVersionUID = 1L;
    private final NodeList parent;
    private int totalNodes = 0;

    public NodeList() {
      this.parent = null;
    }

    private NodeList(NodeList parent) {
      this.parent = parent;
    }

    public Node addFieldMap(final FieldMap map, boolean useSource) {
      LinkedList<Property> path = new LinkedList<Property>();
      Property root = useSource ? map.getSource() : map.getDestination();
      Property container = root;

      while (container.getContainer() != null) {
        path.addFirst(container.getContainer());
        container = container.getContainer();
      }
      /*
       * Attempt to locate the path within the tree of nodes
       * under which this fieldMap should be placed
       */
      Node currentNode = null;
      Node parentNode = null;
      NodeList children = this;

      for (int p = 0, len = path.size(); p < len; ++p) {
        Property pathElement = path.get(p);

        for (Node node : children) {
          if (node.property.equals(pathElement)) {
            currentNode = node;
            children = currentNode.children;
            break;
          }
        }
        if (currentNode == null) {

          currentNode = new Node(pathElement, parentNode, useSource, totalNodes);
          if (parentNode == null) {
            children.add(currentNode);
          }
          parentNode = currentNode;
          for (p += 1; p < len; ++p) {
            currentNode = new Node(path.get(p), parentNode, useSource, totalNodes);
            parentNode = currentNode;
          }
        } else {
          parentNode = currentNode;
          currentNode = null;
        }
      }
      /*
       * Finally add a node for the fieldMap at the end
       */
      if (parentNode == null) {
        root = innermostElement(root);
        currentNode = new Node(root, map, this, useSource, totalNodes);
      } else {
        root = innermostElement(root);
        currentNode = new Node(root, map, parentNode, useSource, totalNodes);
      }

      return currentNode;
    }

    private Property innermostElement(final Property p) {
      Property result = p;
      while (result.getElement() != null) {
        result = result.getElement();
      }
      return result;
    }

    public String toString() {
      StringBuilder out = new StringBuilder();
      out.append("{");
      if (!isEmpty()) {
        for (Node node : this) {
          out.append("\n" + node.toString("  "));
        }
        out.append("\n}");
      } else {
        out.append("}");
      }
      return out.toString();
    }

    private void incrementTotalNodes() {
      if (parent != null) {
        parent.incrementTotalNodes();
      }
      ++totalNodes;
    }

    public boolean add(Node node) {
      incrementTotalNodes();
      return super.add(node);
    }
  }
}
