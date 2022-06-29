package ru.vsu.cs.aisd.g92.lyigina_p_s;

import java.awt.*;

abstract class ObjectView implements IView {
    public static final Integer X_SIZE = 20;
    public static final Integer Y_SIZE = 20;

    private Integer id;
    private Integer viewX;
    private Integer viewY;
    private Integer virtualX;
    private Integer virtualY;

    public ObjectView() {

    }

    public ObjectView(Integer id, Integer viewX, Integer viewY, Integer virtualX, Integer virtualY) {
        this.id = id;
        this.viewX = viewX;
        this.viewY = viewY;
        this.virtualX = virtualX;
        this.virtualY = virtualY;
    }

    public Point getCenter() {
        return new Point(viewX + X_SIZE / 2, viewY + Y_SIZE / 2);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getViewX() {
        return viewX;
    }

    public void setViewX(Integer viewX) {
        this.viewX = viewX;
    }

    public Integer getViewY() {
        return viewY;
    }

    public void setViewY(Integer viewY) {
        this.viewY = viewY;

    }

    public Integer getVirtualX() {
        return virtualX;
    }

    public void setVirtualX(Integer virtualX) {
        this.virtualX = virtualX;
    }

    public Integer getVirtualY() {
        return virtualY;
    }

    public void setVirtualY(Integer virtualY) {
        this.virtualY = virtualY;
    }
}
