package ru.vsu.cs.aisd.g92.lyigina_p_s;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphFrame extends JFrame {
    private JPanel drawPanel;
    private JPanel panelMain;
    private JPanel panelButtons;
    private JButton buttonAddNode;
    private JButton buttonMoveNode;
    private JButton buttonRemoveNode;
    private JButton buttonAddEdge;
    private JButton buttonRemoveEdge;

    enum ViewState{
        ADD_NODE,
        ADD_EDGE,
        REMOVE_NODE,
        REMOVE_EDGE,
        MOVE_NODE,
        DEFAULT
    }

    private List<NodeView> nodes = new ArrayList<>();
    private List<EdgeView> edges = new ArrayList<>();
    private Integer idCounter = 0;
    private NodeView selectedNode = null;
    private EdgeView newEdge = null;

    private Optional<EdgeView> findEdgeView(Integer x, Integer y) {
        return edges.stream().filter(edgeView -> {
            int dx = edgeView.getViewDstX() - edgeView.getViewX();
            int dy = edgeView.getViewDstY() - edgeView.getViewY();
            double m = Math.sqrt(dy*dy + dx*dx);
            double a = dy/m, b = dx/m, c = (dx*edgeView.getViewY()-dy*edgeView.getViewX())/m;
            return Math.abs(a*x - b*y + c) <= 10 &&
                    x <= Math.max(edgeView.getViewX(), edgeView.getViewDstX()) &&
                    x >= Math.min(edgeView.getViewX(), edgeView.getViewDstX()) &&
                    y <= Math.max(edgeView.getViewY(), edgeView.getViewDstY()) &&
                    y >= Math.min(edgeView.getViewY(), edgeView.getViewDstY());
        }).findAny();
    }

    private Optional<NodeView> findNodeView(Integer x, Integer y) {
        return nodes.stream().filter(nodeView -> nodeView.getViewX() <= x
                && nodeView.getViewY() <= y
                && nodeView.getViewX() + NodeView.X_SIZE >= x
                && nodeView.getViewY() + NodeView.Y_SIZE >= y).findAny();
    }

    private void invalidateGraph(JPanel panel) {
        List<IView> views = new ArrayList<>();
        views.addAll(edges);
        views.addAll(nodes);
//        panel.invalidate();
        views.forEach(view -> view.draw(buffer.getGraphics()));
        panel.setForeground(panel.getBackground());
        panel.getGraphics().drawImage(buffer, 0, 0, null);
        buffer.getGraphics().clearRect(0,0,panel.getWidth(), panel.getHeight());
    }

    private Image buffer;
    private ViewState viewState = ViewState.DEFAULT;

    public GraphFrame() {
        this.setTitle("Графы");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        buffer = drawPanel.createImage(drawPanel.getWidth(), drawPanel.getHeight());
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Integer x = e.getX();
                Integer y = e.getY();
                switch (viewState) {
                    case ADD_NODE:
                        NodeView newNode = new NodeView(idCounter++,
                                x, y, x, y);
//                        newNode.draw(panelMain.getGraphics());
                        nodes.add(newNode);
                        break;
                    case ADD_EDGE:
                        Optional<NodeView> ifNode = findNodeView(x, y);
                        if(ifNode.isPresent()) {
                            selectedNode = ifNode.get();
                            newEdge = new EdgeView(
                                    selectedNode.getId(),
                                    selectedNode.getCenter().x,
                                    selectedNode.getCenter().y,
                                    selectedNode.getVirtualX(),
                                    selectedNode.getVirtualY(),
                                    x, y, x, y, null
                            );
                            selectedNode.getEdges().add(newEdge);
                            edges.add(newEdge);
 //                           newEdge.draw(panelMain.getGraphics());
                        }
                        break;
                    case MOVE_NODE:
                        ifNode = findNodeView(x, y);
                        if(ifNode.isPresent()) {
                            selectedNode = ifNode.get();
                        }
                        break;
                    case REMOVE_NODE:
                        ifNode = findNodeView(x, y);
                        if(ifNode.isPresent()) {
                            selectedNode = ifNode.get();
                            nodes.remove(selectedNode);
                            for (EdgeView edge : selectedNode.getEdges()) {
                                edges.remove(edge);
                                if (!edge.getId().equals(selectedNode.getId())) {
                                    for (NodeView node : nodes) {
                                        if (edge.getId().equals(node.getId())) {
                                            node.getEdges().remove(edge);
                                            break;
                                        }
                                    }
                                } else {
                                    for (NodeView node : nodes) {
                                        if (edge.getIdDst().equals(node.getId())) {
                                            node.getEdges().remove(edge);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        selectedNode = null;
                        break;
                    case REMOVE_EDGE:
                        Optional<EdgeView> ifEdge = findEdgeView(x, y);
                        if (ifEdge.isPresent()) {
                            newEdge = ifEdge.get();
                            edges.remove(newEdge);
                            for (NodeView node : nodes) {
                                if (node.getId().equals(newEdge.getId()) ||
                                        node.getId().equals(newEdge.getIdDst()))
                                    node.getEdges().remove(newEdge);
                            }
                        }
                        newEdge = null;
                        break;
                    default:
                        break;
                }

                invalidateGraph(drawPanel);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                Integer x = e.getX();
                Integer y = e.getY();
                switch (viewState) {
                    case ADD_EDGE:
                        if (selectedNode == null || newEdge == null)
                            break;
                        Optional<NodeView> ifNode = findNodeView(x, y);
                        if(ifNode.isPresent()) {
                            NodeView dstNode = ifNode.get();
                            dstNode.getEdges().add(newEdge);
                            newEdge.setViewDstX(dstNode.getCenter().x);
                            newEdge.setViewDstY(dstNode.getCenter().y);
                            newEdge.setIdDst(dstNode.getId());

                            String weight = JOptionPane.showInputDialog("Enter weight");
                            newEdge.setWeight(Integer.parseInt(weight));
                        } else {
                            selectedNode.getEdges().remove(newEdge);
                            edges.remove(newEdge);
                        }
                        newEdge = null;
                        selectedNode = null;
                        break;
                    case MOVE_NODE:
                        if (selectedNode != null) {
                            selectedNode.setViewX(x);
                            selectedNode.setViewY(y);
                        }
                        selectedNode = null;
                        break;
                    default:
                        break;
                }
                invalidateGraph(drawPanel);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Integer x = e.getX();
                Integer y = e.getY();
                //System.out.println(x+","+y);
                switch (viewState) {
                    case ADD_EDGE:
                        if (newEdge != null) {
                            newEdge.setViewDstX(x);
                            newEdge.setViewDstY(y);
                        }
                        break;
                    case MOVE_NODE:
                        if (selectedNode != null) {
                            selectedNode.setViewX(x);
                            selectedNode.setViewY(y);
                        }
                        break;
                    default:
                        break;
                }
                invalidateGraph(drawPanel);
            }
        });
        buttonAddNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewState = ViewState.ADD_NODE;
            }
        });
        buttonMoveNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewState = ViewState.MOVE_NODE;
            }
        });
        buttonRemoveNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewState = ViewState.REMOVE_NODE;
            }
        });
        buttonAddEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewState = ViewState.ADD_EDGE;
            }
        });
        buttonRemoveEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewState = ViewState.REMOVE_EDGE;
            }
        });
    }
}
