package ru.vsu.cs.aisd.g92.lyigina_p_s;

import java.awt.*;

class EdgeView extends ObjectView {

    private Integer viewDstX;
    private Integer viewDstY;
    private Integer virtualDstX;
    private Integer virtualDstY;
    private Integer idDst;
    private Integer weight = 0;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public EdgeView(Integer id,
                    Integer viewX, Integer viewY,
                    Integer virtualX, Integer virtualY,
                    Integer viewDstX, Integer viewDstY,
                    Integer virtualDstX, Integer virtualDstY,
                    Integer idDst) {
        super(id, viewX, viewY, virtualX, virtualY);
        this.viewDstX = viewDstX;
        this.viewDstY = viewDstY;
        this.virtualDstX = virtualDstX;
        this.virtualDstY = virtualDstY;
        this.idDst = idDst;
    }

    public Integer getViewDstX() {
        return viewDstX;
    }

    public void setViewDstX(Integer viewDstX) {
        this.viewDstX = viewDstX;
    }

    public Integer getViewDstY() {
        return viewDstY;
    }

    public void setViewDstY(Integer viewDstY) {
        this.viewDstY = viewDstY;
    }

    public Integer getVirtualDstX() {
        return virtualDstX;
    }

    public void setVirtualDstX(Integer virtualDstX) {
        this.virtualDstX = virtualDstX;
    }

    public Integer getVirtualDstY() {
        return virtualDstY;
    }

    public void setVirtualDstY(Integer virtualDstY) {
        this.virtualDstY = virtualDstY;
    }

    public Integer getIdDst() {
        return idDst;
    }

    public void setIdDst(Integer idDst) {
        this.idDst = idDst;
    }

    public Point getCenter() {
        return new Point((getViewDstX() + getViewX()) / 2, (getViewDstY() + getViewY()) / 2);
    }

    @Override
    public void draw(Graphics engine) {
        engine.drawLine(getViewX(), getViewY(), getViewDstX(), getViewDstY());
        Point c = getCenter();
        engine.drawString(getWeight().toString(), c.x, c.y);
    }
}
