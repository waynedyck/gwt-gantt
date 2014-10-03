package com.imaginedreal.gwtgantt.model;

import java.io.Serializable;

/**
 * Defines the predecessor task of the task that contains it.
 *
 * @author Brad Rydzewski
 */
public class Predecessor implements Serializable {

    private static final long serialVersionUID = -1333014049637190068L;
    /**
     * The unique identifier of the predecessor task.
     */
    private int UID;
    /**
     * The predecessor type. values are Start to Start, Start to Finish,
     * Finish to Finish and Finish to Start.
     */
    private PredecessorType type;

    public Predecessor() {
    }

    public Predecessor(int UID, PredecessorType type) {
        super();
        this.UID = UID;
        this.type = type;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public PredecessorType getType() {
        return type;
    }

    public void setType(PredecessorType type) {
        this.type = type;
    }
}
