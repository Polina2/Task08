package ru.vsu.cs.aisd.g92.lyigina_p_s;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class NodeView extends ObjectView {

    public NodeView(Integer id, Integer viewX, Integer viewY, Integer virtualX, Integer virtualY) {
        super(id, viewX, viewY, virtualX, virtualY);
    }

    private List<EdgeView> edges = new ArrayList<>();

    public List<EdgeView> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeView> edges) {
        this.edges = edges;
    }

    @Override
    public void setViewX(Integer viewX) {
        super.setViewX(viewX);
        Integer center = getCenter().x;
        edges.forEach(edge -> {
            if (Objects.equals(edge.getId(), getId())) {
                edge.setViewX(center);
            } else {
                edge.setViewDstX(center);
            }
        });
    }

    @Override
    public void setViewY(Integer viewY) {
        super.setViewY(viewY);
        Integer center = getCenter().y;
        edges.forEach(edge -> {
            if (Objects.equals(edge.getId(), getId())) {
                edge.setViewY(center);
            } else {
                edge.setViewDstY(center);
            }
        });
    }

    @Override
    public void draw(Graphics engine) {
        engine.drawOval(getViewX(), getViewY(), X_SIZE, Y_SIZE);
        engine.setColor(Color.white);
        engine.fillOval(getViewX(), getViewY(), X_SIZE, Y_SIZE);
        engine.setColor(Color.black);
        engine.drawString(getId().toString(), getViewX() + X_SIZE / 4, getViewY() + Y_SIZE * 3 / 4);
    }
}
